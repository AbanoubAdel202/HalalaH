package com.example.halalah.registration;


import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import javax.crypto.Cipher;

public class RegistrationManager {

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

    public void signAndHashWithGeneratedKeys() {
        try {
            String text = "This is the message being signed";
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1152);
            KeyPair keyPair = keyGen.generateKeyPair();
            sign(text, keyPair.getPrivate(), keyPair.getPublic());
            hashSHA(text, keyPair.getPrivate());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void signAndHashWithLoadedKeys(byte[] privateKeyBytes, byte[] publicKeyBytes) {//, )

        try {
            String text = "This is the message being signed";

            PrivateKey privateKey = PrivateKeyReader.get(privateKeyBytes);

            PublicKey publicKey = PublicKeyReader.get(publicKeyBytes);

            sign(text, privateKey, publicKey);
            hashSHA(text, privateKey);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] sign(String text, PrivateKey privateKey, PublicKey publicKey) throws Exception {
        // Compute signature
        Signature instance = Signature.getInstance("SHA1withRSA");

        instance.initSign(privateKey);
        instance.update((text).getBytes());
        byte[] signature = instance.sign();
        System.out.println("Signature: " + Utils.byteArrayToHex(signature));

        instance.initVerify(publicKey);
        instance.update((text).getBytes());
        System.out.println("verify with signature : " + instance.verify(signature));

        return signature;
    }

    private void hashSHA(String text, PrivateKey privateKey) {
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
