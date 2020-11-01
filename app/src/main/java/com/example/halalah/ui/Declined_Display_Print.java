package com.example.halalah.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.halalah.R;
import com.example.halalah.TMS.TMSManager;
import com.example.halalah.print.Declined_Print;
import com.example.halalah.print.Purchase_Print;
import com.topwise.cloudpos.aidl.printer.AidlPrinter;
import com.topwise.cloudpos.data.PrinterConstant;

public class Declined_Display_Print extends AppCompatActivity implements View.OnClickListener {
    String messagetxt;
    Declined_Print mdeclinedprint;
    private AlertDialog.Builder mAlertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declined__print);

        mAlertDialog = new AlertDialog.Builder(this);
        mdeclinedprint = new Declined_Print(this);
        String responsecode= getIntent().getStringExtra("response code");
        TextView responsetxt = findViewById(R.id.response_codetxt);
        TextView description = findViewById(R.id.descriptiontxt);
        Button ok = findViewById(R.id.OK_btn);
        Button print = findViewById(R.id.PRINTDEC_btn);


        ok.setOnClickListener(this);
        print.setOnClickListener(this);

        responsetxt.setText("RC- "+responsecode);
        messagetxt = TMSManager.getInstance().getMessage(responsecode,"2").m_sEnglish_Message_Text;
        if(messagetxt==null)
        {
            description.setText("Declined");
        }
        else {
            description.setText(messagetxt);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.OK_btn:
                    finish();
                break;
            case R.id.PRINTDEC_btn:
                printdeclined(messagetxt);
                finish();
                break;
        }
    }

    void printdeclined (String messagetxt ){

        try {
            int printState = mdeclinedprint.mPrinterManager.getPrinterState();
            Log.i("declined print", "printState = " + printState);

            if (printState == PrinterConstant.PrinterState.PRINTER_STATE_NOPAPER) {
                showDialog(null, getString(R.string.result_need_paper));
            } else {
                mdeclinedprint.printDetail(messagetxt);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    private void showDialog(String title, String message) {
        if (mAlertDialog != null) {
            mAlertDialog = new AlertDialog.Builder(this);
            mAlertDialog.setMessage(message);
            mAlertDialog.setCancelable(false);
            mAlertDialog.setPositiveButton(R.string.result_dialog_ok, null);
            mAlertDialog.show();
        }
    }

    private void showPrintDialog() {
        if (mAlertDialog != null) {
            mAlertDialog = new AlertDialog.Builder(this);
            mAlertDialog.setMessage(getString(R.string.result_print_more));
            mAlertDialog.setCancelable(false);
            mAlertDialog.setNegativeButton(R.string.result_dialog_cancle, null);
            mAlertDialog.setPositiveButton(R.string.result_dialog_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        int printState = mdeclinedprint.mPrinterManager.getPrinterState();
                        Log.i("declined", "printState = " + printState);

                        if (printState == PrinterConstant.PrinterState.PRINTER_STATE_NOPAPER) {
                            showDialog(null, getString(R.string.result_need_paper));
                        } else {
                            mdeclinedprint.printDetail(messagetxt);
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            });
            mAlertDialog.show();
        }
    }
}