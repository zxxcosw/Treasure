package com.example.treasure.Bean;

import java.io.Serializable;
import java.util.Date;

public class Question implements Serializable {
    private int id;
    private int post_id;
    private int user_id;
    private String question;
    private String answer;
    private Date question_time;
    private Date answer_time;

    public Question() {
    }

    public Question(int id, int post_id, int user_id, String question, String answer, Date question_time, Date answer_time ) {
        this.id = id;
        this.post_id = post_id;
        this.user_id = user_id;
        this.question = question;
        this.answer = answer;
        this.question_time=question_time;
        this.answer_time=answer_time;
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

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Date getQuestion_time() {
        return question_time;
    }

    public void setQuestion_time(Date question_time) {
        this.question_time = question_time;
    }

    public Date getAnswer_time() {
        return answer_time;
    }

    public void setAnswer_time(Date answer_time) {
        this.answer_time = answer_time;
    }
}
