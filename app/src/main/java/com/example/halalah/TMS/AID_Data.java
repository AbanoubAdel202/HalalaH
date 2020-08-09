package com.example.halalah.TMS;

import com.example.halalah.sqlite.database.BaseModel;
import com.example.halalah.sqlite.database.Column;
import com.example.halalah.sqlite.database.Table;

/**********************************************************************/
@Table(name = "AID_Data")
public class AID_Data extends BaseModel {
    @Column(name = "aid", unique = true)
    public String AID;
    @Column(name = "AID_Label")
    public String AID_Label;
    @Column(name = "Terminal_AID_version_numbers")
    public String Terminal_AID_version_numbers;
    @Column(name = "Exact_only_selection")
    public String Exact_only_selection;
    @Column(name = "Skip_EMV_processing")
    public String Skip_EMV_processing;
    @Column(name = "Default_TDOL")
    public String Default_TDOL;
    @Column(name = "Default_DDOL")
    public String Default_DDOL;
    @Column(name = "EMV_additional_tags")
    public String EMV_additional_tags;
    @Column(name = "Denial_action_code")
    public String Denial_action_code;
    @Column(name = "Online_action_code")
    public String Online_action_code;
    @Column(name = "Default_action_code")
    public String Default_action_code;
    @Column(name = "Threshold_Value_for_Biased_Random_Selection")
    public String Threshold_Value_for_Biased_Random_Selection;
    @Column(name = "Target_Percentage")
    public String Target_Percentage;
    @Column(name = "Maximum_Target_Percentage_for_Biased_Random_Selection")
    public String Maximum_Target_Percentage_for_Biased_Random_Selection;

    public AID_Data(){

    }
}
