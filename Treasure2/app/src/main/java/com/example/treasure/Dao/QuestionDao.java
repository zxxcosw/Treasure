package com.example.treasure.Dao;

import android.util.Log;


import com.example.treasure.Bean.Question;
import com.example.treasure.Utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class QuestionDao {
    private static final String TAG = "mysql-treasure-QuesDao";

    public boolean addQuestion(Question question){
        Connection connection = JDBCUtils.getConn();
        try{
            String sql="insert into question(post_id,user_id,question,question_time) values (?,?,?,?)";
            if(connection!=null){
                PreparedStatement ps = connection.prepareStatement(sql);
                if(ps!=null){
                    ps.setInt(1,question.getPost_id());
                    ps.setInt(2,question.getUser_id());
                    ps.setString(3,question.getQuestion());
                    ps.setTimestamp(4,new Timestamp(question.getQuestion_time().getTime()));
                    int rs = ps.executeUpdate();
                    if(rs>0)
                        return true;
                    else
                        return false;
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }

        }catch(Exception e){
            e.printStackTrace();
            Log.e(TAG, "Invalid addQuestion：" + e.getMessage());
            return false;
        }

    }

    public boolean answerQuestion(int q_id, String ans, Date ans_time){
        Connection connection = JDBCUtils.getConn();
        try{
            String sql="update question set answer=?,answer_time=? where id=?";
            if(connection!=null){
                PreparedStatement ps = connection.prepareStatement(sql);
                if(ps!=null){
                    ps.setString(1,ans);
                    ps.setTimestamp(2,new Timestamp(ans_time.getTime()));
                    ps.setInt(3,q_id);
                    int rs = ps.executeUpdate();
                    if(rs>0)
                        return true;
                    else
                        return false;

                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }catch(Exception e){
            e.printStackTrace();
            Log.e(TAG, "Invalid answer：" + e.getMessage());
            return false;
        }
    }

    public List<Question> selectQuestionByPost(int post_id) throws SQLException {
        List<Question> list=new ArrayList<Question>();
        Connection connection = JDBCUtils.getConn();
        String sql="select * from question where post_id=? order by id desc";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1,post_id);
        ResultSet rs=ps.executeQuery();
        while(rs.next()){
            Question question=new Question();
            question.setId(rs.getInt("id"));
            question.setPost_id(rs.getInt("post_id"));
            question.setUser_id(rs.getInt("user_id"));
            question.setQuestion(rs.getString("question"));
            question.setQuestion_time(rs.getTimestamp("question_time"));
            question.setAnswer(rs.getString("answer"));
            question.setAnswer_time(rs.getTimestamp("answer_time"));
            list.add(question);
        }
        return  list;
    }

    public Question findQuestion(int id){
        Connection connection = JDBCUtils.getConn();
        Question question=new Question();
        try{
            String sql = "select * from question where id = ?";
            if(connection!=null){
                PreparedStatement ps = connection.prepareStatement(sql);
                if(ps!=null){
                    ps.setInt(1,id);
                    ResultSet rs = ps.executeQuery();

                    while(rs.next()){
                        question.setId(rs.getInt("id"));
                        question.setPost_id(rs.getInt("post_id"));
                        question.setUser_id(rs.getInt("user_id"));
                        question.setQuestion(rs.getString("question"));
                        question.setQuestion_time(rs.getTimestamp("question_time"));
                        question.setAnswer(rs.getString("answer"));
                        question.setAnswer_time(rs.getTimestamp("answer_time"));
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, "Invalid findQuestion：" + e.getMessage());
            return null;
        }
        return question;
    }

}
