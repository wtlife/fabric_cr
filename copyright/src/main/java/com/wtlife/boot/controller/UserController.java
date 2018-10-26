package com.wtlife.boot.controller;

import com.wtlife.boot.domain.User;
import com.wtlife.boot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@EnableAutoConfiguration
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    @ResponseBody
    String welcome() {
        return "Welcome my first spring boot project!";
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
    String regist(Model model, HttpServletRequest req) {
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
     * 文件上传
     */
    @RequestMapping(value = "/fileUpload")
    String fileUpload() {
        return "file";
    }
}
