package com.example.treasure.Dao;

import com.example.treasure.Bean.User;
import com.example.treasure.Utils.JDBCUtils;
import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class UserDao {
    private static final String TAG = "mysql-treasure-UserDao";

    public int login(String student_id, String password){

        HashMap<String, Object> map = new HashMap<>();
        Connection connection = JDBCUtils.getConn();
        int msg = 0;
        try {
            String sql = "select * from user where student_id = ?";
            if (connection != null){
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null){
                    Log.e(TAG,"Student ID：" + student_id);

                    ps.setString(1, student_id);

                    ResultSet rs = ps.executeQuery();
                    int count = rs.getMetaData().getColumnCount();

                    while (rs.next()){
                        for (int i = 1;i <= count;i++){
                            String field = rs.getMetaData().getColumnName(i);
                            map.put(field, rs.getString(field));
                        }
                    }
                    connection.close();
                    ps.close();

                    if (map.size()!=0){
                        StringBuilder s = new StringBuilder();

                        for (String key : map.keySet()){
                            if(key.equals("password")){
                                if(password.equals(map.get(key))){
                                    msg = 1;
                                }
                                else
                                    msg = 2;
                                break;
                            }
                        }
                    }else {
                        Log.e(TAG, "No result");
                        msg = 3;
                    }
                }else {
                    msg = 0;
                }
            }else {
                msg = 0;
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, "Invalid login：" + e.getMessage());
            msg = 0;
        }
        return msg;
    }



    public boolean register(User user){
        //HashMap<String, Object> map = new HashMap<>();

        Connection connection = JDBCUtils.getConn();

        try {
            String sql = "insert into user(student_id,password,student_name,nick_name,gender,use_nick) values (?,?,?,?,?,?)";
            if (connection != null){
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null){


                    ps.setString(1,user.getStudent_id());
                    ps.setString(2,user.getPassword());
                    ps.setString(3,user.getStudent_name());
                    ps.setString(4,user.getNick_name());
                    ps.setString(5,user.getGender());
                    ps.setInt(6,1);

                    int rs = ps.executeUpdate();
                    if(rs>0)
                        return true;
                    else
                        return false;
                }else {
                    return  false;
                }
            }else {
                return  false;
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "Invalid register：" + e.getMessage());
            return false;
        }

    }

    public boolean editInfo(User user){
        Connection connection = JDBCUtils.getConn();
        try{
            String sql="Update user set student_id=?,student_name=?,nick_name=?,gender=?,password=?,use_nick=? where id=?";
            if(connection!=null){
                PreparedStatement ps = connection.prepareStatement(sql);
                if(ps!=null){
                    ps.setString(1,user.getStudent_id());
                    ps.setString(2,user.getStudent_name());
                    ps.setString(3,user.getNick_name());
                    ps.setString(4,user.getGender());
                    ps.setString(5,user.getPassword());
                    ps.setInt(6,user.getUse_nick());
                    ps.setInt(7,user.getId());
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
            Log.e(TAG, "Invalid editInfo：" + e.getMessage());
            return false;
        }
    }



    public User findUser(String student_id) {


        Connection connection = JDBCUtils.getConn();
        User user = null;
        try {
            String sql = "select * from user where student_id = ?";
            if (connection != null){
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setString(1, student_id);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        int id=rs.getInt(1);
                        String studentid = rs.getString(2);
                        String student_name = rs.getString(3);
                        String nick_name = rs.getString(4);
                        String gender = rs.getString(5);
                        String password = rs.getString(6);
                        int use_nick=rs.getInt(7);
                        user = new User(id,studentid, student_name, nick_name, gender,password,use_nick);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, "Invalid findUser：" + e.getMessage());
            return null;
        }
        return user;
    }

    public User findUserById(int Id) {
        Connection connection = JDBCUtils.getConn();
        User user = null;
        try {
            String sql = "select * from user where id = ?";
            if (connection != null){
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setInt(1, Id);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        int id=rs.getInt(1);
                        String studentid = rs.getString(2);
                        String student_name = rs.getString(3);
                        String nick_name = rs.getString(4);
                        String gender = rs.getString(5);
                        String password = rs.getString(6);
                        int use_nick=rs.getInt(7);
                        user = new User(id,studentid, student_name, nick_name, gender,password,use_nick);

                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, "Invalid findUser：" + e.getMessage());
            return null;
        }
        return user;
    }

}
