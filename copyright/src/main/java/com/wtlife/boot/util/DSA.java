package com.wtlife.boot.util;

import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class DSA {

    public static String getKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
        keyPairGenerator.initialize(256);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        ECPrivateKey ecPrivateKey = (ECPrivateKey) keyPair.getPrivate();
        ECPublicKey ecPublicKey = (ECPublicKey) keyPair.getPublic();

        byte[] pri = ecPrivateKey.getEncoded();
        byte[] pub = ecPublicKey.getEncoded();

        String privateKey = Base64.getEncoder().encodeToString(pri);
        String publiKey = Base64.getEncoder().encodeToString(pub);

        return privateKey + "<split>" + publiKey;
    }

    public static String signature(String data, String pri) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
        byte[] privateKey = Base64.getDecoder().decode(pri);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        PrivateKey privateKey1 = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Signature signature = Signature.getInstance("SHA1withECDSA");
        signature.initSign(privateKey1);
        signature.update(data.getBytes());
        byte[] res = signature.sign();
        String result = Base64.getEncoder().encodeToString(res);
        return result;
    }

    public static boolean verify(String data, String signature, String pub) throws SignatureException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] publicKey = Base64.getDecoder().decode(pub);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        PublicKey publicKey1 = keyFactory.generatePublic(x509EncodedKeySpec);
        Signature signature1 = Signature.getInstance("SHA1withECDSA");
        signature1.initVerify(publicKey1);
        signature1.update(data.getBytes());
        byte[] sig = Base64.getDecoder().decode(signature);
        Boolean bool = signature1.verify(sig);
        return bool;
    }

}
