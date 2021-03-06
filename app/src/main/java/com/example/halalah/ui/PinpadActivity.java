package com.example.halalah.ui;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.InputType;
import android.util.Log;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.TextView;

import com.example.halalah.DeviceTopUsdkServiceManager;
import com.example.halalah.POSTransaction;
import com.example.halalah.POS_MAIN;
import com.example.halalah.PosApplication;
import com.example.halalah.R;
import com.example.halalah.SAF_Info;
import com.example.halalah.Utils;
import com.example.halalah.card.CardManager;
import com.example.halalah.iso8583.BCDASCII;
import com.example.halalah.util.CardSearchErrorUtil;
import com.example.halalah.util.HexUtil;
import com.example.halalah.util.PacketProcessUtils;
import com.topwise.cloudpos.aidl.buzzer.AidlBuzzer;
import com.topwise.cloudpos.aidl.pinpad.AidlPinpad;
import com.topwise.cloudpos.aidl.pinpad.GetPinListener;
import com.topwise.cloudpos.data.BuzzerCode;

import static com.topwise.cloudpos.data.PinpadConstant.KeyType.KEYTYPE_DUKPT_DES;
public class PinpadActivity extends Activity {
    private static final String TAG = Utils.TAGPUBLIC + PinpadActivity.class.getSimpleName();

    //private AidlPinpad mPinpadManager;
    private AidlPinpad mPinpad;
    private POSTransaction.CardType mCardType = POSTransaction.CardType.MAG;

    private byte[] mPinBlock = null;
    private String mPinInput;

    private String mCardNo;

    private String mAmount;

    private TextView mPinTips;
    private TextView mTestCardNo;
    private TextView mTestAmount;
    private TextView mPin;
    private Intent mIntent;
    private Bundle mParam;
    private byte[]   byteNewKSN;
    int     m_WorkKey = 0;//0x01;
    int    iRetRes = -1;

    private boolean mIsCancleInputKey = false;

