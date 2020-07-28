package com.example.halalah.sqlite.database.table;

import com.example.halalah.sqlite.database.Column;
import com.example.halalah.sqlite.database.Table;

@Table(name = "Limits")
public class Limits {
    @Column(name = "contactlessTransactionLimit")
    public String contactlessTransactionLimit;
    @Column(name = "terminalCVMRequiredLimit")
    public String terminalCVMRequiredLimit;
    @Column(name = "terminalContactlessFloorLimit")
    public String terminalContactlessFloorLimit;
}
