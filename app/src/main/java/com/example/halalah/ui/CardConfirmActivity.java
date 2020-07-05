package com.example.halalah.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.halalah.POSTransaction;
import com.example.halalah.PosApplication;
import com.example.halalah.R;
import com.example.halalah.Utils;
import com.example.halalah.card.CardManager;
import com.example.halalah.util.CardSearchErrorUtil;
import com.example.halalah.util.PacketProcessUtils;

public class CardConfirmActivity extends Activity implements View.OnClickListener{
    private static final String TAG = Utils.TAGPUBLIC + CardConfirmActivity.class;

    private TextView mTextCardNo;
    private String mCardNo;
    private String mAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate()");

        setContentView(R.layout.activity_card_confirm);

       /* ActionBar actionBar = this.getActionBar();
        actionBar.setTitle(R.string.title_consume);
        */
        mAmount = PosApplication.getApp().oGPosTransaction.m_sTrxAmount;
        mCardNo = PosApplication.getApp().oGPosTransaction.m_sPAN;

        mTextCardNo = (TextView) findViewById(R.id.card_num);
        mTextCardNo.setText(mCardNo);

        CardManager.getInstance().finishPreActivity();
        CardManager.getInstance().initCardExceptionCallBack(mCallBack);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancle:
                if (PosApplication.getApp().oGPosTransaction.m_enmTrxCardType != POSTransaction.CardType.MAG)
                    CardManager.getInstance().setConfirmCardInfo(false);
                finish();
                break;
            case R.id.btn_ok:
                if (PosApplication.getApp().oGPosTransaction.m_enmTrxCardType == POSTransaction.CardType.MAG) {
                    Intent intent = new Intent(this, PinpadActivity.class);
                    startActivity(intent);
                } else {
                    CardManager.getInstance().setConfirmCardInfo(true);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (PosApplication.getApp().oGPosTransaction.m_enmTrxCardType != POSTransaction.CardType.MAG) {
            CardManager.getInstance().setConfirmCardInfo(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
    }

    CardManager.CardExceptionCallBack mCallBack = new CardManager.CardExceptionCallBack() {
        @Override
        public void callBackTimeOut() {
            Log.i(TAG, "onDestroy()");
        }

        @Override
        public void callBackError(int errorCode) {
            Log.i(TAG, "onDestroy()");
        }

        @Override
        public void callBackCanceled() {
            Log.i(TAG, "onDestroy()");
        }

        @Override
        public void callBackTransResult(int result) {
            Log.i(TAG, "onDestroy()");
            Log.d(TAG, "callBackTransResult result : " + result);
            String resultDetail = null;
            if (result == CardSearchErrorUtil.TRANS_REASON_REJECT) {
                resultDetail = getString(R.string.search_card_trans_result_reject);
            } else if (result == CardSearchErrorUtil.TRANS_REASON_STOP) {
                resultDetail = getString(R.string.search_card_trans_result_stop);
            } else if (result == CardSearchErrorUtil.TRANS_REASON_FALLBACK) {
                resultDetail = getString(R.string.search_card_trans_result_fallback);
            } else if (result == CardSearchErrorUtil.TRANS_REASON_OTHER_UI) {
                resultDetail = getString(R.string.search_card_trans_result_other_ui);
            } else if (result == CardSearchErrorUtil.TRANS_REASON_STOP_OTHERS) {
                resultDetail = getString(R.string.search_card_trans_result_others);
            }
            showResult(resultDetail);
        }

        @Override
        public void finishPreActivity() {
            Log.i(TAG, "onDestroy()");
            CardConfirmActivity.this.finish();
        }
    };

    private void showResult(String detail) {
        Log.i(TAG, "showResult(), detail = "+detail);
        Intent intent = new Intent(this, ShowResultActivity.class);
        intent.putExtra(PacketProcessUtils.PACKET_PROCESS_TYPE, PacketProcessUtils.PACKET_PROCESS_PURCHASE);
        intent.putExtra("result_resDetail", detail);
        startActivity(intent);
        this.finish();
    }
}
