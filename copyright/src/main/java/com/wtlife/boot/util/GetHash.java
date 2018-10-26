package com.wtlife.boot.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GetHash {
    public static byte[] createCheckSum(String file, String hashType) throws IOException, NoSuchAlgorithmException {
        InputStream is = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        MessageDigest messageDigest = MessageDigest.getInstance(hashType);
        int numRead;
        do {
            numRead = is.read(buffer);
            if (numRead > 0) {
                messageDigest.update(buffer, 0, numRead);
            }
        } while (numRead != -1);
        is.close();
        return messageDigest.digest();
    }

    public static String getHash(String fileName, String hashType) throws IOException, NoSuchAlgorithmException {
        byte[] bytes = createCheckSum(fileName, hashType);
        String result = "";
        for (int i = 0; i < bytes.length; i++) {
            result += Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1);//加0x100是因为有的b[i]的十六进制只有1位
        }
        return result;
    }
}
