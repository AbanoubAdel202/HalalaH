package com.example.halalah.sqlite.database.dao;

import android.content.Context;

import com.example.halalah.sqlite.database.BaseDaoImpl;
import com.example.halalah.sqlite.database.table.Connection;
import com.example.halalah.sqlite.database.table.Connection_Parameters;
import com.example.halalah.sqlite.database.MyDBHelper;

import java.util.List;

public class ConnectionParametersDaoImpl extends BaseDaoImpl<Connection_Parameters> {

    public ConnectionParametersDaoImpl(Context context) {
        super(new MyDBHelper(context), Connection_Parameters.class);
    }

    /**
     * get the connection parameters from DB
     *
     */

    public Connection_Parameters get() {
        StringBuffer sb = new StringBuffer("SELECT * FROM Connection_Parameters LIMIT 1");
        List<Connection_Parameters> connectionParametersList = rawQuery(sb.toString(), null);
        if (connectionParametersList == null || connectionParametersList.size() == 0) {
            return null;
        }
        int index = connectionParametersList.size() - 1;
        return connectionParametersList.get(index);
    }

    /**
     * get the primary connection
     *
     */
    public Connection getPrimaryConnection() {
        StringBuffer sb = new StringBuffer("SELECT * FROM Connection_Parameters LIMIT 1");
        List<Connection_Parameters> connectionParametersList = rawQuery(sb.toString(), null);
        if (connectionParametersList == null || connectionParametersList.size() == 0) {
            return null;
        }
        return connectionParametersList.get(0).getConn_primary();
    }

    /**
     * get the secondary connection
     *
     */
    public Connection getSecondaryConnection() {
        StringBuffer sb = new StringBuffer("SELECT * FROM Connection_Parameters LIMIT 1");
        List<Connection_Parameters> connectionParametersList = rawQuery(sb.toString(), null);
        if (connectionParametersList == null || connectionParametersList.size() == 0) {
            return null;
        }
        return connectionParametersList.get(0).getConn_secondary();
    }

    /**
     * get connection type of the primary connection
     *
     */
    public String getPrimaryConnectionType() {
        StringBuffer sb = new StringBuffer("SELECT * FROM Connection_Parameters LIMIT 1");
        List<Connection_Parameters> capklist = rawQuery(sb.toString(), null);
        if (capklist == null || capklist.size() == 0) {
            return null;
        }
        return capklist.get(0).getPrimaryConnectionType();
    }

    /**
     * get connection type of the secondary connection
     *
     */
    public String getSecondaryConnectionType() {
        StringBuffer sb = new StringBuffer("SELECT * FROM Connection_Parameters LIMIT 1");
        List<Connection_Parameters> capklist = rawQuery(sb.toString(), null);
        if (capklist == null || capklist.size() == 0) {
            return null;
        }
        return capklist.get(0).getSecondaryConnectionType();
    }
}
