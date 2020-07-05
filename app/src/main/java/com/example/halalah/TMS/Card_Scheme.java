package com.example.halalah.TMS;

import com.example.halalah.sqlite.repository.SqliteGenericObject;
import com.example.halalah.sqlite.repository.annotation.SqliteNotNull;
import com.example.halalah.sqlite.repository.annotation.SqliteNotNull;
import com.example.halalah.sqlite.repository.annotation.SqlitePrimaryKey;
import com.example.halalah.sqlite.repository.annotation.SqliteTableName;

/**********************************************************************/
@SqliteTableName("Card_Scheme")
  public  class Card_Scheme implements SqliteGenericObject {
        /////////////////
        //Segment1
        ////////////////
        @SqlitePrimaryKey
        @SqliteNotNull
        public String m_sCard_Scheme_ID;
        @SqliteNotNull
        public String m_sCard_Scheme_Name_Arabic;
        @SqliteNotNull
        public String m_sCard_Scheme_Name_English;
        @SqliteNotNull
        public String m_sCard_Scheme_Acquirer_ID;
        @SqliteNotNull
        public String m_sMerchant_Category_Code;
        @SqliteNotNull
        public String m_sMerchant_ID;
        @SqliteNotNull
        public String m_sTerminal_ID;
        @SqliteNotNull
        public String m_sEnable_EMV;
        @SqliteNotNull
        public String m_sCheck_Service_Code;
        @SqliteNotNull
        public String m_sOffline_Refund_PreAuthorization_Capture_Service_Indicator;
        /////////////////
        //Segment2
        ////////////////
        @SqliteNotNull
        public String m_sTransactions_Allowed;
        @SqliteNotNull
        public String m_sCardholder_Authentication;
        @SqliteNotNull
        public String m_sSupervisor_Functions;
        @SqliteNotNull
        public String m_sManual_entry_allowed;
        @SqliteNotNull
        public String m_sFloor_Limit_Indicator;
        @SqliteNotNull
        public String m_sTerminal_Floor_Limit;
        @SqliteNotNull
        public String m_sTerminal_Floor_Limit_Fallback;
        @SqliteNotNull
        public String m_sMaximum_Cash_back;
        @SqliteNotNull
        public String m_sMaximum_transaction_amount_indicator;
        @SqliteNotNull
        public String m_sMaximum_amount_allowed;
        @SqliteNotNull
        public String m_sLuhn_Check;
        @SqliteNotNull
        public String m_sExpiry_Date_Position;
        @SqliteNotNull
        public String m_sDelay_Call_Set_up;
        /////////////////
        //Segment3
        ////////////////
        @SqliteNotNull
        public String[] cardranges;
        @SqliteNotNull
        public String m_sCard_Prefix_Sequence_Indicator;

        public Card_Scheme()
        {
            /////////////////
            //Segment1
            ////////////////
             m_sCard_Scheme_ID="";
             m_sCard_Scheme_Name_Arabic="";
             m_sCard_Scheme_Name_English="";
             m_sCard_Scheme_Acquirer_ID="";
             m_sMerchant_Category_Code="";
             m_sMerchant_ID="";
             m_sTerminal_ID="";
             m_sEnable_EMV="";
             m_sCheck_Service_Code="";
             m_sOffline_Refund_PreAuthorization_Capture_Service_Indicator="";
            /////////////////
            //Segment2
            ////////////////
             m_sTransactions_Allowed="";
             m_sCardholder_Authentication="";
             m_sSupervisor_Functions="";
             m_sFloor_Limit_Indicator="";
             m_sTerminal_Floor_Limit="";
             m_sTerminal_Floor_Limit_Fallback="";
             m_sMaximum_Cash_back="";
             m_sMaximum_transaction_amount_indicator="";
             m_sMaximum_amount_allowed="";
             m_sLuhn_Check ="";
             m_sExpiry_Date_Position="";
             m_sDelay_Call_Set_up="";
            /////////////////
            //Segment3
            ////////////////
             //cardranges[];
             m_sCard_Prefix_Sequence_Indicator="";

        }

        // setting all values of card ranges to 0's
        public void SetDefults() {

        }

    @Override
    public String getId() {
        return m_sCard_Scheme_ID;
    }
}
