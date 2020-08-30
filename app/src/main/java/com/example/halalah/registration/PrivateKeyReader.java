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

    // modulus = "00997b44a6f8e28c60b548e0f2f3fc44dcbbbf82896f23f96a13df3d5af2b385c53468e52fca99e207e500f36c722fc736cde866eefd53934fd6a736f8dc74a10637d298086097973f91963666c428cfc6001c320698d8df599f93ba8e9fa89202a1a9497b51fbf1ae9d3bb38c5c91db9e82034514f389b4e329e64c78c4623bc7e9698e89f5f95974ccc8d6a8eaff9663"
    // exponent = "255555e36cf8d609af8e448e1fac10c011fe26abceed1327a3f808015da14a0d5fc67ceff302d61d98918751bae906f1a7b5121e74f8a74ac5829eab3c8b12694bd9479355c40231ae0c75c5e2aba912681b9c1c90209445bea7808032a6a394568c272a7f4fd13d2632c84906e42d1b81cf72bb0cd80c4bfdd71a0b568fd6b920dc500d9c18421257007868b889672d00"
    private static PrivateKey get(String _modulus, String _exponent) throws GeneralSecurityException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
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
