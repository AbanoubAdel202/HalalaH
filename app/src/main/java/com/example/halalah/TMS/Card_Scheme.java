package com.example.halalah.TMS;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


/**********************************************************************/
@Entity
public class Card_Scheme {
    /////////////////
    //Segment1
    ////////////////
    @NonNull
    @PrimaryKey
    public String m_sCard_Scheme_ID;

    public String m_sCard_Scheme_Name_Arabic;

    public String m_sCard_Scheme_Name_English;

    public String m_sCard_Scheme_Acquirer_ID;

    public String m_sMerchant_Category_Code;

    public String m_sMerchant_ID;

    public String m_sTerminal_ID;

    public String m_sEnable_EMV;

    public String m_sCheck_Service_Code;

    public String m_sOffline_Refund_PreAuthorization_Capture_Service_Indicator;
    /////////////////
    //Segment2
    ////////////////

    public String m_sTransactions_Allowed;

    public String m_sCardholder_Authentication;

    public String m_sSupervisor_Functions;

    public String m_sManual_entry_allowed;

    public String m_sFloor_Limit_Indicator;

    public String m_sTerminal_Floor_Limit;

    public String m_sTerminal_Floor_Limit_Fallback;

    public String m_sMaximum_Cash_back;

    public String m_sMaximum_transaction_amount_indicator;

    public String m_sMaximum_amount_allowed;

    public String m_sLuhn_Check;

    public String m_sExpiry_Date_Position;

    public String m_sDelay_Call_Set_up;
    /////////////////
    //Segment3
    ////////////////

    public String[] cardranges;

    public String m_sCard_Prefix_Sequence_Indicator;

    public Card_Scheme() {
        /////////////////
        //Segment1
        ////////////////
        m_sCard_Scheme_ID = "";
        m_sCard_Scheme_Name_Arabic = "";
        m_sCard_Scheme_Name_English = "";
        m_sCard_Scheme_Acquirer_ID = "";
        m_sMerchant_Category_Code = "";
        m_sMerchant_ID = "";
        m_sTerminal_ID = "";
        m_sEnable_EMV = "";
        m_sCheck_Service_Code = "";
        m_sOffline_Refund_PreAuthorization_Capture_Service_Indicator = "";
        /////////////////
        //Segment2
        ////////////////
        m_sTransactions_Allowed = "";
        m_sCardholder_Authentication = "";
        m_sSupervisor_Functions = "";
        m_sFloor_Limit_Indicator = "";
        m_sTerminal_Floor_Limit = "";
        m_sTerminal_Floor_Limit_Fallback = "";
        m_sMaximum_Cash_back = "";
        m_sMaximum_transaction_amount_indicator = "";
        m_sMaximum_amount_allowed = "";
        m_sLuhn_Check = "";
        m_sExpiry_Date_Position = "";
        m_sDelay_Call_Set_up = "";
        /////////////////
        //Segment3
        ////////////////
        //cardranges[];
        m_sCard_Prefix_Sequence_Indicator = "";

    }

    // setting all values of card ranges to 0's
    public void SetDefults() {

    }
}
