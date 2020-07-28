package com.example.halalah.TMS;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.halalah.sqlite.database.BaseModel;
import com.example.halalah.sqlite.database.Column;
import com.example.halalah.sqlite.database.Table;

/**********************************************************************/
@Table(name = "Aid")
public class Aid extends BaseModel {
    /////////////
    //Segment_1_of_AID_List
    //////////////////////

    @Column(name = "aidName", unique = true)
    public String aidName;

    public Aid(){
        
    }

}
