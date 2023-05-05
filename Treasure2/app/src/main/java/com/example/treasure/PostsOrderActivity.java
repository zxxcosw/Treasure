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

import com.example.treasure.Adapter.PostsOderAdapter;
import com.example.treasure.Bean.Post;
import com.example.treasure.Bean.User;
import com.example.treasure.Dao.PostDao;

import java.util.List;

public class PostsOrderActivity extends AppCompatActivity {

    User user;
    ImageView back;
    ListView list;
    List<Post> posts;
    PostsOderAdapter postsOderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_order);

        user=(User)getIntent().getSerializableExtra("User");
        back=findViewById(R.id.Back_from_myPost);
        list=findViewById(R.id.order_post);

        getData();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PostsOrderActivity.this,MainActivity.class);
                intent.putExtra("User",user);
                intent.putExtra("index",4);
                startActivity(intent);
                finish();
            }
        });
    }

    public void getData(){
        new Thread(){
            public void run(){
                PostDao postDao=new PostDao();
                posts=postDao.selectPostByUser(user.getId());
                int msg = 1;
                hand.sendEmptyMessage(msg);
            }
        }.start();

    }

    @SuppressLint("HandlerLeak")
    final Handler hand = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                postsOderAdapter=new PostsOderAdapter(PostsOrderActivity.this,posts);
                list.setAdapter(postsOderAdapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Post post=posts.get(position);
                        Intent intent=new Intent(PostsOrderActivity.this,OrderReceivedActivity.class);
                        intent.putExtra("post_id",post.getId());
                        intent.putExtra("User",user);
                        startActivity(intent);
                    }
                });
            }

        }
    };
}