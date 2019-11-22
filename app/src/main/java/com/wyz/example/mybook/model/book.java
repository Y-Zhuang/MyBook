package com.wyz.example.mybook.model;


import com.wyz.example.mybook.annotation.SqlConstraint;

public class book {

    @SqlConstraint(isIdentity = true)
    private int id;
    private int userid;
    private String bookname;
    private String bookauthor;
    private String bookintroduce;
    private String money;
    private byte[] bookimage;

    public book(){

    }

    public book(int id, int userid, String bookname, String bookauthor, String bookintroduce, String money, byte[] bookimage){
        this.id = id;
        this.userid = userid;
        this.bookname = bookname;
        this.bookauthor = bookauthor;
        this.bookintroduce = bookintroduce;
        this.money = money;
        this.bookimage = bookimage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getBookauthor() {
        return bookauthor;
    }

    public void setBookauthor(String bookauthor) {
        this.bookauthor = bookauthor;
    }

    public String getBookintroduce() {
        return bookintroduce;
    }

    public void setBookintroduce(String bookintroduce) {
        this.bookintroduce = bookintroduce;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public byte[] getBookimage() {
        return bookimage;
    }

    public void setBookimage(byte[] bookimage) {
        this.bookimage = bookimage;
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof book){
            return this.bookname.equals(((book) obj).bookname);
        }
        return super.equals(obj);
    }

}
