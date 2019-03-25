package nupt.wtlife.common;

import nupt.wtlife.tnwape.TnwAttr;

import java.util.HashMap;
import java.util.Map;

public class Const {
    public String[] type_list = {"peer","org"};
    public TnwAttr[] attrs = new TnwAttr[2];
//    public TnwPolicy policy;
//    public void init(){
//        policy = new TnwPolicy();
//        policy.t=4;
//        policy.n=6;
//
//        policy.kx.put(type_list[0],2);
//        policy.kx.put(type_list[1],1);
//        policy.kx.put(type_list[2],1);
//
//        TnwAttr a2 = new TnwAttr("a2","a",2);
//        TnwAttr b1 = new TnwAttr("b1","b",1);
//        TnwAttr c1 = new TnwAttr("c1","c",1);
//
//        attrs[0]=a2;
//        attrs[1]=b1;
//        attrs[2]=c1;
//    }
    public static Map<String, String> typeMap = new HashMap<String, String>() {
        {
            put("org1", "org");
            put("org2", "org");
            put("peer0", "peer");
            put("peer1", "peer");
        }
    };
    public static Map<String, Integer> weightMap = new HashMap<String, Integer>(){
        {
            put("org1", 1);
            put("org2", 2);
            put("peer0", 1);
            put("peer1", 2);
        }
    };
}
