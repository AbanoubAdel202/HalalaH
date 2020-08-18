package com.example.halalah.registration.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.example.halalah.PosApplication;
import com.example.halalah.R;
import com.example.halalah.connect.CommunicationsHandler;
import com.example.halalah.registration.RegistrationData;
import com.example.halalah.registration.RegistrationManager;
import com.example.halalah.storage.CommunicationInfo;
import com.example.halalah.ui.admin_settings.CommunicationSettingActivity;

import org.parceler.Parcels;

public class RegistrationActivity extends AppCompatActivity {

    RegistrationData mRegistrationData;
    private EditText etRandomLength;
    private EditText etTRSMID;
    private Spinner spVendor;
    private Spinner spSama;
    private Button btnProceed;
    private TextView connectionSettingsTv;
    private CommunicationsHandler mCommunicationsHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("mRegistrationData", "" + this.hashCode());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initViews();
        mCommunicationsHandler =
                CommunicationsHandler.getInstance(new CommunicationInfo(PosApplication.getApp().getApplicationContext()));
    }

    private void openConnectionSettings() {
        startActivity(new Intent(this, CommunicationSettingActivity.class));
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
        connectionSettingsTv = findViewById(R.id.btn_connection);

        btnProceed.setOnClickListener(v -> proceed());

        SpannableString ss = new SpannableString(getString(R.string.connection_settings));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                openConnectionSettings();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        connectionSettingsTv.setText(ss);
        connectionSettingsTv.setMovementMethod(LinkMovementMethod.getInstance());
        connectionSettingsTv.setHighlightColor(Color.TRANSPARENT);
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
