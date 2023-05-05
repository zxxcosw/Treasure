package com.example.treasure;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.treasure.Adapter.OrderReceivedAdapter;
import com.example.treasure.Bean.Order;
import com.example.treasure.Bean.Post;
import com.example.treasure.Bean.User;
import com.example.treasure.Dao.OrderDao;
import com.example.treasure.Dao.PostDao;
import com.example.treasure.Dao.UserDao;

import java.util.ArrayList;
import java.util.List;

public class OrderReceivedActivity extends AppCompatActivity {
    Post post;
    User user;
    int id;
    ImageView back;
    TextView title;
    ArrayList<String> names;
    List<Order> orders;
    OrderReceivedAdapter orderReceivedAdapter;
    ListView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_received);
        user = (User) getIntent().getSerializableExtra("User");
        id = getIntent().getIntExtra("post_id", 0);
        back = findViewById(R.id.back);
        title = findViewById(R.id.post_title);
        list = findViewById(R.id.orderList);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getData();

    }

    public void getData() {
        new Thread() {
            public void run() {
                PostDao postDao = new PostDao();
                post = postDao.findPost(id);
                OrderDao orderDao = new OrderDao();
                orders = orderDao.selectOrderByPost(post.getId());
                UserDao userDao = new UserDao();
                names = new ArrayList<>();
                for (int i = 0; i < orders.size(); i++) {
                    User orderer = userDao.findUserById(orders.get(i).getUser_id());
                    String name;
                    if (orderer.getUse_nick() == 1) {
                        name = orderer.getNick_name();
                    } else {
                        name = orderer.getStudent_name();
                    }
                    names.add(name);
                }
                int msg = 1;
                hand.sendEmptyMessage(msg);
            }
        }.start();

    }

    public void initData() {
        title.setText(post.getTitle());
        orderReceivedAdapter = new OrderReceivedAdapter(OrderReceivedActivity.this, orders, names);
        list.setAdapter(orderReceivedAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(OrderReceivedActivity.this, ChatActivity.class);
                intent.putExtra("User", user);
                intent.putExtra("chatId", orders.get(position).getUser_id());
                startActivity(intent);
                finish();
            }
        });
    }

    @SuppressLint("HandlerLeak")
    final Handler hand = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                initData();
            }

        }
    };
}