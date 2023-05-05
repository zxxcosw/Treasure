package com.example.treasure.Bean;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {

    private int id;
    private int post_id;
    private int user_id;
    private int number;
    private int state; //0-ordered, 1-completed, 2-cancelled
    private Date order_time;

    public Order() {
    }

    public Order(int id, int post_id, int user_id, int number, int state, Date order_time) {
        this.id = id;
        this.post_id = post_id;
        this.user_id = user_id;
        this.number = number;
        this.state = state;
        this.order_time=order_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getOrder_time() {
        return order_time;
    }

    public void setOrder_time(Date order_time) {
        this.order_time = order_time;
    }
}
