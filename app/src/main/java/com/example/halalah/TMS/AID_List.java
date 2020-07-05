package com.example.halalah.TMS;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.halalah.sqlite.repository.SqliteGenericObject;
import com.example.halalah.sqlite.repository.annotation.SqliteNotNull;
import com.example.halalah.sqlite.repository.annotation.SqlitePrimaryKey;
import com.example.halalah.sqlite.repository.annotation.SqliteTableName;

/**********************************************************************/
@Entity
public class AID_List implements SqliteGenericObject {
    /////////////
    //Segment_1_of_AID_List
    //////////////////////
    @PrimaryKey
    public String id;
    @ColumnInfo(name = "AID")
    public String[] AID;

    @Override
    public String getId() {
        return id == null ? "0" : id;
    }

}
