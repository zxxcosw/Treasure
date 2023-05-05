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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.treasure.Bean.Order;
import com.example.treasure.Bean.Post;
import com.example.treasure.Bean.User;
import com.example.treasure.Dao.OrderDao;
import com.example.treasure.Dao.PostDao;
import com.example.treasure.Dao.UserDao;

import java.util.Date;

public class OrderActivity extends AppCompatActivity {

    ImageView back, img;
    TextView poster_name, title, maxNum;
    EditText number;
    Button order;
    int id;

    Post post;
    User user,poster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        user=(User)getIntent().getSerializableExtra("User");
        id=getIntent().getIntExtra("post_id",0);

        back=findViewById(R.id.Back_from_order);
        img=findViewById(R.id.image);
        poster_name=findViewById(R.id.poster_name);
        title=findViewById(R.id.post_name);
        maxNum=findViewById(R.id.max_num);
        number=findViewById(R.id.num);
        order=findViewById(R.id.button);
        getData();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OrderActivity.this, PostActivity.class);
                intent.putExtra("post_id",id);
                intent.putExtra("User",user);
                startActivity(intent);
                finish();
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(post.getState()==1){
                    Toast.makeText(OrderActivity.this,"The post has been closed!", Toast.LENGTH_LONG).show();
                }
                else if(post.getAmount()<Integer.parseInt(number.getText().toString().trim())){
                    Toast.makeText(OrderActivity.this,"The number you order exceeds the total amount!", Toast.LENGTH_LONG).show();
                }
                else {
                    Order order = new Order();
                    order.setPost_id(id);
                    order.setUser_id(user.getId());
                    order.setState(0);
                    order.setNumber(Integer.parseInt(number.getText().toString().trim()));
                    order.setOrder_time(new Date());

                    new Thread() {
                        public void run() {
                            OrderDao orderDao = new OrderDao();
                            Boolean result = orderDao.addOrder(order);
                            int msg;
                            if (!result) {
                                msg = 2;
                            }else{
                                PostDao postDao=new PostDao();
                                post.setAmount(post.getAmount()-order.getNumber());
                                post.stateCheck();
                                Boolean rs=postDao.editPost(post);
                                if(rs){
                                    msg=0;
                                }else{
                                    msg=4;
                                }
                            }
                            hand.sendEmptyMessage(msg);
                        }
                    }.start();
                }


            }
        });


    }

    public void initData(){
        String name;
        if(poster.getUse_nick()==1){
            name=poster.getNick_name();
        }
        else{
            name=poster.getStudent_name();
        }
        poster_name.setText(name);
        title.setText(post.getTitle());
        maxNum.setText("Max: "+post.getAmount());
        img.setImageBitmap((Bitmap)getPicFromBytes(post.getImg(),null));


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

    public void getData(){
        new Thread(){
            public void run(){
                PostDao postDao=new PostDao();
                post=postDao.findPost(id);

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
                Toast.makeText(OrderActivity.this,"Order successfully!", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(OrderActivity.this,MainActivity.class);
                intent.putExtra("User",user);
                intent.putExtra("index",1);
                startActivity(intent);
                finish();
            }
            else if(msg.what==2){
                Toast.makeText(OrderActivity.this,"Failed to order!", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(OrderActivity.this,"Order successfully but failed to update post!", Toast.LENGTH_LONG).show();
            }

        }
    };

}