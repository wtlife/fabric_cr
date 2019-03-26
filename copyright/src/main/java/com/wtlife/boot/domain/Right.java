package com.wtlife.boot.domain;

public class Right {

    public  String name;
    private String author;
    private String press;
    private int    timestamp;
    private String hash;
    private String signature;
    public String IDnumber;


    public Right(String name, String author, String press, int timeStamp, String hash, String signature, String IDnumber) {
        this.name = name;
        this.author = author;
        this.press = press;
        this.timestamp = timeStamp;
        this.hash = hash;
        this.signature = signature;
        this.IDnumber = IDnumber;
    }

    public Right() {
    }

    public String getIDnumber() {
        return IDnumber;
    }

    public void setIDnumber(String IDnumber) {
        this.IDnumber = IDnumber;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String[] toStringArray() {
        return new String[]{this.getName(),
                this.getAuthor(),
                this.getPress(),
                String.valueOf(this.getTimestamp()),
                this.getHash(),
                this.getSignature(),
                this.getIDnumber()};
    }

    @Override
    public String toString() {
        return "Right [name=" + name + ", author=" + author + ",press=" + press + ",timestamp=" + timestamp + ",hash=" + hash + ",signature=" + signature + ",IDnumber=" + IDnumber + "]";
    }
}
