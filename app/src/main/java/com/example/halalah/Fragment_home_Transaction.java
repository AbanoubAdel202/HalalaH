package com.example.halalah;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.halalah.TMS.AID_Data;
import com.example.halalah.TMS.Aid;
import com.example.halalah.TMS.Card_Scheme;
import com.example.halalah.TMS.Connection_Parameters;
import com.example.halalah.TMS.Device_Specific;
import com.example.halalah.TMS.Message_Text;
import com.example.halalah.TMS.Public_Key;
import com.example.halalah.TMS.Retailer_Data;
import com.example.halalah.TMS.TMSManager;

import java.util.List;


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


        Retailer_Data retailer_data = tmsManager.getRetailerData();
        Log.d(TAG, retailer_data.toString());

        List<Card_Scheme> cardSchemaList = tmsManager.getAllCardScheme();
        Log.d(TAG, "cardSchemaList size = " + cardSchemaList.size());
        for (Card_Scheme data : cardSchemaList) {
            Log.d(TAG, data.toString());
        }

        List<Aid> aidList = tmsManager.getAidList();
        Log.d(TAG, "aid List size = " + aidList.size());
        for (Aid data : aidList) {
            Log.d(TAG, data.toString());
        }

        List<AID_Data> aidData = tmsManager.getAidDataList();
        Log.d(TAG, "AidData List size = " + aidData.size());
        for (AID_Data data : aidData) {
            Log.d(TAG, data.toString());
        }

        List<Public_Key> publicKeyList = tmsManager.getAllPublicKeys();
        Log.d(TAG, "publicKey List size = " + publicKeyList.size());
        for (Public_Key data : publicKeyList) {
            Log.d(TAG, data.toString());
        }

        Connection_Parameters connectionParameters = tmsManager.getConnectionParameters();
        Log.d(TAG, connectionParameters.toString());

        Device_Specific deviceSpecific = tmsManager.getDeviceSpecific();
        Log.d(TAG, deviceSpecific.toString());

        List<Message_Text> messagesList = tmsManager.getAllMessages();
        Log.d(TAG, "messages List size = " + messagesList.size());
        for (Message_Text data : messagesList) {
            Log.d(TAG, data.toString());
        }

    }
}
