package com.example.treasure.Dao;

import android.util.Log;

import com.example.treasure.Bean.Msg;
import com.example.treasure.Utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MessageDao {
    private static final String TAG = "mysql-treasure-MsgDao";

    public boolean addChat(int id1, int id2){
        Connection connection=JDBCUtils.getConn();
        try{
            String sql="insert into chat(user1_id,user2_id) values(?,?)";
            if(connection!=null) {
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setInt(1,id1);
                    ps.setInt(2,id2);
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
            Log.e(TAG, "Invalid addChat：" + e.getMessage());
            return false;
        }
    }

    public List<Integer> findChatUser(int id){
        List<Integer> list=new ArrayList<Integer>();
        Connection connection = JDBCUtils.getConn();
        try{
            String sql="select * from chat where user1_id=? or user2_id=? order by id desc";
            if(connection!=null){
                PreparedStatement ps = connection.prepareStatement(sql);
                if(ps!=null) {
                    ps.setInt(1,id);
                    ps.setInt(2,id);
                    ResultSet result = ps.executeQuery();
                    while(result.next()){
                        if(result.getInt("user1_id")==id){
                            list.add(result.getInt("user2_id"));
                        }else{
                            list.add(result.getInt("user1_id"));
                        }
                    }

                }
            }

        }catch(Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Invalid getChat：" + e.getMessage());
        }
        return list;
    }

    public int findChat(int id1,int id2){
        int chatId=0;
        Connection connection = JDBCUtils.getConn();
        try{
            String sql="select * from chat where ( user1_id=? and user2_id=? ) or ( user1_id=? and user2_id=? )";
            if(connection!=null){
                PreparedStatement ps = connection.prepareStatement(sql);
                if(ps!=null) {
                    ps.setInt(1,id1);
                    ps.setInt(2,id2);
                    ps.setInt(3,id2);
                    ps.setInt(4,id1);
                    ResultSet result = ps.executeQuery();
                    while (result.next()) {
                        chatId=result.getInt("id");
                    }
                }

                }

        }catch(Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Invalid getChat：" + e.getMessage());

        }
        return chatId;
    }

    public boolean sendMessage(Msg msg){
        Connection connection = JDBCUtils.getConn();
        try{
            String sql="insert into message(sender_id,receiver_id,text,time) values (?,?,?,?)";
            if(connection!=null) {
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setInt(1, msg.getSender_id());
                    ps.setInt(2, msg.getReceiver_id());
                    ps.setString(3, msg.getText());
                    ps.setTimestamp(4,new Timestamp(msg.getTime().getTime()));
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
            Log.e(TAG, "Invalid sendMsg：" + e.getMessage());
            return false;
        }
    }

    //When chat with specific user
    public List<Msg> selectMessageByUsersId(int id1, int id2){
        List<Msg> list=new ArrayList<Msg>();
        Connection connection = JDBCUtils.getConn();
        try{
            String sql="select * from `message` where ( sender_id=? and receiver_id=? ) or ( sender_id=? and receiver_id=?) order by id asc";
            if(connection!=null){
                PreparedStatement ps = connection.prepareStatement(sql);
                if(ps!=null) {
                    ps.setInt(1,id1);
                    ps.setInt(2,id2);
                    ps.setInt(3,id2);
                    ps.setInt(4,id1);
                    ResultSet result = ps.executeQuery();
                    while(result.next()){
                        Msg msg =new Msg();
                        msg.setId(result.getInt("id"));
                        msg.setSender_id(result.getInt("sender_id"));
                        msg.setReceiver_id(result.getInt("receiver_id"));
                        msg.setText(result.getString("text"));
                        msg.setTime(result.getTimestamp("time"));
                        list.add(msg);

                    }
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Invalid getMsg：" + e.getMessage());
        }
        return list;


    }

    public Msg selectResentMessage(int id1, int id2){
        Msg msg =new Msg();
        Connection connection = JDBCUtils.getConn();
        try{
            String sql="select * from `message` where ( sender_id=? and receiver_id=? ) or ( sender_id=? and receiver_id=? ) order by id desc limit 1";
            if(connection!=null){
                PreparedStatement ps = connection.prepareStatement(sql);
                if(ps!=null) {
                    ps.setInt(1,id1);
                    ps.setInt(2,id2);
                    ps.setInt(3,id2);
                    ps.setInt(4,id1);
                    ResultSet result = ps.executeQuery();
                    while(result.next()){
                        msg.setId(result.getInt("id"));
                        msg.setSender_id(result.getInt("sender_id"));
                        msg.setReceiver_id(result.getInt("receiver_id"));
                        msg.setText(result.getString("text"));
                        msg.setTime(result.getTimestamp("time"));
                    }
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Invalid getMsg：" + e.getMessage());
        }
        return msg;
    }

    //List user and message who have chatted with you in Message page
    public List<Msg> selectMessageByUserId(int id){
        List<Msg> list=new ArrayList<Msg>();
        List<Integer> ids=findChatUser(id);
        for(int i=0;i<ids.size();i++){
            Msg msg=selectResentMessage(id,ids.get(i));

            list.add(msg);
        }
        return list;
    }

}
