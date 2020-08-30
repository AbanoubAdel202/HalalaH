package com.example.halalah.TMS;

import com.example.halalah.sqlite.database.BaseModel;
import com.example.halalah.sqlite.database.Column;
import com.example.halalah.sqlite.database.Table;

@Table(name = "Revoked_Certificates")
public class Revoked_Certificates extends BaseModel {
    ////////////////////
    //Segment 1 of Revoked Certificates
    ///////////////////
    @Column(name = "RID_Revoked_Certificate")
    public String RID_Revoked_Certificate;
    @Column(name = "IDX")
    public String IDX;
    @Column(name = "Cert_serial_number")
    public String Cert_serial_number;

    public Revoked_Certificates() {

    }
}
