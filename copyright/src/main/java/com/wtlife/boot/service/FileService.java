package com.wtlife.boot.service;

import com.wtlife.boot.domain.Document;
import com.wtlife.boot.util.DSA;
import com.wtlife.boot.util.GetHash;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class FileService {

    /**
     * 文件上传
     */
    public String upload(MultipartFile file, String privateKey) {
        if (file.isEmpty()) {
            return "上传失败，请选择文件";
        }
        String fileName = file.getOriginalFilename();
        String filePath = "/home/wutao/temp/";
        File dest = new File(filePath + fileName);
        String signature = null;
        String fileHash = null;
        try {
            file.transferTo(dest);
            fileHash = GetHash.getHash(dest.getAbsolutePath(), "SHA-1");
            signature = DSA.signature(fileHash, privateKey);
        } catch (Exception e) {
            return "上传失败：</br>" + e.getMessage();
        }
        Document doc = new Document(fileName, filePath, fileHash, signature);
        return "上传成功</br>" +
                "绝对路径:" + dest + "</br>" +
                "文件Hash： " + fileHash + "</br>" +
                "文件签名:" + signature;
    }

    public String fileVerify(String hash, String signature, String publicKey) {
        Boolean bool = false;
        try {
            bool = DSA.verify(hash, signature, publicKey);
        } catch (Exception e) {
            e.getMessage();
        }
        if (bool) {
            return "验证成功";
        } else {
            return "验证失败";
        }
    }
}


