package com.wtlife.boot.service;

import com.wtlife.boot.dao.UserDao;
import com.wtlife.boot.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public String registUser(User user) {

        if (userDao.findByName(user.getName()).isEmpty()) {
            userDao.save(user);
            return "用户名  " + user.getName() + " 注册成功";

        } else {
            return "用户名 " + user.getName() + "已被占用！";

        }
    }

    public boolean verifyUser(User user) {
        if (userDao.findByNameAndPassword(user.getName(), user.getPassword()).isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
}
