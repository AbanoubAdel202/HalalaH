package com.example.halalah.ui.admin_settings;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.halalah.DeviceTopUsdkServiceManager;
import com.example.halalah.R;
import com.example.halalah.iso8583.BCDASCII;
import com.topwise.cloudpos.aidl.pinpad.AidlPinpad;

/**
 * 导入主密钥使用
 *
 * @author xukun
 * @version 1.0.0
 * @date 18-6-8
 */

public class ImportKeyActivity extends Activity implements View.OnClickListener {

    private EditText mKeyEdt;
    private EditText mKsnEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_key);
        initView();
    }

    private void initView() {
        mKeyEdt = (EditText) findViewById(R.id.master_key);
        mKsnEdt = (EditText) findViewById(R.id.ksn_value);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.term_para_cancle:
                finish();
                break;
            case R.id.term_para_save:
                String key = mKeyEdt.getText().toString();
                String ksn = mKsnEdt.getText().toString();
                key = "0123456789ABCDEFFEDCBA9876543210";
                ksn = "FFFF9876543210E00000";
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(ksn)) {
                    Log.d("topwise", "key length: " + key.length() +
                            " ksn length: " + ksn.length());
                    if (key.length() % 8 == 0 && ksn.length() == 20) {
                        AidlPinpad pinpad = DeviceTopUsdkServiceManager.getInstance().getPinpadManager(0);
                        try {
                            boolean isSuccess = pinpad.loadDuKPTkey(0,1, BCDASCII.hexStringToBytes(key),
                                    BCDASCII.hexStringToBytes(ksn));
                            if (isSuccess) {
                                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(this, "length error", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
