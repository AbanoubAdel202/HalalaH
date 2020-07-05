package com.example.halalah.TMS;

import com.example.halalah.sqlite.repository.SqliteGenericObject;
import com.example.halalah.sqlite.repository.annotation.SqliteNotNull;
import com.example.halalah.sqlite.repository.annotation.SqliteNotNull;
import com.example.halalah.sqlite.repository.annotation.SqlitePrimaryKey;
import com.example.halalah.sqlite.repository.annotation.SqliteTableName;

/**********************************************************************/
@SqliteTableName("AID_Data")
public class AID_Data implements SqliteGenericObject {
    @SqlitePrimaryKey
    @SqliteNotNull
    public String AID;
    @SqliteNotNull
    public String AID_Label;
    @SqliteNotNull
    public String Terminal_AID_version_numbers;
    @SqliteNotNull
    public String Exact_only_selection;
    @SqliteNotNull
    public String Skip_EMV_processing;
    @SqliteNotNull
    public String Default_TDOL;
    @SqliteNotNull
    public String Default_DDOL;
    @SqliteNotNull
    public String EMV_additional_tags;
    @SqliteNotNull
    public String Denial_action_code;
    @SqliteNotNull
    public String Online_action_code;
    @SqliteNotNull
    public String Default_action_code;
    @SqliteNotNull
    public String Threshold_Value_for_Biased_Random_Selection;
    @SqliteNotNull
    public String Target_Percentage;
    @SqliteNotNull
    public String Maximum_Target_Percentage_for_Biased_Random_Selection;

    @Override
    public String getId() {
        return AID;
    }
}
