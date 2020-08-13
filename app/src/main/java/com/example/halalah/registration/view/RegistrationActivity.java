package com.example.halalah.registration.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.example.halalah.PosApplication;
import com.example.halalah.R;
import com.example.halalah.connect.CommunicationsHandler;
import com.example.halalah.registration.RegistrationData;
import com.example.halalah.registration.RegistrationManager;
import com.example.halalah.storage.CommunicationInfo;

import org.parceler.Parcels;

public class RegistrationActivity extends AppCompatActivity {

    RegistrationData mRegistrationData;
    private EditText etRandomLength;
    private EditText etTRSMID;
    private Spinner spVendor;
    private Spinner spSama;
    private Button btnProceed;
    private CommunicationsHandler mCommunicationsHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("mRegistrationData", "" + this.hashCode());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initViews();
        mCommunicationsHandler =
                CommunicationsHandler.getInstance(new CommunicationInfo(PosApplication.getApp().getApplicationContext()));

        btnProceed.setOnClickListener(v -> proceed());
    }

    private void proceed() {

        String trsmid = etTRSMID.getText().toString();
        if (TextUtils.isEmpty(trsmid) || trsmid.length() != 6) {
            showError(R.string.invalid_trsmid);
            return;
        }

        String randomLengthString = etRandomLength.getText().toString();
        int randomLengthInt;
        try {
            randomLengthInt = Integer.parseInt(randomLengthString);
        } catch (Exception e) {
            e.printStackTrace();
            showError(R.string.invalid_trsmid);
            return;
        }

        try {
            Long longTrsmid = Long.parseLong(trsmid, 16);
            if (longTrsmid == 0) {
                showError(R.string.invalid_trsmid);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError(R.string.invalid_trsmid);
            return;
        }


        try {
            Long longTrsmid = Long.parseLong(trsmid, 16);
            if (longTrsmid == 0) {
                showError(R.string.invalid_trsmid);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError(R.string.invalid_trsmid);
            return;
        }

        String vendorKeyIndex = spVendor.getSelectedItem().toString().trim();
        String samaKeyIndex = spSama.getSelectedItem().toString().trim();

        boolean checkKeys = RegistrationManager.getInstance().loadKeys(this, vendorKeyIndex, samaKeyIndex);

        if (!checkKeys) {
            showError(R.string.invalid_pki_files);
            return;
        }

        mRegistrationData = new RegistrationData();

        mRegistrationData.setTrsmid(trsmid);
        mRegistrationData.setVendorId("50");
        mRegistrationData.setVendorTerminalType("01");
        mRegistrationData.setVendorKeyIndex(spVendor.getSelectedItem().toString().trim());
        mRegistrationData.setSamaKeyIndex(spSama.getSelectedItem().toString().trim());
        mRegistrationData.setRandomLengthIndicator(randomLengthInt);
        mRegistrationData.setVendorKeyLength("144");

        Intent resultIntent = new Intent();
        resultIntent.putExtra("registrationData", Parcels.wrap(mRegistrationData));
        setResult(Activity.RESULT_OK, resultIntent);

        new Handler().postDelayed(() -> {
            finish();
        }, 500);

    }

    private void initViews() {
        etTRSMID = findViewById(R.id.et_trsmid);
        etRandomLength = findViewById(R.id.et_random_length);

        spVendor = findViewById(R.id.vendor_sp);
        spSama = findViewById(R.id.sama_sp);
        btnProceed = findViewById(R.id.btn_proceed);
    }

    private void showError(@StringRes int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        mCommunicationsHandler.closeConnection();
        super.onDestroy();
    }
}
