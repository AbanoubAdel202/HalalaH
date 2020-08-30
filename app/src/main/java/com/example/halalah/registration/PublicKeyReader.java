package com.example.halalah.registration;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class PublicKeyReader {

    public static PublicKey get(byte[] keyBytes)
            throws Exception {

        X509EncodedKeySpec spec =
                new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    // modulus = "00997b44a6f8e28c60b548e0f2f3fc44dcbbbf82896f23f96a13df3d5af2b385c53468e52fca99e207e500f36c722fc736cde866eefd53934fd6a736f8dc74a10637d298086097973f91963666c428cfc6001c320698d8df599f93ba8e9fa89202a1a9497b51fbf1ae9d3bb38c5c91db9e82034514f389b4e329e64c78c4623bc7e9698e89f5f95974ccc8d6a8eaff9663"
    // exponent = "25"
    private static PublicKey get(String _modulus, String _exponent) throws GeneralSecurityException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
        BigInteger modulus = new BigInteger(_modulus, 16);
        BigInteger exponent = new BigInteger(_exponent, 16);
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
        try {
            return keyFactory.generatePublic(keySpec);
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException("Unexpected key format!", e);
        }
    }




}
