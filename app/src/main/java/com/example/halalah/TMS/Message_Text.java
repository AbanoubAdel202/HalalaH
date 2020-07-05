package com.example.halalah.TMS;

import com.example.halalah.sqlite.repository.SqliteGenericObject;
import com.example.halalah.sqlite.repository.annotation.SqliteNotNull;
import com.example.halalah.sqlite.repository.annotation.SqlitePrimaryKey;
import com.example.halalah.sqlite.repository.annotation.SqliteTableName;

/**********************************************************************/
@SqliteTableName("Message_Text")
public class Message_Text implements SqliteGenericObject {
    public Message_Text() {
        m_sMessage_Code = "0";
        m_sDisplay_Code = "0";
        m_sArabic_Message_Text = "0";
        m_sEnglish_Message_Text = "0";
    }

    /////////////////
    //Segment1
    ////////////////
    @SqlitePrimaryKey
    @SqliteNotNull
    public String id;
    @SqliteNotNull
    public String m_sMessage_Code;
    public String m_sDisplay_Code;
    @SqliteNotNull
    public String m_sArabic_Message_Text;
    @SqliteNotNull
    public String m_sEnglish_Message_Text;

    @Override
    public String getId() {
        return id;
    }
}
