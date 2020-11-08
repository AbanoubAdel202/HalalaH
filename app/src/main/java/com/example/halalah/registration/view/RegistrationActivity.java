package com.example.halalah.registration.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.example.halalah.CustomKeyboard;
import com.example.halalah.PosApplication;
import com.example.halalah.R;
import com.example.halalah.connect.CommunicationsHandler;
import com.example.halalah.databinding.ActivityRegistrationBinding;
import com.example.halalah.registration.RegistrationData;
import com.example.halalah.registration.RegistrationManager;
import com.example.halalah.sheet.SheetDialogFragment;
import com.example.halalah.storage.CommunicationInfo;
import com.example.halalah.ui.admin_settings.communication.CommunicationSettingActivity;

import org.parceler.Parcels;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    RegistrationData mRegistrationData;

    private CommunicationsHandler mCommunicationsHandler;
    private ActivityRegistrationBinding binding;
    private CustomKeyboard mCustomKeyboard;
    private StringBuilder stringBuilder = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("mRegistrationData", "" + this.hashCode());
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initializeViews();
        initializeListener();
        binding.etTerminalSerial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomKeyboard.showCustomKeyboard(v);
                binding.btnProceed.setVisibility(View.GONE);
                binding.btnConnection.setVisibility(View.GONE);

            }
        });
        mCommunicationsHandler = CommunicationsHandler.getInstance(new CommunicationInfo(PosApplication.getApp().getApplicationContext()));
    }

    private void initializeListener() {
        KeyboardView.OnKeyboardActionListener mOnKeyboardActionListener = new KeyboardView.OnKeyboardActionListener() {
            @Override
            public void onKey(int primaryCode, int[] keyCodes) {
                if (primaryCode == 55002) {
                    binding.etTerminalSerial.setText(binding.etTerminalSerial.getText().delete(binding.etTerminalSerial.getText().length() - 1, binding.etTerminalSerial.getText().length()));
                    stringBuilder.setLength(stringBuilder.length() - 1);

                } else if (primaryCode == 16) {
                    mCustomKeyboard.hideCustomKeyboard();
                    binding.btnProceed.setVisibility(View.VISIBLE);
                    binding.btnConnection.setVisibility(View.VISIBLE);

                } else if (primaryCode == -5) {
                    stringBuilder.delete(0, binding.etTerminalSerial.length());
                    binding.etTerminalSerial.setText("");
                } else {
                    int number = primaryCode;
                    char c = (char) number;
                    stringBuilder.append(c);
                    binding.etTerminalSerial.setText(stringBuilder.toString());
                }

            }

            @Override
            public void onPress(int arg0) {
            }

            @Override
            public void onRelease(int primaryCode) {
            }

            @Override
            public void onText(CharSequence text) {
            }

            @Override
            public void swipeDown() {
            }

            @Override
            public void swipeLeft() {
            }

            @Override
            public void swipeRight() {
            }

            @Override
            public void swipeUp() {
            }
        };
        Keyboard mKeyboard = new Keyboard(RegistrationActivity.this, R.xml.hexkbd);

// Lookup the KeyboardView
        KeyboardView mKeyboardView = (KeyboardView) findViewById(R.id.keyboardview);
// Attach the keyboard to the view
        mKeyboardView.setKeyboard(mKeyboard);

// Install the key handler
        mKeyboardView.setOnKeyboardActionListener(mOnKeyboardActionListener);

    }

    private void openConnectionSettings() {
        startActivity(new Intent(this, CommunicationSettingActivity.class));
    }

    private void bypass() {


        Intent resultIntent = new Intent();
        resultIntent.putExtra("registrationData", Parcels.wrap(mRegistrationData));
        setResult(Activity.RESULT_CANCELED, resultIntent);

        new Handler().postDelayed(() -> {
            finish();
        }, 500);

    }

    private void proceed() {

        String trsmid = binding.etTerminalSerial.getText().toString();
        if (TextUtils.isEmpty(trsmid) || trsmid.length() != 6) {
            showError(R.string.invalid_trsmid);
            return;
        }

        String randomLengthString = "16";
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

        boolean checkKeys = RegistrationManager.getInstance().loadKeys(this, binding.vendorSp.getSelectedItem().toString().trim(), binding.samaSp.getSelectedItem().toString().trim());

        if (!checkKeys) {
            showError(R.string.invalid_pki_files);
            return;
        }

        mRegistrationData = new RegistrationData();

        mRegistrationData.setTrsmid(trsmid);
        mRegistrationData.setVendorId("50");
        mRegistrationData.setVendorTerminalType("01");
        mRegistrationData.setVendorKeyIndex(binding.vendorSp.getSelectedItem().toString().trim());
        mRegistrationData.setSamaKeyIndex(binding.samaSp.getSelectedItem().toString().trim());
        mRegistrationData.setRandomLengthIndicator(randomLengthInt);
        mRegistrationData.setVendorKeyLength("144");

        Intent resultIntent = new Intent();
        resultIntent.putExtra("registrationData", Parcels.wrap(mRegistrationData));
        setResult(Activity.RESULT_OK, resultIntent);

        new Handler().postDelayed(() -> {
            finish();
        }, 500);

    }

    private void initializeViews() {

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.green));
        binding.btnProceed.setOnClickListener(this);
        binding.btnConnection.setOnClickListener(this);
        mCustomKeyboard = new CustomKeyboard(this, R.id.keyboardview, R.xml.hexkbd);

    }

    private void showError(@StringRes int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        mCommunicationsHandler.closeConnection();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == binding.btnConnection.getId()) {
            openConnectionSettings();
        } else if (v.getId() == binding.btnProceed.getId()) {
            proceed();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!mCustomKeyboard.isCustomKeyboardVisible()) {
            binding.btnProceed.setVisibility(View.VISIBLE);
            binding.btnConnection.setVisibility(View.VISIBLE);
            mCustomKeyboard.hideCustomKeyboard();

        } else {
            finish();

        }

    }
}
