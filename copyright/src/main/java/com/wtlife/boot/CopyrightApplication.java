package com.wtlife.boot;

import com.wtlife.boot.dao.FabricClient;
import com.wtlife.boot.util.Config;
import nupt.wtlife.wcpabe.Wcpabe;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class CopyrightApplication {
    static {
        try {
//            Wcpabe.setup(Config.pk_file,Config.msk_file);
            FabricClient.init();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(CopyrightApplication.class, args);
    }
}
