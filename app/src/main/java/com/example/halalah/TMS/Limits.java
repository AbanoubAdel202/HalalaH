package com.example.halalah.TMS;

import androidx.annotation.NonNull;

import com.example.halalah.sqlite.database.Column;
import com.example.halalah.sqlite.database.Table;

import java.io.Serializable;

@Table(name = "Limits")
public class Limits implements Serializable {

    public Limits() {

    }

    @Column(name = "contactlessTransactionLimit")
    private String contactlessTransactionLimit;
    @Column(name = "terminalCVMRequiredLimit")
    private String terminalCVMRequiredLimit;
    @Column(name = "terminalContactlessFloorLimit")
    private String terminalContactlessFloorLimit;

    public String getContactlessTransactionLimit() {
        return contactlessTransactionLimit;
    }

    public void setContactlessTransactionLimit(String contactlessTransactionLimit) {
        this.contactlessTransactionLimit = contactlessTransactionLimit;
    }

    public String getTerminalCVMRequiredLimit() {
        return terminalCVMRequiredLimit;
    }

    public void setTerminalCVMRequiredLimit(String terminalCVMRequiredLimit) {
        this.terminalCVMRequiredLimit = terminalCVMRequiredLimit;
    }

    public String getTerminalContactlessFloorLimit() {
        return terminalContactlessFloorLimit;
    }

    public void setTerminalContactlessFloorLimit(String terminalContactlessFloorLimit) {
        this.terminalContactlessFloorLimit = terminalContactlessFloorLimit;
    }

    @NonNull
    @Override
    public String toString() {
        return contactlessTransactionLimit + "," + terminalCVMRequiredLimit + "," + terminalContactlessFloorLimit;
    }
}
