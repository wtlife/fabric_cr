package com.wtlife.boot.service;

import com.wtlife.boot.dao.FabricClient;
import com.wtlife.boot.domain.Org;
import com.wtlife.boot.domain.Right;
import com.wtlife.boot.util.Config;
import org.apache.naming.factory.FactoryBase;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

@Service
public class FabricService {

    /**
     * Invoke
     */
    //版权登记
    public String registRight(Right right) throws TransactionException, InvalidArgumentException, UnsupportedEncodingException, ProposalException, MalformedURLException {
        Channel channel = FabricClient.client.newChannel(Config.ChannelId);
        Org org = Config.getConfigure().get("org1");
        String peerName = "peer0.center.copyright.com";
        String ordererName = "orderer.copyright.com";

        channel.addPeer(FabricClient.client.newPeer(peerName, org.getPeerLocation(peerName)));
        channel.addOrderer(FabricClient.client.newOrderer(ordererName, org.getOrdererLocation(ordererName)));
        channel.initialize();
        return FabricClient.regist(channel, right);
    }

    /**
     * Query
     */
    //根据名字查询
    public String queryRightByName(Right right) throws Exception {
        Channel channel = FabricClient.client.newChannel(Config.ChannelId);
        Org org = Config.getConfigure().get("org1");
        String peerName = "peer0.center.copyright.com";
        String ordererName = "orderer.copyright.com";

        channel.addPeer(FabricClient.client.newPeer(peerName, org.getPeerLocation(peerName)));
        channel.addOrderer(FabricClient.client.newOrderer(ordererName, org.getOrdererLocation(ordererName)));
        channel.initialize();

        return FabricClient.query(channel, right);
    }
}
