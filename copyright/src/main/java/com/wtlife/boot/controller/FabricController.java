package com.wtlife.boot.controller;

import com.wtlife.boot.dao.UserDao;
import com.wtlife.boot.domain.Right;
import com.wtlife.boot.domain.User;
import com.wtlife.boot.service.FabricService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
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
    String registRight(Model model,String userid,String username) {
        model.addAttribute("right", new Right());
        model.addAttribute("username",username);
        model.addAttribute("userid",userid);
        return "right/registRight";
    }

    @RequestMapping(value = "/registRight")
    @ResponseBody
    String registUser(Right right,String policy,String Id,String username) {
//        UserDao userDao = null;
//        String id = userDao.findUser(username).getIDnumber();
        right.IDnumber=Id;
        return fabricService.registRight(right,policy);
    }

    /**
     * 版权查询
     */
    @RequestMapping(value = "/rightQuery")
    String queryRight(Model model,String username) {
        model.addAttribute("right", new Right());
        model.addAttribute("username",username);
        return "right/queryRight";
    }

    @RequestMapping(value = "/queryRightByName")
    @ResponseBody
    String queryRightByName(Right right,String username) {
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
