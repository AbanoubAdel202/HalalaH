package com.example.halalah.sqlite.database.table;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.halalah.sqlite.database.BaseModel;
import com.example.halalah.sqlite.database.Column;
import com.example.halalah.sqlite.database.Table;

/**********************************************************************/
@Table(name = "AID_List")
public class AID_List extends BaseModel {
    /////////////
    //Segment_1_of_AID_List
    //////////////////////

    @Column(name = "AID")
    public String[] AID;

}
