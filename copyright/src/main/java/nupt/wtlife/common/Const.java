package nupt.wtlife.common;

import nupt.wtlife.tnwape.TnwAttr;

import java.util.HashMap;
import java.util.Map;

public class Const {
    public String[] type_list = {"peer","org"};
    public TnwAttr[] attrs = new TnwAttr[2];

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
            put("org1", 2);
            put("org2", 1);
            put("peer0", 2);
            put("peer1", 1);
        }
    };
}
