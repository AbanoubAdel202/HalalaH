package com.example.halalah.ui;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.halalah.POSTransaction;
import com.example.halalah.POS_MAIN;
import com.example.halalah.PosApplication;
import com.example.halalah.R;
import com.example.halalah.SAF_Info;
import com.example.halalah.Utils;
import com.example.halalah.card.CardManager;
import com.example.halalah.connect.CommunicationsHandler;
import com.example.halalah.connect.SendReceiveListener;
import com.example.halalah.iso8583.BCDASCII;
import com.example.halalah.iso8583.ISO8583;
import com.example.halalah.packet.PackPacket;
import com.example.halalah.packet.UnpackPacket;
import com.example.halalah.packet.UnpackUtils;
import com.example.halalah.storage.CommunicationInfo;
import com.example.halalah.util.PacketProcessUtils;

public class PacketProcessActivity extends Activity implements SendReceiveListener {
    private static final String TAG = Utils.TAGPUBLIC + PacketProcessActivity.class.getSimpleName();

    private static final int MSG_TIME_UPDATE = 100;
    private static final int MSG_CONNECTION_STATUS = 200;
    private static final long MSG_TIME_UPDATE_DALAY = 1000;

    public TextView mTextProcDetail;
    public TextView mTextProcStatus;
    private TextView mTextTime;

    private CommunicationInfo mCommunicationInfo;
    private PackPacket mPackPacket;
    private UnpackPacket mUnpackPacket;
    private byte[] mSendPacket = null;
    private byte[] mRecePacket = null;
    private String mResponse;
    private String mResponseDetail;

