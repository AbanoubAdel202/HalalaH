package com.example.halalah.sqlite.database.table;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.example.halalah.sqlite.database.BaseModel;
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
    @NonNull
    public String m_sMessage_Code;
    @NonNull
    public String m_sDisplay_Code;

    public String m_sArabic_Message_Text;

    public String m_sEnglish_Message_Text;

}
