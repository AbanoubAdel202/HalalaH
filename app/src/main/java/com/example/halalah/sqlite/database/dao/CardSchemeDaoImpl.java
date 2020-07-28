package com.example.halalah.sqlite.database.dao;

import android.content.Context;

import com.example.halalah.TMS.Card_Scheme;
import com.example.halalah.sqlite.database.BaseDaoImpl;
import com.example.halalah.sqlite.database.MyDBHelper;
import com.example.halalah.util.AidToCardSchemeMapper;

import java.util.List;

public class CardSchemeDaoImpl extends BaseDaoImpl<Card_Scheme> {
    public CardSchemeDaoImpl(Context context) {
        super(new MyDBHelper(context), Card_Scheme.class);
    }

    /**
     * select all aid from database
     *
     * @return
     */
    public List<Card_Scheme> getAll() {
        StringBuffer sb = new StringBuffer("select * from Card_Scheme");
        List<Card_Scheme> aidlist = rawQuery(sb.toString(), null);
        return aidlist;
    }

    /**
     * select aid from database
     *
     * @param id the hexstring of aid
     * @return
     */
    public Card_Scheme getById(String id) {
        StringBuffer sb = new StringBuffer("select * from Card_Scheme where m_sCard_Scheme_ID='")
                .append(id).append("'");
        List<Card_Scheme> aidlist = rawQuery(sb.toString(), null);
        if (aidlist == null || aidlist.size() == 0) {
            return null;
        }
        return aidlist.get(0);
    }

    public Card_Scheme getByAid(String aid) {
        String cardSchemeId = AidToCardSchemeMapper.getCardSchemeByAID(aid);
        StringBuffer sb = new StringBuffer("select * from Card_Scheme where m_sCard_Scheme_ID='")
                .append(cardSchemeId).append("'");
        List<Card_Scheme> aidlist = rawQuery(sb.toString(), null);
        if (aidlist == null || aidlist.size() == 0) {
            return null;
        }
        return aidlist.get(0);
    }

    public Card_Scheme getByPAN(String pan){
        StringBuffer sb = new StringBuffer("SELECT * from Card_Scheme where cardRangesStr LIKE '%")
                .append(pan).append("%'");
        List<Card_Scheme> list = rawQuery(sb.toString(), null);
        if (list == null || list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    // TODO check mada fist
    // TODO SAVE ALL as ranges no :

}
