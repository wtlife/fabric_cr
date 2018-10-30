package com.wtlife.boot.service;

import com.alibaba.fastjson.JSON;
import com.wtlife.boot.dao.FabricClient;
import com.wtlife.boot.domain.Org;
import com.wtlife.boot.domain.Right;
import com.wtlife.boot.util.Config;
import com.wtlife.boot.util.DateStamp;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.TransactionInfo;
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
        Channel channel = FabricClient.client.getChannel(Config.ChannelId);
        Org org = Config.getConfigure().get("org1");
        String peerName = "peer0.center.copyright.com";
        String ordererName = "orderer.copyright.com";

        String json = FabricClient.query(channel, right);
        Right res = JSON.parseObject(json, Right.class);
        String time = DateStamp.getDate(String.valueOf(res.getTimestamp()));
        return "名   称:" + res.getName() + "</br>" +
                "作   者:" + res.getAuthor() + "</br>" +
                "登记机构:" + res.getPress() + "</br>" +
                "登记时间:" + time + "</br>" +
                "作品哈希:" + res.getHash() + "</br>" +
                "作品签名:" + res.getSignature() + "</br>";
    }

    //    根据交易id查询交易信息
    public String queryTxInfoById(String id) {
        Channel channel = FabricClient.client.getChannel(Config.ChannelId);
        TransactionInfo txInfo = null;
        try {
            txInfo = channel.queryTransactionByID(id);
        } catch (ProposalException e) {
            e.printStackTrace();
            return "Proposal出错" + e.getMessage();
        } catch (InvalidArgumentException e) {
            return "请输入正确的id" + e.getMessage();
        }
        System.out.println("TransactionInfo:\n 交易Id:" + txInfo.getTransactionID()
                + "\n ValidationCode: \n" + txInfo.getValidationCode().getNumber());
        return "TransactionInfo:</br> 交易Id:" + txInfo.getTransactionID()
                + "</br> ValidationCode: </br>" + txInfo.getValidationCode().getNumber();
    }
}
