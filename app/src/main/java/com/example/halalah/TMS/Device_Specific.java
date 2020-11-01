package com.example.halalah.TMS;

import com.example.halalah.sqlite.database.BaseModel;
import com.example.halalah.sqlite.database.Column;
import com.example.halalah.sqlite.database.Table;

import java.io.Serializable;

@Table(name = "Device_Specific")
public class Device_Specific extends BaseModel implements Serializable {

    private Limits p1Limits;
    @Column(name = "p1LimitsStr")
    private String p1LimitsStr;
    private Limits vcLimits;
    @Column(name = "vcLimitsStr")
    private String vcLimitsStr;
    private Limits mcLimits;
    @Column(name = "mcLimitsStr")
    private String mcLimitsStr;

    @Column(name = "m_sMax_SAF_Depth")
    public String m_sMax_SAF_Depth;
    @Column(name = "m_sMax_SAF_Cumulative_Amount")
    public String m_sMax_SAF_Cumulative_Amount;
    @Column(name = "m_sIdle_Time")
    public String m_sIdle_Time;
    @Column(name = "m_sMax_Reconciliation_Amount")
    public String m_sMax_Reconciliation_Amount;
    @Column(name = "m_sMax_Transactions_Processed")
    public String m_sMax_Transactions_Processed;
    @Column(name = "m_sQR_Code_Print_Indicator")
    public String m_sQR_Code_Print_Indicator;

    public Device_Specific() {

    }

    public Limits getP1Limits() {
        return p1Limits;
    }

    public void setP1Limits(Limits p1Limits) {
        this.p1Limits = p1Limits;
        this.p1LimitsStr = p1Limits.toString();
    }

    public String getP1LimitsStr() {
        return p1LimitsStr;
    }

    public void setP1LimitsStr(String p1LimitsStr) {
        this.p1LimitsStr = p1LimitsStr;
        if (p1Limits == null) {
            p1Limits = buildLimits(p1LimitsStr);
        }
    }

    public Limits getVcLimits() {
        return vcLimits;
    }

    public void setVcLimits(Limits vcLimits) {
        this.vcLimits = vcLimits;
        this.vcLimitsStr = vcLimits.toString();
    }

    public String getVcLimitsStr() {
        return vcLimitsStr;
    }

    public void setVcLimitsStr(String vcLimitsStr) {
        this.vcLimitsStr = vcLimitsStr;
        if (vcLimitsStr != null) {
            vcLimits = buildLimits(vcLimitsStr);
        }
    }

    public Limits getMcLimits() {
        return mcLimits;
    }

    public void setMcLimits(Limits mcLimits) {
        this.mcLimits = mcLimits;
        this.mcLimitsStr = mcLimits.toString();
    }

    public String getMcLimitsStr() {
        return mcLimitsStr;
    }

    public void setMcLimitsStr(String mcLimitsStr) {
        this.mcLimitsStr = mcLimitsStr;
        if (mcLimitsStr != null) {
            mcLimits = buildLimits(mcLimitsStr);
        }
    }

    public Limits buildLimits(String input) {
        Limits limits = new Limits();
        try {
            String[] splittedLimits = input.split(",");
            limits.setContactlessTransactionLimit(splittedLimits[0]);
            limits.setTerminalCVMRequiredLimit(splittedLimits[1]);
            limits.setTerminalContactlessFloorLimit(splittedLimits[2]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return limits;
    }
}