    private Bundle mBundle;
    private int mProcType;
    private int mProcTime = 20;
    private int mProcNum = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packet_process);

        mTextProcDetail = findViewById(R.id.proc_detail);
        mTextProcStatus = findViewById(R.id.proc_status);
        mTextTime = findViewById(R.id.proc_time);
        mHandle.sendEmptyMessageDelayed(MSG_TIME_UPDATE, MSG_TIME_UPDATE_DALAY);

        mBundle = getIntent().getExtras();
        mProcType = mBundle.getInt(PacketProcessUtils.PACKET_PROCESS_TYPE);
        Log.i(TAG, "mProcType = " + mProcType);
        // setActionBarText();   //todo mohamed fix actionbar issue (getactionbar() getsupportactionbar()) style issue

        mCommunicationInfo = new CommunicationInfo(this);

        mPackPacket = new PackPacket(this, mCommunicationInfo.getTPDU()); //todo add TPDU to message
        mUnpackPacket = new UnpackPacket(this, mProcType);


        getPacketAndSend();
        CardManager.getInstance().finishPreActivity();
    }

    private void setActionBarText() {
        ActionBar actionBar = this.getActionBar();
        if (mProcType == PacketProcessUtils.PACKET_PROCESS_ONLINE_INIT) {
            actionBar.setTitle(getString(R.string.socket_proc_commu) + "(" + getString(R.string.online_init) + ") ");
        } else if (mProcType == PacketProcessUtils.PACKET_PROCESS_ADMIN) {
            actionBar.setTitle(getString(R.string.socket_proc_commu) + "(" + getString(R.string.Admin) + ") ");
        } else if (mProcType == PacketProcessUtils.PACKET_PROCESS_PURCHASE_WITH_NAQD) {
            actionBar.setTitle(getString(R.string.socket_proc_commu) + "(" + getString(R.string.Purchase_with_naqd) + ") ");
        } else if (mProcType == PacketProcessUtils.PACKET_PROCESS_AUTHORISATION) {
            actionBar.setTitle(getString(R.string.socket_proc_commu) + "(" + getString(R.string.Authorization) + ") ");
        } else if (mProcType == PacketProcessUtils.PACKET_PROCESS_TMS_FILE_DOWNLOAD) {
            actionBar.setTitle(getString(R.string.socket_proc_commu) + "(" + getString(R.string.text_ic_para_download) + ") ");
        } else if (mProcType == PacketProcessUtils.PACKET_PROCESS_PURCHASE) {
            actionBar.setTitle(getString(R.string.socket_proc_commu) + "(" + getString(R.string.Purchase) + ") ");

        }
    }

    private void getPacketAndSend() {

        // mSendPacket = BCDASCII.hexStringToBytes("038460000006038020000000003939303630353035353333363030303030303030303038333030303030303030303030303033303030303030303039393036303530353533333630303030303030303030300210303C04C10EC68813009000000000000008000031153530091949120210000608999900013135333533303230353030323230343030313030303030303030303839393036303530353533333630303000885F573131313137303333373030303030303938363202BDADCEF7C5A9D0C5C1AABACFC9E7202002343832313030303102490230025F5109BDBBD2D7B3C9B9A6025F551530303030303102303030303331023039313902300206118F0382025E10B0E6B1BE3A3134303432390AC9CCBBA7C3FB3ABDF8CFCDC3C5B5EA5FB2E2CAD4330AC9CCBBA7BAC53A3939303630353035353333363030300AD6D5B6CBBAC53A30303030303030382020B2D9D7F7D4B13A30310AB7A2BFA8BBFAB9B93ABDADCEF7C5A9D0C5C1AABACFC9E720200ACAD5B5A5BBFAB9B93A34383231303030310A7EBFA8BAC53A3632323638322A2A2A2A2A2A2A2A2A3732343020537E0A7EBDBBD2D7C0E0D0CD3ACFFBB7D17E0AD3D0D0A7C6DA3A34393132202020C5FAB4CEBAC53A3030303030310AC6BED6A4BAC53A30303030333120CADAC8A8C2EB3A3230343030310ABDBBD2D7B2CEBFBCBAC53A3135333533303230353030320ACAB1BCE43A323031372D30392D31392031353A33353A33300A7EBDF0B6EE3A524D4220302E30387E0A2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D0ABDBBD2D7B5A5BAC53A3131313730333337303030303030393836322020420A2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D0A02117EB3D6BFA8C8CBC7A9C3FB313A7E0A0A0AB1BEC8CBC8B7C8CFD2D4C9CFBDBBD2D72CCDACD2E2BDABC6E4BCC6C8EBB1BED5CBBBA70A0212CDA8C1AABFCDBBA7BAC53A3939303630353035353333363030300A7EB3D6BFA8C8CBC7A9C3FB3A7E0A0A0AB1BEC8CBC8B7C8CFD2D4C9CFBDBBD2D72CCDACD2E2BDABC6E4BCC6C8EBB1BED5CBBBA70A0213CDA8C1AABFCDBBA7BAC53A3939303630353035353333363030300A7EB3D6BFA8C8CBC7A9C3FB3A7E0A0A0AB1BEC8CBC8B7C8CFD2D4C9CFBDBBD2D72CCDACD2E2BDABC6E4BCC6C8EBB1BED5CBBBA70A02313439260000000000000000192200000100050000000000034355504243343337323036");

        mSendPacket = mPackPacket.getSendPacket();
        Log.d(TAG, "getPacketAndSend: mSendPacket = " + BCDASCII.bytesToHexString(mSendPacket));
        CommunicationsHandler communicationsHandler = CommunicationsHandler.getInstance(new CommunicationInfo(this));
        communicationsHandler.setSendReceiveListener(this);
        POS_MAIN.SaveLastTransaction(PosApplication.getApp().oGPosTransaction, POS_MAIN.CurrentSaving.SAVE);
        communicationsHandler.sendReceive(mSendPacket);
    }

    @Override
    public void onSuccess(byte[] receivedPacket)
    {
        Log.i(TAG, "onSuccess");

        if (receivedPacket != null)
        {
            mRecePacket = receivedPacket;
            mHandle.removeMessages(MSG_TIME_UPDATE);
            PosApplication.getApp().oGTerminal_Operation_Data.breversal_flg=false;
            PosApplication.getApp().oGPOS_MAIN.PerfomTermHostResponseFlow(mRecePacket,0,this);

            CommunicationsHandler.getInstance(mCommunicationInfo).closeConnection();


            finish();



        }


    }

    @Override
    public void onFailure(int errorCode)
    {
        Log.d(TAG, "onFailure: start");
        showResult(mResponse, mResponseDetail, errorCode);

    }

    @Override
    public void showConnectionStatus(int connectionStatus) {
        Log.d(TAG, "CONNECTION STATUS " + connectionStatus);
        Toast.makeText(this, "CONNECTION STATUS " + connectionStatus, Toast.LENGTH_SHORT).show();
    }



    private void PerfomTermHostResponseFlow(int errReason) {


        PosApplication.getApp().oGPOS_MAIN.PerfomTermHostResponseFlow(mRecePacket,errReason,this);
        CommunicationsHandler.getInstance(mCommunicationInfo).closeConnection();


    /*    // old flow
        Bundle data = new Bundle();
        mUnpackPacket.Process_Rece_Packet(this, mRecePacket, data);
        mResponse = mUnpackPacket.getResponse();
        mResponseDetail = mUnpackPacket.getResponseDetail();

        if (mResponse != null && mResponse.equals("00")) {
            mProcNum++;
            if (mProcType == PacketProcessUtils.PACKET_PROCESS_ONLINE_INIT && mProcNum < 4) {
                String temp = null;
                if (mResponseDetail != null && mResponseDetail.length() > 2) {
                    temp = mResponseDetail.substring(0, 2);
                    Log.i(TAG, "temp = " + temp);
                }
                if (temp != null && temp.equals("01")) {
                    showResult(mResponse, mResponseDetail, errReason);
                } else {
                    getPacketAndSend();
                }
            } else if (mProcType == PacketProcessUtils.PACKET_PROCESS_PURCHASE && (mResponse != null) && (mResponse.equals("00"))) {
                //todo if comulative limit exeeded host response 196 we need to openpinpad for entering pin and resend transaction again
                byte[] field47 = mUnpackPacket.getField47();
             //   Display_printResult(mResponse, mResponseDetail, field47);

                if(!PosApplication.getApp().oGPosTransaction.m_is_mada)
                SAF_Info.SAVE_IN_SAF(PosApplication.getApp().oGPosTransaction);

            }
            else if(mProcType == PacketProcessUtils.PACKET_PROCESS_AUTHORISATION && (mResponse != null) && (mResponse.equals("00")))
            {
                if(!PosApplication.getApp().oGPosTransaction.m_is_mada) {
                    SAF_Info.SAVE_IN_SAF(PosApplication.getApp().oGPosTransaction);
                }
                else
                {
                    byte[] field47 = mUnpackPacket.getField47();
              //      Display_printResult(mResponse, mResponseDetail, field47);
                }

            }
            else if(mProcType == PacketProcessUtils.PACKET_PROCESS_AUTHORISATION_ADVICE && (mResponse != null) && (mResponse.equals("00")))
            {
                if(!PosApplication.getApp().oGPosTransaction.m_is_mada) {
                    SAF_Info.SAVE_IN_SAF(PosApplication.getApp().oGPosTransaction);
                }
                else
                {
                    byte[] field47 = mUnpackPacket.getField47();
                 //   Display_printResult(mResponse, mResponseDetail, field47);
                }

            }
            else {
                showResult(mResponse, mResponseDetail, errReason);
            }
        } else {
            showResult(mResponse, mResponseDetail, errReason);
        }*/
    }

    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TIME_UPDATE:
                    mProcTime--;
                    mTextTime.setText(String.valueOf(mProcTime));
                    if (mProcTime != 0) {
                        mHandle.sendEmptyMessageDelayed(MSG_TIME_UPDATE, MSG_TIME_UPDATE_DALAY);
                    } else {
                        showResult(mResponse, mResponseDetail, PacketProcessUtils.SOCKET_PROC_ERROR_REASON_RECE_TIME_OUT);
                        finish();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void Display_printResult(String response, String resDetail, POSTransaction POStrx) {
        Log.i(TAG, "showSuccessResult(), response = " + response + ", resDetail = " + resDetail + ", printDetail = " + POStrx);
        if (mProcType == PacketProcessUtils.PACKET_PROCESS_PURCHASE &&
                PosApplication.getApp().oGPosTransaction.m_enmTrxCardType != POSTransaction.CardType.MAG) {
            CardManager.getInstance().setRequestOnline(true, mResponse, PosApplication.getApp().oGPosTransaction.m_sICCRelatedTags);
        }
        mHandle.removeMessages(MSG_TIME_UPDATE);
        Intent intent = new Intent(this, Display_PrintActivity.class);
        intent.putExtra("result_response", response);
        intent.putExtra("result_resDetail", resDetail);
        intent.putExtra("POSTransaction", (Parcelable) POStrx);
        startActivity(intent);
        this.finish();
    }



    private void showResult(String response, String resDetail, int errReason) {
        Log.i(TAG, "showResult(), response = " + response + ", resDetail = " + resDetail + ", errReason = " + errReason);
        if (mProcType == PacketProcessUtils.PACKET_PROCESS_PURCHASE &&
                PosApplication.getApp().oGPosTransaction.m_enmTrxCardType != POSTransaction.CardType.MAG) {
            CardManager.getInstance().setRequestOnline(true, mResponse, PosApplication.getApp().oGPosTransaction.m_sICCRelatedTags);
        }
        mHandle.removeMessages(MSG_TIME_UPDATE);
        Intent intent = new Intent(this, ShowResultActivity.class);
        intent.putExtra(PacketProcessUtils.PACKET_PROCESS_TYPE, mProcType);
        intent.putExtra("result_response", mResponse);
        intent.putExtra("result_resDetail", mResponseDetail);
        intent.putExtra("result_errReason", errReason);
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
           // PerfomTermHostResponseFlow(0);old flow
            PosApplication.getApp().oGPOS_MAIN.PerfomTermHostResponseFlow(mRecePacket,0,this);
            CommunicationsHandler.getInstance(mCommunicationInfo).closeConnection();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        CommunicationsHandler.getInstance(mCommunicationInfo).closeConnection();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        CommunicationsHandler.getInstance(mCommunicationInfo).closeConnection();
        super.onBackPressed();
    }
}
