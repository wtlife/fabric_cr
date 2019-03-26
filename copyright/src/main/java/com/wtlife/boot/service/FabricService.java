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

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Service
public class FabricService {
    /*
    初始化
     */
    static Channel channel;
    static {
        try {
            channel = FabricClient.client.newChannel(Config.ChannelId);
            Org org = Config.getConfigure().get("org1");
            channel.addPeer(FabricClient.client.newPeer(Config.peer0org1, org.getPeerLocation(Config.peer0org1)));
            channel.addOrderer(FabricClient.client.newOrderer(Config.ordererName, org.getOrdererLocation(Config.ordererName)));
            channel.initialize();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
      Invoke
     */
    //版权登记
    public String registRight(Right right,String policy) {

        /*
        policy
        t,n,peer=,org=,...
         */

        System.out.println(right.toString());

        String cphtext = null;
        try {
            cphtext = Wcpabe.enc(Config.pk_file,
                    policy,
                    right.getIDnumber(),
                    Config.work_dir+right.name+"enc_file");
        } catch (Exception e) {
            e.printStackTrace();
            return "加密阶段异常！"+"<br>"+e.getMessage();
        }
        right.setIDnumber(cphtext);
        try {
            return FabricClient.regist(channel, right);
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
            return "输入有误，请检测输入参数！"+"<br>"+e.getMessage();
        } catch (ProposalException e) {
            e.printStackTrace();
            return "发送proposal异常"+"<br>"+e.getMessage();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "不支持的编码！"+"<br>"+e.getMessage();
        }
    }

    /**
     * Query
     */
    //根据名字查询
    public String queryRightByName(Right right,String username) {

        String json = null;
        try {
            json = FabricClient.query(channel, right);
        } catch (Exception e) {
            return "查询信息出错，请检测输入!"+"<br>"+e.getMessage();
        }
        Right res = JSON.parseObject(json, Right.class);
        String time = DateStamp.getDate(String.valueOf(res.getTimestamp()));
        String IDnumber="";

        String plain= null;
        try {
            plain = Wcpabe.dec(Config.pk_file,
                    Config.work_dir+username+"prv_file",

                    //<--设置属性-->
                    Config.user_press,

                    Config.work_dir+right.getName()+"enc_file");
        } catch (IOException e) {
            return "IO导致的解密阶段异常，请检测是否正确设置属性，或者属性是否匹配";
        }
        if ((plain.equals("0"))||(plain.equals("Given final block not properly padded"))||plain.equals(null)){
            IDnumber="你的属性不满足该部分消息的访问策略！";
        }else {
            IDnumber = plain;
        }

        return  "作品名称:" + res.getName()      + "</br>" +
                "作品作者:" + res.getAuthor()    + "</br>" +
                "登记机构:" + res.getPress()     + "</br>" +
                "登记时间:" + time               + "</br>" +
                "作品哈希:" + res.getHash()      + "</br>" +
                "作品签名:" + res.getSignature() + "</br>" +
                "作者身份:" + IDnumber           + "<br>"  ;
    }

    //    根据交易id查询交易信息
    public String queryTxInfoById(String id) {
//        Channel channel = FabricClient.client.getChannel(Config.ChannelId);
        TransactionInfo txInfo = null;
        try {
            txInfo = channel.queryTransactionByID(id);
        } catch (ProposalException e) {
            e.printStackTrace();
            return "发送Proposal出错"  + e.getMessage();
        } catch (InvalidArgumentException e) {
            return "请输入正确的交易id" + e.getMessage();
        }
        System.out.println("TransactionInfo:\n 交易Id:" + txInfo.getTransactionID()
                + "\n ValidationCode: \n" + txInfo.getValidationCode().getNumber());
        return "TransactionInfo:</br> 交易Id:" + txInfo.getTransactionID()
                + "</br> ValidationCode: </br>" + txInfo.getValidationCode().getNumber();
    }
}
