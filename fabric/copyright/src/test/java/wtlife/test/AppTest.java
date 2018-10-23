package wtlife.test;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.log4j.Logger;
import org.hyperledger.fabric.sdk.Channel;
import org.junit.Before;
import org.junit.Test;
import wtlife.app.bean.Right;
import wtlife.app.client.FabricClient;
import wtlife.app.config.Config;

/**
 * Unit test for simple App.
 */
public class AppTest {
    private static Logger logger = Logger.getLogger(AppTest.class);
    private static String CONNFIG_Orderer = "grpc://127.0.0.1:7050";
    private static String CONNFIG_Peer0Org1 = "grpc://127.0.0.1:11051";
    private static String CHANNELID = "mychannel";
    private static Right right = new Right("work1", "wutao", "center", 0, "0xhash", "sigsigsig");

    @Before
    public void Setup() throws Exception {
        logger.debug("Fabric Test Init........");
        FabricClient fabricClient = new FabricClient();
        FabricClient.init();
    }

    /**
     * 测试链码插入操作
     */
    @Ignore
    @Test
    public void TestChainCodeInstert() throws Exception {
        logger.debug("测试Fabric插入功能");
        Channel channel = FabricClient.client.newChannel(CHANNELID);
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
        logger.info("测试Fabric 查询功能");
        Channel channel = FabricClient.client.newChannel(Config.CHANNELNAME);
        channel.addPeer(FabricClient.client.newPeer("peer0.center.copyright.com", CONNFIG_Peer0Org1));
        channel.addOrderer(FabricClient.client.newOrderer("orderer.copyright.com", CONNFIG_Orderer));
        channel.initialize();
        FabricClient.query(channel, right);
    }

    /**
     *
     */
    @Ignore
    @Test
    public void TestChainCodeRegist() throws Exception {
        logger.debug("测试Fabric 循环插入1000个值测试监控值是否包含变化");
        Channel channel = FabricClient.client.newChannel(CHANNELID);
        channel.addPeer(FabricClient.client.newPeer("peer0.center.copyright.com", CONNFIG_Peer0Org1));
        channel.addOrderer(FabricClient.client.newOrderer("orderer.copyright.com", CONNFIG_Orderer));
        channel.initialize();
        for (int i = 0; i < 1000; i++) {
            Right right = new Right("work", "wutao", "", i, "0xhash", "sigsig");
            FabricClient.regist(channel, right);
        }
        logger.debug("测试完成");
    }
}
