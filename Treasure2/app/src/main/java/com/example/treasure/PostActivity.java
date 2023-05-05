package com.example.treasure;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;

import com.example.treasure.Bean.Post;

import com.example.treasure.Bean.User;
import com.example.treasure.Dao.PostDao;

import com.example.treasure.Dao.UserDao;


import java.text.SimpleDateFormat;


public class PostActivity extends AppCompatActivity {

    User user,poster; //current user, poster
    Post post;

    ImageView back_from_post, post_img;
    Button edit_order,qa;
    TextView title, content, post_user, post_date, expired_date,state, price, o_price;

    Boolean isPoster;
    int lastPage;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        int id=getIntent().getIntExtra("post_id",0);
        user=(User)getIntent().getSerializableExtra("User");
        lastPage=getIntent().getIntExtra("page",1);

        back_from_post=findViewById(R.id.Back_from_post);
        post_img=findViewById(R.id.post_img);
        edit_order=findViewById(R.id.edit_orOrder);

        qa=findViewById(R.id.qa);

        title=findViewById(R.id.post_title);
        content=findViewById(R.id.post_content);
        post_user=findViewById(R.id.post_user);
        post_date=findViewById(R.id.post_date);
        expired_date=findViewById(R.id.expired_date);
        state=findViewById(R.id.state);
        price=findViewById(R.id.price);
        o_price=findViewById(R.id.oPrice);


        getData(id); //post_id

        back_from_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //last page is main page
                if(lastPage==1){
                    Intent intent=new Intent(PostActivity.this, MainActivity.class);
                    intent.putExtra("User",user);
                    intent.putExtra("index",0);
                    startActivity(intent);
                    finish();
                }
                //last page is myPost page
                else if(lastPage==2){
                    Intent intent=new Intent(PostActivity.this,MyPostActivity.class);
                    intent.putExtra("User",user);
                    startActivity(intent);
                    finish();
                }else{
                    finish();
                }

            }
        });








    }

    public void getData(int id){
        new Thread(){
            public void run(){
                PostDao postDao= new PostDao();
                post=postDao.findPost(id);

                UserDao userDao=new UserDao();
                poster=userDao.findUserById(post.getPoster_id());

                int msg=1;
                hand.sendEmptyMessage(msg);
            }
        }.start();


    }

    public static Bitmap getPicFromBytes(byte[] bytes, BitmapFactory.Options opts) {
        if (bytes != null)
            if (opts != null)
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
                        opts);
            else
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;
    }

    public void initData(){
        String name;
        if(poster.getUse_nick()==1){
            name=poster.getNick_name();
        }
        else{
            name=poster.getStudent_name();
        }
        post_user.setText(name);
        title.setText(String.valueOf(post.getTitle()));
        content.setText(String.valueOf(post.getContent()));
        post_img.setImageBitmap((Bitmap)getPicFromBytes(post.getImg(),null));
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        post_date.setText(dataFormat.format(post.getPost_time()));
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        expired_date.setText("Expired at "+dft.format(post.getEnd_time()));
        price.setText("Price: "+ post.getPrice());
        o_price.setText("Original Price: "+ post.getO_price());
        if(post.getState()==1){
            state.setVisibility(View.VISIBLE);

        }
        else{
            state.setVisibility(View.GONE);

        }


    }

    @SuppressLint("HandlerLeak")
    final Handler hand = new Handler()
    {
        public void handleMessage(Message msg) {
            if(msg.what==1){
                initData();

                isPoster=post.getPoster_id()==user.getId();
                if(post.getState()==1){
                    edit_order.setVisibility(View.GONE);
                    qa.setVisibility(View.GONE);
                }
                else{
                    edit_order.setVisibility(View.VISIBLE);
                    //Other users are viewing the post, order
                    if (post.getPoster_id()!=user.getId()){
                        edit_order.setText("Order");
                    }

                    //Poster is viewing the post, edit
                    else{
                        edit_order.setText("Edit");
                    }
                    edit_order.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(isPoster){
                                Intent intent=new Intent(PostActivity.this, EditPostActivity.class);
                                intent.putExtra("post_id",post.getId());
                                intent.putExtra("User",user);
                                intent.putExtra("page",lastPage);
                                startActivity(intent);
                                finish();
                            }else{
                                Intent intent=new Intent(PostActivity.this,OrderActivity.class);
                                intent.putExtra("User",user);
                                intent.putExtra("post_id",post.getId());
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

                    qa.setVisibility(View.VISIBLE);
                    qa.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(PostActivity.this,QAActivity.class);
                            intent.putExtra("post_id",post.getId());
                            intent.putExtra("User",user);
                            intent.putExtra("page",lastPage);
                            startActivity(intent);

                        }
                    });
                }

            }

        }
    };
}