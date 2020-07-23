package com.example.halalah.TMS;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Connection_Parameters {

    @NonNull
    @PrimaryKey
    public int pk;

    private String primaryConnectionType;
    private String secondaryConnectionType;
    private Connection conn_primary;
    private Connection conn_secondary;

    public Connection_Parameters() {
        conn_primary = new Connection();
        conn_secondary = new Connection();
    }

    public Connection getConn_primary() {
        return conn_primary;
    }

    public void setConn_primary(Connection conn_primary) {
        this.conn_primary = conn_primary;
        this.primaryConnectionType = conn_primary.Communication_Type;
    }

    public Connection getConn_secondary() {
        return conn_secondary;
    }

    public void setConn_secondary(Connection conn_secondary) {
        this.conn_secondary = conn_secondary;
        this.secondaryConnectionType = conn_secondary.Communication_Type;
    }

    public String getPrimaryConnectionType() {
        return primaryConnectionType;
    }

    public String getSecondaryConnectionType() {
        return secondaryConnectionType;
    }

    public void setPrimaryConnectionType(String primaryConnectionType) {
        this.primaryConnectionType = primaryConnectionType;
    }

    public void setSecondaryConnectionType(String secondaryConnectionType) {
        this.secondaryConnectionType = secondaryConnectionType;
    }
}
