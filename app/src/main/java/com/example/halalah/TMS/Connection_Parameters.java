package com.example.halalah.TMS;

import com.example.halalah.sqlite.database.BaseModel;
import com.example.halalah.sqlite.database.Column;
import com.example.halalah.sqlite.database.Table;

@Table(name = "Connection_Parameters")
public class Connection_Parameters extends BaseModel {
    @Column(name = "primaryConnectionType")
    private String primaryConnectionType;
    @Column(name = "secondaryConnectionType")
    private String secondaryConnectionType;
    @Column(name = "conn_primary")
    private Connection conn_primary;
    @Column(name = "conn_secondary")
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
