package com.example.halalah.sqlite.storage;

import android.content.Context;

import com.example.halalah.TMS.Gsm;
import com.example.halalah.TMS.Message_Text;
import com.example.halalah.sqlite.repository.impl.Repository;

import java.util.List;

public class MessagesRepo extends Repository<Message_Text> {

    public MessagesRepo(Context context) {
        super(context);
    }

    public Message_Text getById(String aid) {
        Message_Text wrapper = new Message_Text();
        wrapper.id = aid;

        return queryById(wrapper);
    }

    public List<Message_Text> getAll() {
        return queryAll();
    }


}
