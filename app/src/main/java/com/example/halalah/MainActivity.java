package com.example.halalah;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.halalah.TMS.AID_Data;
import com.example.halalah.TMS.AID_List;
import com.example.halalah.TMS.Card_Scheme;
import com.example.halalah.TMS.SAMA_TMS;
import com.example.halalah.iso8583.BCDASCII;

import com.example.halalah.sqlite.repository.component.AppDatabase;
import com.example.halalah.sqlite.storage.AIDRepo;
import com.example.halalah.sqlite.storage.TMSManager;
import com.topwise.cloudpos.aidl.emv.AidlPboc;
import com.topwise.cloudpos.aidl.pinpad.AidlPinpad;
import com.topwise.cloudpos.data.PinpadConstant;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    Context context;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView date = findViewById(R.id.Date);
        TextView time = findViewById(R.id.TIME);
        TextView status = findViewById(R.id.Status);

        testRoomDB();

//        testTMSManager();

    }

    private void testRoomDB() {
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();

//        AID_Data aid_data = new AID_Data();
//        aid_data.AID = "2";
//        aid_data.AID_Label = "AID_Label";
//        aid_data.Default_DDOL = "DDOL";
//        aid_data.Terminal_AID_version_numbers = "Terminal_AID_version_numbers";

        AID_List aid_list = new AID_List();
        aid_list.AID = new String[]{"1","2","3"};

        db.aidListDao().insertAll(new AID_List[]{aid_list});

        if (db.aidListDao().getAll() != null && !db.aidListDao().getAll().isEmpty()){
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void testTMSManager() {
        SAMA_TMS sama_tms = new SAMA_TMS();
        TMSManager tmsManager = TMSManager.getInstance(this);

        for (int i = 0; i < 1; i++) {
            String[] DE72buffer = {
                    "3060143\u001D0111604081438\u001C1720\u001CÇåÑêãÇæ ÇãÓÈÑÓ 72\u001C400101490012\u001CMobily\u001C2\u001C682\u001C682\u001C02\u001C\u001CSAR\u001C\u001C\u001C\u001C\u001D012 ÇäÙäêÇ\u001COlaya 1222\u001D013 ÇäÑêÇÖ\u001CRIYADH\u001D014E0F8C8\u001C4000F0A000\u001C\u001C22\u001C0\u001C03\u001C03",
                    "3060243\u001D021JC\u001CÇåÑêãÇæ ÇãÓÈÑÓ\u001CJCB\u001CSAIB\u001C5311\u001C400101490012   \u001C01490123\u001C0\u001C1\u001C0\u001D022JC\u001C1111111111111111\u001C1011100000000000\u001C0001000000000000\u001C0000100000000000\u001C0\u001C\u001C\u001C\u001C0000000000000000\u001C\u001C1\u001C00\u001C0\u001D023JC\u001C34FFFF|38FFFF|37FFFF\u001CF\u001D021DC\u001CåÇêÓÊÑè\u001CDiscover\u001CSAIB\u001C5311\u001C400101490012   \u001C01490123\u001C0\u001C1\u001C0\u001D022DC\u001C1111111111111111\u001C2001000000000000\u001C0001000000000000\u001C0000000000000000\u001C1\u001C400\u001C2\u001C200\u001C1001000000000000\u001C50000\u001C1\u001C00\u001C0\u001D023DC\u001C50FFFF|56FFFF|57FFFF|6FFFFF|7FFFFF\u001CF\u001D021MC\u001CåÇÓÊÑ ãÇÑÏ\u001CMASTERCARD\u001CSAIB\u001C5311\u001C400101490012   \u001C01490123\u001C0\u001C1\u001C1\u001D022MC\u001C1111111111111111\u001C3011102000000000\u001C0001000000000000\u001C0000100000000000\u001C1\u001C400\u001C\u001C\u001C0000000000000000\u001C20000\u001C1\u001C00\u001C0\u001D023MC\u001C51FFFF|52FFFF|53FFFF|54FFFF|55FFFF\u001CF\u001D021P1\u001CåÏé èÓÈÇæ\u001CMoa AND AHM\u001CSAIB\u001C5311\u001C400101490012   \u001C01490123\u001C1\u001C1\u001C0\u001D022P1\u001C1111111111111111\u001C2102000000000000\u001C0001000000000000\u001C0000000000000000\u001C1\u001C400\u001C2\u001C200\u001C0000000000000000\u001C20000\u001C1\u001C00\u001C0",
                    "3060343\u001D023P1\u001C455036|588845|440647|440795|588846|493428|588850|588847|490980|588849|588851|605141|589206|585265|588983|588848|504300|440533|410685|636120|417633|431361|443927|446404|549760|557655|409201|400861|588982|400681|432328|486094\u001CF\u001D021VC\u001CáêÒÇ\u001CVISA\u001CSAIB\u001C5311\u001C400101490012   \u001C01490123\u001C0\u001C1\u001C1\u001D022VC\u001C1111111111111111\u001C3333333000000000\u001C0001000000000000\u001C1111111111111111\u001C1\u001C400\u001C\u001C\u001C0000000000000000\u001C40000\u001C1\u001C00\u001C0\u001D023VC\u001C4FFFFF\u001CF",
                    "3060443\u001D021VD\u001CáêÒÇ\u001CVisa Electron\u001CSAIB\u001C5311\u001C123456789018   \u001C01490001\u001C1\u001C0\u001C0\u001D022VD\u001C1111111000000000\u001C3333330000000000\u001C0001001000000000\u001C1111110000000000\u001C1\u001C400\u001C\u001C200\u001C0000000000000000\u001C200000\u001C1\u001C00\u001C0\u001D023VD\u001C4FFFFF\u001CF",
                    "3060543\u001D0310\u001C0\u001CåâÈèäç\u001CAPPROVED\u001D0310\u001C1\u001CåâÈèäç\u001CAPPROVED\u001D0310\u001C2\u001CåâÈèäç\u001CAPPROVED test\u001D0311\u001C0\u001CåâÈèäç\u001CAPPROVED\u001D0311\u001C1\u001CåâÈèäç\u001CAPPROVED\u001D0311\u001C2\u001CåâÈèäç\u001CAPPROVED\u001D0313\u001C0\u001CåâÈèäç\u001CAPPROVED\u001D0313\u001C1\u001CåâÈèäç\u001CAPPROVED\u001D0313\u001C2\u001CåâÈèäç\u001CAPPROVED\u001D0317\u001C0\u001CåâÈèäç\u001CAPPROVED\u001D0317\u001C1\u001CåâÈèäç\u001CAPPROVED\u001D0317\u001C2\u001CåâÈèäç\u001CAPPROVED\u001D03187\u001C0\u001CåâÈèäç\u001CAPPROVED\u001D03187\u001C1\u001CåâÈèäç\u001CAPPROVED\u001D03187\u001C2\u001CåâÈèäç\u001CAPPROVED\u001D03189\u001C0\u001CåâÈèäç\u001CAPPROVED\u001D03189\u001C1\u001CåâÈèäç\u001CAPPROVED\u001D03189\u001C2\u001CåâÈèäç\u001CAPPROVED\u001D031100\u001C0\u001CåÑáèÖÉ\u001CDECLINED\u001D031100\u001C1\u001CåÑáèÖÉ\u001CDECLINED\u001D031100\u001C2\u001CåÑáèÖÉ\u001CDECLINED\u001D031101\u001C0\u001CÇäÈ×ÇâÉ åæÊçêÉ ÇÊÕä ÈÇäÈæã\u001CExpired Card\u001D031101\u001C1\u001CÇäÈ×ÇâÉ åæÊçêÉ ÇÊÕä ÈÇäÈæã\u001CExpired Card\u001D031101\u001C2\u001CÇäÈ×ÇâÉ åæÊçêÉ ÇÊÕä ÈÇäÈæã\u001CExpired Card\u001D031102\u001C0\u001CÇäÙåäêÉ åÔÈèçç\u001CSuspected fraud\u001D031102\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031102\u001C2\u001CåÑáèÖç\u001CDECLINED\u001D031103\u001C0\u001CåÑáèÖç\u001CDECLINED\u001D031103\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031103\u001C2\u001CåÑáèÖç\u001CDECLINED\u001D031104\u001C0\u001CåÑáèÖç\u001CDECLINED",
                    "3060643\u001D031104\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031104\u001C2\u001CÇäÈ×ÇâÉ åâêÏÉ ÚêÑ åÓåèÍ ÈÇäÙåäêÉ 72\u001CRestricted card Transaction not allowed\u001D031105\u001C0\u001CåÑáèÖç\u001CDECLINED\u001D031105\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031105\u001C2\u001CåÑáèÖç\u001CDECLINED\u001D031106\u001C0\u001CåÑáèÖç\u001CDECLINED\u001D031106\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031106\u001C2\u001CÊÌÇèÒ ÇäåÍÇèäÇÊ ÇäåÓåèÍ ÈçÇ\u001CAllowable PIN tries exceeded\u001D031107\u001C0\u001CåÑáèÖç\u001CDECLINED\u001D031107\u001C1\u001CåÑáèÖç\u001CDECLINED Contact your bank\u001D031107\u001C2\u001CåÑáèÖç ÇÊÕä ÈÇäÈæã\u001CDECLINED Contact your bank\u001D031108\u001C0\u001CåÑáèÖç\u001CDECLINED\u001D031108\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031108\u001C2\u001CåÑáèÖç ÇÊÕä ÈÇäÈæã\u001CDECLINED Contact your bank\u001D031109\u001C0\u001CåÑáèÖç\u001CDECLINED\u001D031109\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031109\u001C2\u001CåÑáèÖç\u001CDECLINED\u001D031110\u001C0\u001CåÑáèÖç\u001CDECLINED\u001D031110\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031110\u001C2\u001CåÑáèÖç\u001CDECLINED\u001D031111\u001C0\u001CåÑáèÖç\u001CDECLINED\u001D031111\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031111\u001C2\u001CåÑáèÖç ÇÊÕä ÈÇäÈæã\u001CDECLINED Contact your bank\u001D031112\u001C0\u001CåÑáèÖç\u001CDECLINED\u001D031112\u001C1\u001CåÑáèÖç\u001CDECLINED",
                    "3060743\u001D031112\u001C2\u001CåÑáèÖç\u001CDECLINED\u001D031114\u001C0\u001CåÑáèÖç\u001CDECLINED\u001D031114\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031114\u001C2\u001CåÑáèÖç ÇÊÕä ÈÇäÈæã\u001CDECLINED Contact your bank\u001D031115\u001C0\u001CåÑáèÖç\u001CDECLINED\u001D031115\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031115\u001C2\u001CÚêÑ åÓåèÍ ÈÇäÙåäêç\u001CTransaction not allowed\u001D031116\u001C0\u001CåÑáèÖç\u001CDECLINED\u001D031116\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031116\u001C2\u001CÇäÑÕêÏ äÇ êÓåÍ\u001CNot sufficient funds\u001D031117\u001C0\u001CÇäÑâå ÇäÓÑê ÚêÑ ÕÍêÍ\u001CIncorrect PIN\u001D031117\u001C1\u001CÇÏÎä ÇäÑâå ÇäÓÑê åÑÉ ÇÎÑé\u001CRetry PIN\u001D031118\u001C0\u001CåÑáèÖç\u001CDECLINED\u001D031118\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031118\u001C2\u001CåÑáèÖç ÇÊÕä ÈÇäÈæã\u001CDECLINED Contact your bank\u001D031119\u001C0\u001CåÑáèÖç\u001CDECLINED\u001D031119\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031119\u001C2\u001CÇäÙåäêÉ ÚêÑ åÓåèÍ ÈçÇ\u001CTransaction not permitted to cardholder\u001D031120\u001C0\u001CåÑáèÖç\u001CDECLINED\u001D031120\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031120\u001C2\u001CåÑáèÖç\u001CDECLINED\u001D031121\u001C0\u001CåÑáèÖç\u001CDECLINED\u001D031121\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031121\u001C2\u001CÊÌÇèÒ ÇäÍÏ ÇäåÓåèÍ\u001CExceeds withdrawal amount limit",
                    "3060843\u001D031122\u001C0\u001CåÑáèÖç\u001CDECLINED\u001D031122\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031122\u001C2\u001CåÑáèÖç\u001CDECLINED\u001D031123\u001C0\u001CåÑáèÖç\u001CDECLINED\u001D031123\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031123\u001C2\u001CåÑáèÖç Êå ÊÌÇèÒ ÇäÍÏ\u001CDECLINED Exceeds withdrawal frequency limit\u001D031125\u001C0\u001CåÑáèÖç\u001CDECLINED\u001D031125\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031125\u001C2\u001CåÑáèÖç\u001CDECLINED Contact your bank\u001D031126\u001C0\u001CåÑáèÖç\u001CDECLINED\u001D031126\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031126\u001C2\u001CåÑáèÖç\u001CDECLINED\u001D031127\u001C0\u001CåÑáèÖç\u001CDECLINED\u001D031127\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031127\u001C2\u001CÇäÑâå ÇäÓÑê ÎÇ×Æ\u001CWRONG PIN\u001D031128\u001C0\u001CåÑáèÖç\u001CDECLINED\u001D031128\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031128\u001C2\u001CåÑáèÖç\u001CDECLINED\u001D031129\u001C0\u001CåÑáèÖç\u001CSuspected Counterfeit Card\u001D031129\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031129\u001C2\u001CåÑáèÖç ÇÊÕä ÈÇäÈæã\u001CDECLINED Contact your bank\u001D031182\u001C0\u001CåÑáèÖç\u001CDECLINED\u001D031182\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031182\u001C2\u001CåÑáèÖç\u001CDECLINED\u001D031183\u001C0\u001CåÑáèÖç\u001CDECLINED\u001D031183\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031183\u001C2\u001CåÑáèÖç\u001CDECLINED\u001D031184\u001C0\u001CåÑáèÖç\u001CDECLINED",
                    "3060943\u001D031184\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031184\u001C2\u001CåÑáèÖç\u001CDECLINED\u001D031185\u001C0\u001CåÑáèÖç\u001CDECLINED\u001D031185\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031185\u001C2\u001CåÑáèÖç\u001CDECLINED\u001D031188\u001C0\u001CåÑáèÖç\u001CDECLINED\u001D031188\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031188\u001C2\u001CåÑáèÖç\u001CDECLINED\u001D031190\u001C0\u001CåÑáèÖç\u001CDECLINED\u001D031190\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031190\u001C2\u001CåÑáèÖç\u001CDECLINED\u001D031200\u001C0\u001CåÑáèÖç\u001CDECLINED\u001D031200\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031200\u001C2\u001CåÑáèÖç\u001CDECLINED\u001D031201\u001C0\u001CåÑáèÖç\u001CDECLINED\u001D031201\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031201\u001C2\u001CÇäÈ×ÇâÉ åæÊçêç ÇÊÕä ÈÇäÈæã\u001CExpired Card Contact you bank\u001D031202\u001C0\u001CÇäÙåäêç åÔÈèçç\u001CSuspected fraud\u001D031202\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031202\u001C2\u001CåÑáèÖç\u001CDECLINED\u001D031203\u001C0\u001CåÑáèÖç\u001CDECLINED\u001D031203\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031203\u001C2\u001CåÑáèÖç\u001CDECLINED\u001D031204\u001C0\u001CåÑáèÖç\u001CDECLINED\u001D031204\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031204\u001C2\u001CÇäÙåäêç ÚêÑ åÓåèÍ ÈçÇ\u001CTransaction not allowed\u001D031205\u001C0\u001CåÑáèÖç\u001CDECLINED\u001D031205\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031205\u001C2\u001CåÑáèÖç\u001CDECLINED\u001D031206\u001C0\u001CåÑáèÖç\u001CDECLINED",
                    "3061043\u001D031206\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031206\u001C2\u001CÇäÑâå ÇäÓÑê ÎÇ×Æ ÇÊÕä ÈÇäÈæã\u001CWrong PIN Contact your bank\u001D031207\u001C0\u001CåÑáèÖç\u001CDECLINED\u001D031207\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031207\u001C2\u001CåÑáèÖç\u001CDECLINED\u001D031208\u001C0\u001CÈ×Çâç åáâèÏç\u001CLost Card\u001D031208\u001C1\u001C\u001CDECLINED\u001D031208\u001C2\u001C\u001CDECLINED Contact your bank\u001D031209\u001C0\u001CÈ×Çâç åÓÑèâç\u001CStolen Card\u001D031209\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031209\u001C2\u001CåÑáèÖç ÇÊÕä ÈÇäÈæã\u001CDECLINED Contact your bank\u001D031210\u001C0\u001CåÑáèÖç\u001CSuspected Counerfeit Card\u001D031210\u001C1\u001CåÑáèÖç\u001CDECLINED\u001D031210\u001C2\u001CåÑáèÖç ÇÊÕä ÈÇäÈæã\u001CDECLINED Contact your bank\u001D031400\u001C0\u001CåÓÊäåç\u001CAccepted\u001D031400\u001C1\u001CåÓÊäåç\u001CAccepted\u001D031400\u001C2\u001CåÓÊäåç\u001CAccepted",
                    "3061143\u001D041A000000003\u001C92\u001C01\u001C01\u001C996AF56F569187D09293C14810450ED8EE3357397B18A2458EFAA92DA3B6DF6514EC060195318FD43BE9B8F0CC669E3F844057CBDDF8BDA191BB64473BC8DC9A730DB8F6B4EDE3924186FFD9B8C7735789C23A36BA0B8AF65372EB57EA5D89E7D14E9C7B6B557460F10885DA16AC923F15AF3758F0F03EBD3C5C2C949CBA306DB44E6A2C076C5F67E281D7EF56785DC4D75945E491F01918800A9E2DC66F60080566CE0DAF8D17EAD46AD8E30A247C9F\u001C3\u001C429C954A3859CEF91295F663C963E582ED6EB253\u001C9S\u001C221231",
                    "3061243\u001D041A000000003\u001C94\u001C01\u001C01\u001CACD2B12302EE644F3F835ABD1FC7A6F62CCE48FFEC622AA8EF062BEF6FB8BA8BC68BBF6AB5870EED579BC3973E121303D34841A796D6DCBC41DBF9E52C4609795C0CCF7EE86FA1D5CB041071ED2C51D2202F63F1156C58A92D38BC60BDF424E1776E2BC9648078A03B36FB554375FC53D57C73F5160EA59F3AFC5398EC7B67758D65C9BFF7828B6B82D4BE124A416AB7301914311EA462C19F771F31B3B57336000DFF732D3B83DE07052D730354D297BEC72871DCCF0E193F171ABA27EE464C6A97690943D59BDABB2A27EB71CEEBDAFA1176046478FD62FEC452D5CA393296530AA3F41927ADFE434A2DF2AE3054F8840657A26E0FC617\u001C3\u001CC4A3C43CCF87327D136B804160E47D43B60E6E0F\u001CDS\u001C221231",
                    "3061343\u001D041A000000003\u001C95\u001C01\u001C01\u001CBE9E1FA5E9A803852999C4AB432DB28600DCD9DAB76DFAAA47355A0FE37B1508AC6BF38860D3C6C2E5B12A3CAAF2A7005A7241EBAA7771112C74CF9A0634652FBCA0E5980C54A64761EA101A114E0F0B5572ADD57D010B7C9C887E104CA4EE1272DA66D997B9A90B5A6D624AB6C57E73C8F919000EB5F684898EF8C3DBEFB330C62660BED88EA78E909AFF05F6DA627B\u001C3\u001CEE1511CEC71020A9B90443B37B1D5F6E703030F6\u001C80\u001C221231\u001D041A000000003\u001C99\u001C01\u001C01\u001CAB79FCC9520896967E776E64444E5DCDD6E13611874F3985722520425295EEA4BD0C2781DE7F31CD3D041F565F747306EED62954B17EDABA3A6C5B85A1DE1BEB9A34141AF38FCF8279C9DEA0D5A6710D08DB4124F041945587E20359BAB47B7575AD94262D4B25F264AF33DEDCF28E09615E937DE32EDC03C54445FE7E382777\u001C3\u001C4ABFFD6B1C51212D05552E431C5B17007D2F5E6D\u001C74\u001C221231",
                    "3061443\u001D041A000000004\u001C04\u001C01\u001C01\u001CA6DA428387A502D7DDFB7A74D3F412BE762627197B25435B7A81716A700157DDD06F7CC99D6CA28C2470527E2C03616B9C59217357C2674F583B3BA5C7DCF2838692D023E3562420B4615C439CA97C44DC9A249CFCE7B3BFB22F68228C3AF13329AA4A613CF8DD853502373D62E49AB256D2BC17120E54AEDCED6D96A4287ACC5C04677D4A5A320DB8BEE2F775E5FEC5\u001C3\u001C381A035DA58B482EE2AF75F4C3F2CA469BA4AA6C\u001C40\u001C221231",
                    "3061543\u001D041A000000004\u001C05\u001C01\u001C01\u001CB8048ABC30C90D976336543E3FD7091C8FE4800DF820ED55E7E94813ED00555B573FECA3D84AF6131A651D66CFF4284FB13B635EDD0EE40176D8BF04B7FD1C7BACF9AC7327DFAA8AA72D10DB3B8E70B2DDD811CB4196525EA386ACC33C0D9D4575916469C4E4F53E8E1C912CC618CB22DDE7C3568E90022E6BBA770202E4522A2DD623D180E215BD1D1507FE3DC90CA310D27B3EFCCD8F83DE3052CAD1E48938C68D095AAC91B5F37E28BB49EC7ED597\u001C3\u001CEBFA0D5D06D8CE702DA3EAE890701D45E274C845\u001C4W\u001C221231",
                    "3061643\u001D041A000000004\u001C06\u001C01\u001C01\u001CCB26FC830B43785B2BCE37C81ED334622F9622F4C89AAE641046B2353433883F307FB7C974162DA72F7A4EC75D9D657336865B8D3023D3D645667625C9A07A6B7A137CF0C64198AE38FC238006FB2603F41F4F3BB9DA1347270F2F5D8C606E420958C5F7D50A71DE30142F70DE468889B5E3A08695B938A50FC980393A9CBCE44AD2D64F630BB33AD3F5F5FD495D31F37818C1D94071342E07F1BEC2194F6035BA5DED3936500EB82DFDA6E8AFB655B1EF3D0D7EBF86B66DD9F29F6B1D324FE8B26CE38AB2013DD13F611E7A594D675C4432350EA244CC34F3873CBA06592987A1D7E852ADC22EF5A2EE28132031E48F74037E3B34AB747F\u001C3\u001CF910A1504D5FFB793D94F3B500765E1ABCAD72D9\u001C6W\u001C221231",
                    "3061743\u001D041A000000004\u001CF3\u001C01\u001C01\u001C98F0C770F23864C2E766DF02D1E833DFF4FFE92D696E1642F0A88C5694C6479D16DB1537BFE29E4FDC6E6E8AFD1B0EB7EA0124723C333179BF19E93F10658B2F776E829E87DAEDA9C94A8B3382199A350C077977C97AFF08FD11310AC950A72C3CA5002EF513FCCC286E646E3C5387535D509514B3B326E1234F9CB48C36DDD44B416D23654034A66F403BA511C5EFA3\u001C3\u001CA69AC7603DAF566E972DEDC2CB433E07E8B01A9A\u001C40\u001C221231",
                    "3061843\u001D041A000000004\u001CF8\u001C01\u001C01\u001CA1F5E1C9BD8650BD43AB6EE56B891EF7459C0A24FA84F9127D1A6C79D4930F6DB1852E2510F18B61CD354DB83A356BD190B88AB8DF04284D02A4204A7B6CB7C5551977A9B36379CA3DE1A08E69F301C95CC1C20506959275F41723DD5D2925290579E5A95B0DF6323FC8E9273D6F849198C4996209166D9BFC973C361CC826E1\u001C3\u001CF06ECC6D2AAEBF259B7E755A38D9A9B24E2FF3DD\u001C3K\u001C221231",
                    "3061943\u001D041A000000004\u001CFA\u001C01\u001C01\u001CA90FCD55AA2D5D9963E35ED0F440177699832F49C6BAB15CDAE5794BE93F934D4462D5D12762E48C38BA83D8445DEAA74195A301A102B2F114EADA0D180EE5E7A5C73E0C4E11F67A43DDAB5D55683B1474CC0627F44B8D3088A492FFAADAD4F42422D0E7013536C3C49AD3D0FAE96459B0F6B1B6056538A3D6D44640F94467B108867DEC40FAAECD740C00E2B7A8852D\u001C3\u001C5BED4068D96EA16D2D77E03D6036FC7A160EA99C\u001C40\u001C221231",
                    "3062043\u001D041A000000004\u001CEF\u001C01\u001C01\u001CA191CB87473F29349B5D60A88B3EAEE0973AA6F1A082F358D849FDDFF9C091F899EDA9792CAF09EF28F5D22404B88A2293EEBBC1949C43BEA4D60CFD879A1539544E09E0F09F60F065B2BF2A13ECC705F3D468B9D33AE77AD9D3F19CA40F23DCF5EB7C04DC8F69EBA565B1EBCB4686CD274785530FF6F6E9EE43AA43FDB02CE00DAEC15C7B8FD6A9B394BABA419D3F6DC85E16569BE8E76989688EFEA2DF22FF7D35C043338DEAA982A02B866DE5328519EBBCD6F03CDD686673847F84DB651AB86C28CF1462562C577B853564A290C8556D818531268D25CC98A4CC6A0BDFFFDA2DCCA3A94C998559E307FDDF915006D9A987B07DDAEB3B\u001C3\u001C21766EBB0EE122AFB65D7845B73DB46BAB65427A\u001CDS\u001C221231",
                    "3062143\u001D041A000000004\u001CF1\u001C01\u001C01\u001CA0DCF4BDE19C3546B4B6F0414D174DDE294AABBB828C5A834D73AAE27C99B0B053A90278007239B6459FF0BBCD7B4B9C6C50AC02CE91368DA1BD21AAEADBC65347337D89B68F5C99A09D05BE02DD1F8C5BA20E2F13FB2A27C41D3F85CAD5CF6668E75851EC66EDBF98851FD4E42C44C1D59F5984703B27D5B9F21B8FA0D93279FBBF69E090642909C9EA27F898959541AA6757F5F624104F6E1D3A9532F2A6E51515AEAD1B43B3D7835088A2FAFA7BE7\u001C03\u001CD8E68DA167AB5A85D8C3D55ECB9B0517A1A5B4BB\u001C9S\u001C221231\u001D041A000000004\u001CF3\u001C01\u001C01\u001C98F0C770F23864C2E766DF02D1E833DFF4FFE92D696E1642F0A88C5694C6479D16DB1537BFE29E4FDC6E6E8AFD1B0EB7EA0124723C333179BF19E93F10658B2F776E829E87DAEDA9C94A8B3382199A350C077977C97AFF08FD11310AC950A72C3CA5002EF513FCCC286E646E3C5387535D509514B3B326E1234F9CB48C36DDD44B416D23654034A66F403BA511C5EFA3\u001C3\u001CA69AC7603DAF566E972DEDC2CB433E07E8B01A9A\u001C80\u001C221231",
                    "3062243\u001D041A000000004\u001CF8\u001C01\u001C01\u001CA1F5E1C9BD8650BD43AB6EE56B891EF7459C0A24FA84F9127D1A6C79D4930F6DB1852E2510F18B61CD354DB83A356BD190B88AB8DF04284D02A4204A7B6CB7C5551977A9B36379CA3DE1A08E69F301C95CC1C20506959275F41723DD5D2925290579E5A95B0DF6323FC8E9273D6F849198C4996209166D9BFC973C361CC826E1\u001C3\u001CF06ECC6D2AAEBF259B7E755A38D9A9B24E2FF3DD\u001C74\u001C221231\u001D041A000000004\u001CFA\u001C01\u001C01\u001CA90FCD55AA2D5D9963E35ED0F440177699832F49C6BAB15CDAE5794BE93F934D4462D5D12762E48C38BA83D8445DEAA74195A301A102B2F114EADA0D180EE5E7A5C73E0C4E11F67A43DDAB5D55683B1474CC0627F44B8D3088A492FFAADAD4F42422D0E7013536C3C49AD3D0FAE96459B0F6B1B6056538A3D6D44640F94467B108867DEC40FAAECD740C00E2B7A8852D\u001C3\u001C5BED4068D96EA16D2D77E03D6036FC7A160EA99C\u001C80\u001C221231",
                    "3062343\u001D041A000000004\u001CFE\u001C01\u001C01\u001CA653EAC1C0F786C8724F737F172997D63D1C3251C44402049B865BAE877D0F398CBFBE8A6035E24AFA086BEFDE9351E54B95708EE672F0968BCD50DCE40F783322B2ABA04EF137EF18ABF03C7DBC5813AEAEF3AA7797BA15DF7D5BA1CBAF7FD520B5A482D8D3FEE105077871113E23A49AF3926554A70FE10ED728CF793B62A1\u001C3\u001C9A295B05FB390EF7923F57618A9FDA2941FC34E0\u001C74\u001C221231\u001D041A000000228\u001C11\u001C01\u001C01\u001C9886CFFCF819714F78EFDA4EFAF7844EDCC1C23EAEAF91D779CBBA3D0B9D7949155C1877BD057185F998781E9AD1EF40B1DAC8F67DF4F2BD3971CB2F733ADB7B1C67CA778AE25E82985B4FBA52BF089A782FF31D31C6A27916EA18F0A6FBD681A19F5F2E29AF22FFE68A307EED56D28069E1E00961221CF090DDF9C12945B85E1DA8DA207E525F1DCA2A4CD5F33B9FFB\u001C03\u001CE42B8ABAC3D3EAA9EDBEE8D961B247B9B0354AC3\u001C80\u001C221231",
                    "3062443\u001D041A000000228\u001C12\u001C01\u001C01\u001CB02088940B9E83954D8ED1BACC5635671FF2A858D011F97D0C70FDA6763872B3CBCB9943CCF38E5C9B5DDE74FCA012AFB7E1BC63DC6DEA627410BE64B25313F13F742AFED1F70CF0DF5CFB6C769C91393D0F6B046C8F69B046B856A27C696E1333556A6D4AB6EE615A3C97D34820F7ED9119FBB9AF1FB7F3F58BCCA61637DA0A1C860850EAA1F80F67CC5052B4B541FC08A61E574181BFDFAEEF58DA6837BF82FBED2C74F8F177AC66975C6EE5B4F4F5\u001C03\u001C58167ACD34348A5631BFAA2286BA33C66044DC7E\u001C9S\u001C221231\u001D041A000000228\u001C14\u001C01\u001C01\u001CAFB012B34E1370D76691E3FFBA095FE0AB7E81301C10A7D2C2CAC0E520820DC51DBE27483AEAEE7C968C2C32D39D55CF8D7C4F220D73BEC45576C3C40E32D4353D2596420BBF00A04E0879F5912EEF6A5F968ECC0F3F01838B2EF7A6E23796A975A4223B30F6522AEB080B42E014B9A9A5A5AEBDF45E01C52F5A3D2CD2908EE4A7486016CEB64573BEEBD8157AB369AB\u001C03\u001C8E20983BA782B780075FC7669E023AC77D5946F0\u001C80\u001C221231",
                    "3062543\u001D041A000000228\u001C0C\u001C01\u001C01\u001CB34A3BEB799F296AD709D78B361891EF8017B1B8367E582DA8F90F7D19BAF8DB34E883EC2ABE45A878EABEA8CD60E1C23D2543927505B9BDE2174556A4DFAABAC1159E1E5CB6E8596F7E99C751A3EA176FBB5872842F84E2CAED16167480A301803424562F8C37E81325EBA861D47EE3DABA65AC59A067D493D1D09B561D84ABB3FDC89B8FA933465057CB25B6E533CDC418D256BBB2D4A2C9146EA921AB3A2E5488AA17C8AE2E5D8D865E95378D0885\u001C03\u001C1CCBFF1A83B61EA6FEA65D6E838975DCB700FA4A\u001C9S\u001C221231",
                    "3062643\u001D041A000000228\u001C0D\u001C01\u001C01\u001CBFDB4B25E87FF13611C454C8CA1B96F33526AF48DD097D628A4799F76B9AE1D3B370A36958FD903EA6E5806C9418B603D63ED92770C5EB03D328C9D1E236FB71C171DC1BBD47ACD52B96F42B4ED7988D68280CD287F14E3D6D4B090413EC76BE12842D335B464F8F38FF31F8F3C9365C7F9AA428DF101268B2BF1764548B79ADF2E0D8359D5543214A75BD8551B27F2018F04C2AF9207AD72AC8D0D179C04BCA755E9F616BE35AD930EC8FAB00A1E2665547361A4B4396D47F436610D93DC287C0C9B8A417E96E0C457A5327B791A4E4B906D7CDB48BECF52B1B33318F5D55486336875BF581B808DD8F64E221117B1B12DAA6A62E44FBCF\u001C03\u001CC0874DB01615B6B847A38D8CEFFF901E2BD5C8A7\u001CDS\u001C221231",
                    "3062743\u001D041A000000228\u001C0E\u001C01\u001C01\u001CACD679B50E3B179CB289F9E62FB8F65EC3789CE94371064F22C40BEE7E0CAEFD173467827F8F15F15E739A6BA4686EE5C42B4893DBB85D597004CE873E40BF46B751B8EA552E72100A860DE6304E5F5D07EB2ED481CAEAFA454493901A0F140AE1070771878F6B8F01842B80AAFD88B9C339CDBD668807169D2E2DC3CFE981554813E353A25B5935ABAE204760E8A913\u001C03\u001C05E6C1039FCF05CA29BFBE4D11F9BA3743DBDB89\u001C80\u001C221231",
                    "3062843\u001D041A000000228\u001C18\u001C01\u001C01\u001CA523924AFD826DAD39BC4532CB121C19A702D2B0D3F29CE79E2CBD0F847BC112A5FF61EF0E3913A6DF63A3E8017FC2B19F0E61304889A88E406DAC0FF82A423052E5387EF6C073D2B8C6004D2D4077C5179A78902CE4A8F361A85C6F46D56A75F374AF7AAD0F8409098AC1F388517184001AA316D05C842907BF0D62F8A05E083DBC8FED48FF84108D1C411C5540604408C42066E6B2ED465BC0DCBBB06383EE88C1CF0A7F694317C8B3A8EF1019059B\u001C03\u001C9FE167D85CB9A9EF79FCA0B2CAB09C764850B93C\u001C9S\u001C221231",
                    "3062943\u001D041A000000228\u001C19\u001C01\u001C01\u001CA856777B6B4AF4D1C5D8E955852D57539FAC9B51B7879E3AC99A9F9B32F9A9DE267D6FB3BFAB54A4C4955EF90A5C4561C714A5F1E57550D33B320FF9238CE703CD4834712E32A3AC09B11320DFECFD885CF58EAAEAE2462FA0F194CCA29F0FF3431653DDD30B51101D98ED4377E3AC6B525AAB8D2023804C8724B3A98A4E94D3AE358FA1FC05E4A8DCFEFCFB5E930834D1E94AA665F923F40CDB06C3ABF213165ADE547E67A2800FD15D32EE42FCC30A07F1F6709E4984AE55A7DB79D4EBE184392358F1CBFE1D7C772A62954759AB7BA563EF4E09B72A961D19C2B5870EEC69F6E28493FD3EF0CF8F97FC6584B75696675045015DB4AA95\u001C03\u001C498EF9E709EAF1A93F8AD35561504A109FB2DACB\u001CDS\u001C221231",
                    "3063043\u001D041A000000228\u001C20\u001C01\u001C01\u001CEBA2E3FBE75D51C519B7A498CFE53F51519B292C1BBE0C78C14FBE38E3717DE0C0ECC04605879EF617B97ED1E8E989FCD2C7DDF61EE09E96F7EADA7D9F553426D6D6A8BE4DCF943D6C8F3627265520F757BBA16FF68749F3D796A0AAACA0ED0929BE112BF7CAB87BED4D9B5DD9B7A0CAC7F9CA513A6BFC03B4C20EFDC03E2B58D76E2ABF466665CB9D64AA412FBBF85259C480DA2F0896AB28FBB26022EFAE74CCDB9C36749E8D29AE4069A1298A0B07A7F72DFF8E6F442A2393DFF7E4E1F06F\u001C03\u001C48AE1E709DBBBDC9CEED5B5FEEE233CD1248CA70\u001CAO\u001C221231",
                    "3063143\u001D041A000000228\u001C21\u001C01\u001C01\u001C863B43586D710D2ECCF922644ACDD7015057A26BE7D6999A65D023DE94CD81A171E93C5BAB92C753767A4720C2ACFBF358387790CCD437806F9C1F19CF66FCF20BF42570FFE21ED742608F56C9CB0B4F277CF8EF3394C8BC595B314044197B7AAEAADBF1E44D763CDFE3DF368CBA6B09788F8EAEF9B47DEC02BEA131D58551430621D71AA5EEDE29FC1AC8CBE44CE92177E01EEBC080E94BEAD5FA0CAEF4B487\u001C03\u001C64D1FDA19D5E0893A6A7A31987805BB9A3D19B8B\u001C8W\u001C221231",
                    "3063243\u001D041A000000228\u001C22\u001C01\u001C01\u001C9CC38384539AFFDB955ADF9FCF03A6A669B5F68D91DBE1562002D4617E75FF0BEF16731D8CAE5B6E690E00C0F106BD8EB127DAD03108D40576C95572B3C43E4A9641C11C4C5AED06643543CD02B6E3811FBC4F72956CA9D8641374CFB659263B8B22B5C5A3B624E99BF5CBFD34B99A069DA312B1F7C03CF8F4CFA91BB269DBD565073A9AFCB1DA7D839F13D43B57F924BB85E1BCCE28BE5A8EC03AA56FED231B13B920A3BA4227F53F927EC27F9DB20C32EB2AD8F9C0770EC97D930764E844C9\u001C03\u001CE924B09D66B00B7811678092DF8F95D460F4CC47\u001CAO\u001C221231",
                    "3063343\u001D041A000000228\u001C23\u001C01\u001C01\u001CEEEACE9FD905E43DD1EC43F471570F9255379E43813267F0C6C0363977C607E8E169B834A5072977ADA9BDF4D0F50748F3D2DED1F863A9A510C4D67BC923EB53E77711BB079B32F2837F1381F141B27B9361E67DDB5AEF107F05231042A9D003DA49338476FBA2E8FFD8D48621C830A6BAB87751570BEAB77AA501846E8F9EDE25EAD306C45AA21CEEB506E5256AADB01AAA0A5C5773DB7A75DBFB5D1EA30C89BFCB4937C0B1B6EDADFF12F9808F1E9129A39AC6996C7D9E551BD1AF924320D965BC0726AD9F9CE430415F1FDF9AC37C3DC0454452D73F0E0B1CBC8214522F5F\u001C03\u001C6DE12BDA2B42F9950578C2C50A94A216636D6045\u001CCG\u001C221231",
                    "3063443\u001D041A000000228\u001C28\u001C01\u001C01\u001CC05B993401063615EF211036DD8154066BE4BBEC7E93A82E83C65E2CFD76E498A6DBF6135C816F606B9564A30D259A9FD5463AA78261223FBB4718EAD4A17347E11BE475BF0DDD9BE9315DCF585D58863A7BCA0E67440586DA098E33047C0DF6F6A1D1BD081BF283321DDF248FDFFA9FB749D0FDA47ADE2E7C0AAA76B146A00A5EBDA270C52832E8132FBC631EAC1120F02215829EB1D852B1969F1C1504A659AB6057C92AF92D981C8171B68E3300E3\u001C03\u001CF1B99C16FF415746FF423241E66F17AF0235B2C2\u001C9S\u001C221231",
                    "3063543\u001D041A000000228\u001C29\u001C01\u001C01\u001CB6D73BF68564C88A1AEE8BA70A5F60CE495CA722E097DADEEBB83B28040B1BAD16DBC9AC3CD181BA89193638E600AF397D220F0339A8E792AA08C1878482ACC463B3B3A257AE8667CDBC1D6613CB9CBB612830FDA7F7BA689A148EFFF34476F6E0A70C819C10B3B6150909B58BF9403F5BB2E9790EE82C50C8D6FB267C726DC255AE97FABF5A357B2A0FBD1387168D83B25ECD912027B3868F072E025240CF780CC8E5839823727E5547FD1366A203F4F70FA82660B8401D4D2D06FD9A4036D14C53F6289D6FDC724E7D06F31ED93AC1\u001C03\u001CE26D23C1F45519BE5FA796F639BEB750E8F7A349\u001CBK\u001C221231",
                    "3063643\u001D041A000000228\u001CF0\u001C01\u001C01\u001C79573B3263C847E96CCD9AAFD08D7BAFCA12C3991F5957B9B8E65C3E227EF575E81477D73FAE069724E07286383DE464464AE22F249D4F4F0A0AE12144BBA57424B83F9721DB754907BCB09BE08DA0BA84E4F4B3BA5B559A70F2540CA5FFD8F301F971D3EA7E279FD51F26C0BE2AEAB9D7E99CFA65D16BFB70FA9F20A80505216763E4A86455E0E754E87295ED73479013092DAD015F8B7797AA80E965FD96BD769181F8ECDF928A127869FE7FB3F371\u001C03\u001C927f73c7e2208fe57983d87cf656af5c80fafef6\u001C9S\u001C201231",
                    "3063743\u001D041A000000025\u001C0E\u001C01\u001C01\u001CAA94A8C6DAD24F9BA56A27C09B01020819568B81A026BE9FD0A3416CA9A71166ED5084ED91CED47DD457DB7E6CBCD53E560BC5DF48ABC380993B6D549F5196CFA77DFB20A0296188E969A2772E8C4141665F8BB2516BA2C7B5FC91F8DA04E8D512EB0F6411516FB86FC021CE7E969DA94D33937909A53A57F907C40C22009DA7532CB3BE509AE173B39AD6A01BA5BB85\u001C03\u001CA7266ABAE64B42A3668851191D49856E17F8FBCD\u001C80\u001C221231",
                    "3063843\u001D0511\u001C03\u001C*99***1#\u001CAPP\u001C60\u001C114.198.232.20\u001C2008\u001C3\u001C60\u001C0000000000000000\u001D0512\u001C04\u001C10.30.1.96\u001C2008\u001C3\u001C60\u001C0000000000000000",
                    "3063943\u001D061P1100001000007500VC100000750007500MC10000075000750000902000005030000000006660",
                    "3064043\u001D071A0000000031010  \u001CA0000000032010  \u001CA0000000041010  \u001CA0000000043060  \u001CA0000002281010  \u001CA0000002282010  \u001CA000000025010402\u001C\u001C\u001C",
                    "3064143\u001D081A0000000031010  \u001CVISA CREDIT         \u001C008C0084    \u001C0\u001C0\u001C9700                                    \u001C9F3704                                  \u001C9F530152\u001C0010000000\u001CD84004F800\u001CD84004A800\u001C00000290\u001C0\u001C0\u001D081A0000000032010  \u001CVisa Electron       \u001C008D008D    \u001C0\u001C0\u001C9700                                    \u001C9F3704                                  \u001C9F530152\u001C0810000000\u001CDC5004F800\u001CDC5000A800\u001C00000290\u001C0\u001C0\u001D081A0000000041010  \u001CMASTERCARD          \u001C0002        \u001C0\u001C0\u001C970F9F02065F2A029A039C0195059F3704      \u001C9F3704                                  \u001C9F530152\u001C0400000000\u001CF850ACF800\u001CF850ACA000\u001C00000000\u001C\u001C\u001D081A0000000043060  \u001CMAESTRO             \u001C0002        \u001C0\u001C0\u001C970F9F02065F2A029A039C0195059F3704      \u001C9F3704                                  \u001C9F530152\u001C0040000000\u001CF850ACF800\u001CFC50ACA000\u001C00000002\u001C\u001C",
                    "3064243\u001D081A0000002281010  \u001CSPAN MCHIP          \u001C0002        \u001C0\u001C0\u001C\u001C9F3704                                  \u001C\u001C0010000000\u001CFC408CF800\u001CFC408CA800\u001C00000290\u001C\u001C\u001D081A0000002282010  \u001CSPAN VSDC           \u001C0084008C    \u001C0\u001C0\u001C\u001C9F3704                                  \u001C\u001C0010000000\u001CFC408CF800\u001CFC408CA800\u001C00000290\u001C\u001C",
                    "3064343\u001D081A000000025010402\u001CAMEX                \u001C\u001C0\u001C0\u001C\u001C9F3704                                  \u001C\u001C0000000000\u001CCC00000000\u001CCC00000000\u001C\u001C\u001C"

            };


            sama_tms.Get_Sama_param(DE72buffer[i], this);
        }
//        tmsManager.insert(sama_tms.aid_list);
//        tmsManager.insert(sama_tms.aid_data);
//        tmsManager.insert(sama_tms.card_scheme);
//        tmsManager.insert(sama_tms.connection_parameters);
//        tmsManager.insert(sama_tms.device_specific);
//        tmsManager.insert(sama_tms.public_keys);
//        tmsManager.insert(sama_tms.retailer_data);
//        tmsManager.insert(sama_tms.revoked_certificates);

        AID_Data aid_data = new AID_Data();
        aid_data.AID = "2";
        aid_data.AID_Label = "AID_Label";
        aid_data.Default_DDOL = "DDOL";
        aid_data.Terminal_AID_version_numbers = "Terminal_AID_version_numbers";


        AIDRepo repository = new AIDRepo(this);
        if (repository.insert(aid_data)){
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        }
        AID_Data aid_data1 = repository.getById("1");
        Toast.makeText(this, aid_data1.Terminal_AID_version_numbers, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        PosApplication.getApp().oGPosTransaction.Reset();
        Intent returnhome = new Intent(this, Fragment_home_Transaction.class);

        startActivity(returnhome);

    }

    @Override
    public void onUserInteraction() {

        // if we need to do someting if user interacting
        //listen for user action
        //Start_transaction(pos_transaction,Purchase)
    }

    @Override
    protected void onResume() {
        super.onResume();
//        PosApplication.getApp().oGPosTransaction.Reset();
//        AidlLed mAidlLed = DeviceTopUsdkServiceManager.getInstance().getLedManager();
//        try {
//            if(mAidlLed != null){
//                mAidlLed.setLed(0 , false);
//            }
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
    }

    private boolean Terminal_Initialization() {

        // todo Terminal initialization
        //check hardware();
        // load Terminal configuration file(); //TMS parameter

        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1000);
                downLoadParM();
                downLoadKeys();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mProgressDialog != null && mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }

                    }
                });
            }
        }).start();

        return true;


    }

    private boolean downLoadParM() {
        AidlPboc mPbocManager = DeviceTopUsdkServiceManager.getInstance().getPbocManager();
        try {
            //Read the IC card parameter configuration file under assert and load the relevant parameters into the EMV kernel
            try {
                boolean updateResult = false;
                boolean flag = true;
                int i = 0;
                String success = "";
                String fail = "";
                // 获取IC卡参数信息
                mPbocManager.updateAID(0x03, null);
                mPbocManager.updateCAPK(0x03, null);

                InputStream ins = this.getAssets().open("icparam/ic_param.txt");
                if (ins != null && ins.available() != 0x00) {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(ins));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        // 未到达文件末尾
                        if (null != line) {
                            if (line.startsWith("AID")) {
                                // 更新AID
                                updateResult = mPbocManager.updateAID(0x01, line.split("=")[1]);

                            } else { // 更新RID
                                updateResult = mPbocManager.updateCAPK(0x01, line.split("=")[1]);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return true;
    }


    private boolean downLoadKeys() {

        final AidlPinpad pinpadManager = DeviceTopUsdkServiceManager.getInstance().getPinpadManager(0);
        final byte[] tmk = BCDASCII.hexStringToBytes("89F8B0FDA2F2896B9801F131D32F986D89F8B0FDA2F2896B");
        final byte[] tak = BCDASCII.hexStringToBytes("92B1754D6634EB22");
        final byte[] tpk = BCDASCII.hexStringToBytes("B5E175AC5FD8DD8A03AD23A35C5BAB6B");
        final byte[] trk = BCDASCII.hexStringToBytes("744185122EEC284830694CAD383B4F7A");
        boolean mIsSuccess = false;

        try {
            mIsSuccess = pinpadManager.loadMainkey(0, tmk, null);

            mIsSuccess = pinpadManager.loadWorkKey(PinpadConstant.WKeyType.WKEY_TYPE_MAK, 0, 0, tak, null);

            mIsSuccess = pinpadManager.loadWorkKey(PinpadConstant.WKeyType.WKEY_TYPE_PIK, 0, 0, tpk, null);

            mIsSuccess = pinpadManager.loadWorkKey(PinpadConstant.WKeyType.WKEY_TYPE_TDK, 0, 0, trk, null);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return true;
    }


    private void StartMADA_APP() {
        Load_Terminal_operation_data();
        boolean bRegistered = PosApplication.getApp().oGTerminal_Operation_Data.bregistered;
        Initialize_Security();
        if (bRegistered == true) {
            Initialize_EMV_Configuration();
            Initialize_CTLS_configuration();
        } else {

            while (!PosApplication.getApp().oGTerminal_Operation_Data.bregistered)  //todo add tries counter due to connection failures
            {
                POS_MAIN oPos_Main = new POS_MAIN();
                oPos_Main.Start_Transaction(PosApplication.getApp().oGPosTransaction, POSTransaction.TranscationType.TERMINAL_REGISTRATION);

            }
        }
    }

    private void Initialize_Security() {
        //todo initialize security parameter
    }

    private void Initialize_EMV_Configuration() {
        //todo initialize emv parameters
    }

    private void Initialize_CTLS_configuration() {
        //todo initialize contactless parameters
    }

    private void Load_Terminal_operation_data() {
        //todo Load Terminal operation data from database
    }


}
