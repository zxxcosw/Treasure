package com.example.treasure.Bean;

import java.io.Serializable;
import java.util.Date;

public class Msg implements Serializable {
    private int id;
    private int sender_id;
    private int receiver_id;
    private String text;
    private Date time;

    public Msg() {
    }

    public Msg(int id, int sender_id, int receiver_id, String text, Date time) {
        this.id = id;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.text = text;
        this.time = time;
    }

    public Msg(int sender_id, int receiver_id, String text, Date time) {
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.text = text;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
