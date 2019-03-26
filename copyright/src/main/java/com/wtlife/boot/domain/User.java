package com.wtlife.boot.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;


    @Column(nullable = false)
    private String IDnumber;

    @Column(nullable = false)
    private String publicKey;

    @Column(nullable = false)
    private String prv_file;

    public String getPrv_file() {
        return prv_file;
    }

    public void setPrv_file(String prv_file) {
        this.prv_file = prv_file;
    }

    public String  getIDnumber() {
        return IDnumber;
    }

    public void setIDnumber(String  IDnumber) {
        this.IDnumber = IDnumber;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User(String name, String password) {

        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
