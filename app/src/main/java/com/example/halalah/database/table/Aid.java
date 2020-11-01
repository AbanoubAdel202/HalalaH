package com.example.halalah.database.table;


import android.util.Log;

import com.example.halalah.database.BaseModel;
import com.example.halalah.database.Column;
import com.example.halalah.database.Table;
import com.topwise.cloudpos.struct.BytesUtil;
import com.topwise.cloudpos.struct.Tlv;
import com.topwise.cloudpos.struct.TlvList;

@Table(name = "tb_aid")
public class Aid extends BaseModel {
    @Column(name = "aid",style = "10,32",unique = true)
    private String aid = "";
    @Column(name = "selFlag",style = "0,1")
    private byte selFlag = 0;
    @Column(name = "priority",style = "0,1")
    private byte priority;
    @Column(name = "targetPer",style = "0,100")
    private byte targetPer;
    @Column(name = "maxTargetPer",style = "0,100")
    private byte maxTargetPer;
    @Column(name = "floorLimitFlg")
    private boolean floorLimitFlg = false;
    @Column(name = "floorLimit")
    private int floorLimit = 0;
    @Column(name = "rdClssTxnLimitFlg")
    private boolean rdClssTxnLimitFlg = false;
    @Column(name = "rdClssTxnLimit")
    private int rdClssTxnLimit = 0;
    @Column(name = "rdCVMLimitFlg")
    private boolean rdCVMLimitFlg = false;
    @Column(name = "rdCVMLimit")
    private int rdCVMLimit = 0;
    @Column(name = "rdClssFloorLimitFlg")
    private boolean rdClssFloorLimitFlg = false;
    @Column(name = "rdClssFloorLimit")
    private int rdClssFloorLimit = 0;
    @Column(name = "randTransSel")
    private boolean randTransSel = false;
    @Column(name = "velocityCheck")
    private boolean velocityCheck = false;
    @Column(name = "threShold")
    private int threShold;
    @Column(name = "tacDenial",style = "0,5")
    private byte[] tacDenial = new byte[0];
    @Column(name = "tacOnline",style = "0,5")
    private byte[] tacOnline = new byte[0];
    @Column(name = "tacDefault",style = "0,5")
    private byte[] tacDefault = new byte[0];
    @Column(name = "acquierId",style = "0,6")
    private byte[] acquierId = new byte[0];
    @Column(name = "dDol",style = "0,256")
    private byte[] dDol = new byte[0];
    @Column(name = "tDol",style = "0,256")
    private byte[] tDol = new byte[0];
    @Column(name = "version",style = "0,2")
    private byte[] version = new byte[0];
    @Column(name = "riskManData",style = "0,8")
    private byte[] riskManData = new byte[0];
    @Column(name = "merchName",style = "0,128")
    private byte[] merchName = new byte[0];
    @Column(name = "merchCateCode",style = "0,2")
    private byte[] merchCateCode = new byte[0];
    @Column(name = "merchId",style = "0,15")
    private byte[] merchId = new byte[0];
    @Column(name = "termId",style = "0,8")
    private byte[] termId = new byte[0];
    @Column(name = "transCurrCode",style = "0,2")
    private byte[] transCurrCode = new byte[0];
    @Column(name = "transCurrExp")
    private byte transCurrExp;
    @Column(name = "referCurrCode",style = "0,2")
    private byte[] referCurrCode = new byte[0];
    @Column(name = "referCurrExp")
    private byte referCurrExp;
    @Column(name = "referCurrCon")
    private int referCurrCon;
    @Column(name = "ecTtlFlg")
    private boolean ecTtlFlg = false;
    @Column(name = "ecTtl",style = "0,6")
    private byte[] ecTtl = new byte[0];
    @Column(name = "isAllowOnlinePIN",style = "0,1")
    private boolean isAllowOnlinePIN = true;
    @Column(name = "kernelType")
    private byte kernelType;
    @Column(name = "reserved")
    private byte reserved;

