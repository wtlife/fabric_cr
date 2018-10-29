package com.wtlife.boot.service;

import com.wtlife.boot.util.GetHash;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@Service
public class FileService {

    /**
     * 文件上传
     */
    public String upload(MultipartFile file) throws NoSuchAlgorithmException {
        if (file.isEmpty()) {
            return "上传失败，请选择文件";
        }

        String fileName = file.getOriginalFilename();
        String filePath = "/home/wutao/temp/";
        File dest = new File(filePath + fileName);
        try {
            file.transferTo(dest);
            String fileHash = GetHash.getHash(dest.getAbsolutePath(), "SHA-1");
            return "上传成功</br>" +
                    "Hash： " + fileHash;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "上传失败！";
    }
}


