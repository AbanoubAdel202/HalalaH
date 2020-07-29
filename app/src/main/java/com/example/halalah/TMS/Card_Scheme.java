package com.example.halalah.TMS;

import com.example.halalah.sqlite.database.BaseModel;
import com.example.halalah.sqlite.database.Column;
import com.example.halalah.sqlite.database.Table;
import com.example.halalah.util.ArrayUtils;

import java.util.ArrayList;
import java.util.List;


/**********************************************************************/
@Table(name = "Card_Scheme")
public class Card_Scheme extends BaseModel {
    /////////////////
    //Segment1
    ////////////////
    @Column(name = "m_sCard_Scheme_ID", unique = true)
    public String m_sCard_Scheme_ID;

    @Column(name = "m_sCard_Scheme_Name_Arabic")
    public String m_sCard_Scheme_Name_Arabic;

    @Column(name = "m_sCard_Scheme_Name_English")
    public String m_sCard_Scheme_Name_English;

    @Column(name = "m_sCard_Scheme_Acquirer_ID")
    public String m_sCard_Scheme_Acquirer_ID;

    @Column(name = "m_sMerchant_Category_Code")
    public String m_sMerchant_Category_Code;

    @Column(name = "m_sMerchant_ID")
    public String m_sMerchant_ID;

    @Column(name = "m_sTerminal_ID")
    public String m_sTerminal_ID;

    @Column(name = "m_sEnable_EMV")
    public String m_sEnable_EMV;

    @Column(name = "m_sCheck_Service_Code")
    public String m_sCheck_Service_Code;

    @Column(name = "m_sOffline_Refund_PreAuthorization_Capture_Service_Indicator")
    public String m_sOffline_Refund_PreAuthorization_Capture_Service_Indicator;
    /////////////////
    //Segment2
    ////////////////

    @Column(name = "m_sTransactions_Allowed")
    public String m_sTransactions_Allowed;

    @Column(name = "m_sCardholder_Authentication")
    public String m_sCardholder_Authentication;

    @Column(name = "m_sSupervisor_Functions")
    public String m_sSupervisor_Functions;

    @Column(name = "m_sManual_entry_allowed")
    public String m_sManual_entry_allowed;

    @Column(name = "m_sFloor_Limit_Indicator")
    public String m_sFloor_Limit_Indicator;

    @Column(name = "m_sTerminal_Floor_Limit")
    public String m_sTerminal_Floor_Limit;

    @Column(name = "m_sTerminal_Floor_Limit_Fallback")
    public String m_sTerminal_Floor_Limit_Fallback;

    @Column(name = "m_sMaximum_Cash_back")
    public String m_sMaximum_Cash_back;

    @Column(name = "m_sMaximum_transaction_amount_indicator")
    public String m_sMaximum_transaction_amount_indicator;

    @Column(name = "m_sMaximum_amount_allowed")
    public String m_sMaximum_amount_allowed;

    @Column(name = "m_sLuhn_Check")
    public String m_sLuhn_Check;

    @Column(name = "m_sExpiry_Date_Position")
    public String m_sExpiry_Date_Position;

    @Column(name = "m_sDelay_Call_Set_up")
    public String m_sDelay_Call_Set_up;

    /////////////////
    //Segment3
    ////////////////

    private List<String> cardRanges = new ArrayList<>();
    @Column(name = "cardRangesStr")
    private String cardRangesStr;

    @Column(name = "m_sCard_Prefix_Sequence_Indicator")
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
        cardRanges = new ArrayList<>();
        cardRangesStr = "";

    }

    // setting all values of card ranges to 0's
    public void SetDefults() {

    }

    public List<String> getCardRanges() {
        if (cardRanges == null && cardRangesStr != null) {
            cardRanges = ArrayUtils.convertStringToList(cardRangesStr);
        }
        return cardRanges;
    }

    public void setCardRanges(String[] cardRanges) {
        for (int i = 0; i < cardRanges.length; i++) {
            this.cardRanges.add(cardRanges[i]);
        }
        this.cardRangesStr = ArrayUtils.convertArrayToString(cardRanges);
    }

    public String getCardRangesStr() {
        return cardRangesStr;
    }

    public void setCardRangesStr(String cardRangesStr) {
        this.cardRangesStr = cardRangesStr;
    }

    @Override
    public String toString() {
        return "=>\n" + m_sCard_Scheme_ID + ", \n" +
                "RangesArray=[" + ArrayUtils.convertListToString(cardRanges) + "], \n\n";
    }
}
