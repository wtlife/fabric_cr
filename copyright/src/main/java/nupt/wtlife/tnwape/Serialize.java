package nupt.wtlife.tnwape;

import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.DefaultCurveParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import nupt.wtlife.common.Utils;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class Serialize {

    private static String curveParams = "type a\n"
            + "q 87807107996633125224377819847540498158068831994142082"
            + "1102865339926647563088022295707862517942266222142315585"
            + "8769582317459277713367317481324925129998224791\n"
            + "h 12016012264891146079388821366740534204802954401251311"
            + "822919615131047207289359704531102844802183906537786776\n"
            + "r 730750818665451621361119245571504901405976559617\n"
            + "exp2 159\n" + "exp1 107\n" + "sign1 1\n" + "sign0 1\n";

    /* Method has been test okay */
    public static void serializeElement(ArrayList<Byte> arrlist, Element e) {
        byte[] arr_e = e.toBytes();
        serializeUint32(arrlist, arr_e.length);
        byteArrListAppend(arrlist, arr_e);
    }

    /* Method has been test okay */
    public static int unserializeElement(byte[] arr, int offset, Element e) {
        int len;
        int i;
        byte[] e_byte;

        len = unserializeUint32(arr, offset);
        e_byte = new byte[(int) len];
        offset += 4;
        for (i = 0; i < len; i++)
            e_byte[i] = arr[offset + i];
        e.setFromBytes(e_byte);

        return (int) (offset + len);
    }

    public static void serializeString(ArrayList<Byte> arrlist, String s) {
        byte[] b = s.getBytes();
        serializeUint32(arrlist, b.length);
        byteArrListAppend(arrlist, b);
    }

    /*
     * Usage:
     * StringBuffer sb = new StringBuffer("");
     * offset = unserializeString(arr, offset, sb);
     * String str = sb.substring(0);
     */
    public static int unserializeString(byte[] arr, int offset, StringBuffer sb) {
        int i;
        int len;
        byte[] str_byte;

        len = unserializeUint32(arr, offset);
        offset += 4;
        str_byte = new byte[len];
        for (i = 0; i < len; i++)
            str_byte[i] = arr[offset + i];

        sb.append(new String(str_byte));
        return offset + len;
    }

    public static byte[] serializeTnwPub(TnwPub pk) {
        ArrayList<Byte> arrlist = new ArrayList<Byte>();

		serializeString(arrlist, pk.pairingDesc);
        serializeElement(arrlist, pk.g);
        serializeElement(arrlist, pk.h);
        serializeElement(arrlist, pk.gp);
        serializeElement(arrlist, pk.g_hat_alpha);

        return Byte_arr2byte_arr(arrlist);
    }

    public static TnwPub unserializeTnwPub(byte[] b) {
        TnwPub pk;
        int offset;

        pk= new TnwPub();
        offset = 0;

        StringBuffer sb = new StringBuffer("");
        offset = unserializeString(b, offset, sb);

        CurveParameters params = new DefaultCurveParameters()
                .load(new ByteArrayInputStream(curveParams.getBytes()));
        pk.pairingDesc = curveParams;
        pk.p = PairingFactory.getPairing(params);

        Pairing pairing = pk.p;

        pk.g = pairing.getG1().newElement();
        pk.h = pairing.getG1().newElement();
        pk.gp = pairing.getG2().newElement();
        pk.g_hat_alpha = pairing.getGT().newElement();

        offset = unserializeElement(b, offset, pk.g);
        offset = unserializeElement(b, offset, pk.h);
        offset = unserializeElement(b, offset, pk.gp);
        offset = unserializeElement(b, offset, pk.g_hat_alpha);

        return pk;
    }

    /* Method has been test okay */
    public static byte[] serializeTnwmsk(TnwMsk msk) {
        ArrayList<Byte> arrlist = new ArrayList<Byte>();

        serializeElement(arrlist, msk.beta);
        serializeElement(arrlist, msk.g_alpha);

        return Byte_arr2byte_arr(arrlist);
    }

    /* Method has been test okay */
    public static TnwMsk unserializeTnwMsk(TnwPub pk, byte[] b) {
        int offset = 0;
        TnwMsk msk = new TnwMsk();

        msk.beta = pk.p.getZr().newElement();
        msk.g_alpha = pk.p.getG2().newElement();

        offset = unserializeElement(b, offset, msk.beta);
        offset = unserializeElement(b, offset, msk.g_alpha);

        return msk;
    }

    public static byte[] serializeTnwIsk(MTnwISk iSk) {
        int dixLen;
        ArrayList<Byte> arrayList = new ArrayList<Byte>();
        dixLen=iSk.Dix.length;

        serializeElement(arrayList,iSk.g_kid);
        serializeElement(arrayList,iSk.r);

        for (int i = 0; i <dixLen ; i++) {
            serializeElement(arrayList,iSk.Dix[i]);
            serializeElement(arrayList,iSk.Diy[i]);
        }

        return Byte_arr2byte_arr(arrayList);
    }

    public static MTnwISk unserializeTnwIsk(TnwPub pk,byte[] b,int weight) {
        MTnwISk iSk=new MTnwISk();
        int offset = 0;

        iSk.g_kid=pk.p.getG2().newElement();
        iSk.r=pk.p.getZr().newElement();
        offset=unserializeElement(b,offset,iSk.g_kid);
        offset=unserializeElement(b,offset,iSk.r);

        iSk.Dix=new Element[weight];
        iSk.Diy=new Element[weight];
        for (int i = 0; i < weight ; i++) {
            Element _dix=pk.p.getG2().newElement();
            Element _diy=pk.p.getG1().newElement();

            offset=unserializeElement(b,offset, _dix);
            offset=unserializeElement(b,offset, _diy);

            iSk.Dix[i]=_dix;
            iSk.Diy[i]=_diy;
        }

        return iSk;
    }


    public static byte[] serializeTnwSk(TnwSk key) {
        int len =key.Dx.length;
        ArrayList<Byte> arrlist= new ArrayList<Byte>();

        for (int i = 0; i <len ; i++) {
            serializeElement(arrlist,key.Dx[i]);
            serializeElement(arrlist,key.Dy[i]);
        }

        serializeElement(arrlist,key.L);
        serializeElement(arrlist,key.K);

        return Byte_arr2byte_arr(arrlist);
    }

    public static TnwSk unserializeTnwSk(TnwPub pk,byte[] b,String attr_str){
        TnwSk key = new TnwSk();
        int offset = 0;

        TnwAttr[] attrs = Utils.parseAttr(attr_str) ;
        int weight = MTnwabe.getTotalWeight(attrs);
        key.Dx=new Element[weight];
        key.Dy=new Element[weight];

        for (int i = 0; i <weight ; i++) {
            Element _dx=pk.p.getG1().newElement();
            Element _dy=pk.p.getG2().newElement();

            offset = unserializeElement(b,offset,_dx);
            offset = unserializeElement(b,offset,_dy);

            key.Dx[i]=_dx;
            key.Dy[i]=_dy;
        }

        Element L = pk.p.getG2().newElement();
        Element K = pk.p.getG2().newElement();

        offset=unserializeElement(b,offset,L);
        offset=unserializeElement(b,offset,K);

        key.L=L;
        key.K=K;
        key.attrs=attrs;

        return key;
    }

    public static byte[] serializeCphtext(TnwCiphertext cphText,String policy_str) {
        ArrayList<Byte> arrlist=new ArrayList<Byte>();

        serializeString(arrlist,policy_str);

        for (int i = 0; i <cphText.policy.Cx.size(); i++) {
            serializeElement(arrlist,cphText.policy.Cx.get(i));
            serializeElement(arrlist,cphText.policy.Cy.get(i));
        }

        serializeElement(arrlist,cphText.C);
        serializeElement(arrlist,cphText.C1);

        return Byte_arr2byte_arr(arrlist);
    }

    public static TnwCiphertext unserializeCphtext(TnwPub pk,byte[] b){
        TnwCiphertext ciphertext = new TnwCiphertext();
        Element cxi,cyi;
        int offset=0;

        StringBuffer sb = new StringBuffer("");
        offset=unserializeString(b,offset,sb);
        String policy_str=sb.substring(0);

//        System.out.println(policy_str);
        ciphertext.policy= Utils.parsePolicy(policy_str);


        for (int i = 0; i <ciphertext.policy.t ; i++) {
            cxi=pk.p.getG2().newElement();
            cyi=pk.g.duplicate();

            offset=unserializeElement(b,offset,cxi);
            offset=unserializeElement(b,offset,cyi);

            ciphertext.policy.Cx.add(cxi);
            ciphertext.policy.Cy.add(cyi);
        }

        ciphertext.C=pk.p.getGT().newElement();
        ciphertext.C1=pk.p.getG1().newElement();
        offset=unserializeElement(b,offset,ciphertext.C);
        offset=unserializeElement(b,offset,ciphertext.C1);
        return ciphertext;
    }


    private static int byte2int(byte b) {
        if (b >= 0)
            return b;
        return (256 + b);
    }

    private static void byteArrListAppend(ArrayList<Byte> arrlist, byte[] b) {
        int len = b.length;
        for (int i = 0; i < len; i++)
            arrlist.add(Byte.valueOf(b[i]));
    }

    private static byte[] Byte_arr2byte_arr(ArrayList<Byte> B) {
        int len = B.size();
        byte[] b = new byte[len];

        for (int i = 0; i < len; i++)
            b[i] = B.get(i).byteValue();

        return b;
    }

        /* Method has been test okay */
    /* potential problem: the number to be serialize is less than 2^31 */
    private static void serializeUint32(ArrayList<Byte> arrlist, int k) {
        int i;
        byte b;

        for (i = 3; i >= 0; i--) {
            b = (byte) ((k & (0x000000ff << (i * 8))) >> (i * 8));
            arrlist.add(Byte.valueOf(b));
        }
    }

    /*
     * Usage:
     *
     * You have to do offset+=4 after call this method
     */
    /* Method has been test okay */
    private static int unserializeUint32(byte[] arr, int offset) {
        int i;
        int r = 0;

        for (i = 3; i >= 0; i--)
            r |= (byte2int(arr[offset++])) << (i * 8);
        return r;
    }

}
