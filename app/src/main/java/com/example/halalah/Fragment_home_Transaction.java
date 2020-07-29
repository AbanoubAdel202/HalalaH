package com.example.halalah;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.halalah.TMS.Message_Text;
import com.example.halalah.TMS.TMSManager;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_home_Transaction#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_home_Transaction extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = Utils.TAGPUBLIC + Fragment_home_Transaction.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_home_Transaction() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_home_Transaction.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_home_Transaction newInstance(String param1, String param2) {
        Fragment_home_Transaction fragment = new Fragment_home_Transaction();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
View root =inflater.inflate(R.layout.fragment_home_transaction, container, false);
        Button mada1_btn = root.findViewById(R.id.Mada1_btn);
        mada1_btn.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {

        TMSManager tmsManager = TMSManager.getInstance();
        Message_Text message;


        message = tmsManager.getMessage("117", "0");
        Log.d(TAG, "1. 117 message " + message.m_sEnglish_Message_Text);


        message = tmsManager.getMessage("0", "0");
        Log.d(TAG, "2. 0 message " + message.m_sEnglish_Message_Text);

        message = tmsManager.getMessage("916", "0");
//        cardScheme = tmsManager.getCardSchemeByPAN("4516010000545654");//P1
//        Log.d(TAG, "1. cardSchemeID " + cardScheme.m_sCard_Scheme_ID + ", limit" + cardScheme.m_sMaximum_amount_allowed);
//
//        cardScheme = tmsManager.getCardSchemeByPAN("4506330000946622");//JC
//        Log.d(TAG, "2. cardSchemeID " + cardScheme.m_sCard_Scheme_ID + ", limit" + cardScheme.m_sMaximum_amount_allowed);
//
//        cardScheme = tmsManager.getCardSchemeByPAN("4511510002154364");//JC
//        Log.d(TAG, "3. cardSchemeID " + cardScheme.m_sCard_Scheme_ID + ", limit" + cardScheme.m_sMaximum_amount_allowed);
//
//        cardScheme = tmsManager.getCardSchemeByPAN("543131203232222");//MC
//        Log.d(TAG, "3. cardSchemeID " + cardScheme.m_sCard_Scheme_ID + ", limit" + cardScheme.m_sMaximum_amount_allowed);
//
//        cardScheme = tmsManager.getCardSchemeByPAN("7777774734535");//
//        Log.d(TAG, "4. cardSchemeID " + cardScheme.m_sCard_Scheme_ID + ", limit" + cardScheme.m_sMaximum_amount_allowed);
//
//        cardScheme = tmsManager.getCardSchemeByPAN("78946");//
//        Log.d(TAG, "5. cardSchemeID " + cardScheme.m_sCard_Scheme_ID + ", limit" + cardScheme.m_sMaximum_amount_allowed);


//        cardScheme = tmsManager.getCardSchemeByPAN("999994734535");//
//        Log.d(TAG, "5. cardSchemeID " + cardScheme.m_sCard_Scheme_ID + ", limit" + cardScheme.m_sMaximum_amount_allowed);

//        cardScheme = tmsManager.getCardSchemeByPAN(null);//
//        Log.d(TAG, "6. cardSchemeID " + cardScheme.m_sCard_Scheme_ID + ", limit" + cardScheme.m_sMaximum_amount_allowed);
//
//        cardScheme = tmsManager.getCardSchemeByAID("A0000000041010");//MC
//        Log.d(TAG, "cardSchemeID " + cardScheme.m_sCard_Scheme_ID + ", limit" + cardScheme.m_sMaximum_amount_allowed);
//
////        cardScheme = tmsManager.getCardSchemeByAID("A000000004");//MC
////        Log.d(TAG, "cardSchemeID " + cardScheme.m_sCard_Scheme_ID + ", limit" + cardScheme.m_sMaximum_amount_allowed);
//
//        cardScheme = tmsManager.getCardSchemeByAID("");//MC
//        Log.d(TAG, "cardSchemeID " + cardScheme.m_sCard_Scheme_ID + ", limit" + cardScheme.m_sMaximum_amount_allowed);


//        Retailer_Data retailer_data = tmsManager.getRetailerData();
//        Log.d(TAG, retailer_data.toString());
//
//        List<Card_Scheme> cardSchemaList = tmsManager.getAllCardScheme();
//        Log.d(TAG, "cardSchemaList size = " + cardSchemaList.size());
//        for (Card_Scheme data : cardSchemaList) {
//            Log.d(TAG, data.toString());
//        }
//
//        List<Aid> aidList = tmsManager.getAidList();
//        Log.d(TAG, "aid List size = " + aidList.size());
//        for (Aid data : aidList) {
//            Log.d(TAG, data.toString());
//        }
//
//        List<AID_Data> aidData = tmsManager.getAidDataList();
//        Log.d(TAG, "AidData List size = " + aidData.size());
//        for (AID_Data data : aidData) {
//            Log.d(TAG, data.toString());
//        }
//
//        List<Public_Key> publicKeyList = tmsManager.getAllPublicKeys();
//        Log.d(TAG, "publicKey List size = " + publicKeyList.size());
//        for (Public_Key data : publicKeyList) {
//            Log.d(TAG, data.toString());
//        }
//
//        Connection_Parameters connectionParameters = tmsManager.getConnectionParameters();
//        Log.d(TAG, connectionParameters.toString());
//
//        Device_Specific deviceSpecific = tmsManager.getDeviceSpecific();
//        Log.d(TAG, deviceSpecific.toString());
//
//        List<Message_Text> messagesList = tmsManager.getAllMessages();
//        Log.d(TAG, "messages List size = " + messagesList.size());
//        for (Message_Text data : messagesList) {
//            Log.d(TAG, data.toString());
//        }

    }
}
