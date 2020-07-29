package com.example.halalah.sqlite.database.dao;

import android.content.Context;
import android.util.Log;

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
        List<Card_Scheme> list = rawQuery(sb.toString(), null);
        if (list == null || list.size() == 0) {
            return null;
        }
        for (int i = 0; i < list.size(); i++) {
            Card_Scheme s = list.get(i);
            s.getCardRanges();
            list.set(i, s);
        }
        return list;
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
        List<Card_Scheme> list = rawQuery(sb.toString(), null);
        if (list == null || list.size() == 0) {
            return null;
        }
        Card_Scheme s = list.get(0);
        s.getCardRanges();

        return s;
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

    public Card_Scheme getByPAN(String pan) {

        if (pan.length() < 6) {
            return null;
        }
        if (pan.length() > 6) {
            pan = pan.substring(0, 6);
        }

        // Search in Mada first
        Card_Scheme madaScheme = getById("P1");
        if (schemaHasPAN(pan, madaScheme)) {
            madaScheme.getCardRanges();
            return madaScheme;
        }

        // search the whole scheme list
        List<Card_Scheme> cardSchemeList = getAll();
        return findPanInSchemeList(pan, cardSchemeList);
    }

    private Card_Scheme findPanInSchemeList(String pan, List<Card_Scheme> cardSchemeList) {
        if (pan != null && cardSchemeList != null) {
            for (Card_Scheme scheme : cardSchemeList) {
                if (schemaHasPAN(pan, scheme)) {
                    scheme.getCardRanges();
                    return scheme;
                }
            }
        }
        return null;
    }

    private boolean schemaHasPAN(String pan, Card_Scheme scheme) {
        try {
            List<String> ranges = scheme.getCardRanges();
            if (ranges != null && ranges.size() > 0) {
                for (int i = 0; i < ranges.size(); i++) {
                    String range = ranges.get(i);
                    if (range != null && !range.isEmpty()) {

                        if (range.contains(":")) {
                            String[] boundaries = range.split("-");
                            Long min = Long.parseLong(boundaries[0]);
                            Long max = Long.parseLong(boundaries[1]);
                            Long panLong = Long.parseLong(pan);
                            if (panLong >= min && panLong <= max) {
                                return true;
                            }
                        } else if (range.contains("F")) {

                            range = range.replaceAll("F", "");
                            if (pan.startsWith(range)) {
                                return true;
                            }
                        } else if (range.equals(pan)) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public long insertOrUpdate(Card_Scheme cardScheme) {
        Log.d("cardSchemeUpdate", "id: " + cardScheme.m_sCard_Scheme_ID);
        StringBuffer sb = new StringBuffer("select * from Card_Scheme where m_sCard_Scheme_ID='")
                .append(cardScheme.m_sCard_Scheme_ID).append("'");
        List<Card_Scheme> list = rawQuery(sb.toString(), null);
        if (list == null || list.isEmpty()) {
            insert(cardScheme);
        } else {
            cardScheme.setId(list.get(0).getId());
            int result = update(cardScheme);
            Log.d("cardSchemeUpdate", "" + result);

        }
        return -1;
    }

}
