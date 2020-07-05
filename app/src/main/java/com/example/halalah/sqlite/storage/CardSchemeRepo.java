package com.example.halalah.sqlite.storage;

import android.content.Context;

import com.example.halalah.TMS.AID_List;
import com.example.halalah.TMS.Card_Scheme;
import com.example.halalah.sqlite.repository.impl.Repository;

import java.util.List;

public class CardSchemeRepo extends Repository<Card_Scheme> {

    public CardSchemeRepo(Context context) {
        super(context);
    }

    public Card_Scheme getById(String aid) {
        Card_Scheme wrapper = new Card_Scheme();
        wrapper.m_sCard_Scheme_ID = aid;

        return queryById(wrapper);
    }

    public List<Card_Scheme> getAll() {
        return queryAll();
    }


}
