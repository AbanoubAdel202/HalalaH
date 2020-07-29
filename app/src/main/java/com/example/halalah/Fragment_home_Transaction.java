package com.example.halalah;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.halalah.TMS.Card_Scheme;
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


        Card_Scheme cardScheme = new Card_Scheme();
        cardScheme.m_sCard_Scheme_ID = "JC";
        cardScheme.setCardRangesStr("1,2,3");
        cardScheme.setCardRanges(new String[]{"1", "2", "3"});
        tmsManager.insert(cardScheme);
        Card_Scheme jc = tmsManager.getCardSchemeByID("JC");
        Log.d(TAG, "" + jc.getCardRangesStr());
//
//       Card_Scheme a1 = tmsManager.getCardSchemeByPAN("38");
//       Log.d(TAG,  "" +a1);
//       String a2 = tmsManager.getCardSchemeByPAN("588845").m_sCard_Scheme_ID;//P1
//        Log.d(TAG, a2);
//       String b1 = tmsManager.getCardSchemeByPAN("01490001").m_sCard_Scheme_ID;//VD
//        Log.d(TAG, b1);
//       String b2 = tmsManager.getCardSchemeByPAN("605141").m_sCard_Scheme_ID;//P1
//        Log.d(TAG, b2);
//       String c1 = tmsManager.getCardSchemeByPAN("38FFFF").m_sCard_Scheme_ID;//JC
//        Log.d(TAG, c1);
//       String d1 = tmsManager.getCardSchemeByPAN("400000").m_sCard_Scheme_ID;//VD
//        Log.d(TAG, d1);


    }
}
