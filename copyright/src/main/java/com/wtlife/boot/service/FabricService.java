package com.wtlife.boot.service;

import com.alibaba.fastjson.JSON;
import com.wtlife.boot.dao.FabricClient;
import com.wtlife.boot.domain.Org;
import com.wtlife.boot.domain.Right;
import com.wtlife.boot.util.Config;
import com.wtlife.boot.util.DateStamp;
import nupt.wtlife.wcpabe.Wcpabe;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.TransactionInfo;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.stereotype.Service;


@Service
public class FabricService {

    /**
     * Invoke
     */
    //版权登记
    public String registRight(Right right,String policy) throws Exception {
        Channel channel = FabricClient.client.newChannel(Config.ChannelId);
        Org org = Config.getConfigure().get("org1");

        channel.addPeer(FabricClient.client.newPeer(Config.peer0org1, org.getPeerLocation(Config.peer0org1)));
        channel.addOrderer(FabricClient.client.newOrderer(Config.ordererName, org.getOrdererLocation(Config.ordererName)));
        channel.initialize();

        /*
        policy
        a org1  =a1 1, org2  = a2  2
        b peer0 =b1 1, peer0 = b2  2
         */


        String cphtext = Wcpabe.enc(Config.pk_file,
                policy,
                right.getId(),
                Config.work_dir+right.name+"enc_file");
        right.setId(cphtext);
        return FabricClient.regist(channel, right);
    }

    /**
     * Query
     */
    //根据名字查询
    public String queryRightByName(Right right,String username) throws Exception {
        Channel channel = FabricClient.client.getChannel(Config.ChannelId);
        Org org = Config.getConfigure().get("org1");

        String json = FabricClient.query(channel, right);
        Right res = JSON.parseObject(json, Right.class);
        String time = DateStamp.getDate(String.valueOf(res.getTimestamp()));

        String plain= Wcpabe.dec(Config.pk_file,Config.work_dir+username+"prv_file",Config.attr10,Config.work_dir+right.getName()+"enc_file");
        if (plain!="0"){
            res.setId(plain);
        }
        return "名   称:" + res.getName() + "</br>" +
                "作   者:" + res.getAuthor() + "</br>" +
                "登记机构:" + res.getPress() + "</br>" +
                "登记时间:" + time + "</br>" +
                "作品哈希:" + res.getHash() + "</br>" +
                "作品签名:" + res.getSignature() + "</br>"+
                "作者身份:" + res.getId()+"<br>";
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
