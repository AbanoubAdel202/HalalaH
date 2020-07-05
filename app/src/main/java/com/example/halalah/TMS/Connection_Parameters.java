package com.example.halalah.TMS;

import com.example.halalah.sqlite.repository.SqliteGenericObject;
import com.example.halalah.sqlite.repository.annotation.SqliteNotNull;
import com.example.halalah.sqlite.repository.annotation.SqlitePrimaryKey;
import com.example.halalah.sqlite.repository.annotation.SqliteTableName;

/**********************************************************************/
@SqliteTableName("Connection_Parameters")
public class Connection_Parameters implements SqliteGenericObject {
    @SqlitePrimaryKey
    @SqliteNotNull
    public String id;
    @SqliteNotNull
    public Conn_Primary conn_primary;
    @SqliteNotNull
    public Conn_ٍSecondary conn_secondary;

    public Connection_Parameters() {
        conn_primary = new Conn_Primary();
        conn_secondary = new Conn_ٍSecondary();
    }

    @Override
    public String getId() {
        return id;
    }
}