    Activity pinpadact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinpad);

       /* ActionBar actionBar = this.getActionBar();
        actionBar.setTitle(R.string.title_consume);
*/
        mIntent = getIntent();
        mParam = mIntent.getExtras();

        mAmount = PosApplication.getApp().oGPosTransaction.m_sTrxAmount;
        mCardNo = PosApplication.getApp().oGPosTransaction.m_sPAN;
        mTestCardNo = (TextView) findViewById(R.id.card_num);
        mTestAmount = (TextView) findViewById(R.id.trad_amount);
        mPinTips = (TextView) findViewById(R.id.input_amount_tip);

        mPin = (TextView) findViewById(R.id.pin_num);
       // mPin.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        mTestCardNo.setText(getString(R.string.pin_tip_card_num) + mCardNo);
        mTestAmount.setText(getString(R.string.pin_tip_amount) + mAmount);

        //mPinpadManager = DeviceServiceManager.getInstance().getPinpadManager(0);
        mPinpad=DeviceTopUsdkServiceManager.getInstance().getPinpadManager(0);

        mCardType = PosApplication.getApp().oGPosTransaction.m_enmTrxCardType;
        CardManager.getInstance().finishPreActivity();

        showPinpadActivity(PosApplication.getApp().oGPosTransaction.m_sPAN, PosApplication.getApp().oGPosTransaction.m_sTrxAmount);

        CardManager.getInstance().finishPreActivity();
        CardManager.getInstance().initCardExceptionCallBack(mCallBack);
        pinpadact=this;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mPin.setText(mPinInput);
                    break;
                default:
                    break;
            }
        }
    };

    public void showPinpadActivity(final String cardNo, final String amount) {
        Log.i(TAG, "showPinpadActivity(), cardNo = " + cardNo);


        new Thread() {
            @Override
            public void run() {
                try {
                    mPinpad.setPinKeyboardMode(PosApplication.getApp().oGTerminal_Operation_Data.m_iPinKeyboardMode);// keyboard out of order =1 , in order =0
                    byteNewKSN = mPinpad.getDUKPTKsn(m_WorkKey, true);
                    Log.i(TAG,"getDUKPTKsn with  KSN [ "+ HexUtil.bcd2str(byteNewKSN)+" ]  and m_WorkKey ["+m_WorkKey+" ]");

                    mPinpad.getPin(SetPINParam(), mPinListener);
                    Log.i(TAG,"getPin with Bundle input [ "+SetPINParam().toString()+" ] Returned [ "+iRetRes+"]");
                    /*mPinpad.setPinKeyboardMode(1);
                    Log.d("topwise", "mPinListener: " + mPinListener);
                    mPinpad.getPin(getDukptParam(cardNo, amount), mPinListener);
                    AidlPinpad pinpad = DeviceTopUsdkServiceManager.getInstance().getPinpadManager(0);
                    Log.i(TAG, "pinpad: " + pinpad);
                    if (pinpad != null) {
                        try {
                            PosApplication.getApp().mConsumeData.setKsnValue(pinpad.getDUKPTKsn(1, false));
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d(TAG, "pinpad.getDUKPTKsn(): " + BCDASCII.bytesToHexString(pinpad.getDUKPTKsn(1, false)));*/
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private Bundle getDukptParam(String cardNo, String amount) {
        Log.i(TAG, "getParam()");

        final Bundle param = new Bundle();
        param.putInt("wkeyid", 0x01);
        param.putInt("keytype", 0x00);
        param.putInt("key_type", 0x0d);
        param.putByteArray("random", null);
        param.putInt("inputtimes", 1);
        param.putInt("minlength", 4);
        param.putInt("maxlength", 12);
        param.putString("pan", cardNo);
        param.putString("tips", "RMB:" + amount);
        param.putBoolean("is_lkl", false);
        param.putString("input_pin_mode", "4,5,6,7,8,9,10,11,12");  // Adding 0 for PIN by pass on Visa or MasterCard
        param.putInt("timeout",60);
        return param;
    }

    private Bundle SetPINParam() {
        Log.i(TAG, "getParam()");
        int     m_WorkKey = 0;//0x01;  // Towpise Key index
        int type = 0;
        if (mParam != null) {
            type = mParam.getInt("type", 3) == 3 ? 0 : 1;
        }

        Bundle bundle = new Bundle();
        byte   byteNewKSN;

        Log.i(TAG," GetUserPIN STarted Amount [ "+PosApplication.getApp().oGPosTransaction.m_sTrxAmount+ " ] and Card PAN [ "+PosApplication.getApp().oGPosTransaction.m_sPAN+" ]");

        // Set Key Info
        bundle.putInt("wkeyid", m_WorkKey);
        bundle.putInt("key_type", KEYTYPE_DUKPT_DES);  // DUKPT Key Type
        bundle.putInt("keytype", type);                // Trx Key Type (offline , Online)
        bundle.putByteArray("random", null);
        bundle.putInt("inputtimes", 1);
        bundle.putInt("minlength", 4);
        bundle.putInt("maxlength", 12);
        bundle.putString("pan", PosApplication.getApp().oGPosTransaction.m_sPAN);
        bundle.putString("tips", PosApplication.getApp().oGPosTransaction.m_sTrxAmount);
        bundle.putBoolean("is_lkl", false);
        bundle.putString("input_pin_mode", "0,4,5,6,7,8,9,10,11,12");
        bundle.putInt("timeout",60);

        return bundle;
    }

    /*private GetPinListener mGetPinListener = new GetPinListener.Stub() {

        @Override
        public void onInputKey(int len, String msg) throws RemoteException {
            Log.i(TAG, "onInputKey(), len = " + len + ", msg = " + msg);

            mPinInput = msg;
            mHandler.sendEmptyMessage(1);
        }

        @Override
        public void onConfirmInput(byte[] pin) throws RemoteException {
            Log.i(TAG, "onConfirmInput(), pin = " + BCDASCII.bytesToHexString(pin));

            PosApplication.getApp().mConsumeData.setPin(pin);
            if (ConsumeData.CARD_TYPE_MAG == mCardType) {
                Intent intent = new Intent(PinpadActivity.this, PacketProcessActivity.class);
                intent.putExtra(PacketProcessUtils.PACKET_PROCESS_TYPE, PacketProcessUtils.PACKET_PROCESS_CONSUME);
                startActivity(intent);
            } else {
                if (pin == null) {
                    CardManager.getInstance().setImportPin("000000");
                } else {
                    CardManager.getInstance().setImportPin(BCDASCII.bytesToHexString(pin));
                }
            }
        }

        @Override
        public void onCancelKeyPress() throws RemoteException {
            Log.i(TAG, "onCancelKeyPress()");
            mIsCancleInputKey = true;
            if (ConsumeData.CARD_TYPE_MAG != mCardType) {
                CardManager.getInstance().setImportPin("");
            }
            CardManager.getInstance().stopCardDealService(PinpadActivity.this);
            finish();
        }

        @Override
        public void onStopGetPin() throws RemoteException {
            Log.i(TAG, "onStopGetPin()");
            if (ConsumeData.CARD_TYPE_MAG != mCardType) {
                CardManager.getInstance().setImportPin("");
            }
            CardManager.getInstance().stopCardDealService(PinpadActivity.this);
            finish();
        }

        @Override
        public void onError(int errorCode) throws RemoteException {
            Log.i(TAG, "onError(), errorCode = " + errorCode);
            if (ConsumeData.CARD_TYPE_MAG != mCardType) {
                CardManager.getInstance().setImportPin("");
            } else {
                showResult(getString(R.string.search_card_trans_result_stop));
            }
            CardManager.getInstance().stopCardDealService(PinpadActivity.this);
            finish();
        }
    };*/


    private GetPinListener mPinListener = new GetPinListener.Stub() {
        @Override
        public void onInputKey(int len, String msg) throws RemoteException {
            Log.i(TAG, "onInputKey(), len = " + len + ", msg = " + msg);
          //  AidlBuzzer aidlBuzzer=DeviceTopUsdkServiceManager.getInstance().getBeepManager();
           // aidlBuzzer.beep(BuzzerCode.BUZZER_MODE_NORAML,0);

            AudioManager mAudioMgr = (AudioManager) getSystemService(AUDIO_SERVICE);
            mAudioMgr.playSoundEffect(AudioManager.FX_KEY_CLICK);
            mPinInput = msg;
            mHandler.sendEmptyMessage(1);
        }

        @Override
        public void onError(int errorCode) throws RemoteException {
            Log.i(TAG, "onError(), errorCode = " + errorCode);
            if (POSTransaction.CardType.MAG != mCardType) {
                CardManager.getInstance().setImportPin("");
            } else {
                showResult(getString(R.string.search_card_trans_result_stop));
            }
            CardManager.getInstance().stopCardDealService(PinpadActivity.this);
            finish();
        }

        @Override
        public void onConfirmInput(byte[] pin) throws RemoteException {
            Log.i(TAG, "onConfirmInput(), pin = " + BCDASCII.bytesToHexString(pin));

            boolean isOnline = true;
            if (mIntent != null) {
                Bundle bundle = mIntent.getExtras();
                if (bundle != null) {
                    isOnline = bundle.getBoolean("online");
                }
            }
            Log.d(TAG, "isOnline: " + isOnline);
            if(pin!=null) {
                PosApplication.getApp().oGPosTransaction.m_sTrxPIN = BCDASCII.bytesToHexString(pin);
            }
            if (isOnline) {
                //socket connection
                Bundle bundle = new Bundle();
                if(PosApplication.getApp().oGPosTransaction.m_is_mada) //MADA
                {
                    switch (PosApplication.getApp().oGPosTransaction.m_enmTrxType) {

                        case PURCHASE:

                            bundle.putInt(PacketProcessUtils.PACKET_PROCESS_TYPE, PacketProcessUtils.PACKET_PROCESS_PURCHASE);
                            if (POSTransaction.CardType.MANUAL == mCardType) {
                                PosApplication.getApp().oGPosTransaction.m_enmTrxCVM = POSTransaction.CVM.ONLINE_PIN;
                            }

                            CardManager.getInstance().startActivity(PinpadActivity.this, bundle, PacketProcessActivity.class);
                        case REFUND:
                            bundle.putInt(PacketProcessUtils.PACKET_PROCESS_TYPE, PacketProcessUtils.PACKET_PROCESS_REFUND);
                            if (POSTransaction.CardType.MANUAL != mCardType) {
                                CardManager.getInstance().startActivity(PinpadActivity.this, bundle, PacketProcessActivity.class);
                            }


                        case PURCHASE_ADVICE:
                            bundle.putInt(PacketProcessUtils.PACKET_PROCESS_TYPE, PacketProcessUtils.PACKET_PROCESS_PURCHASE_ADVICE);
                            //mada preauthcompeletion have to go online and not saved in saf
                                CardManager.getInstance().startActivity(PinpadActivity.this, bundle, PacketProcessActivity.class);

                        case AUTHORISATION_ADVICE:
                        case AUTHORISATION:
                            bundle.putInt(PacketProcessUtils.PACKET_PROCESS_TYPE, PacketProcessUtils.PACKET_PROCESS_AUTHORISATION);
                            if (POSTransaction.CardType.MANUAL != mCardType) {
                                CardManager.getInstance().startActivity(PinpadActivity.this, bundle, PacketProcessActivity.class);
                            }


                        case PURCHASE_WITH_NAQD:
                        case AUTHORISATION_EXTENSION:
                            bundle.putInt(PacketProcessUtils.PACKET_PROCESS_TYPE, PacketProcessUtils.PACKET_PROCESS_AUTHORISATION);
                            if (POSTransaction.CardType.MANUAL != mCardType) {
                                CardManager.getInstance().startActivity(PinpadActivity.this, bundle, PacketProcessActivity.class);
                            }
                        case CASH_ADVANCE:
                            break;
                        case SADAD_BILL:
                            if (mCardType==POSTransaction.CardType.ICC ||mCardType==POSTransaction.CardType.CTLS||mCardType==POSTransaction.CardType.MAG) {
                                CardManager.getInstance().startActivity(PinpadActivity.this, bundle, PacketProcessActivity.class);
                            }

                    }
                }else //ICS
                    {
                        switch (PosApplication.getApp().oGPosTransaction.m_enmTrxType) {

                            case PURCHASE:

                                bundle.putInt(PacketProcessUtils.PACKET_PROCESS_TYPE, PacketProcessUtils.PACKET_PROCESS_PURCHASE);
                                if (POSTransaction.CardType.MANUAL == mCardType) {
                                    PosApplication.getApp().oGPosTransaction.m_enmTrxCVM = POSTransaction.CVM.ONLINE_PIN;
                                }

                                CardManager.getInstance().startActivity(PinpadActivity.this, bundle, PacketProcessActivity.class);

                            case REFUND:

                                bundle.putInt(PacketProcessUtils.PACKET_PROCESS_TYPE, PacketProcessUtils.PACKET_PROCESS_REFUND);
                                if(PosApplication.getApp().oGPosTransaction.m_card_scheme.m_sOffline_Refund_PreAuthorization_Capture_Service_Indicator=="1") {
                                    SAF_Info.SAVE_IN_SAF(PosApplication.getApp().oGPosTransaction);

                                    CardManager.getInstance().startActivity(PinpadActivity.this, bundle, ShowResultActivity.class);
                                }
                                break;
                            case PURCHASE_ADVICE:
                                bundle.putInt(PacketProcessUtils.PACKET_PROCESS_TYPE, PacketProcessUtils.PACKET_PROCESS_PURCHASE_ADVICE);
                                if (POSTransaction.CardType.MANUAL == mCardType ||mCardType==POSTransaction.CardType.ICC ||mCardType==POSTransaction.CardType.CTLS||mCardType==POSTransaction.CardType.MAG)
                                {                                                                                                                 //Manual Entry Attended POS Preauthorization Completion Transaction
                                    CardManager.getInstance().startActivity(PinpadActivity.this, bundle, PacketProcessActivity.class);   //for IBCS Scheme Cardsapprove a manual entry attended POS terminal
                                }                                                                                                                 // preauthorization capture online without storing it in the SAF file locally at the terminal.

                                 break;
                            case AUTHORISATION_ADVICE:
                            case AUTHORISATION:
                                bundle.putInt(PacketProcessUtils.PACKET_PROCESS_TYPE, PacketProcessUtils.PACKET_PROCESS_AUTHORISATION);
                                if (mCardType== POSTransaction.CardType.MAG ||mCardType==POSTransaction.CardType.CTLS)
                                {                                                                                                                 //Manual Entry Attended POS Preauthorization Completion Transaction
                                    CardManager.getInstance().startActivity(PinpadActivity.this, bundle, PacketProcessActivity.class);   //for IBCS Scheme Cardsapprove a manual entry attended POS terminal
                                }

                            case PURCHASE_WITH_NAQD:
                            case AUTHORISATION_EXTENSION:
                            case CASH_ADVANCE:
                        }


                }
                /*byte[] sendData = PosApplication.getApp().mConsumeData.getICData();
                Log.d(TAG, BCDASCII.bytesToHexString(sendData));
                JsonAndHttpsUtils.sendJsonData(mContext, BCDASCII.bytesToHexString(sendData));*/
            } else {


                if (POSTransaction.CardType.MAG == mCardType) {
                    Intent intent = new Intent(PinpadActivity.this, PacketProcessActivity.class);
                    intent.putExtra(PacketProcessUtils.PACKET_PROCESS_TYPE, PacketProcessUtils.PACKET_PROCESS_PURCHASE);
                    startActivity(intent);
                }
                else if(POSTransaction.CardType.MANUAL == mCardType){
                    PosApplication.getApp().oGPosTransaction.m_enmTrxCVM= POSTransaction.CVM.ONLINE_PIN;
                    Intent intent = new Intent(PinpadActivity.this, PacketProcessActivity.class);
                    intent.putExtra(PacketProcessUtils.PACKET_PROCESS_TYPE, PacketProcessUtils.PACKET_PROCESS_PURCHASE);
                    startActivity(intent);
                }

                else {
                    if (pin == null) {
                        CardManager.getInstance().setImportPin("bypass");
                    } else {
                        CardManager.getInstance().setImportPin(BCDASCII.bytesToHexString(pin));
                    }
                }
            }
        }

        @Override
        public void onCancelKeyPress() throws RemoteException {
            Log.i(TAG, "onCancelKeyPress()");
            mIsCancleInputKey = true;
            if (POSTransaction.CardType.MAG != mCardType) {
                CardManager.getInstance().setImportPin("");
            }
            CardManager.getInstance().stopCardDealService(PinpadActivity.this);
            finish();
        }

        @Override
        public void onStopGetPin() throws RemoteException {
            Log.i(TAG, "onStopGetPin()");
            if (POSTransaction.CardType.MAG != mCardType) {
                CardManager.getInstance().setImportPin("");
            }
            CardManager.getInstance().stopCardDealService(PinpadActivity.this);
            finish();
        }
    };

    CardManager.CardExceptionCallBack mCallBack = new CardManager.CardExceptionCallBack() {
        @Override
        public void callBackTimeOut() {
            Log.i(TAG, "onDestroy() callBackTimeOut");
        }

        @Override
        public void callBackError(int errorCode) {
            Log.i(TAG, "onDestroy() callBackError");
        }

        @Override
        public void callBackCanceled() {
            Log.i(TAG, "onDestroy() callBackCanceled");
        }

        @Override
        public void callBackTransResult(int result) {
            Log.d(TAG, "callBackTransResult result : " + result);
            if (mIsCancleInputKey) {
                return;
            }
            String resultDetail = null;
            if (result == CardSearchErrorUtil.TRANS_APPROVE) {

                //todo check if this offline approved transcation
                if(PosApplication.getApp().oGPosTransaction.m_enmTrxCardType== POSTransaction.CardType.ICC)
                PosApplication.getApp().oGPOS_MAIN.finalizing_EMV_transaction(pinpadact);
                resultDetail = getString(R.string.search_card_trans_result_approval);
            } else if (result == CardSearchErrorUtil.TRANS_REASON_REJECT) {

                resultDetail = getString(R.string.search_card_trans_result_reject);
                POS_MAIN.CheckandSaveInSAF(PosApplication.getApp().oGPosTransaction,true);
                PosApplication.getApp().oGPOS_MAIN.DeSAF(SAF_Info.DESAFtype.PARTIAL);
            } else if (result == CardSearchErrorUtil.TRANS_REASON_STOP) {
                resultDetail = getString(R.string.search_card_trans_result_stop);
              /*  POS_MAIN.CheckandSaveInSAF(PosApplication.getApp().oGPosTransaction,true);
                PosApplication.getApp().oGPOS_MAIN.DeSAF(SAF_Info.DESAFtype.PARTIAL);*/

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
            PinpadActivity.this.finish();
        }
    };

    private void showResult(String detail) {
        Log.i(TAG, "showResult(), detail = " + detail);

        if(detail.equals(getString(R.string.search_card_trans_result_approval))){
            Intent intent = new Intent(this, Display_PrintActivity.class);
            intent.putExtra("result_errReason", 0);
            intent.putExtra("result_response", "00");

         //   startActivity(intent);
        } else if(detail.equals(getString(R.string.search_card_trans_result_reject))) {
            Intent intent = new Intent(this, ShowResultActivity.class);
            intent.putExtra(PacketProcessUtils.PACKET_PROCESS_TYPE, PacketProcessUtils.PACKET_PROCESS_PURCHASE);
            intent.putExtra("result_errReason", 0);
            intent.putExtra("result_resDetail", detail);
            intent.putExtra("result_response", "00");
            startActivity(intent);
        }

        this.finish();
    }
}