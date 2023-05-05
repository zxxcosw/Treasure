package com.example.treasure.Dao;

import android.util.Log;

import com.example.treasure.Bean.Post;
import com.example.treasure.Utils.JDBCUtils;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PostDao {
    private static final String TAG = "mysql-treasure-PostDao";

    public boolean addPost(Post post){
        Connection connection = JDBCUtils.getConn();
        try{
            String sql = "insert into post(title,content,poster_id,state,post_time,end_time,amount,img,price,o_price) values (?,?,?,?,?,?,?,?,?,?)";
            if (connection!=null){
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps!=null){
                    ps.setString(1,post.getTitle());
                    ps.setString(2,post.getContent());
                    ps.setInt(3,post.getPoster_id());
                    ps.setInt(4,post.getState());
                    ps.setTimestamp(5,new Timestamp(post.getPost_time().getTime()));
                    ps.setDate(6,new Date(post.getEnd_time().getTime()));
                    ps.setInt(7,post.getAmount());
                    Blob pic = connection.createBlob();
                    pic.setBytes(1, post.getImg());
                    ps.setBlob(8,pic);
                    ps.setString(9,post.getPrice());
                    ps.setString(10,post.getO_price());
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
            Log.e(TAG, "Invalid addPost：" + e.getMessage());
            return false;
        }

    }

    public boolean editPost(Post post){
        Connection connection = JDBCUtils.getConn();
        try{
            String sql="update post set title=?,content=?,end_time=?,amount=?,img=?,state=?,price=?,o_price=? where id=?";
            if(connection!=null){
                PreparedStatement ps = connection.prepareStatement(sql);
                if(ps!=null){
                    ps.setString(1,post.getTitle());
                    ps.setString(2,post.getContent());
                    ps.setDate(3,new Date(post.getEnd_time().getTime()));
                    ps.setInt(4,post.getAmount());
                    Blob pic = connection.createBlob();
                    pic.setBytes(1, post.getImg());
                    ps.setBlob(5,pic);
                    ps.setInt(6,post.getState());
                    ps.setString(7,post.getPrice());
                    ps.setString(8,post.getO_price());
                    ps.setInt(9,post.getId());

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
            Log.e(TAG, "Invalid editPost：" + e.getMessage());
            return false;
        }
    }

    public boolean closePost(int post_id){
        Connection connection = JDBCUtils.getConn();
        try{
            String sql="update post set state=1 where id=?";
            if(connection!=null){
                PreparedStatement ps = connection.prepareStatement(sql);
                if(ps!=null) {
                    ps.setInt(1,post_id);
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
            Log.e(TAG, "Invalid closePost：" + e.getMessage());
            return false;
        }

    }



    public List<Post> selectMainPost() throws SQLException, IOException {
        List<Post> list=new ArrayList<Post>();
        Connection connection = JDBCUtils.getConn();
        String sql="select * from post where state=0 order by id desc";
        Statement statement=connection.createStatement();
        ResultSet result=statement.executeQuery(sql);
        while(result.next()){
            Post post=new Post();
            post.setId(result.getInt("id"));
            post.setTitle(result.getString("title"));
            post.setContent(result.getString("content"));
            post.setPoster_id(result.getInt("poster_id"));
            post.setState(result.getInt("state"));
            post.setPost_time(result.getTimestamp("post_time"));
            post.setEnd_time(result.getDate("end_time"));
            post.setAmount(result.getInt("amount"));
            post.setImg(getBytesByBlob(result.getBlob("img")));
            post.setPrice(result.getString("price"));
            post.setO_price(result.getString("o_price"));
            post.stateCheck();
            if (post.getState()==0){
                list.add(post);
            }
            else{
                editPost(post);
            }
        }

        return list;
    }

    public List<Post> selectPostByUser(int poster_id){
        List<Post> list=new ArrayList<Post>();
        Connection connection=JDBCUtils.getConn();
        try{
            String sql="select * from post where poster_id=? order by id desc";
            if(connection!=null) {
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setInt(1,poster_id);
                    ResultSet result = ps.executeQuery();
                    while(result.next()){
                        Post post=new Post();
                        post.setId(result.getInt("id"));
                        post.setTitle(result.getString("title"));
                        post.setContent(result.getString("content"));
                        post.setPoster_id(result.getInt("poster_id"));
                        post.setState(result.getInt("state"));
                        post.setPost_time(result.getTimestamp("post_time"));
                        post.setEnd_time(result.getDate("end_time"));
                        post.setAmount(result.getInt("amount"));
                        post.setImg(getBytesByBlob(result.getBlob("img")));
                        post.setPrice(result.getString("price"));
                        post.setO_price(result.getString("o_price"));
                        int old_state=post.getState();
                        post.stateCheck();
                        if(old_state!=post.getState()){
                            editPost(post);
                        }
                        list.add(post);
                    }

                }
            }
        }catch(Exception e){
            e.printStackTrace();
            Log.e(TAG, "Invalid findPost：" + e.getMessage());
        }
        return list;
    }

    public Post findPost(int id){
        Connection connection = JDBCUtils.getConn();
        Post post=new Post();
        try{
            String sql = "select * from post where id = ?";
            if(connection!=null){
                PreparedStatement ps = connection.prepareStatement(sql);
                if(ps!=null){
                    ps.setInt(1,id);
                    ResultSet rs = ps.executeQuery();

                    while(rs.next()){
                        post.setId(rs.getInt("id"));
                        post.setTitle(rs.getString("title"));
                        post.setContent(rs.getString("content"));
                        post.setPoster_id(rs.getInt("poster_id"));
                        post.setState(rs.getInt("state"));
                        post.setPost_time(rs.getTimestamp("post_time"));
                        post.setEnd_time(rs.getDate("end_time"));
                        post.setAmount(rs.getInt("amount"));
                        post.setImg(getBytesByBlob(rs.getBlob("img")));
                        post.setPrice(rs.getString("price"));
                        post.setO_price(rs.getString("o_price"));
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, "Invalid findPost：" + e.getMessage());
            return null;
        }
        return post;
    }

    private byte[] getBytesByBlob(Blob pic) throws SQLException, IOException {
        InputStream is = pic.getBinaryStream();
        int len = -1;
        byte[] buf = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while((len = is.read(buf)) != -1) {
            baos.write(buf, 0, len);
        }
        is.close();
        baos.close();
        byte[] bytes = baos.toByteArray();
        return bytes;
    }

    public List<Post> getSearchPost(String keyword){
        List<Post> list=new ArrayList<Post>();
        Connection connection=JDBCUtils.getConn();
        try{
            String sql="select * from post where title like ? order by id desc";
            if(connection!=null) {
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setString(1,"%"+keyword+"%");
                    ResultSet result = ps.executeQuery();
                    while(result.next()){
                        Post post=new Post();
                        post.setId(result.getInt("id"));
                        post.setTitle(result.getString("title"));
                        post.setContent(result.getString("content"));
                        post.setPoster_id(result.getInt("poster_id"));
                        post.setState(result.getInt("state"));
                        post.setPost_time(result.getTimestamp("post_time"));
                        post.setEnd_time(result.getDate("end_time"));
                        post.setAmount(result.getInt("amount"));
                        post.setImg(getBytesByBlob(result.getBlob("img")));
                        post.setPrice(result.getString("price"));
                        post.setO_price(result.getString("o_price"));
                        int old_state=post.getState();
                        post.stateCheck();
                        if(old_state!=post.getState()){
                            editPost(post);
                        }
                        list.add(post);
                    }

                }
            }

        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, "Invalid findPost：" + e.getMessage());

        }
        return list;
    }

    public boolean deletePost(int id){
        Connection connection=JDBCUtils.getConn();
        try{
            String sql="delete from post where id = ?";
            if(connection!=null) {
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
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

        }catch(Exception e){
            e.printStackTrace();
            Log.e(TAG, "Invalid deletePost：" + e.getMessage());
            return false;
        }
    }




}

