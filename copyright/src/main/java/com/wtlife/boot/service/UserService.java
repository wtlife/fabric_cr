package com.wtlife.boot.service;

import com.wtlife.boot.dao.UserDao;
import com.wtlife.boot.domain.User;
import com.wtlife.boot.util.DSA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public String registUser(User user) {

        if (userDao.findByName(user.getName()).isEmpty()) {
            String[] keyPair = null;
            try {
                keyPair = DSA.getKeyPair().split("<split>");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            String privateKey = keyPair[0];
            String pubKey = keyPair[1];
            user.setPublicKey(pubKey);
            userDao.save(user);
            return "用户名" + user.getName() + "注册成功！</br>"
                    + "你的publicKey:" + user.getPublicKey() + "</br>"
                    + "你的privateKey:" + privateKey + "</br>"
                    + "请妥善保管!无备份!";

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
