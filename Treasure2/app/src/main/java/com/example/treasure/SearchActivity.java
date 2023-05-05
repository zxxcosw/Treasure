package com.example.treasure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.treasure.Adapter.MyPostAdapter;
import com.example.treasure.Bean.Post;
import com.example.treasure.Bean.User;
import com.example.treasure.Dao.PostDao;
import com.example.treasure.Dao.UserDao;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    ImageView back, search;
    EditText keyword;
    ListView listView;
    User user;

    List<Post> list;
    ArrayList<String> names;
    MyPostAdapter myPostAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        user=(User)getIntent().getSerializableExtra("User");

        back=findViewById(R.id.back);
        search=findViewById(R.id.search);
        keyword=findViewById(R.id.Input);
        listView=findViewById(R.id.list);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(SearchActivity.this,MainActivity.class);
                intent.putExtra("User",user);
                intent.putExtra("index",0);
                startActivity(intent);
                finish();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(keyword.getText().toString().trim().equals("")){
                    keyword.setError("Ask some questions here!");
                }else{
                    list=new ArrayList<>();
                    new Thread(){
                        public void run(){
                            PostDao postDao=new PostDao();
                            list=postDao.getSearchPost(keyword.getText().toString().trim());
                            UserDao userDao=new UserDao();
                            names=new ArrayList<>();
                            for(int i=0;i<list.size();i++){
                                User poster=userDao.findUserById(list.get(i).getPoster_id());
                                String name;
                                if(poster.getUse_nick()==1){
                                    name=poster.getNick_name();
                                }else{
                                    name=poster.getStudent_name();
                                }
                                names.add(name);
                            }
                            int msg=1;
                            hand.sendEmptyMessage(msg);
                        }
                    }.start();
                }
            }
        });


    }



    final Handler hand = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.what==1){
                myPostAdapter=new MyPostAdapter(getApplicationContext(),list,names);
                listView.setAdapter(myPostAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Post post=list.get(position);
                        Intent intent=new Intent(SearchActivity.this,PostActivity.class);
                        intent.putExtra("User",user);
                        intent.putExtra("post_id",post.getId());
                        intent.putExtra("page",3);
                        startActivity(intent);
                    }
                });
            }

        }
    };
}