package com.example.treasure.Bean;


import java.io.Serializable;
import java.util.Date;

public class Post implements Serializable {
    private int id;
    private String title;
    private String content;
    private int poster_id;
    private int state;  //0-Posting, 1-closed
    private Date post_time;
    private Date end_time;
    private int amount;
    private byte[] img;
    private String price;
    private String o_price;


    public Post() {
    }

    public Post(String title, String content, int poster_id, int state, Date post_time, Date end_time, int amount, byte[] img, String price, String o_price) {
        this.title = title;
        this.content = content;
        this.poster_id = poster_id;
        this.state = state;
        this.post_time = post_time;
        this.end_time = end_time;
        this.amount = amount;
        this.img=img;
        this.price=price;
        this.o_price=o_price;

    }

    public Post(int id, String title, String content, int poster_id, int state, Date post_time, Date end_time, int amount,byte[] img,String price, String o_price) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.poster_id = poster_id;
        this.state = state;
        this.post_time = post_time;
        this.end_time = end_time;
        this.amount = amount;
        this.img=img;
        this.price=price;
        this.o_price=o_price;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPoster_id() {
        return poster_id;
    }

    public void setPoster_id(int poster_id) {
        this.poster_id = poster_id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getPost_time() {
        return post_time;
    }

    public void setPost_time(Date post_time) {
        this.post_time = post_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getO_price() {
        return o_price;
    }

    public void setO_price(String o_price) {
        this.o_price = o_price;
    }

    public void stateCheck(){
        if(new Date().after(end_time)||amount<=0){
            state=1;
        }
        else{
            state=0;
        }
    }
}
