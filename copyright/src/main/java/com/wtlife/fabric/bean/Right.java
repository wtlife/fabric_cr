package com.wtlife.fabric.bean;

public class Right {
    public String Name;
    public String Author;
    public String Press;
    public int TimeStamp;
    public String Hash;
    public String Signature;

    public Right(String name, String author, String press, int timeStamp, String hash, String signature) {
        Name = name;
        Author = author;
        Press = press;
        TimeStamp = timeStamp;
        Hash = hash;
        Signature = signature;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getPress() {
        return Press;
    }

    public void setPress(String press) {
        Press = press;
    }

    public int getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        TimeStamp = timeStamp;
    }

    public String getHash() {
        return Hash;
    }

    public void setHash(String hash) {
        Hash = hash;
    }

    public String getSignature() {
        return Signature;
    }

    public void setSignature(String signature) {
        Signature = signature;
    }

    public String[] toStringArray() {
        return new String[]{this.getName(),
                this.getAuthor(),
                this.getPress(),
                String.valueOf(this.getTimeStamp()),
                this.getHash(),
                this.getSignature()};
    }
}
