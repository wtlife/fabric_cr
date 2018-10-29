package com.wtlife.boot.controller;

import com.wtlife.boot.domain.Right;
import com.wtlife.boot.domain.User;
import com.wtlife.boot.service.FabricService;
import com.wtlife.boot.service.UserService;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;


@Controller
@EnableAutoConfiguration
public class UserController {

    /**
     * login & regist
     */
    @Autowired
    private UserService userService;

    @GetMapping(value = "/")
    String index(){
        return "redirect:/login";
    }

    @RequestMapping("/notVerify")
    @ResponseBody
    String notVerify() {
        return "username or password NOT correct";
    }

    @RequestMapping("/login")
    String login(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @RequestMapping("/regist")
    String regist(Model model) {
        model.addAttribute("user", new User());
        return "regist";
    }

    @RequestMapping(value = "/registUser", method = RequestMethod.POST)
    @ResponseBody
    String registUser(User user, Model model) {
        return userService.registUser(user);
    }

    @RequestMapping(value = "/userLogin", method = RequestMethod.POST)
    String userLogin(User user, Model model) {
        boolean verify = userService.verifyUser(user);
        if (verify) {
            model.addAttribute("name", user.getName());
            return "result";
        } else {
            return "redirect:/notVerify";
        }
    }

    /**
     * Fabric
     */

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
    @RequestMapping(value = "rightQuery")
    String queryRight(Model model) {
        model.addAttribute("right",new Right());
        return "right/queryRight";
    }
    @RequestMapping(value = "queryRightByName")
    @ResponseBody
    String queryRightByName(Right right) throws Exception {
        return fabricService.queryRightByName(right);
    }

    /**
     * 交易查询
     */
    @RequestMapping(value = "TxQuery")
    String queryTx(){
        return "right/queryTx";
    }
    @RequestMapping(value = "queryTxinfoById")
    @ResponseBody
    String queryTxinfoById(String id) throws ProposalException, InvalidArgumentException {
        return fabricService.queryTxinfoById(id);
    }
}
