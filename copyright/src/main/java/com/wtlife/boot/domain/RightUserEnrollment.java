package com.wtlife.boot.domain;

import org.hyperledger.fabric.sdk.Enrollment;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

public class RightUserEnrollment implements Enrollment {
    public String CERTDIR = new File(System.getProperty("user.dir")).getParent();
    private String keyfilepath;
    private String certfilepath;

    public RightUserEnrollment(String USERTYPE, String userName, String mspid) {
        if (mspid.equals("centerMSP")) {
            CERTDIR = CERTDIR + "/right/workspaces/center/crypto-config";
        } else if (mspid.equals("press1MSP")) {
            CERTDIR = CERTDIR + "/right/workspaces/press1/crypto-config";
        }


        if ("peer".equals(USERTYPE)) {
            CERTDIR = CERTDIR + "/peerOrganizations";
        } else if ("orderer".equals(USERTYPE)) {
            CERTDIR = CERTDIR + "/ordererOrganizations";
        }


        if (mspid.equals("centerMSP")) {
            certfilepath = CERTDIR + "/center.copyright.com/users/" + userName + "@center.copyright.com/msp/signcerts/";
            File skfile = new File(certfilepath);
            certfilepath = certfilepath + skfile.listFiles()[0].getName();

            keyfilepath = CERTDIR + "/center.copyright.com/users/" + userName + "@center.copyright.com/msp/keystore/";
            File keyfile = new File(keyfilepath);
            keyfilepath = keyfilepath + keyfile.listFiles()[0].getName();
        } else if (mspid.equals("press1MSP")) {
            certfilepath = CERTDIR + "/press1.copyright.com/users/" + userName + "@press1.copyright.com/msp/signcerts/";
            File skfile = new File(certfilepath);
            certfilepath = certfilepath + skfile.listFiles()[0].getName();

            keyfilepath = CERTDIR + "/press1.copyright.com/users/" + userName + "@press1.copyright.com/msp/keystore/";
            File keyfile = new File(keyfilepath);
            keyfilepath = keyfilepath + keyfile.listFiles()[0].getName();
        }
    }

    @Override
    public PrivateKey getKey() {
        try {
            return loadPrivateKey(Paths.get(keyfilepath));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getCert() {
        try {
            return new String(Files.readAllBytes(Paths.get(certfilepath)));
        } catch (Exception e) {
            return "";
        }
    }


    public static PrivateKey loadPrivateKey(Path fileName) throws IOException, GeneralSecurityException {
        PrivateKey key = null;
        InputStream is = null;
        try {
            is = new FileInputStream(fileName.toString());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder builder = new StringBuilder();
            boolean inKey = false;
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                if (!inKey) {
                    if (line.startsWith("-----BEGIN ") && line.endsWith(" PRIVATE KEY-----")) {
                        inKey = true;
                    }
                    continue;
                } else {
                    if (line.startsWith("-----END ") && line.endsWith(" PRIVATE KEY-----")) {
                        inKey = false;
                        break;
                    }
                    builder.append(line);
                }
            }
            //
            byte[] encoded = DatatypeConverter.parseBase64Binary(builder.toString());
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            KeyFactory kf = KeyFactory.getInstance("EC");
            key = kf.generatePrivate(keySpec);
        } finally {
            is.close();
        }
        return key;
    }
}
