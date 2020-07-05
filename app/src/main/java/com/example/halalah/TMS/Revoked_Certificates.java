package com.example.halalah.TMS;

import com.example.halalah.sqlite.repository.SqliteGenericObject;
import com.example.halalah.sqlite.repository.annotation.SqliteNotNull;
import com.example.halalah.sqlite.repository.annotation.SqlitePrimaryKey;
import com.example.halalah.sqlite.repository.annotation.SqliteTableName;

/**********************************************************************/
@SqliteTableName("Revoked_Certificates")
public class Revoked_Certificates implements SqliteGenericObject {
    ////////////////////
    //Segment 1 of Revoked Certificates
    ///////////////////
    @SqlitePrimaryKey
    @SqliteNotNull
    public String id;
    @SqliteNotNull
    public String RID_Revoked_Certificate;
    @SqliteNotNull
    public String IDX;
    @SqliteNotNull
    public String Cert_serial_number;

    @Override
    public String getId() {
        return id;
    }
}
