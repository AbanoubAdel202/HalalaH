package com.example.halalah.connect;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.halalah.Utils;
import com.example.halalah.storage.CommunicationInfo;
import com.example.halalah.util.PacketProcessUtils;

import java.io.InputStream;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class CommunicationsHandler {

    private static final String TAG = Utils.TAGPUBLIC + CommunicationsHandler.class.getSimpleName();


    private static final int UI_STATUS_CONNECTED = 1;
    private static final int UI_STATUS_RECONNECTING = 2;
    private static final int UI_STATUS_WAITING = 3;
    private static final int UI_STATUS_FAILED = 4;
    private static final int UI_STATUS_DISCONNECTED = -1;

    private static final int MSG_CONNECTION_STATUS = 200;

    private static CommunicationsHandler mInstance;
    private static CommunicationInfo mCommunicationInfo;
    private SendReceiveListener mSendReceiveListener;

    private byte[] mSendPacket;
    private byte[] mRecePacket;
    private static iConnect mSocketManager;
    private static String mHostIp;
    private static String mHostPort;
    private CountDownTimer timeoutCounter;
    private static BehaviorSubject<Boolean> sendReceiveBS;
    private boolean mIsFinancialMessage = true;
    private HeadersInterceptor headersInterceptor = new HeadersInterceptor();

    public static CommunicationsHandler getInstance(CommunicationInfo communicationInfo) {
        return getInstance(communicationInfo, null);
    }

    public static CommunicationsHandler getInstance(CommunicationInfo communicationInfo, InputStream certificateIS) {
        if (mInstance == null) {
            mInstance = new CommunicationsHandler(communicationInfo, certificateIS);
        }
        return mInstance;
    }

    private CommunicationsHandler(CommunicationInfo communicationInfo, InputStream certificateIS) {
        if (communicationInfo == null) {
            return;
        }
        mCommunicationInfo = communicationInfo;
        mHostIp = mCommunicationInfo.getHostIP();
        mHostPort = mCommunicationInfo.getHostPort();
        if (mCommunicationInfo.isSSLEnabled()) {
            mSocketManager = SSLSocketManager.getInstance(certificateIS);
        } else {
            mSocketManager = PlainSocketManager.getInstance();
        }
    }

    public void preConnect() {
        BehaviorSubject<Integer> connectionStatusBS = BehaviorSubject.create();
        Observable o = Observable.fromCallable(() -> mSocketManager.connect(mCommunicationInfo.getHostIP(), mCommunicationInfo.getHostPort()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        o.subscribe(connectionStatusBS);
        connectionStatusBS.observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    public void setSendReceiveListener(SendReceiveListener sendReceiveListener) {
        mSendReceiveListener = sendReceiveListener;
    }

    public Observable<Boolean> sendReceive(byte[] packetToSend) {
        sendReceiveBS = BehaviorSubject.create();

        mSendPacket = packetToSend;
        Observable o = Observable.fromCallable(() -> sendReceive())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        o.subscribe(sendReceiveBS);
        return sendReceiveBS;
    }

    private Boolean sendReceive() {
        // TODO Apply connection configuration logic
        mHostIp = mCommunicationInfo.getHostIP();
        mHostPort = mCommunicationInfo.getHostPort();
        if (mHostIp == null || mHostPort == null) {
            mHostIp = mCommunicationInfo.getSpareHostIP();
            mHostPort = mCommunicationInfo.getSpareHostPort();
            if (mHostIp == null || mHostPort == null) {
                Log.d(TAG, "ip or port is null");
                publishResult(PacketProcessUtils.SOCKET_PROC_ERROR_REASON_IP_PORT);
                return false;
            }
        }
        Log.d(TAG, "ip and port is ok. IP: " + mHostIp + " PORT: " + mHostPort);

        switch (mSocketManager.getConnectionStatus()) {
            case iConnect.CONNECTION_STATUS_CONNECTED:
                updateUI(UI_STATUS_CONNECTED);
                Log.d(TAG, "switch CONNECTION_STATUS_CONNECTED");
                break;
            case iConnect.CONNECTION_STATUS_DISCONNECTED:
                Log.d(TAG, "switch CONNECTION_STATUS_DISCONNECTED");
                // TODO updateUI Connecting / Reconnecting
                updateUI(UI_STATUS_RECONNECTING);
                reconnect(5000, 3);
//                publishResult(PacketProcessUtils.SOCKET_PROC_ERROR_REASON_RECE_TIME_OUT);
                return false;
            case iConnect.CONNECTION_STATUS_IN_PROGRESS:
                Log.d(TAG, "switch CONNECTION_STATUS_IN_PROGRESS");
                updateUI(UI_STATUS_WAITING);
                waitForCurrentCall(5000, 750);
//                publishResult(PacketProcessUtils.SOCKET_PROC_ERROR_REASON_RECE_TIME_OUT);
                return false;
        }

        if (mIsFinancialMessage) {
            // Add length and tpdu
            mSendPacket = headersInterceptor.addHeaders(mSendPacket, mCommunicationInfo.getTPDU());
        }
        int isSendSuccess = mSocketManager.send(mSendPacket);
        if (mSendPacket != null && isSendSuccess <= 0) {
            Log.d(TAG, "send packet failed.");
            publishResult(PacketProcessUtils.SOCKET_PROC_ERROR_REASON_SEND);
            return false;
        }
        Log.d(TAG, "send packet success.");

        mRecePacket = mSocketManager.receive();
        // TODO TIMEOUT
        if ((mRecePacket == null) || (mRecePacket.length <= 0)) {
            Log.d(TAG, "receive packet failed.");
            publishResult(PacketProcessUtils.SOCKET_PROC_ERROR_REASON_RECE);
            return false;
        }
        if (mIsFinancialMessage) {
            // check received packet integrity
            if (headersInterceptor.checkPacketIntegrity(mRecePacket)){
                mRecePacket = headersInterceptor.getResponseBody(mRecePacket);
            } else {
                publishResult(PacketProcessUtils.SOCKET_PROC_ERROR_REASON_RECE);
                return false;
            }
        }
        Log.d(TAG, "receive packet success.");
        publishResult(PacketProcessUtils.SUCCESS, mRecePacket);
        return true;
    }

    private void reconnect(int connectionTimeOut, int timesToReconnect) {
        new Handler(Looper.getMainLooper()).post(() -> {
            timeoutCounter = new CountDownTimer(timesToReconnect * connectionTimeOut, connectionTimeOut) {
                @Override
                public void onTick(long millisUntilFinished) {
                    preConnect();
                    Log.d(TAG, "reconnect seconds remaining: " + millisUntilFinished / 1000);
                    if (isConnected()) {
                        this.onFinish();
                    }
                }

                @Override
                public void onFinish() {
                    Log.d(TAG, "reconnect Timeout Counter DONE! ");
                    if (isConnected()) {
                        sendReceive(mSendPacket);
                    } else {
                        updateUI(UI_STATUS_FAILED);
                    }
                    timeoutCounter.cancel();
                }
            }.start();
        });
    }

    private void waitForCurrentCall(int totalTimeToWait, int intervalTime) {
        new Handler(Looper.getMainLooper()).post(() -> {

            timeoutCounter = new CountDownTimer(totalTimeToWait, intervalTime) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Log.d(TAG, "waitForCurrentCall seconds remaining: " + millisUntilFinished / 1000);
                    if (isCurrentCallFinished()) {
                        this.onFinish();
                    }
                }

                @Override
                public void onFinish() {
                    Log.d(TAG, "waitForCurrentCall Timeout Counter DONE! ");
                    if (isConnected()) {
                        sendReceive(mSendPacket);
                    } else {
                        updateUI(UI_STATUS_FAILED);
                        reconnect(5000, 3);
                    }
                    timeoutCounter.cancel();
                }
            }.start();
        });
    }

    private boolean isConnected() {
        Log.d(TAG, "isConnected = " + (mSocketManager.getConnectionStatus() == iConnect.CONNECTION_STATUS_CONNECTED));
        return mSocketManager.getConnectionStatus() == iConnect.CONNECTION_STATUS_CONNECTED;
    }

    private boolean isCurrentCallFinished() {
        Log.d(TAG, "finishedCurrentTransaction = " + (mSocketManager.getConnectionStatus() != iConnect.CONNECTION_STATUS_IN_PROGRESS));
        return mSocketManager.getConnectionStatus() != iConnect.CONNECTION_STATUS_IN_PROGRESS;
    }

    private void publishResult(int resultCode) {
        publishResult(resultCode, null);
    }

    private void publishResult(int resultCode, byte[] receivedPacket) {
        if (mSendReceiveListener != null) {
            if (resultCode == 0) {
                mSendReceiveListener.onSuccess(receivedPacket);
            } else {
                mSendReceiveListener.onFailure(resultCode);
            }
        }
    }

    private void updateUI(int uiStatus) {
        Message message = mHandle.obtainMessage(MSG_CONNECTION_STATUS, new Integer(uiStatus));
        message.sendToTarget();
    }

    public void closeConnection() {
        if (mSocketManager != null) {
            mSocketManager.disconnect();
            updateUI(UI_STATUS_DISCONNECTED);
            Log.d(TAG, "Connection closed");

        }
    }

    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CONNECTION_STATUS:
                    if (mSendReceiveListener != null) {
                        Integer data = (Integer) msg.obj;
                        mSendReceiveListener.showConnectionStatus(data);
                    }
                    break;
                default:
                    break;
            }
        }
    };



}