    public Aid(){

    }

    public Aid(byte kernelType){
        setKernelType(kernelType);
    }

    public void fromTlvList(TlvList tlvList) {
        final String TAG = "Aid,fromTlvList";

        Tlv tlv = tlvList.getTlv("9F06");
        if (tlv != null) {
            setAid(tlv.getHexValue());
            Log.d(TAG, "9F06: " + tlv.getHexValue());
        }

        tlv = tlvList.getTlv("DF01");
        if (tlv != null) {
            setSelFlag(tlv.getValue()[0]);
            Log.d(TAG, "DF01: " + tlv.getHexValue());
        }

        tlv = tlvList.getTlv("DF17");
        if (tlv != null) {
            setTargetPer(tlv.getValue()[0]);
            Log.d(TAG, "DF17: " + tlv.getHexValue());
        }

        tlv = tlvList.getTlv("DF16");
        if (tlv != null) {
            setMaxTargetPer(tlv.getValue()[0]);
            Log.d(TAG, "DF16: " + tlv.getHexValue());
        }

        tlv = tlvList.getTlv("9F1B");
        if (tlv != null) {
            setFloorLimit(BytesUtil.bytes2Int(tlv.getValue(), true));
            Log.d(TAG, "9F1B: " + tlv.getHexValue());
        }

        tlv = tlvList.getTlv("DF19");
        if (tlv != null) {
            Log.d(TAG, "DF19: " + tlv.getHexValue());
            int value = Integer.valueOf(tlv.getHexValue());
            Log.d(TAG, "DF19 INT: " + value);
            setRdClssFloorLimit(value);
        }

        tlv = tlvList.getTlv("DF20");
        if (tlv != null) {
            Log.d(TAG, "DF20: " + tlv.getHexValue());
            int value = Integer.valueOf(tlv.getHexValue());
            Log.d(TAG, "DF20 INT: " + value);
            setRdClssTxnLimit(value);
        }

        tlv = tlvList.getTlv("DF21");
        if (tlv != null) {
            Log.d(TAG, "DF21: " + tlv.getHexValue());
            int value = Integer.valueOf(tlv.getHexValue());
            Log.d(TAG, "DF21 INT: " + value);
            setRdCVMLimit(value);
        }

        tlv = tlvList.getTlv("DF15");
        if (tlv != null) {
            setThreShold(BytesUtil.bytes2Int(tlv.getValue(), true));
            Log.d(TAG, "DF15: " + tlv.getHexValue());
        }

        tlv = tlvList.getTlv("DF13");
        if (tlv != null) {
            setTacDenial(tlv.getValue());
            Log.d(TAG, "DF13: " + tlv.getHexValue());
        }

        tlv = tlvList.getTlv("DF12");
        if (tlv != null) {
            setTacOnline(tlv.getValue());
            Log.d(TAG, "DF12: " + tlv.getHexValue());
        }

        tlv = tlvList.getTlv("DF11");
        if (tlv != null) {
            setTacDefault(tlv.getValue());
            Log.d(TAG, "DF11: " + tlv.getHexValue());
        }

        tlv = tlvList.getTlv("9F01");
        if (tlv != null) {
            setAcquierId(tlv.getValue());
            Log.d(TAG, "9F01: " + tlv.getHexValue());
        }

        tlv = tlvList.getTlv("DF14");
        if (tlv != null) {
            setdDol(tlv.getValue());
            Log.d(TAG, "DF14: " + tlv.getHexValue());
        }

        tlv = tlvList.getTlv("DF8102");
        if (tlv != null) {
            settDol(tlv.getValue());
            Log.d(TAG, "DF8102: " + tlv.getHexValue());
        }

        tlv = tlvList.getTlv("9F08");
        if (tlv != null) {
            setVersion(tlv.getValue());
            Log.d(TAG, "9F08: " + tlv.getHexValue());
        }

        tlv = tlvList.getTlv("9F09");
        if (tlv != null) {
            setVersion(tlv.getValue());
            Log.d(TAG, "9F09: " + tlv.getHexValue());
        }

        tlv = tlvList.getTlv("9F1D");
        if (tlv != null) {
            setRiskManData(tlv.getValue());
            Log.d(TAG, "9F1D: " + tlv.getHexValue());
        }

        tlv = tlvList.getTlv("9F4E");
        if (tlv != null) {
            setMerchName(tlv.getValue());
            Log.d(TAG, "9F4E: " + tlv.getHexValue());
        }

        tlv = tlvList.getTlv("9F15");
        if (tlv != null) {
            setMerchCateCode(tlv.getValue());
            Log.d(TAG, "9F15: " + tlv.getHexValue());
        }

        tlv = tlvList.getTlv("9F16");
        if (tlv != null) {
            setMerchId(tlv.getValue());
            Log.d(TAG, "9F16: " + tlv.getHexValue());
        }

        tlv = tlvList.getTlv("9F1C");
        if (tlv != null) {
            setTermId(tlv.getValue());
            Log.d(TAG, "9F1C: " + tlv.getHexValue());
        }

        tlv = tlvList.getTlv("5F2A");
        if (tlv != null) {
            setTransCurrCode(tlv.getValue());
            Log.d(TAG, "5F2A: " + tlv.getHexValue());
        }

        tlv = tlvList.getTlv("5F36");
        if (tlv != null) {
            setTransCurrExp(tlv.getValue()[0]);
            Log.d(TAG, "5F36: " + tlv.getHexValue());
        }

        tlv = tlvList.getTlv("9F3C");
        if (tlv != null) {
            setReferCurrCode(tlv.getValue());
            Log.d(TAG, "9F3C: " + tlv.getHexValue());
        }

        tlv = tlvList.getTlv("9F3D");
        if (tlv != null) {
            setReferCurrExp(tlv.getValue()[0]);
            Log.d(TAG, "9F3D: " + tlv.getHexValue());
        }

        tlv = tlvList.getTlv("DF8101");
        if (tlv != null) {
            setReferCurrCon(BytesUtil.bytes2Int(tlv.getValue(), true));
            Log.d(TAG, "DF8101: " + tlv.getHexValue());
        }

        tlv = tlvList.getTlv("9F7B");
        if (tlv != null) {
            setEcTtl(tlv.getValue());
            Log.d(TAG, "9F7B: " + tlv.getHexValue());
        }
    }

