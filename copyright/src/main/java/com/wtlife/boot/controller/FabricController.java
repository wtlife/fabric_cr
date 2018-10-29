package com.wtlife.boot.controller;

import com.wtlife.boot.domain.Right;
import com.wtlife.boot.service.FabricService;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

@Controller
@EnableAutoConfiguration
public class FabricController {
    @Autowired
    private FabricService fabricService;

    /**
     * 版权注册
     */

    @RequestMapping(value = "/rightRegist")
    String registRight(Model model) {
        model.addAttribute("right", new Right());
        return "right/registRight";
    }

    @RequestMapping(value = "/registRight")
    @ResponseBody
    String registUser(Right right) throws InvalidArgumentException, TransactionException, ProposalException, MalformedURLException, UnsupportedEncodingException {
        return fabricService.registRight(right);
    }

    /**
     * 版权查询
     */
    @RequestMapping(value = "/rightQuery")
    String queryRight(Model model) {
        model.addAttribute("right", new Right());
        return "right/queryRight";
    }

    @RequestMapping(value = "/queryRightByName")
    @ResponseBody
    String queryRightByName(Right right) throws Exception {
        String message = fabricService.queryRightByName(right);
        return message;
    }

    /**
     * 交易查询
     */
    @RequestMapping(value = "/TxQuery")
    String queryTx() {
        return "right/queryTx";
    }

    @RequestMapping(value = "/queryTxInfoById", method = RequestMethod.POST)
    @ResponseBody
    String queryTxInfoById(String Id) {
        if (Id.length() != 64) {
            return "TxId格式出错";
        }
        String message = fabricService.queryTxInfoById(Id);
        return message;
    }

}
