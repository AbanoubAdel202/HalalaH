package com.example.halalah.connect;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.halalah.PosApplication;
import com.example.halalah.ui.PacketProcessActivity;
import com.example.halalah.R;
import com.example.halalah.Utils;

import com.example.halalah.iso8583.BCDASCII;
import com.example.halalah.storage.CommunicationInfo;
import com.example.halalah.util.PacketProcessUtils;

public class SocketProcessTask extends AsyncTask<byte[], Integer, Void> {
    private static final String TAG = Utils.TAGPUBLIC + SocketProcessTask.class.getSimpleName();

    private static final int RETRY_MAX_CNT = 3;

    private static final int SOCKET_STATUS_CONN = 1;
    private static final int SOCKET_STATUS_SEND = 2;
    private static final int SOCKET_STATUS_RECE = 3;

    private Context mContext;
    private PacketProcessActivity mActivity;
    private CommunicationInfo mCommunicationInfo;
    private int mProcType;

    private SocketProcEndListener mSocketProcEndListener;

    private byte[] mSendPacket;
    private byte[] mRecePacket;
    private CommSocket mCommSocket;
    private String mHostIp;
    private String mHostPort;

    private int mSendNum = 0;
    public int mErrorReson = 0;

    public SocketProcessTask(PacketProcessActivity activity, int procType) {
        mProcType = procType;
        mActivity = activity;
        mCommunicationInfo = new CommunicationInfo(activity);
    }

    public interface SocketProcEndListener {
        public void onSocketProcEnd(byte[] recePacket, int errReason);
    }

    public void setSocketProcEndListener(SocketProcEndListener socketProcEndListener) {
        mSocketProcEndListener = socketProcEndListener;
    }

