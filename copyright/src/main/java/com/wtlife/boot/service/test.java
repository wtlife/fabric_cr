package com.wtlife.boot.service;

import com.wtlife.boot.util.DSA;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

public class test {
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, InvalidKeyException {
        String[] keypair = DSA.getKeyPair().split("<split>");
        String data = new String("who am i");
        String pri = keypair[0];
        String pub = keypair[1];

        System.out.println("pub:" + pub);
        System.out.println(pub.length());
        System.out.println("pri:" + pri);
        System.out.println(pri.length());

        String signature = DSA.signature(data, pri);
        System.out.println(signature);
        System.out.println(signature.length());

        System.out.println(DSA.verify(data, signature, pub));
    }
}
