package com.example.treasure.Dao;

import android.util.Log;

import com.example.treasure.Bean.Order;

import com.example.treasure.Utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {
    private static final String TAG = "mysql-treasure-OrderDao";

    public boolean addOrder(Order order){
        Connection connection = JDBCUtils.getConn();
        try{
            String sql="insert into `order`(post_id,user_id,number,state,order_time) values (?,?,?,?,?)";
            if(connection!=null){
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps!=null) {
                    ps.setInt(1,order.getPost_id());
                    ps.setInt(2,order.getUser_id());
                    ps.setInt(3,order.getNumber());
                    ps.setInt(4,order.getState());
                    ps.setTimestamp(5,new Timestamp(order.getOrder_time().getTime()));
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

        }
        catch(Exception e){
            e.printStackTrace();
            Log.e(TAG, "Invalid addOrder：" + e.getMessage());
            return false;
        }

    }


    public List<Order> selectOrderByUser(int user_id)  {
        List<Order> list=new ArrayList<Order>();
        Connection connection = JDBCUtils.getConn();
        try{
            String sql="select * from `order` where user_id=? order by id desc";
            if(connection!=null){
                PreparedStatement ps = connection.prepareStatement(sql);
                if(ps!=null){
                    ps.setInt(1,user_id);
                    ResultSet result = ps.executeQuery();
                    while(result.next()){
                        Order order=new Order();
                        order.setId(result.getInt("id"));
                        order.setPost_id(result.getInt("post_id"));
                        order.setUser_id(result.getInt("user_id"));
                        order.setNumber(result.getInt("number"));
                        order.setState(result.getInt("state"));
                        order.setOrder_time(result.getTimestamp("order_time"));
                        list.add(order);
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            Log.e(TAG, "Invalid findOrder：" + e.getMessage());
        }
        return list;
    }

    public List<Order> selectOrderByPost(int post_id){
        List<Order> list=new ArrayList<Order>();
        Connection connection = JDBCUtils.getConn();
        try{
            String sql="select * from `order` where post_id=? order by id desc";
            if(connection!=null){
                PreparedStatement ps = connection.prepareStatement(sql);
                if(ps!=null){
                    ps.setInt(1,post_id);
                    ResultSet result = ps.executeQuery();
                    while(result.next()){
                        Order order=new Order();
                        order.setId(result.getInt("id"));
                        order.setPost_id(result.getInt("post_id"));
                        order.setUser_id(result.getInt("user_id"));
                        order.setNumber(result.getInt("number"));
                        order.setState(result.getInt("state"));
                        order.setOrder_time(result.getTimestamp("order_time"));
                        list.add(order);
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            Log.e(TAG, "Invalid findOrder：" + e.getMessage());
        }
        return list;
    }

    public Order findOrder(int id){
        Connection connection = JDBCUtils.getConn();
        Order order=new Order();
        try{
            String sql="select * from `order` where id = ?";
            if(connection!=null){
                PreparedStatement ps = connection.prepareStatement(sql);
                if(ps!=null){
                    ps.setInt(1,id);
                    ResultSet rs = ps.executeQuery();
                    while(rs.next()){
                        order.setId(rs.getInt("id"));
                        order.setPost_id(rs.getInt("post_id"));
                        order.setUser_id(rs.getInt("user_id"));
                        order.setNumber(rs.getInt("number"));
                        order.setState(rs.getInt("state"));
                        order.setOrder_time(rs.getTimestamp("order_time"));
                    }
                }
            }

        }catch(Exception e){
            e.printStackTrace();
            Log.e(TAG, "Invalid findOrder：" + e.getMessage());
            return null;
        }
        return  order;
    }

    public boolean changeState(int order_id, int state){
        Connection connection = JDBCUtils.getConn();
        try{
            String sql="update `order` set state=? where id=?";
            if(connection!=null){
                PreparedStatement ps = connection.prepareStatement(sql);
                if(ps!=null) {
                    ps.setInt(1,state);
                    ps.setInt(2,order_id);
                    int rs = ps.executeUpdate();
                    if(rs>0)
                        return true;
                    else
                        return false;
                }
                else{
                    return  false;
                }
            }
            else{
                return false;
            }

        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "Invalid changeOrderState：" + e.getMessage());
            return false;
        }

    }

    public boolean deleteOrder(int id){
        Connection connection = JDBCUtils.getConn();
        try{
            String sql="delete from `order` where id=?";
            if(connection!=null){
                PreparedStatement ps = connection.prepareStatement(sql);
                if(ps!=null){
                    ps.setInt(1,id);
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
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "Invalid deleteOrder：" + e.getMessage());
            return false;
        }
    }

}
