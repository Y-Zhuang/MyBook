package com.wyz.example.mybook.model;

import com.wyz.example.mybook.annotation.SqlConstraint;

public class user {

    @SqlConstraint(isIdentity = true)
    private int id;
    private String name;
    private String pwd;
    private String role;
    private byte[] picture;

    public user(){

    }

    public user(int id, String name, String pwd, String role, byte[] picture){
        this.id = id;
        this.name = name;
        this.pwd = pwd;
        this.role = role;
        this.picture = picture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

}
