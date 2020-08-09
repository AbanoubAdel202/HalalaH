package com.example.halalah.connect;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.halalah.Utils;
import com.example.halalah.iso8583.BCDASCII;
import com.example.halalah.storage.CommunicationInfo;
import com.example.halalah.util.PacketProcessUtils;


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
    private static SocketManager mSocketManager;
    private static String mHostIp;
    private static String mHostPort;
    private CountDownTimer timeoutCounter;
    private static BehaviorSubject<Boolean> sendReceiveBS;

    private CommunicationsHandler() {

    }

    public void preConnect() {
        Observable.fromCallable(() -> SocketManager.getInstance()
                .preConnect(mCommunicationInfo.getHostIP(), mCommunicationInfo.getHostPort()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    public static CommunicationsHandler getInstance(CommunicationInfo communicationInfo) {
        if (mInstance == null) {
            mInstance = new CommunicationsHandler();
            mCommunicationInfo = communicationInfo;
            mHostIp = mCommunicationInfo.getHostIP();
            mHostPort = mCommunicationInfo.getHostPort();
            mSocketManager = SocketManager.getInstance();
        }
        return mInstance;
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

        switch (SocketManager.getInstance().getConnectionStatus()) {
            case SocketManager.CONNECTION_STATUS_CONNECTED:
                updateUI(UI_STATUS_CONNECTED);
                Log.d(TAG, "switch CONNECTION_STATUS_CONNECTED");
                break;
            case SocketManager.CONNECTION_STATUS_DISCONNECTED:
                Log.d(TAG, "switch CONNECTION_STATUS_DISCONNECTED");
                updateUI(UI_STATUS_RECONNECTING);
                reconnect(5000, 3);
                publishResult(PacketProcessUtils.SOCKET_PROC_ERROR_REASON_RECE_TIME_OUT);
                return false;
            case SocketManager.CONNECTION_STATUS_IN_PROGRESS:
                Log.d(TAG, "switch CONNECTION_STATUS_IN_PROGRESS");
                updateUI(UI_STATUS_WAITING);
                waitForCurrentCall(5000, 750);
                publishResult(PacketProcessUtils.SOCKET_PROC_ERROR_REASON_RECE_TIME_OUT);
                return false;
        }

        int isSendSuccess = mSocketManager.send(mSendPacket);
        if (mSendPacket != null && isSendSuccess <= 0) {
            Log.d(TAG, "send packet failed.");
            publishResult(PacketProcessUtils.SOCKET_PROC_ERROR_REASON_SEND);
            return false;
        }
        Log.d(TAG, "send packet success.");

//                publishProgress(SOCKET_STATUS_RECE);
        mRecePacket = mSocketManager.recv();
       // mRecePacket = BCDASCII.hexStringToBytes("313232313732333030374331324543323842303531363438343738333530313034373439313230303030303030303030303030303130303031323036303231383132303030303438313831323036303435343436373130333031353133333443303030323030313030353533313130363538383834393334343834373833353031303437343931323D323130323232313138383838373538303230343534343630303030343734373439313230303034373030303132333031343930313233303130313132333435363738202020303036534149425031363832313347FFF00111100000000836303931353982023C009F3602026A9F26081912478F9943C9AD9F2701409F34034200009F1E0830373030303031319F101206010A0360AC040A0200000000004EAF333E9F3303E0F8C89F350122950508800400009F3704C162B7389F02060000000010009F03060000000000009F1A0206825F2A0206829A031812069C01008407A000000228201050046D6164619F120A6D6164612044656269744F07A00000022820103432313130303030303034373132303630313534343631383132303630343534343630363538383834393030313136303131303232303330303030303830343030353030374E323434313338453034363433343330393031303030303030303030303131303030303030303030313230303030303030303031333030303030303030303134303030303030303030303030313530363030303331363032303230323032FBB9A7");

        if ((mRecePacket == null) || (mRecePacket.length <= 0)) {
            Log.d(TAG, "receive packet failed.");
            publishResult(PacketProcessUtils.SOCKET_PROC_ERROR_REASON_RECE);
            return false;
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
                    Log.d(TAG, "reconnect seconds remaining: " + millisUntilFinished / 1000);
                    if (isConnected()) {
                        this.onFinish();
                    }
                }

                @Override
                public void onFinish() {
                    Log.d(TAG, "reconnect Timeout Counter DONE! ");
                    if (isConnected()) {
                        sendReceive();
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
                    if (finishedCurrentTransaction()) {
                        this.onFinish();
                    }
                }

                @Override
                public void onFinish() {
                    Log.d(TAG, "waitForCurrentCall Timeout Counter DONE! ");
                    if (isConnected()) {
                        sendReceive();
                    } else {
                        updateUI(UI_STATUS_RECONNECTING);
                        reconnect(5000, 3);
                    }
                    timeoutCounter.cancel();
                }
            }.start();
        });
    }

    private boolean isConnected() {
        Log.d(TAG, "isConnected = " + (SocketManager.getInstance().getConnectionStatus() == SocketManager.CONNECTION_STATUS_CONNECTED));
        return SocketManager.getInstance().getConnectionStatus() == SocketManager.CONNECTION_STATUS_CONNECTED;
    }

    private boolean finishedCurrentTransaction() {
        Log.d(TAG, "finishedCurrentTransaction = " + (SocketManager.getInstance().getConnectionStatus() != SocketManager.CONNECTION_STATUS_IN_PROGRESS));
        return SocketManager.getInstance().getConnectionStatus() != SocketManager.CONNECTION_STATUS_IN_PROGRESS;
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
            mSocketManager.close();
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
