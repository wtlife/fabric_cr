package com.wtlife.boot.Fabric;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.TransactionInfo;
import org.junit.Before;
import org.junit.Test;
import com.wtlife.boot.domain.Right;
import com.wtlife.boot.dao.FabricClient;
import com.wtlife.boot.util.Config;


public class FabricTest {
    private static String CONNFIG_Orderer = "grpc://127.0.0.1:7050";
    private static String CONNFIG_Peer0Org1 = "grpc://127.0.0.1:11051";
    private static Right right = new Right("workk3", "wutao", "center", 0, "0xhash", "signature");

    @Before
    public void Setup() throws Exception {
        System.out.println("Fabric Test Init........");
        FabricClient fabricClient = new FabricClient();
        FabricClient.init();
    }

    /**
     * 测试链码插入操作
     */
    @Ignore
    @Test
    public void TestChainCodeInstert() throws Exception {
        System.out.println("测试Fabric插入功能");
        Channel channel = FabricClient.client.newChannel(Config.ChannelId);
        channel.addPeer(FabricClient.client.newPeer("peer0.center.copyright.com", CONNFIG_Peer0Org1));
        channel.addOrderer(FabricClient.client.newOrderer("orderer.copyright.com", CONNFIG_Orderer));
        channel.initialize();
        FabricClient.regist(channel, right);
    }

    /**
     * 测试链码查询操作
     */
    @Test
    public void TestChainCodeQuery() throws Exception {
        System.out.println("测试Fabric查询功能");
        Channel channel = FabricClient.client.newChannel(Config.ChannelId);
        channel.addPeer(FabricClient.client.newPeer("peer0.center.copyright.com", CONNFIG_Peer0Org1));
        channel.addOrderer(FabricClient.client.newOrderer("orderer.copyright.com", CONNFIG_Orderer));
        channel.initialize();
        FabricClient.query(channel, right);
    }

    @Test
    public void TestQueryTxinfo() throws Exception {
        System.out.println("测试查询Fabric交易信息");
        Channel channel = FabricClient.client.newChannel(Config.ChannelId);
        channel.addPeer(FabricClient.client.newPeer("peer0.center.copyright.com", CONNFIG_Peer0Org1));
        channel.addOrderer(FabricClient.client.newOrderer("orderer.copyright.com", CONNFIG_Orderer));
        channel.initialize();
        TransactionInfo txInfo = channel.queryTransactionByID("55352912cc4c6b1c58317c8eb9376c45b9fcdb2c81057aac828e88abbb4215be");
        System.out.println("QueryTransactionByID returned TransactionInfo:\ntxID:" + txInfo.getTransactionID()
                + "\nvalidation code " + txInfo.getValidationCode().getNumber());
    }
}