    public TlvList getTlvList(){
        final String TAG = "Aid,getTlvList";

        TlvList tlvList = new TlvList();
        tlvList.addTlv("9F06",aid);
        tlvList.addTlv("DF01",new byte[]{selFlag});
        tlvList.addTlv("DF17",new byte[]{targetPer});
        tlvList.addTlv("DF16",new byte[]{maxTargetPer});
        tlvList.addTlv("9F1B",BytesUtil.int2Bytes(floorLimit,true));
        tlvList.addTlv("DF19",BytesUtil.hexString2Bytes(String.format("%012d", rdClssFloorLimit)));
        tlvList.addTlv("DF20",BytesUtil.hexString2Bytes(String.format("%012d", rdClssTxnLimit)));
        tlvList.addTlv("DF21",BytesUtil.hexString2Bytes(String.format("%012d", rdCVMLimit)));
        tlvList.addTlv("DF15",BytesUtil.int2Bytes(threShold,true));
        tlvList.addTlv("DF13",tacDenial);
        tlvList.addTlv("DF12",tacOnline);
        tlvList.addTlv("DF11",tacDefault);
        tlvList.addTlv("9F01",acquierId);
        tlvList.addTlv("DF14",dDol);
        tlvList.addTlv("DF8102",tDol);
        tlvList.addTlv("9F09",version);
        tlvList.addTlv("9F1D",riskManData);
        tlvList.addTlv("9F4E",merchName);
        tlvList.addTlv("9F15",merchCateCode);
        tlvList.addTlv("9F16",merchId);
        tlvList.addTlv("9F1C",termId);
        tlvList.addTlv("5F2A",transCurrCode);
        tlvList.addTlv("5F36",new byte[]{transCurrExp});
        tlvList.addTlv("9F3C",referCurrCode);
        tlvList.addTlv("9F3D",new byte[]{referCurrExp});
        tlvList.addTlv("DF8101",BytesUtil.int2Bytes(referCurrCon,true));
        tlvList.addTlv("9F7B",ecTtl);
        return tlvList;
    }

