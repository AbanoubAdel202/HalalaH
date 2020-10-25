package com.example.halalah.registration;

import com.example.halalah.PosApplication;
import com.example.halalah.util.ExtraUtil;

import org.parceler.Parcel;

import java.io.Serializable;

@Parcel
public class RegistrationData implements Serializable {
    String vendorId;                // 50
    String vendorTerminalType;      // 01
    String trsmid;                  // 010203
    String vendorKeyIndex;          // 00,01
    String samaKeyIndex;            // 00,01
    int randomLengthIndicator = 16; // 000010
    String randomLengthIndicatorHex; // 000010
    String randomStringSequence;    //
    String vendorKeyLength;         // 1152 = "148" * 2 = 288 ==> should be send as 3 byte = 000090
    String vendorSignature;         // "encryptedData"

    public RegistrationData() {
        randomStringSequence = generateRandom();
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = String.format("%02X", Long.valueOf(vendorId, 16));
    }

    public String getVendorTerminalType() {
        return vendorTerminalType;
    }

    public void setVendorTerminalType(String vendorTerminalType) {
        this.vendorTerminalType = String.format("%02X", Long.valueOf(vendorTerminalType, 16));
    }

    public String getTrsmid() {
        return trsmid;
    }

    public void setTrsmid(String trsmid) {
        this.trsmid = String.format("%06X", Long.valueOf(trsmid, 16));
    }

    public String getVendorKeyIndex() {
        return vendorKeyIndex;
    }

    public void setVendorKeyIndex(String vendorKeyIndex) {
        this.vendorKeyIndex = String.format("%02X", Long.valueOf(vendorKeyIndex, 16));
    }

    public String getSamaKeyIndex() {
        return samaKeyIndex;
    }

    public void setSamaKeyIndex(String samaKeyIndex) {
        this.samaKeyIndex = String.format("%02X", Long.valueOf(samaKeyIndex, 16));
    }

    public int getRandomLengthIndicator() {
        return randomLengthIndicator;
    }

    public void setRandomLengthIndicator(int randomLengthIndicator) {
        this.randomLengthIndicator = randomLengthIndicator;
        this.randomLengthIndicatorHex = String.format("%06X", Integer.valueOf(randomLengthIndicator));
        randomStringSequence = generateRandom();
    }

    public String getRandomLengthIndicatorHex() {
        return randomLengthIndicatorHex;
    }

    public String getRandomStringSequence() {
        // choose a Character random from this String
        return randomStringSequence;
    }

    public String getVendorKeyLength() {
        return vendorKeyLength;
    }

    public void setVendorKeyLength(String vendorKeyLength) {
        this.vendorKeyLength = String.format("%06X", Integer.valueOf(vendorKeyLength));
    }

    public String getVendorSignature() {
        RegistrationManager manager = RegistrationManager.getInstance();
        StringBuilder vendorHashingData = new StringBuilder();
        String dateTime = ExtraUtil.GetDate_Time();
        PosApplication.getApp().oGPosTransaction.m_sTrxDateTime = dateTime;
        vendorHashingData.append(Utils.asciiToHex(dateTime).toString());
        vendorHashingData.append(Utils.asciiToHex( "814").toString()); // Terminal Registration Function Code
        vendorHashingData.append(vendorId);
        vendorHashingData.append(vendorTerminalType);
        vendorHashingData.append(trsmid);
        vendorHashingData.append(getRandomStringSequence());

        return vendorSignature = manager.signAndHashWithLoadedKeys(vendorHashingData.toString());

    }

    private String generateRandom() {
        String hexString = "ABCDEF0123456789";

        int lengthInCharacters = randomLengthIndicator * 2;

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(lengthInCharacters);

        for (int i = 0; i < lengthInCharacters; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index = (int) (hexString.length() * Math.random());

            // add Character one by one in end of sb
            sb.append(hexString.charAt(index));
        }
        return randomStringSequence = sb.toString();
    }
}
