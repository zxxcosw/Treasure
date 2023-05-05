package com.example.treasure.Bean;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String student_id;
    private String student_name;
    private String nick_name;
    private String gender;
    private String password;
    private int use_nick;  //0--Use real name  1--Use nick name  Default value:1

    public User() {
    }

    public User(int id, String student_id, String student_name, String nick_name, String gender, String password, int use_nick) {
        this.id=id;
        this.student_id=student_id;
        this.student_name=student_name;
        this.nick_name=nick_name;
        this.gender=gender;
        this.password = password;
        this.use_nick=use_nick;
    }

    public User(String student_id, String student_name, String nick_name, String gender, String password, int use_nick) {
        this.student_id=student_id;
        this.student_name=student_name;
        this.nick_name=nick_name;
        this.gender=gender;
        this.password = password;
        this.use_nick=use_nick;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUse_nick() {
        return use_nick;
    }

    public void setUse_nick(int use_nick) {
        this.use_nick = use_nick;
    }
}
