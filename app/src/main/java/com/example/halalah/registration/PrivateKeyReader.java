package com.example.halalah.registration;


import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;

public class PrivateKeyReader {

    public static PrivateKey get(byte[] keyBytes)
            throws Exception {

        PKCS8EncodedKeySpec spec =
                new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    public static PrivateKey get(String _modulus, String _exponent) throws GeneralSecurityException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        BigInteger modulus = new BigInteger(_modulus, 16);
        BigInteger exponent = new BigInteger(_exponent, 16);
        RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(modulus, exponent);
        try {
            return keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException("Unexpected key format!", e);
        }
    }

}
