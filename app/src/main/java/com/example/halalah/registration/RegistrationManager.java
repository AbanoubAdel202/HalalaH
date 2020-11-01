package com.example.halalah.registration;


import android.content.Context;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

public class RegistrationManager {
    private static RegistrationManager instance;

    // TODO use this code when we need to this registration Manager
//    RegistrationManager registrationManager = new RegistrationManager();
//        registrationManager.testWithGeneratedKeys();
//
//        try {
//        byte[] privateKeyBytes = AssetsHelper.loadKeyFile(getActivity(), "private_key.der");
//        byte[] publicKeyBytes = AssetsHelper.loadKeyFile(getActivity(), "public_key.der");
//        registrationManager.testWithLoadedKeys(privateKeyBytes, publicKeyBytes);
//    } catch (IOException e) {
//        e.printStackTrace();
//    }
    private PrivateKey privateKey;
    private PublicKey publicKey;

    private RegistrationManager() {
    }

    public static RegistrationManager getInstance() {
        if (instance == null) {
            instance = new RegistrationManager();
        }
        return instance;
    }

    public boolean loadKeys(Context context, String vendorKeyIndex, String samaKeyIndex) {
        try {
           // byte[] privateKeyBytes = AssetsHelper.loadKeyFile(context, "vendor_private_" + vendorKeyIndex + ".der");
            //byte[] publicKeyBytes = AssetsHelper.loadKeyFile(context, "sama_public_" + samaKeyIndex + ".der");
           //// byte[] privateKeyBytes = AssetsHelper.loadKeyFile(context, "Hala.pem");

            /*
            * todo load and inject Vendor and Saudi Payment keys from Key Stone and keep them encrypted
            */
            // Hala RSA Key 1152 bit (Hala ASTREX keys)
            String sHalaModulus ="9BEC65EF180397836B8573D7E97EA4BECA636D0A57068D975212DB84AC87EFED448EFB488292B9BB422EA9D18E4121254C8D0F78018CEFC1A3A77E3FC56DD04E1C8BF1807FE1054C5CC8A4BE010243C4A69A45EA83213C4D188B45B1741EF3C208EA06F29DA21EE23895E1DB46EBEEA8031EA0558733C1C1F95F55D88047F7BB99B1852D2BF1BFDA3244A91799586E2B";
            String sHalaPrivateExponent ="5DCA9FAE68E590384F6F2135D990828CF9B5411C1D2925F6C639EDAE2B1242329930E0DAC7207774BAE4D2E5E90A2864055E3CD5D528EFF7ACF7CDAB9298F1DA6AB58B79DE727EFA75CE3242A95880D08D84A1C9378EAB6DA34B25E1DF19EA103FE6DC4564F27BF5E72177527F7481C523E891AD5E2CA9C039BF47BEE476C4928439DCB3456CE84F782236EA0BDA1321";
            String sHalaPublicExponent = "010001";
            // Loading Hala Key for Signing
            privateKey = PrivateKeyReader.get(sHalaModulus,sHalaPrivateExponent);
            // privateKey = PrivateKeyReader.get(privateKeyBytes);
            //Load Saudi Payment Key for Validation


            // Saudi Payment Public Key 1152 bit (Hala ASTREX keys)
            String sSPModulus ="9BEC65EF180397836B8573D7E97EA4BECA636D0A57068D975212DB84AC87EFED448EFB488292B9BB422EA9D18E4121254C8D0F78018CEFC1A3A77E3FC56DD04E1C8BF1807FE1054C5CC8A4BE010243C4A69A45EA83213C4D188B45B1741EF3C208EA06F29DA21EE23895E1DB46EBEEA8031EA0558733C1C1F95F55D88047F7BB99B1852D2BF1BFDA3244A91799586E2B";
            String sSPPublicExponent = "010001";
            publicKey = PublicKeyReader.get(sSPModulus,sSPPublicExponent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

 /*   public String signAndHashWithGeneratedKeys(String text) {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1152);
            KeyPair keyPair = keyGen.generateKeyPair();
            return sign(hashSHA(text, keyPair.getPrivate()), keyPair.getPrivate(), keyPair.getPublic());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

    public String signAndHashWithLoadedKeys(String text) {
        try {
            String sHashedData =  SHA1(text);
            //String TestHash =SHA1("30323132303934303034383134470101111001BC4EE82A3E8477A37DAA1DC8F2D6DCD9");
            String signedTxt =sign(Utils.hexStringToByteArray(sHashedData), privateKey);
            // for testing only
            //boolean Verify = Verify(Utils.hexStringToByteArray("6FE107DE0D11A930BE0110F8E5BB677D16933D0D1DEF9913AD2CFABD0413F9A227DA70210EBD48C25B38FD9D3246A72168B996101E6707FAFFC1950FBF199016CE40BA0E11D647312FA4F9AD4FBDE232CCB316B0EE92B19A17DE1572D18436B6DEE83FA82A10F11984F58F658E6B0BF32DC7774AF3F9F4653553F8F24D278E0284CA3719F8B294529711DC1BF0C05DE9"),Utils.hexStringToByteArray("5363A88B0591DCFED8D74F95C68AF2DC3F44DE24"),publicKey);
            boolean Verify = Verify(Utils.hexStringToByteArray(signedTxt),Utils.hexStringToByteArray(sHashedData),publicKey);
            return signedTxt;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String sign(byte[] hashedText, PrivateKey privateKey) throws Exception {
        // Compute signature
        Signature instance = Signature.getInstance("NONEwithRSA");

        instance.initSign(privateKey);
        instance.update(hashedText);
        byte[] signature = instance.sign();
        String strSignatureVtxt =Utils.convertToHex(signature);
        System.out.println("Signature: " + strSignatureVtxt);

/*        instance.initVerify(publicKey);
        instance.update(hashedText);
        System.out.println("verify with signature : " + instance.verify(signature));*/

        return strSignatureVtxt;
    }

    public static String SHA1(String text)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-1");
        byte[] sha1hash = new byte[40];
        md.update(Utils.hexStringToByteArray(text));
        sha1hash = md.digest();
        return Utils.convertToHex(sha1hash);
    }

    /*private byte[] hashSHA(String text, PrivateKey privateKey) {
        try {
            // Compute digest
            String strTestingPlainText ="8022A1ACA61755F5A60912ECA2C1AFE9527861DA";
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            byte[] digest = sha1.digest(strTestingPlainText.getBytes());
            sha1.update(digest,0,digest.length);

            digest = sha1.digest();

            return digest;

            // Encrypt digest
*//*
            Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            //byte[] cipherText = cipher.doFinal(digest);


            String strTestingPlainText ="8022A1ACA61755F5A60912ECA2C1AFE9527861DA";
            byte[] bytePlainText = strTestingPlainText.getBytes();
            byte[] cipherText = cipher.doFinal(bytePlainText);

            // Display results
            System.out.println("Input data: " + text);
            System.out.println("Digest: " + Utils.byteArrayToHex(digest));
            System.out.println("Cipher text: " + Utils.byteArrayToHex(cipherText));

           return cipherText;
*//*

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

    private boolean Verify(byte[] signature, byte [] Text , PublicKey publicKey) throws Exception {
        // Compute signature
        Signature instance = Signature.getInstance("NONEwithRSA");

        instance.initVerify(publicKey);
        instance.update(Text);
        boolean bResults = instance.verify(signature);

        return bResults;
    }
}
