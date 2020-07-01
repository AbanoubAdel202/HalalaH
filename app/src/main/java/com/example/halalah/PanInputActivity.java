package com.example.halalah;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.halalah.ui.P_NAQD_InputActivity;
import com.example.halalah.ui.PinpadActivity;
import com.example.halalah.ui.Refund_InputActivity;
import com.example.halalah.ui.SearchCardActivity;

public class PanInputActivity extends Activity implements View.OnClickListener{
    private static final String TAG = PanInputActivity.class.getSimpleName();

    private static final int MSG_TIME_UPDATE = 100;

    private TextView mTextPAN;

    private Button mBtnConfirm;
    private StringBuilder mPANBuilder;
    private StringBuilder mPAN;



    private int mTime = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate()");
        setContentView(R.layout.activity_pan_input);

        /*ActionBar actionBar = this.getActionBar();
        actionBar.setTitle(R.string.title_consume);*/
        mBtnConfirm = (Button) findViewById(R.id.btn_search_card);
        mTextPAN = (TextView) findViewById(R.id.edit_pan);

        mPANBuilder = new StringBuilder("");

        mBtnConfirm.setText(R.string.amount_input_search_card);
        mHandle.sendEmptyMessage(MSG_TIME_UPDATE);

        /*if( PosApplication.getApp().oGPosTransaction.m_enmTrxType== POSTransaction.TranscationType.PURCHASE
                ||PosApplication.getApp().oGPosTransaction.m_enmTrxType== POSTransaction.TranscationType.AUTHORISATION
                ||PosApplication.getApp().oGPosTransaction.m_enmTrxType==POSTransaction.TranscationType.AUTHORISATION_ADVICE
                ||PosApplication.getApp().oGPosTransaction.m_enmTrxType==POSTransaction.TranscationType.PURCHASE_ADVICE) {


        }
         else if (PosApplication.getApp().oGPosTransaction.m_enmTrxType== POSTransaction.TranscationType.PURCHASE_WITH_NAQD)
        {
           // todo show naqd amount


        }
        else if (PosApplication.getApp().oGPosTransaction.m_enmTrxType== POSTransaction.TranscationType.REFUND) {

            //TODO show RRN and other DATA activity
        }
        else if(PosApplication.getApp().oGPosTransaction.m_enmTrxType== POSTransaction.TranscationType.AUTHORISATION_EXTENSION) {

        }*/
    }

    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TIME_UPDATE:
                    if (mTime == 0) {
                        finish();
                    } else {
                        mHandle.sendEmptyMessageDelayed(MSG_TIME_UPDATE, 1000);
                    }
                    mTime--;
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_0:
                setText("0");
                break;
            case R.id.btn_1:
                setText("1");
                break;
            case R.id.btn_2:
                setText("2");
                break;
            case R.id.btn_3:
                setText("3");
                break;
            case R.id.btn_4:
                setText("4");
                break;
            case R.id.btn_5:
                setText("5");
                break;
            case R.id.btn_6:
                setText("6");
                break;
            case R.id.btn_7:
                setText("7");
                break;
            case R.id.btn_8:
                setText("8");
                break;
            case R.id.btn_9:
                setText("9");
                break;
            case R.id.btn_back:
                if (mPANBuilder.length() > 1) {
                    mPANBuilder.delete(mPANBuilder.length() - 1, mPANBuilder.length());
                }
                setText(null);
                break;
            case R.id.btn_clear:
                mPANBuilder.delete(1, mPANBuilder.length());
                setText(null);
                break;
            case R.id.btn_cancle:
                finish();
                break;
            case R.id.btn_search_card:




                    PosApplication.getApp().oGPosTransaction.m_sPAN = mPAN.toString();;
            Intent Pinpadact=new Intent(getApplicationContext(), PinpadActivity.class);
            startActivity(Pinpadact);
                    finish();

            default:
                break;
        }
    }

    private void setText(String charNum) {
        String temp = mPANBuilder.toString();
        Log.i(TAG, "temp = "+temp);

        if (temp.length() > 19) {
            return;
        }

        if (charNum != null) {
            mPANBuilder.append(charNum);
        }

        temp = mPANBuilder.toString();
        Log.i(TAG, "temp = "+temp);

        if (temp.equals("R 0")) {
            temp = "R";
            mPANBuilder.delete(1, 2);
        }
        mPAN = new StringBuilder(temp);
        Log.i(TAG, "mPAN before = "+mPAN);


        mTextPAN.setText(mPAN);

        temp = temp.substring(1);
        Log.i(TAG, "temp.isEmpty() = "+temp.isEmpty());
        if (temp.isEmpty() || Long.parseLong(temp) == 0) {
            Log.i(TAG, "false");
            mBtnConfirm.setEnabled(false);
        } else {
            Log.i(TAG, "true");
            mBtnConfirm.setEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
        mHandle.removeMessages(MSG_TIME_UPDATE);
    }
}