    public String getAid() {
        return this.aid;
    }

    public void setAid(String aid) {
        if (aid != null) {
            this.aid = aid;
        }
    }

    public byte getSelFlag() {
        return this.selFlag;
    }

    public void setSelFlag(byte selFlag) {
        this.selFlag = selFlag;
    }

    public byte getPriority() {
        return this.priority;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }

    public byte getTargetPer() {
        return this.targetPer;
    }

    public void setTargetPer(byte targetPer) {
        this.targetPer = targetPer;
    }

    public byte getMaxTargetPer() {
        return this.maxTargetPer;
    }

    public void setMaxTargetPer(byte maxTargetPer) {
        this.maxTargetPer = maxTargetPer;
    }

    public boolean isFloorLimitFlg() {
        return this.floorLimitFlg;
    }

    public int getFloorLimit() {
        return this.floorLimit;
    }

    public void setFloorLimit(int floorLimit) {
        if (floorLimit > 0) {
            this.floorLimitFlg = true;
        }

        this.floorLimit = floorLimit;
    }

    public boolean isRdClssTxnLimitFlg() {
        return rdClssTxnLimitFlg;
    }

    public int getRdClssTxnLimit() {
        return rdClssTxnLimit;
    }

    public void setRdClssTxnLimit(int rdClssTxnLimit) {
        if (rdClssTxnLimit > 0) {
            this.rdClssTxnLimitFlg = true;
        }
        this.rdClssTxnLimit = rdClssTxnLimit;
    }

    public boolean isRdCVMLimitFlg() {
        return rdCVMLimitFlg;
    }

    public int getRdCVMLimit() {
        return rdCVMLimit;
    }

    public void setRdCVMLimit(int rdCVMLimit) {
        if (rdCVMLimit > 0) {
            this.rdCVMLimitFlg = true;
        }
        this.rdCVMLimit = rdCVMLimit;
    }

    public boolean isRdClssFloorLimitFlg() {
        return rdClssFloorLimitFlg;
    }

    public int getRdClssFloorLimit() {
        return rdClssFloorLimit;
    }

    public void setRdClssFloorLimit(int rdClssFloorLimit) {
        if (rdClssFloorLimit > 0) {
            this.rdClssFloorLimitFlg = true;
        }
        this.rdClssFloorLimit = rdClssFloorLimit;
    }

    public boolean isRandTransSel() {
        return this.randTransSel;
    }

    public void setRandTransSel(boolean randTransSel) {
        this.randTransSel = randTransSel;
    }

    public boolean isVelocityCheck() {
        return this.velocityCheck;
    }

    public void setVelocityCheck(boolean velocityCheck) {
        this.velocityCheck = velocityCheck;
    }

    public int getThreShold() {
        return this.threShold;
    }

    public void setThreShold(int threShold) {
        this.threShold = threShold;
    }

    public byte[] getTacDenial() {
        return this.tacDenial;
    }

    public void setTacDenial(byte[] tacDenial) {
        if (tacDenial != null) {
            this.tacDenial = tacDenial;
        }

    }

    public byte[] getTacOnline() {
        return this.tacOnline;
    }

    public void setTacOnline(byte[] tacOnline) {
        if (tacOnline != null) {
            this.tacOnline = tacOnline;
        }

    }

