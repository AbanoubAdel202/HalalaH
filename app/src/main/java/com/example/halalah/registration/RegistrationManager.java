package com.example.halalah.registration;


import android.content.Context;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import javax.crypto.Cipher;

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
            byte[] privateKeyBytes = AssetsHelper.loadKeyFile(context, "vendor_private_" + vendorKeyIndex + ".der");
            byte[] publicKeyBytes = AssetsHelper.loadKeyFile(context, "sama_public_" + samaKeyIndex + ".der");

            privateKey = PrivateKeyReader.get(privateKeyBytes);
            publicKey = PublicKeyReader.get(publicKeyBytes);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public byte[] signAndHashWithGeneratedKeys(String text) {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1152);
            KeyPair keyPair = keyGen.generateKeyPair();
            return sign(hashSHA(text, keyPair.getPrivate()), keyPair.getPrivate(), keyPair.getPublic());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] signAndHashWithLoadedKeys(String text) {//, )
        try {
            return sign(hashSHA(text, privateKey), privateKey, publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] sign(byte[] hashedText, PrivateKey privateKey, PublicKey publicKey) throws Exception {
        // Compute signature
        Signature instance = Signature.getInstance("SHA1withRSA");

        instance.initSign(privateKey);
        instance.update(hashedText);
        byte[] signature = instance.sign();
        System.out.println("Signature: " + Utils.byteArrayToHex(signature));

        instance.initVerify(publicKey);
        instance.update(hashedText);
        System.out.println("verify with signature : " + instance.verify(signature));

        return signature;
    }

    private byte[] hashSHA(String text, PrivateKey privateKey) {
        try {
            // Compute digest
            MessageDigest sha1 = MessageDigest.getInstance("SHA1");
            byte[] digest = sha1.digest((text).getBytes());

            // Encrypt digest
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] cipherText = cipher.doFinal(digest);

            // Display results
            System.out.println("Input data: " + text);
            System.out.println("Digest: " + Utils.byteArrayToHex(digest));
            System.out.println("Cipher text: " + Utils.byteArrayToHex(cipherText));

            return cipherText;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
