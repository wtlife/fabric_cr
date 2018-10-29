package com.wtlife.boot.controller;

import com.wtlife.boot.domain.Document;
import com.wtlife.boot.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.security.NoSuchAlgorithmException;

@Controller
@EnableAutoConfiguration
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 文件上传
     */
    @GetMapping("/upload")
    public String upload(Model model) {
        model.addAttribute("document", new Document());
        return "/file/upload";
    }

    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file,String privateKey) {
        String message = fileService.upload(file,privateKey);
        return message;
    }
}
