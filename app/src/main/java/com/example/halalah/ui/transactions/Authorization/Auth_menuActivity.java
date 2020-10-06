package com.example.halalah.ui.transactions.Authorization;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.halalah.POSTransaction;
import com.example.halalah.PosApplication;
import com.example.halalah.R;
import com.example.halalah.connect.CommunicationsHandler;
import com.example.halalah.storage.CommunicationInfo;

import java.io.InputStream;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Auth_menuActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
           // hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (AUTO_HIDE) {
                        delayedHide(AUTO_HIDE_DELAY_MILLIS);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    view.performClick();
                    break;
                default:
                    break;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_auth_menu);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);

        Button auth_btn= (Button) findViewById(R.id.auth_btn);
        Button authorization_advice_btn= (Button) findViewById(R.id.authorization_advice_btn);
        Button authorization_Extension_btn= (Button) findViewById(R.id.authorization_Extension_btn);
        Button authorization_void_btn= (Button) findViewById(R.id.authorization_void_btn);
        auth_btn.setOnClickListener(this);
        authorization_advice_btn.setOnClickListener(this);
        authorization_Extension_btn.setOnClickListener(this);
        authorization_void_btn.setOnClickListener(this);
     /*   // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);*/
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void onClick(View v) {

       switch(v.getId())
        {
            case R.id.auth_btn:
                PosApplication.getApp().oGPosTransaction.m_sTrxAmount="1000";
                PosApplication.getApp().oGPOS_MAIN.Start_Transaction(PosApplication.getApp().oGPosTransaction, POSTransaction.TranscationType.AUTHORISATION,null);
                break;

            case R.id.authorization_advice_btn://AUTHORISATION_ADVICE:
                preConnect();
                PosApplication.getApp().oGPosTransaction.m_enmTrxType=POSTransaction.TranscationType.AUTHORISATION_ADVICE;
                PosApplication.getApp().oGPOS_MAIN.Start_Transaction(PosApplication.getApp().oGPosTransaction,POSTransaction.TranscationType.AUTHORISATION_ADVICE,null);
                break;
            case R.id.authorization_void_btn://AUTHORISATION_VOID:
                preConnect();
                PosApplication.getApp().oGPOS_MAIN.Start_Transaction(PosApplication.getApp().oGPosTransaction, POSTransaction.TranscationType.AUTHORISATION_VOID,null);

                break;
            case R.id.authorization_Extension_btn://AUTHORISATION_EXTENSION
                preConnect();
                PosApplication.getApp().oGPOS_MAIN.Start_Transaction(PosApplication.getApp().oGPosTransaction, POSTransaction.TranscationType.AUTHORISATION_EXTENSION,null);

                break;

        }
    }private void preConnect() {
        // open socket to be ready to sending/receiving financial messages
        CommunicationInfo communicationInfo = new CommunicationInfo(PosApplication.getApp().oGPOS_MAIN.mcontext);
        InputStream caInputStream = getResources().openRawResource(R.raw.bks);
        CommunicationsHandler.getInstance(communicationInfo, caInputStream).connect();
    }
}