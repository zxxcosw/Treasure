package com.example.treasure;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.treasure.Adapter.MyPostAdapter;
import com.example.treasure.Bean.Post;
import com.example.treasure.Bean.User;
import com.example.treasure.Dao.PostDao;

import java.util.ArrayList;
import java.util.List;

public class MyPostActivity extends AppCompatActivity {

    User user;
    ImageView back;
    ListView list;
    List<Post> posts;
    MyPostAdapter myPostAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post);

        user = (User) getIntent().getSerializableExtra("User");
        back = findViewById(R.id.Back_from_myPost);
        list = findViewById(R.id.myPostList);

        getData();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyPostActivity.this,MainActivity.class);
                intent.putExtra("User",user);
                intent.putExtra("index",4);
                startActivity(intent);
                finish();

            }
        });

    }

    public void initData(){
        String name;
        if(user.getUse_nick()==1){
            name=user.getNick_name();
        }
        else{
            name=user.getStudent_name();
        }
        ArrayList<String> names=new ArrayList<>();
        for(int i=0;i<posts.size();i++){
            names.add(name);
        }
        myPostAdapter=new MyPostAdapter(getApplicationContext(),posts,names);
        list.setAdapter(myPostAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Post post=posts.get(position);
                Intent intent=new Intent(MyPostActivity.this,PostActivity.class);
                intent.putExtra("User",user);
                intent.putExtra("post_id",post.getId());
                intent.putExtra("page",2);
                startActivity(intent);
                finish();
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder( MyPostActivity.this)
                        .setMessage("Do you want to delete this post?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new Thread(){
                                    public void run(){
                                        PostDao postDao=new PostDao();
                                        boolean result=postDao.deletePost(posts.get(position).getId());
                                        int msg=0;
                                        if(!result){
                                            msg=2;
                                        }
                                        hand.sendEmptyMessage(msg);
                                    }
                                }.start();


                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog =  builder.create();
                dialog.show();
                return true;
            }
        });
    }

    public void getData() {
        new Thread() {
            public void run() {
                PostDao postDao = new PostDao();
                posts = postDao.selectPostByUser(user.getId());
                int msg = 1;
                hand.sendEmptyMessage(msg);
            }
        }.start();

    }

    @SuppressLint("HandlerLeak")
    final Handler hand = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                initData();
            }else if(msg.what==0){
                Toast.makeText(MyPostActivity.this,"Delete successfully!",
                        Toast.LENGTH_SHORT).show();
                getData();
            }else if(msg.what==2){
                Toast.makeText(MyPostActivity.this,"Failed to delete!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };
}