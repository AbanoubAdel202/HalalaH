package com.example.halalah.registration;

import com.example.halalah.PosApplication;
import com.example.halalah.util.ExtraUtil;

import org.parceler.Parcel;

@Parcel
public class RegistrationData {
    String vendorId;              // 50
    String vendorTerminalType;    // 01
    String trsmid;                // 010203
    String vendorKeyIndex;        // 00,01
    String samaKeyIndex;          // 00,01
    String randomLengthIndicator;  // 0010
    String randomStringSequence;   //
    String vendorKeyLength; // 1152 = "148" * 2 = 288
    String vendorSignature;        // "encryptedData"

    public RegistrationData() {
        randomStringSequence = generateRandom();
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorTerminalType() {
        return vendorTerminalType;
    }

    public void setVendorTerminalType(String vendorTerminalType) {
        this.vendorTerminalType = vendorTerminalType;
    }

    public String getTrsmid() {
        return trsmid;
    }

    public void setTrsmid(String trsmid) {
        this.trsmid = trsmid;
    }

    public String getVendorKeyIndex() {
        return vendorKeyIndex;
    }

    public void setVendorKeyIndex(String vendorKeyIndex) {
        this.vendorKeyIndex = vendorKeyIndex;
    }

    public String getSamaKeyIndex() {
        return samaKeyIndex;
    }

    public void setSamaKeyIndex(String samaKeyIndex) {
        this.samaKeyIndex = samaKeyIndex;
    }

    public String getRandomLengthIndicator() {
        return randomLengthIndicator;
    }

    public void setRandomLengthIndicator(String randomLengthIndicator) {
        this.randomLengthIndicator = randomLengthIndicator;
        randomStringSequence = generateRandom();
    }

    public String getRandomStringSequence() {
        // choose a Character random from this String
        return randomStringSequence;
    }

    public String getVendorKeyLength() {
        return vendorKeyLength;
    }

    public void setVendorKeyLength(String vendorKeyLength) {
        this.vendorKeyLength = vendorKeyLength;
    }

    public String getVendorSignature() {
        RegistrationManager manager = RegistrationManager.getInstance();
        StringBuilder vendorHashingData = new StringBuilder();
        String dateTime = ExtraUtil.GetDate_Time();
        vendorHashingData.append(dateTime);
        PosApplication.getApp().oGPosTransaction.m_sTrxDateTime = dateTime;
        vendorHashingData.append("814"); // Terminal Registration Function Code
        vendorHashingData.append(vendorId);
        vendorHashingData.append(vendorTerminalType);
        vendorHashingData.append(trsmid);
        vendorHashingData.append(getRandomStringSequence());

        return vendorSignature = Utils.byteArrayToHex(manager.signAndHashWithLoadedKeys(vendorHashingData.toString()));

    }

    private String generateRandom() {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        Long length = 32L;
        try {
            length = Long.parseLong(randomLengthIndicator, 16);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(length.intValue());

        for (int i = 0; i < length; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return randomStringSequence = sb.toString();
    }
}
