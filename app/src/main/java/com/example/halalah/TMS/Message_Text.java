package com.example.halalah.TMS;

import com.example.halalah.sqlite.database.BaseModel;
import com.example.halalah.sqlite.database.Column;
import com.example.halalah.sqlite.database.Table;

/**********************************************************************/
@Table(name = "Message_Text")
public class Message_Text extends BaseModel {

    public Message_Text() {
        m_sMessage_Code = "0";
        m_sDisplay_Code = "0";
        m_sArabic_Message_Text = "0";
        m_sEnglish_Message_Text = "0";
    }

    /////////////////
    //Segment1
    ////////////////
    @Column(name = "m_sMessage_Code")
    public String m_sMessage_Code;
    @Column(name = "m_sDisplay_Code")
    public String m_sDisplay_Code;
    @Column(name = "m_sArabic_Message_Text")
    public String m_sArabic_Message_Text;
    @Column(name = "m_sEnglish_Message_Text")
    public String m_sEnglish_Message_Text;

}
