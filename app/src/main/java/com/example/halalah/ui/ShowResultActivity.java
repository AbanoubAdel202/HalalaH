package com.example.halalah.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.halalah.DeviceTopUsdkServiceManager;
import com.example.halalah.POS_MAIN;
import com.example.halalah.PosApplication;
import com.example.halalah.R;
import com.example.halalah.SAF_Info;
import com.example.halalah.Utils;
import com.example.halalah.util.PacketProcessUtils;
import com.topwise.cloudpos.aidl.led.AidlLed;

public class ShowResultActivity extends Activity implements View.OnClickListener{
    private static final String TAG = Utils.TAGPUBLIC + ShowResultActivity.class.getSimpleName();

    private ImageView mImageResult;
    private TextView mTextResponse;
    private TextView mTextResDetail;

    private int mProcType;
    public int mErrorReson = 0;
    private String mResponse;
    private String mResponseDetail;

    private AidlLed mAidlLed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate()");
        setContentView(R.layout.activity_show_result);

        mImageResult = findViewById(R.id.image_result);
        mTextResponse = findViewById(R.id.result_response);
        mTextResDetail = findViewById(R.id.result_detail);

        Bundle data = getIntent().getExtras();
        mProcType = data.getInt(PacketProcessUtils.PACKET_PROCESS_TYPE);
        mResponse = data.getString("result_response");
        mResponseDetail = data.getString("result_resDetail");
        mErrorReson = data.getInt("result_errReason");
        Log.i(TAG, "mResponse = " + mResponse + ", mResponseDetail" + mResponseDetail + ", mErrorReson" + mErrorReson);

        setActionBarText();

        showResult();
    }

    private void setActionBarText() {
        ActionBar actionBar = this.getActionBar();
        if (mProcType == PacketProcessUtils.PACKET_PROCESS_ONLINE_INIT) {
            actionBar.setTitle(getString(R.string.online_init));
        }
        /* else if (mProcType == PacketProcessUtils.PACKET_PROCESS_PARAM_TRANS) {
            actionBar.setTitle(getString(R.string.text_param_trans));
        } else if (mProcType == PacketProcessUtils.PACKET_PROCESS_IC_PARA_DOWNLOAD) {
            actionBar.setTitle(getString(R.string.text_ic_para_download));
        }*/
        else if (mProcType == PacketProcessUtils.PACKET_PROCESS_PURCHASE) {
          //  actionBar.setTitle(getString(R.string.Purchase));
        }
    }

    private void showResult() {
        if (mErrorReson > 0) {
            if (mErrorReson == PacketProcessUtils.SOCKET_PROC_ERROR_REASON_IP_PORT) {
                mResponseDetail = getString(R.string.result_error_ip_or_port);
            } else if (mErrorReson == PacketProcessUtils.SOCKET_PROC_ERROR_REASON_CONNE) {
                mResponseDetail = getString(R.string.result_error_conn);
            } else if (mErrorReson == PacketProcessUtils.SOCKET_PROC_ERROR_REASON_SEND) {
                mResponseDetail = getString(R.string.result_error_send);
            } else if (mErrorReson == PacketProcessUtils.SOCKET_PROC_ERROR_REASON_RECE) {
                mResponseDetail = getString(R.string.result_error_rece);
            } else if (mErrorReson == PacketProcessUtils.SOCKET_PROC_ERROR_REASON_RECE_TIME_OUT) {
                mResponseDetail = getString(R.string.result_error_rece_time_out);
                POS_MAIN.CheckandSaveInSAF(PosApplication.getApp().oGPosTransaction,true);
                PosApplication.getApp().oGPOS_MAIN.DeSAF(SAF_Info.DESAFtype.PARTIAL);
            }
            mImageResult.setImageDrawable(getDrawable(R.drawable.trans_faild));
        } else {
            if (mProcType == PacketProcessUtils.PACKET_PROCESS_ONLINE_INIT) {
                if (mResponse != null && mResponse.equals("00")) {
                    mResponse = null;
                    String temp = null;
                    if (mResponseDetail != null && mResponseDetail.length() > 2) {
                        temp = mResponseDetail.substring(0, 2);
                        Log.i(TAG, "temp = " + temp);
                    }
                    if (temp != null && temp.equals("01")) {
                        mResponseDetail = mResponseDetail.substring(2);
                        mImageResult.setImageDrawable(getDrawable(R.drawable.trans_faild));
                    } else {
                        mResponseDetail = getString(R.string.result_sucess_online_init);
                        mImageResult.setImageDrawable(getDrawable(R.drawable.trans_success));
                    }
                } else {
                    mResponseDetail = getString(R.string.result_failed_online_init);
                    mImageResult.setImageDrawable(getDrawable(R.drawable.trans_faild));
                }
            }
            else if(mProcType == PacketProcessUtils.PACKET_PROCESS_PURCHASE){

                mResponseDetail = getString(R.string.timeout_error);
                mImageResult.setImageDrawable(getDrawable(R.drawable.trans_faild));
                PosApplication.getApp().oGTerminal_Operation_Data.breversal_flg = true;
            }
            else {
                if (mResponse == null || !mResponse.equals("00")) {
                    if (mResponseDetail == null) {
                        mResponseDetail = getString(R.string.result_error_unkown);
                        PosApplication.getApp().oGTerminal_Operation_Data.breversal_flg = true;
                    }
                    mImageResult.setImageDrawable(getDrawable(R.drawable.trans_faild));
                }
            }
        }

        mTextResponse.setText(mResponse);
        mTextResDetail.setText(mResponseDetail);
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick()");
        switch (v.getId()) {
            case R.id.btn_exit:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAidlLed = DeviceTopUsdkServiceManager.getInstance().getLedManager();
        try {
            if(mAidlLed != null){
                mAidlLed.setLed(0 , false);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
    }
}
