package nupt.wtlife.wcpabe;

import it.unisa.dia.gas.jpbc.Element;
import nupt.wtlife.common.AES;
import nupt.wtlife.tnwape.*;
import nupt.wtlife.common.Utils;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Wcpabe {

    public static void setup(String pk_str,String msk_str) throws IOException {
        byte[] pk_byte,msk_byte;
        TnwPub pk = new TnwPub();
        TnwMsk msk = new TnwMsk();
        Tnwabe.setup(pk,msk);

        pk_byte = Serialize.serializeTnwPub(pk);
        Utils.writeFile(pk_str,pk_byte);

        msk_byte = Serialize.serializeTnwmsk(msk);
        Utils.writeFile(msk_str,msk_byte);


//        DEBUG
//        System.out.println("pk=="+pk.g_hat_alpha.toString());
    }

    public static void inkeyGen(String pk_str,String attr_str, String inkey_str) throws IOException, NoSuchAlgorithmException {
        byte[] inkey_byte,pk_byte;
        TnwPub pk ;

        pk_byte = Utils.readFile(pk_str);
        pk=Serialize.unserializeTnwPub(pk_byte);

        TnwAttr[] tnwAttrs = Utils.parseAttr(attr_str);
        MTnwISk iSk = MTnwabe.inkeyGen(tnwAttrs,pk);

        inkey_byte = Serialize.serializeTnwIsk(iSk);
        Utils.writeFile(inkey_str,inkey_byte);

//       DEBUG
//        System.out.println("pk=="+pk.g_hat_alpha.toString());
//        System.out.println("isk=="+iSk.Diy[0].toString());


    }

    public static void keyGen(String inkey_str,String pk_str,String msk_str,String key_str,String attr_str) throws IOException, NoSuchAlgorithmException {
        byte[] inkey_byte,pk_byte,msk_byte,key_byte;
        TnwPub pk;
        TnwMsk msk;
        MTnwISk isk;
        TnwSk key;

        pk_byte=Utils.readFile(pk_str);
        pk=Serialize.unserializeTnwPub(pk_byte);

        msk_byte= Utils.readFile(msk_str);
        msk=Serialize.unserializeTnwMsk(pk,msk_byte);

        TnwAttr[] tnwAttrs = Utils.parseAttr(attr_str);
        int weight=MTnwabe.getTotalWeight(tnwAttrs);

        inkey_byte=Utils.readFile(inkey_str);
        isk=Serialize.unserializeTnwIsk(pk,inkey_byte,weight);
        isk.attrs=tnwAttrs;


        key=MTnwabe.keyGen(pk,msk,isk);
        key_byte=Serialize.serializeTnwSk(key);
        Utils.writeFile(key_str,key_byte);

//        DEBUG
//        System.out.println("key.K=="+key.K.toString());
//        System.out.println("key.L=="+key.L.toString());
//
//        System.out.println("key.DX[0]=="+key.Dx[0].toString());
//        System.out.println("key.DX[1]=="+key.Dx[1].toString());
//        System.out.println("key.DY[0]=="+key.Dy[0].toString());
//        System.out.println("key.DY[1]=="+key.Dy[1].toString());
//        System.out.println("key.ATTR=="+key.attrs[1].name);
    }

    public static String enc(String pk_str,String policy_str,String message,String enc_file) throws Exception {
        TnwPub pk;
        TnwCiphertext cphText;
        TnwCphKey cphKey;
        TnwPolicy policy;
        byte[] message_byte;
        byte[] cph_byte;
        byte[] aes_byte;
        byte[] pk_byte;
        Element m;

        pk_byte=Utils.readFile(pk_str);
        pk = Serialize.unserializeTnwPub(pk_byte);

        policy=Utils.parsePolicy(policy_str);

        cphKey=MTnwabe.enc(pk,policy);
        cphText=cphKey.cph;
        m = cphKey.key;

        cph_byte=Serialize.serializeCphtext(cphText,policy_str);

        message_byte = message.getBytes();
        aes_byte= AES.encrypt(m.toBytes(),message_byte);

        Utils.writeEncfile(enc_file,cph_byte,aes_byte);

//        DEBUG
//        System.out.println("C1=="+cphText.C1);
//        System.out.println("PK==="+pk.g_hat_alpha.toString());
//        System.out.println("AES=="+Base64.getEncoder().encodeToString(aes_byte));
//        System.out.println("CPH=="+Base64.getEncoder().encodeToString(cph_byte));
//        System.out.println("m="+m.toString());

        return new String(Base64.getEncoder().encodeToString(aes_byte));
    }

    public static String dec(String pk_str,String key_str,String attr_str,String enc) throws Exception {
        TnwPub pk;
        TnwSk sk;
        TnwCiphertext ciphertext;

        byte[] pk_byte;
        byte[] sk_byte;
        byte[] aes_byte,cph_byte;
        byte[][] tmp;
        byte[] mess_byte;

        pk_byte= Utils.readFile(pk_str);
        pk=Serialize.unserializeTnwPub(pk_byte);

        sk_byte=Utils.readFile(key_str);
        sk=Serialize.unserializeTnwSk(pk,sk_byte,attr_str);

        tmp=Utils.readEncfile(enc);
        aes_byte=tmp[0];
        cph_byte=tmp[1];
        ciphertext=Serialize.unserializeCphtext(pk,cph_byte);

        TnwElementBoolean tnwElementBoolean = MTnwabe.dec(ciphertext,sk,pk);

//        DEBUG
//        System.out.println("key.K=="+sk.K.toString());
//        System.out.println("key.L=="+sk.L.toString());
//        System.out.println("key.DX[0]=="+sk.Dx[0].toString());
//        System.out.println("key.DX[1]=="+sk.Dx[1].toString());
//        System.out.println("key.DY[0]=="+sk.Dy[0].toString());
//        System.out.println("key.DY[1]=="+sk.Dy[1].toString());
//        System.out.println("key.ATTR=="+sk.attrs[1].name);
//        System.out.println("C1=="+ciphertext.C1);
//        System.out.println("PK==="+pk.g_hat_alpha.toString());
//        System.out.println("AES=="+ Base64.getEncoder().encodeToString(aes_byte));
//        System.out.println("CPH=="+ Base64.getEncoder().encodeToString(cph_byte));
//        System.out.println("m="+tnwElementBoolean.e.toString());

        if (!(tnwElementBoolean.b)){
            return "0";
        }else {
            mess_byte=AES.decrypt(tnwElementBoolean.e.toBytes(),aes_byte);
            return new String(mess_byte);
        }
    }
}
