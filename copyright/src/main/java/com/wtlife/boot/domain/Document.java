package com.wtlife.boot.domain;

public class Document {
    private String name;
    private String hash;
    private String path;
    private String signature;
    private String owner;

    public Document() {
    }

    public Document(String fileName, String filePath, String fileHash, String signature) {
        this.name = fileName;
        this.path = filePath;
        this.hash = fileHash;
        this.signature = signature;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
