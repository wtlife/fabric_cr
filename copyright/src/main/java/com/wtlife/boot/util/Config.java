package com.wtlife.boot.util;

import com.wtlife.boot.domain.Org;
import com.wtlife.boot.domain.RightUser;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;

import java.util.HashMap;

public class Config {
    public static String ChainCodeName = "myrightcc";
    public static String ChainCodeVersion = "2.0";
    public static String ChannelId = "mychannel";


    /*
    属性加密工作目录
    */
    public static String work_dir= "/home/wutao/Append/workspaces/fabric/fabric_cr/copyright/abe_temp/";
    public static String pk_file = work_dir+"pk_file";
    public static String msk_file = work_dir+"msk_file";

    /*
    用户属性
     */
    public static String attr01= "peer0,org1";
    public static String attr11= "peer1,org1";
    public static String attr02= "peer0,org2";
    public static String attr12= "peer1,org2";


    /*
    peer与对于地址端口
    */
    public static String peer0org1= "peer0.center.copyright.com";
    public static String peer1org1= "peer1.center.copyright.com";
    public static String peer0org2= "peer0.press1.copyright.com";
    public static String peer1org2= "peer1.press1.copyright.com";
    public static String ordererName= "orderer.copyright.com";

    public static HashMap<String, Org> getConfigure() {
        HashMap<String, Org> orgHashMap = new HashMap<>();
        Org org1 = new Org("center", "centerMSP");
        org1.addPeerLocation("peer0.center.copyright.com", "grpc://127.0.0.1:11051");
        org1.addPeerLocation("peer1.center.copyright.com", "grpc://127.0.0.1:11053");
        org1.addOrdererLocation("orderer.copyright.com", "grpc://127.0.0.1:7050");
        RightUser Adminorg1 = new RightUser("peer", "Admin", "centerMSP");
        org1.addUser(Adminorg1);
        org1.setAdmin(Adminorg1);


        Org org2 = new Org("press1", "press1MSP");
        org2.addPeerLocation("peer0.center.copyright.com", "grpc://127.0.0.1:14052");
        org2.addPeerLocation("peer1.center.copyright.com", "grpc://127.0.0.1:14054");
        org2.addOrdererLocation("orderer.copyright.com", "grpc://127.0.0.1:7050");
        RightUser Adminorg2 = new RightUser("peer", "Admin", "press1MSP");
        RightUser User1org2 = new RightUser("peer", "User1", "press1MSP");
        org2.addUser(Adminorg2);
        org2.addUser(User1org2);
        org2.setAdmin(Adminorg2);

        orgHashMap.put("org1", org1);
        orgHashMap.put("org2", org2);
        return orgHashMap;
    }
}
