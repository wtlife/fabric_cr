package com.wtlife.boot.controller;

import com.wtlife.boot.domain.User;
import com.wtlife.boot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@EnableAutoConfiguration
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/")
    String index() {
        return "redirect:/login";
    }

    /**
     * 用户验证
     */
    @RequestMapping("/notVerify")
    @ResponseBody
    String notVerify() {
        return "username or password NOT correct";
    }


    /**
     * 用户注册
     */
    @RequestMapping("/regist")
    String regist(Model model) {
        model.addAttribute("user", new User());
        return "regist";
    }

    @RequestMapping(value = "/registUser", method = RequestMethod.POST)
    @ResponseBody
    String registUser(User user) {
        return userService.registUser(user);
    }

    /**
     * 用户登录
     */
    @RequestMapping("/login")
    String login(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @RequestMapping(value = "/userLogin", method = RequestMethod.POST)
    String userLogin(User user, Model model) {
        boolean verify = userService.verifyUser(user);
        if (verify) {
            model.addAttribute("name", user.getName());
            return "/result";
        } else {
            return "redirect:/notVerify";
        }
    }
}
