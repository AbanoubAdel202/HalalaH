package com.example.halalah.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.halalah.POSTransaction;
import com.example.halalah.POS_MAIN;
import com.example.halalah.PosApplication;
import com.example.halalah.R;

import java.io.EOFException;
import java.io.IOException;

public class AmountInputActivity extends Activity implements View.OnClickListener{
    private static final String TAG = AmountInputActivity.class.getSimpleName();

    private static final int MSG_TIME_UPDATE = 100;

    private TextView mTextAmount;
    private Button mBtnConfirm;
    private StringBuilder mAmountBuilder;
    private StringBuilder mAmount;

    private int mTime = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate()");
        setContentView(R.layout.activity_amount_input);

        /*ActionBar actionBar = this.getActionBar();
        actionBar.setTitle(R.string.title_consume);*/
        mBtnConfirm = (Button) findViewById(R.id.btn_search_card);
        mTextAmount = (TextView) findViewById(R.id.edit_amount);

        mAmountBuilder = new StringBuilder("R");

        mBtnConfirm.setText(R.string.amount_input_search_card);
        mHandle.sendEmptyMessage(MSG_TIME_UPDATE);
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
                if (mAmountBuilder.length() > 1) {
                    mAmountBuilder.delete(mAmountBuilder.length() - 1, mAmountBuilder.length());
                }
                setText(null);
                break;
            case R.id.btn_clear:
                mAmountBuilder.delete(1, mAmountBuilder.length());
                setText(null);
                break;
            case R.id.btn_cancle:
                finish();
                break;
            case R.id.btn_search_card:
                //todo : mostafa to return to POS_main with AMount using callback
                String sAmount=mAmount.substring(1);


                    PosApplication.getApp().oGPosTransaction.m_sTrxAmount=sAmount;
                    try {
                        Intent intent = new Intent(this, SearchCardActivity.class);
                        startActivity(intent);
                    }
                     catch (Exception e)
                                {
                                    Log.i(TAG,  e.toString());
                                }

               finish();
            default:
                break;
        }
    }

    private void setText(String charNum) {
        String temp = mAmountBuilder.toString();
        Log.i(TAG, "temp = "+temp);

        if (temp.length() > 12) {
            return;
        }

        if (charNum != null) {
            mAmountBuilder.append(charNum);
        }

        temp = mAmountBuilder.toString();
        Log.i(TAG, "temp = "+temp);

        if (temp.equals("R 0")) {
            temp = "R";
            mAmountBuilder.delete(1, 2);
        }
        mAmount = new StringBuilder(temp);
        Log.i(TAG, "mAmount before = "+mAmount);
        for (int i = 0 ; i < 4 - mAmountBuilder.length(); i++) {
            mAmount.insert(1, "0");
        }
        Log.i(TAG, "mAmount = "+mAmount);
        mAmount.insert(mAmount.length()-2, ".");
        mTextAmount.setText(mAmount);

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