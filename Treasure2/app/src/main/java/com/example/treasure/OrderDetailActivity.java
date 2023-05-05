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
import android.widget.Toast;

import com.example.treasure.Bean.Order;
import com.example.treasure.Bean.Post;
import com.example.treasure.Bean.User;
import com.example.treasure.Dao.OrderDao;
import com.example.treasure.Dao.PostDao;
import com.example.treasure.Dao.UserDao;

import java.text.SimpleDateFormat;

public class OrderDetailActivity extends AppCompatActivity {
    int order_id;
    User user,poster;
    Order order;
    Post post;


    ImageView back, pic;
    TextView title, number, date, state;
    Button complete, cancel,msg, seePost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        order_id=getIntent().getIntExtra("order_id",0);
        user=(User)getIntent().getSerializableExtra("User");

        back=findViewById(R.id.Back_from_order);
        pic=findViewById(R.id.main_pic);
        title=findViewById(R.id.main_title);
        number=findViewById(R.id.num);
        date=findViewById(R.id.main_date);
        state=findViewById(R.id.state);
        complete=findViewById(R.id.complete);
        cancel=findViewById(R.id.cancel);
        msg=findViewById(R.id.msg);
        seePost=findViewById(R.id.post);


        getData();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OrderDetailActivity.this, MainActivity.class);
                intent.putExtra("User",user);
                intent.putExtra("index",1);
                startActivity(intent);
                finish();

            }
        });






    }



    public void initData(){
        pic.setImageBitmap((Bitmap)getPicFromBytes(post.getImg(),null));
        title.setText(post.getTitle());
        number.setText("Number: "+order.getNumber());
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date.setText(dataFormat.format(order.getOrder_time()));
        String s;
        if(order.getState()==0){
            s="Ordered";
        }
        else if(order.getState()==1){
            s="completed";
        }
        else{
            s="Cancelled";
        }
        state.setText(s);

        if(order.getState()==0){

            complete.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
            complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread(){
                        public void run(){
                            order.setState(1);
                            OrderDao orderDao=new OrderDao();
                            boolean result=orderDao.changeState(order.getId(),1);
                            int msg=0;
                            if(!result){
                                msg=3;
                            }
                            hand.sendEmptyMessage(msg);
                        }
                    }.start();

                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread(){
                        public void run(){
                            order.setState(2);
                            OrderDao orderDao=new OrderDao();
                            boolean result=orderDao.changeState(order.getId(),2);
                            post.setAmount(post.getAmount()+order.getNumber());
                            post.stateCheck();
                            PostDao postDao=new PostDao();
                            postDao.editPost(post);
                            int msg=2;
                            if(!result){
                                msg=3;
                            }
                            hand.sendEmptyMessage(msg);

                        }
                    }.start();

                }
            });

        }
        else{
            complete.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);

        }

        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(OrderDetailActivity.this,ChatActivity.class);
                intent.putExtra("User",user);
                intent.putExtra("chatId",poster.getId());
                startActivity(intent);
                finish();
            }
        });

        seePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OrderDetailActivity.this,PostActivity.class);
                intent.putExtra("User",user);
                intent.putExtra("post_id",post.getId());
                intent.putExtra("page",3);
                startActivity(intent);
            }
        });



    }

    public void getData(){
        new Thread(){
            public void run(){
                OrderDao orderDao=new OrderDao();
                order=orderDao.findOrder(order_id);
                PostDao postDao=new PostDao();
                post=postDao.findPost(order.getPost_id());
                UserDao userDao=new UserDao();
                poster=userDao.findUserById(post.getPoster_id());
                int msg=1;
                hand.sendEmptyMessage(msg);
            }
        }.start();
    }

    @SuppressLint("HandlerLeak")
    final Handler hand = new Handler()
    {
        public void handleMessage(Message msg) {
            if(msg.what==1){
                initData();
            }
            else if(msg.what==0){
                Toast.makeText(OrderDetailActivity.this,"You set this order as completed!", Toast.LENGTH_LONG).show();
                getData();
            }
            else if(msg.what==2){
                Toast.makeText(OrderDetailActivity.this,"Cancel order successfully!", Toast.LENGTH_LONG).show();
                getData();
            }else {
                Toast.makeText(OrderDetailActivity.this,"Failed to change state!", Toast.LENGTH_LONG).show();
            }

        }
    };

    public static Bitmap getPicFromBytes(byte[] bytes, BitmapFactory.Options opts) {
        if (bytes != null)
            if (opts != null)
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
                        opts);
            else
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;
    }
}