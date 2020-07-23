package com.example.halalah.TMS;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**********************************************************************/
@Entity
public class AID_List {
    /////////////
    //Segment_1_of_AID_List
    //////////////////////
    @NonNull
    @PrimaryKey
    public int pk;
    @ColumnInfo(name = "AID")
    public String[] AID;

}
