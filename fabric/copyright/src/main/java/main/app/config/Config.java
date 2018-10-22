package main.app.config;

import main.app.org.Org;
import main.app.user.RightUser;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;

import java.net.MalformedURLException;
import java.util.HashMap;

public class Config {
    public static String CHAINCODENAME="epointchaincodecommon";
    public static String CHAINCODEVERSION="0.1";
    public static String CHANNLNAME="epointchannel";
    public static HashMap<String,Org> getConfigure() throws MalformedURLException, InvalidArgumentException {
        HashMap<String,Org> orgHashMap=new HashMap<>();
        Org org1=new Org("org1","Org1MSP");
        org1.addPeerLocation("peer0org1","grpc://192.168.188.112:7051");
        org1.addPeerLocation("peer1org1","grpc://192.168.188.113:7051");
        org1.addOrdererLocation("orderer","grpc://192.168.188.111:7050");
        org1.setCALocation("http://192.168.188.110:7054");
        RightUser Adminorg1=new RightUser("peer","Admin","org1MSP");
        RightUser User1org1=new RightUser("peer","User1","org1MSP");
        org1.addUser(Adminorg1);
        org1.addUser(User1org1);
        org1.setAdmin(Adminorg1);


        Org org2=new Org("org2","Org2MSP");
        org2.addPeerLocation("peer0org2","grpc://192.168.188.114:7051");
        org2.addPeerLocation("peer1org2","grpc://192.168.188.115:7051");
        org2.addOrdererLocation("orderer","grpc://192.168.188.111:7050");
        RightUser Adminorg2=new RightUser("peer","Admin","org2MSP");
        RightUser User1org2=new RightUser("peer","User1","org2MSP");
        org1.addUser(Adminorg2);
        org1.addUser(User1org2);
        org1.setAdmin(Adminorg2);

        orgHashMap.put("org1",org1);
        orgHashMap.put("org2",org2);
        return orgHashMap;
    }
}