    @Override
    protected void onPreExecute() {
        Log.i(TAG, "onPreExecute()");
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(byte[]... params) {
        Log.i(TAG,"doInBackground()");


            mSendPacket = (params[0]);
            mCommSocket = new CommSocket();

            publishProgress(SOCKET_STATUS_CONN);

            //todo communication choosing (wifi , GSM , GPRS, 3G)
            mHostIp = mCommunicationInfo.getHostIP();
           // mHostIp= PosApplication.getApp().oGSama_TMS.connection_parameters.conn_primary.gprs.Network_IP_address;
            mHostPort = mCommunicationInfo.getHostPort();
           // mHostPort =PosApplication.getApp().oGSama_TMS.connection_parameters.conn_primary.gprs.Network_TCP_port;
            if (mHostIp == null || mHostPort == null) {
                mHostIp = mCommunicationInfo.getSpareHostIP();
                mHostPort = mCommunicationInfo.getSpareHostPort();
                if (mHostIp == null || mHostPort == null) {
                    Log.e(TAG, "ip or port is null");
                    mErrorReson = PacketProcessUtils.SOCKET_PROC_ERROR_REASON_IP_PORT;
                    return null;
                }
            }
            Log.e(TAG, "ip and port is ok. IP: " + mHostIp + " PORT: " + mHostPort);

            while (mSendNum < RETRY_MAX_CNT) {
                Log.e(TAG, "mSendNum:" + mSendNum);
                if (!mCommSocket.open(mHostIp, mHostPort)) {
                    Log.e(TAG, mSendNum + " open failed");
                    mSendNum++;
                } else {
                    Log.e(TAG, mSendNum + " open success");
                    break;
                }
            }
            if (mSendNum == 3) {
                Log.e(TAG, "socket open failed.");
                mErrorReson = PacketProcessUtils.SOCKET_PROC_ERROR_REASON_CONNE;
                return null;
            }
            Log.e(TAG, "socket open success.");

            publishProgress(SOCKET_STATUS_SEND);
            if (mSendPacket != null && mCommSocket.send(mSendPacket) <= 0) {
                Log.e(TAG, "send packet failed.");
                mErrorReson = PacketProcessUtils.SOCKET_PROC_ERROR_REASON_SEND;
                return null;
            }
            Log.e(TAG, "send packet success.");

//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

            publishProgress(SOCKET_STATUS_RECE);
            mRecePacket = mCommSocket.recv();
            if ((mRecePacket == null) || (mRecePacket.length <= 0)) {
                Log.e(TAG, "receive packet failed.");
                mErrorReson = PacketProcessUtils.SOCKET_PROC_ERROR_REASON_RECE;
                return null;
            }

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        Log.i(TAG, "onProgressUpdate(), values" + values[0]);
        super.onProgressUpdate(values);
        if (values[0] == SOCKET_STATUS_CONN) {
            if (mActivity != null) {
                mActivity.mTextProcStatus.setText(R.string.socket_proc_status_conning);
            }
        } else if (values[0] == SOCKET_STATUS_SEND) {
            if (mActivity != null) {
                mActivity.mTextProcStatus.setText(R.string.socket_proc_status_sending);
            }
        } else if (values[0] == SOCKET_STATUS_RECE) {
            if (mActivity != null) {
                mActivity.mTextProcStatus.setText(R.string.socket_proc_status_receing);
            }
        }
    }

    @Override
    protected void onPostExecute(Void avoid) {
        Log.i(TAG, "onPostExecute()");
        super.onPostExecute(avoid);
        if (mActivity != null) {
            // test start
            if (mProcType == PacketProcessUtils.PACKET_PROCESS_PURCHASE) {
                mRecePacket = BCDASCII.hexStringToBytes("038460000006038020000000003939303630353035353333363030303030303030303038333030303030303030303030303033303030303030303039393036303530353533333630303030303030303030300210303C04C10EC68813009000000000000008000031153530091949120210000608999900013135333533303230353030323230343030313030303030303030303839393036303530353533333630303000885F573131313137303333373030303030303938363202BDADCEF7C5A9D0C5C1AABACFC9E7202002343832313030303102490230025F5109BDBBD2D7B3C9B9A6025F551530303030303102303030303331023039313902300206118F0382025E10B0E6B1BE3A3134303432390AC9CCBBA7C3FB3ABDF8CFCDC3C5B5EA5FB2E2CAD4330AC9CCBBA7BAC53A3939303630353035353333363030300AD6D5B6CBBAC53A30303030303030382020B2D9D7F7D4B13A30310AB7A2BFA8BBFAB9B93ABDADCEF7C5A9D0C5C1AABACFC9E720200ACAD5B5A5BBFAB9B93A34383231303030310A7EBFA8BAC53A3632323638322A2A2A2A2A2A2A2A2A3732343020537E0A7EBDBBD2D7C0E0D0CD3ACFFBB7D17E0AD3D0D0A7C6DA3A34393132202020C5FAB4CEBAC53A3030303030310AC6BED6A4BAC53A30303030333120CADAC8A8C2EB3A3230343030310ABDBBD2D7B2CEBFBCBAC53A3135333533303230353030320ACAB1BCE43A323031372D30392D31392031353A33353A33300A7EBDF0B6EE3A524D4220302E30387E0A2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D0ABDBBD2D7B5A5BAC53A3131313730333337303030303030393836322020420A2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D0A02117EB3D6BFA8C8CBC7A9C3FB313A7E0A0A0AB1BEC8CBC8B7C8CFD2D4C9CFBDBBD2D72CCDACD2E2BDABC6E4BCC6C8EBB1BED5CBBBA70A0212CDA8C1AABFCDBBA7BAC53A3939303630353035353333363030300A7EB3D6BFA8C8CBC7A9C3FB3A7E0A0A0AB1BEC8CBC8B7C8CFD2D4C9CFBDBBD2D72CCDACD2E2BDABC6E4BCC6C8EBB1BED5CBBBA70A0213CDA8C1AABFCDBBA7BAC53A3939303630353035353333363030300A7EB3D6BFA8C8CBC7A9C3FB3A7E0A0A0AB1BEC8CBC8B7C8CFD2D4C9CFBDBBD2D72CCDACD2E2BDABC6E4BCC6C8EBB1BED5CBBBA70A02313439260000000000000000192200000100050000000000034355504243343337323036");
                mErrorReson = 0;
            } else if (mProcType == PacketProcessUtils.PACKET_PROCESS_SCAN) {
                mRecePacket = BCDASCII.hexStringToBytes("02DA600000060380200000000039393036303530353533333630303030303030303030383330303030303030303030303030333030303030303030393930363035303535333336303030303030303030303002103038048102C60013400100000000000001000020180649111803000008999900013030303030303030303839393036303530353533333630303000895F5109BDBBD2D7B3C9B9A6025F554A313131380230303030303102313131373033333730303030303133393639023131313730333337303030303031333936390256535035313102323038383930323432373931353235320204758F038201D610B3CCD0F2B0E6B1BEBAC53A3134303432390A7EC9CCBBA7C3FBB3C63ABDF8CFCDC3C5B5EA5FB2E2CAD4337E0A7EC9CCBBA7BAC53A3939303630353035353333363030307E0A7ED6D5B6CBBAC53A303030303030303820207E0AB2D9D7F7D4B1BAC53A30310AD6A7B8B6D5CBBAC53A0ABDBBD2D7B5A5BAC53A3131313730333337303030303031333936390ABDBBD2D7B2CEBFBCBAC53A303531363433323539380A7EBDBBD2D7C0E0D0CD3AD6A7B8B6B1A6D6A7B8B67E0A7EBDF0B6EE3A524D423A302E30317E0ABDBBD2D7CAB1BCE43A323031372D31312D31382031383A30363A35332002112D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D0ACEDED0E8C3DCC2EB2FCEDED0E8C7A9C3FB0AD0EBD6AA3AD6A7B8B6B3C9B9A62CC7EBC1F4D2E2D7CABDF0B1E4BBAFA1A30A02122D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D0ACEDED0E8C3DCC2EB2FCEDED0E8C7A9C3FB0AD0EBD6AA3AD6A7B8B6B3C9B9A62CC7EBC1F4D2E2D7CABDF0B1E4BBAFA1A30A02132D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D0ACEDED0E8C3DCC2EB2FCEDED0E8C7A9C3FB0AD0EBD6AA3AD6A7B8B6B3C9B9A62CC7EBC1F4D2E2D7CABDF0B1E4BBAFA1A30A0200145200000100000000034149503542394539413136");
                mErrorReson = 0;
            }

            // test end
            mActivity.onSocketProcessEnd(mRecePacket, mErrorReson);
        }
        if (mContext != null && mSocketProcEndListener != null) {
            mSocketProcEndListener.onSocketProcEnd(mRecePacket, mErrorReson);
        }
        if (mCommSocket != null) {
            mCommSocket.close();
        }
    }

    public void setStop() {
        if (mCommSocket != null) {
            mCommSocket.setStop();
        }
    }
}