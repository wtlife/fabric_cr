package com.wtlife.boot;

import com.wtlife.boot.dao.FabricClient;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.MalformedURLException;

@SpringBootApplication
public class CopyrightApplication {

    public static void main(String[] args) throws InvalidArgumentException, MalformedURLException, CryptoException {
        FabricClient fabricClient = new FabricClient();
        FabricClient.init();
        SpringApplication.run(CopyrightApplication.class, args);
    }
}
