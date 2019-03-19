package nupt.wtlife.common;

import nupt.wtlife.tnwape.TnwAttr;
import nupt.wtlife.tnwape.TnwPolicy;

import java.io.*;

public class Utils {
    /* read byte[] from inputfile */
    public static byte[] readFile(String inputfile) throws IOException {
        InputStream is = new FileInputStream(inputfile);
        int size = is.available();
        byte[] content = new byte[size];

        is.read(content);

        is.close();
        return content;
    }

    /* write byte[] into outputfile */
    public static void writeFile(String outputfile, byte[] b) throws IOException {
        OutputStream os = new FileOutputStream(outputfile);
        os.write(b);
        os.close();
    }

    public static void writeEncfile(String encfile, byte[] cphBuf, byte[] aesBuf) throws IOException {
        int i;
        OutputStream os = new FileOutputStream(encfile);

        /* write aes_buf */
        for (i = 3; i >= 0; i--)
            os.write(((aesBuf.length & (0xff << 8 * i)) >> 8 * i));
        os.write(aesBuf);

        /* write cph_buf */
        for (i = 3; i >= 0; i--)
            os.write(((cphBuf.length & (0xff << 8 * i)) >> 8 * i));
        os.write(cphBuf);

        os.close();

    }

    public static byte[][] readEncfile(String encfile) throws IOException {
        int i, len;
        InputStream is = new FileInputStream(encfile);
        byte[][] res = new byte[2][];
        byte[] aesBuf, cphBuf;

        /* read aes buf */
        len = 0;
        for (i = 3; i >= 0; i--)
            len |= is.read() << (i * 8);
        aesBuf = new byte[len];

        is.read(aesBuf);

        /* read cph buf */
        len = 0;
        for (i = 3; i >= 0; i--)
            len |= is.read() << (i * 8);
        cphBuf = new byte[len];

        is.read(cphBuf);

        is.close();

        res[0] = aesBuf;
        res[1] = cphBuf;
        return res;
    }

    public static TnwAttr[] parseAttr(String attr_str) {
        /*a1,b1,...*/
        TnwAttr[] tnwAttrs=new TnwAttr[2];
        String[] attrArray = attr_str.split(",");
        String identity = attrArray[0];
        String organization = attrArray[1];

        for (int i = 0; i <tnwAttrs.length ; i++) {
            tnwAttrs[i]=new TnwAttr(attrArray[i],
                    Const.typeMap.get(attrArray[i]),
                    Const.weightMap.get(attrArray[i]));
//            System.out.println(tnwAttrs[i].name+"***"+tnwAttrs[i].type+"***"+tnwAttrs[i].weight);
        }
        return tnwAttrs;
    }

    public static TnwPolicy parsePolicy(String policy_str){
        /* t,n,a=,b=,...*/
        TnwPolicy policy = new TnwPolicy();
        String[] policy_arr = policy_str.split(",");
        policy.t=Integer.valueOf(policy_arr[0]);
        policy.n=Integer.valueOf(policy_arr[1]);

        for (int i = 2; i <policy_arr.length ; i++) {
            String[] threshold =policy_arr[i].split("=");
            String type = threshold[0];
            String value = threshold[1];

            policy.kx.put(type,Integer.valueOf(value));
//            System.out.println(type + policy.kx.get(type));
        }
        return policy;
    }
}
