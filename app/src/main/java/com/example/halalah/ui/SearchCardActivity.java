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
import android.widget.Toast;

import com.example.halalah.POSTransaction;
import com.example.halalah.PanInputActivity;
import com.example.halalah.PosApplication;
import com.example.halalah.R;
import com.example.halalah.Utils;
import com.example.halalah.card.CardManager;
import com.example.halalah.connect.CommunicationsHandler;
import com.example.halalah.storage.CommunicationInfo;
import com.example.halalah.util.CardSearchErrorUtil;
import com.example.halalah.util.PacketProcessUtils;

public class SearchCardActivity extends Activity{
    private static final String TAG = Utils.TAGPUBLIC + SearchCardActivity.class.getSimpleName();

    private static final int MSG_TIME_UPDATE = 100;

    private TextView mTextAmount;
    private TextView mTextTime;
    private String mAmount;
    private Toast mToast;
    private Button  mmanualbtn;
    private Activity searchactivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate()");
        setContentView(R.layout.activity_search_card);


        //PosApplication.getApp().getDeviceManager();
        mTextAmount = (TextView) findViewById(R.id.trad_amount);
        mTextTime = (TextView) findViewById(R.id.text_time);
        mmanualbtn=(Button)findViewById(R.id.manual_card_btn);
        mmanualbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId())
                {
                    case R.id.manual_card_btn:
                        PosApplication.getApp().oGPosTransaction.m_enmTrxCardType= POSTransaction.CardType.MANUAL;
                        Intent PAN_Entry = new Intent(getApplicationContext(), PanInputActivity.class);
                        startActivity(PAN_Entry);
                        break;

                }
            }
        });
        mAmount = PosApplication.getApp().oGPosTransaction.m_sTrxAmount;
        mTextAmount.setText(getString(R.string.trans_amount)+mAmount);

        CardManager.getInstance().startCardDealService(this);
        CardManager.getInstance().initCardExceptionCallBack(exceptionCallBack);
        searchactivity=this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");
    }

    Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "handleMessage what : " + msg.what);
            switch (msg.what) {
                case CardSearchErrorUtil.CARD_SEARCH_ERROR_REASON_MAG_READ:
                    showTips(R.string.search_card_error_msr_read);
                    break;
                case CardSearchErrorUtil.CARD_SEARCH_ERROR_REASON_MAG_EMV:
                    showTips(R.string.search_card_error_msr_is_ic);
                    break;
                case CardSearchErrorUtil.CARD_SEARCH_ERROR_REASON_MAG_EMV_s:
                    showTips(R.string.search_card_error_msr_is_ic);
                    break;
                default:
                    break;
            }
            CardManager.getInstance().startCardDealService(SearchCardActivity.this);
        }
    };

    private void showTips(int resId) {
        if (null != mToast) {
            mToast.cancel();
        }
        mToast = Toast.makeText(SearchCardActivity.this, getString(resId), Toast.LENGTH_SHORT);
        mToast.show();
    }

    private CardManager.CardExceptionCallBack exceptionCallBack = new CardManager.CardExceptionCallBack() {
        @Override
        public void callBackTimeOut() {
            Log.d(TAG, "callBackTimeOut");
            SearchCardActivity.this.finish();
        }

        @Override
        public void callBackError(int errorCode) {
            Log.d(TAG, "callBackError errorCode : " + errorCode);
            mHandle.sendEmptyMessage(errorCode);
        }


        @Override
        public void callBackCanceled() {
            Log.d(TAG, "callBackCanceled");
        }

        @Override
        public void callBackTransResult(int result) {
            Log.d(TAG, "callBackTransResult result : " + result);
            String resultDetail = null;
            if (result == CardSearchErrorUtil.TRANS_APPROVE) {
                resultDetail = "Transaction Approval";
                //should we add final flow here
                PosApplication.getApp().oGPOS_MAIN.finalizing_EMV_transaction(searchactivity);
            }
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
            Log.d(TAG, "finishPreActivity");
            SearchCardActivity.this.finish();
        }
    };

    private void showResult(String detail) {
        Log.i(TAG, "showResult(), detail = "+detail);
        if(detail.equals(getString(R.string.search_card_trans_result_approval))){
            Intent intent = new Intent(this, Display_PrintActivity.class);
            intent.putExtra("result_errReason", 0);
            intent.putExtra("result_response", "00");

            startActivity(intent);
        } else {
            Intent intent = new Intent(this, ShowResultActivity.class);
            intent.putExtra(PacketProcessUtils.PACKET_PROCESS_TYPE, PacketProcessUtils.PACKET_PROCESS_PURCHASE);
            intent.putExtra("result_resDetail", detail);
            startActivity(intent);
        }
            this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed");
        CardManager.getInstance().stopCardDealService(this);
        CommunicationsHandler.getInstance(new CommunicationInfo(this)).closeConnection();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
    }
}
