package com.wtlife.boot.controller;

import com.wtlife.boot.domain.Right;
import com.wtlife.boot.domain.User;
import com.wtlife.boot.service.FabricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@EnableAutoConfiguration
public class FabricController {
    @Autowired
    private FabricService fabricService;

    /**
     * 版权注册
     */

    @RequestMapping(value = "/rightRegist")
    String registRight(Model model,String userid) {
        model.addAttribute("right", new Right());
        model.addAttribute("userid", userid);
        return "right/registRight";
    }

    @RequestMapping(value = "/registRight")
    @ResponseBody
    String registUser(Right right,String policy) {
        return fabricService.registRight(right,policy);
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
    String queryRightByName(Right right,String username) throws Exception {
        String message = fabricService.queryRightByName(right,username);
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
