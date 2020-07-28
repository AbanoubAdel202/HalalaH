package com.example.halalah.sqlite.database.dao;

import android.content.Context;

import com.example.halalah.sqlite.database.BaseDaoImpl;
import com.example.halalah.sqlite.database.table.Message_Text;
import com.example.halalah.sqlite.database.MyDBHelper;

import java.util.List;

public class MessageDaoImpl extends BaseDaoImpl<Message_Text> {
    public MessageDaoImpl(Context context) {
        super(new MyDBHelper(context), Message_Text.class);
    }

    /**
     * select all messages from database
     *
     * @return
     */
    public List<Message_Text> getAll() {
        StringBuffer sb = new StringBuffer("select * from Message_Text");
        List<Message_Text> messageTextList = rawQuery(sb.toString(), null);
        return messageTextList;
    }

    /**
     * select single message from database
     *
     * @param messageCode
     * @param displayCode
     *
     * @return Message
     */
    public Message_Text get(String messageCode, String displayCode) {
        StringBuffer sb = new StringBuffer("select * from Message_Text where m_sMessage_Code='")
                .append(messageCode).append("'")
                .append(" AND m_sDisplay_Code='")
                .append(displayCode).append("'");
        List<Message_Text> list = rawQuery(sb.toString(), null);
        if (list == null || list.size() == 0) {
            return null;
        }
        return list.get(0);
    }



}
