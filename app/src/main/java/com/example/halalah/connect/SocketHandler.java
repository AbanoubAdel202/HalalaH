package com.example.halalah.connect;

import android.util.Log;

import com.example.halalah.Utils;
import com.example.halalah.storage.CommunicationInfo;
import com.example.halalah.util.PacketProcessUtils;


import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SocketHandler {

    private static final String TAG = Utils.TAGPUBLIC + SocketHandler.class.getSimpleName();

    private static final int RETRY_MAX_CNT = 3;

    private static final int SOCKET_STATUS_CONN = 1;
    private static final int SOCKET_STATUS_SEND = 2;
    private static final int SOCKET_STATUS_RECE = 3;
    private static SocketHandler mInstance;

    private static CommunicationInfo mCommunicationInfo;
    private int mProcType;

    private SocketProcessTask.SocketProcEndListener mSocketProcEndListener;

    private byte[] mSendPacket;
    private byte[] mRecePacket;
    private static CommSocket mCommSocket;
    private static String mHostIp;
    private static String mHostPort;

    private static int mSendNum = 0;
    public int mErrorReason = 0;

    private SocketHandler() {

    }

    public static SocketHandler getInstance(CommunicationInfo communicationInfo) {
        if (mInstance == null) {
            mInstance = new SocketHandler();

            mCommunicationInfo = communicationInfo;
            mHostIp = mCommunicationInfo.getHostIP();
            mHostPort = mCommunicationInfo.getHostPort();

            mCommSocket = CommSocket.getInstance();
        }
        return mInstance;
    }

    public Observable<Boolean> sendReceive(int procType, byte[] packetToSend) {

        return Observable.fromCallable(() -> sendReceive(packetToSend))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Boolean sendReceive(byte[] packetToSend) {
        mHostIp = mCommunicationInfo.getHostIP();
        mHostPort = mCommunicationInfo.getHostPort();
        if (mHostIp == null || mHostPort == null) {
            mHostIp = mCommunicationInfo.getSpareHostIP();
            mHostPort = mCommunicationInfo.getSpareHostPort();
            if (mHostIp == null || mHostPort == null) {
                Log.e(TAG, "ip or port is null");
                mErrorReason = PacketProcessUtils.SOCKET_PROC_ERROR_REASON_IP_PORT;
                return false;
            }
        }
        Log.e(TAG, "ip and port is ok. IP: " + mHostIp + " PORT: " + mHostPort);

//        while (mSendNum < RETRY_MAX_CNT) {
//            Log.e(TAG, "mSendNum:" + mSendNum);
//            boolean socketOpened = mCommSocket.open(mHostIp, mHostPort);
//            if (!socketOpened) {
//                Log.e(TAG, mSendNum + " open failed");
//                mSendNum++;
//            } else {
//                Log.e(TAG, mSendNum + " open success");
//                break;
//            }
//        }
//        if (mSendNum == 3) {
//            Log.e(TAG, "socket open failed.");
//            mErrorReason = PacketProcessUtils.SOCKET_PROC_ERROR_REASON_CONNE;
//            return false;
//        }
//
//        Log.e(TAG, "socket open success.");

//                publishProgress(SOCKET_STATUS_SEND);
//        CommSocket.getInstance().getConnectionStatus()
//                .subscribe(status -> {
//                    Log.d(Utils.TAGPUBLIC + "sending  => ", "" + status);
//                });
        CommSocket.getInstance().close();
        int isSendSuccess = mCommSocket.send(packetToSend);
        if (packetToSend != null && isSendSuccess <= 0) {
            Log.e(TAG, "send packet failed.");
            mErrorReason = PacketProcessUtils.SOCKET_PROC_ERROR_REASON_SEND;
            return false;
        }
        Log.e(TAG, "send packet success.");

//                publishProgress(SOCKET_STATUS_RECE);
        mRecePacket = mCommSocket.recv();
        if ((mRecePacket == null) || (mRecePacket.length <= 0)) {
            Log.e(TAG, "receive packet failed.");
            mErrorReason = PacketProcessUtils.SOCKET_PROC_ERROR_REASON_RECE;
            return false;
        }

        return false;
    }

}
