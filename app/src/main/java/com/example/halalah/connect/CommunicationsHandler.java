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

    public static final int SUCCESS = 0;

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
//        mRecePacket = mSocketManager.recv();
        mRecePacket = BCDASCII.hexStringToBytes("038460000006038020000000003939303630353035353333363030303030303030303038333030303030303030303030303033303030303030303039393036303530353533333630303030303030303030300210303C04C10EC68813009000000000000008000031153530091949120210000608999900013135333533303230353030323230343030313030303030303030303839393036303530353533333630303000885F573131313137303333373030303030303938363202BDADCEF7C5A9D0C5C1AABACFC9E7202002343832313030303102490230025F5109BDBBD2D7B3C9B9A6025F551530303030303102303030303331023039313902300206118F0382025E10B0E6B1BE3A3134303432390AC9CCBBA7C3FB3ABDF8CFCDC3C5B5EA5FB2E2CAD4330AC9CCBBA7BAC53A3939303630353035353333363030300AD6D5B6CBBAC53A30303030303030382020B2D9D7F7D4B13A30310AB7A2BFA8BBFAB9B93ABDADCEF7C5A9D0C5C1AABACFC9E720200ACAD5B5A5BBFAB9B93A34383231303030310A7EBFA8BAC53A3632323638322A2A2A2A2A2A2A2A2A3732343020537E0A7EBDBBD2D7C0E0D0CD3ACFFBB7D17E0AD3D0D0A7C6DA3A34393132202020C5FAB4CEBAC53A3030303030310AC6BED6A4BAC53A30303030333120CADAC8A8C2EB3A3230343030310ABDBBD2D7B2CEBFBCBAC53A3135333533303230353030320ACAB1BCE43A323031372D30392D31392031353A33353A33300A7EBDF0B6EE3A524D4220302E30387E0A2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D0ABDBBD2D7B5A5BAC53A3131313730333337303030303030393836322020420A2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D0A02117EB3D6BFA8C8CBC7A9C3FB313A7E0A0A0AB1BEC8CBC8B7C8CFD2D4C9CFBDBBD2D72CCDACD2E2BDABC6E4BCC6C8EBB1BED5CBBBA70A0212CDA8C1AABFCDBBA7BAC53A3939303630353035353333363030300A7EB3D6BFA8C8CBC7A9C3FB3A7E0A0A0AB1BEC8CBC8B7C8CFD2D4C9CFBDBBD2D72CCDACD2E2BDABC6E4BCC6C8EBB1BED5CBBBA70A0213CDA8C1AABFCDBBA7BAC53A3939303630353035353333363030300A7EB3D6BFA8C8CBC7A9C3FB3A7E0A0A0AB1BEC8CBC8B7C8CFD2D4C9CFBDBBD2D72CCDACD2E2BDABC6E4BCC6C8EBB1BED5CBBBA70A02313439260000000000000000192200000100050000000000034355504243343337323036");

        if ((mRecePacket == null) || (mRecePacket.length <= 0)) {
            Log.d(TAG, "receive packet failed.");
            publishResult(PacketProcessUtils.SOCKET_PROC_ERROR_REASON_RECE);
            return false;
        }
        Log.d(TAG, "receive packet success.");
        publishResult(SUCCESS, mRecePacket);
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
            mSendReceiveListener.onSocketProcessEnd(receivedPacket, resultCode);
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