    public byte[] getTacDefault() {
        return this.tacDefault;
    }

    public void setTacDefault(byte[] tacDefault) {
        if (tacDefault != null) {
            this.tacDefault = tacDefault;
        }

    }

    public byte[] getAcquierId() {
        return this.acquierId;
    }

    public void setAcquierId(byte[] acquierId) {
        if (acquierId != null) {
            this.acquierId = acquierId;
        }

    }

    public byte[] getdDol() {
        return this.dDol;
    }

    public void setdDol(byte[] dDol) {
        if (dDol != null) {
            this.dDol = dDol;
        }
    }

    public byte[] gettDol() {
        return this.tDol;
    }

    public void settDol(byte[] tDol) {
        if (tDol != null) {
            this.tDol = tDol;
        }

    }

    public byte[] getVersion() {
        return this.version;
    }

    public void setVersion(byte[] version) {
        if (version != null) {
            this.version = version;
        }

    }

    public byte[] getRiskManData() {
        return this.riskManData;
    }

    public void setRiskManData(byte[] riskManData) {
        if (riskManData != null) {
            this.riskManData = riskManData;
        }

    }

    public byte[] getMerchName() {
        return this.merchName;
    }

    public void setMerchName(byte[] merchName) {
        if (merchName != null) {
            this.merchName = merchName;
        }

    }

    public byte[] getMerchCateCode() {
        return this.merchCateCode;
    }

    public void setMerchCateCode(byte[] merchCateCode) {
        if (merchCateCode != null) {
            this.merchCateCode = merchCateCode;
        }

    }

    public byte[] getMerchId() {
        return this.merchId;
    }

    public void setMerchId(byte[] merchId) {
        if (merchId != null) {
            this.merchId = merchId;
        }

    }

    public byte[] getTermId() {
        return this.termId;
    }

    public void setTermId(byte[] termId) {
        if (termId != null) {
            this.termId = termId;
        }

    }

    public byte[] getTransCurrCode() {
        return this.transCurrCode;
    }

    public void setTransCurrCode(byte[] transCurrCode) {
        if (transCurrCode != null) {
            this.transCurrCode = transCurrCode;
        }

    }

    public byte getTransCurrExp() {
        return this.transCurrExp;
    }

    public void setTransCurrExp(byte transCurrExp) {
        this.transCurrExp = transCurrExp;
    }

    public byte[] getReferCurrCode() {
        return this.referCurrCode;
    }

    public void setReferCurrCode(byte[] referCurrCode) {
        if (referCurrCode != null) {
            this.referCurrCode = referCurrCode;
        }

    }

    public byte getReferCurrExp() {
        return this.referCurrExp;
    }

    public void setReferCurrExp(byte referCurrExp) {
        this.referCurrExp = referCurrExp;
    }

    public int getReferCurrCon() {
        return this.referCurrCon;
    }

    public void setReferCurrCon(int referCurrCon) {
        this.referCurrCon = referCurrCon;
    }

    public boolean getEcTtlFlg() {
        return this.ecTtlFlg;
    }

    public void setEcTtlFlg(boolean ecTtlFlg) {
        this.ecTtlFlg = ecTtlFlg;
    }

    public byte[] getEcTtl() {
        return this.ecTtl;
    }

    public void setEcTtl(byte[] ecTtl) {
        if (ecTtl != null) {
            this.ecTtl = ecTtl;
        }
    }

    public boolean isAllowOnlinePIN() {
        return this.isAllowOnlinePIN;
    }

    public void setAllowOnlinePIN(boolean isAllowOnlinePIN) {
        this.isAllowOnlinePIN = isAllowOnlinePIN;
    }

    public byte getKernelType() {
        return this.kernelType;
    }

    public void setKernelType(byte kernelType) {
        this.kernelType = kernelType;
    }

    public byte getReserved() {
        return this.reserved;
    }

    public void setReserved(byte reserved) {
        this.reserved = reserved;
    }

}
