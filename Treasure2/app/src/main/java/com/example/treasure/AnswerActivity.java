package com.example.treasure;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.treasure.Bean.Post;
import com.example.treasure.Bean.Question;
import com.example.treasure.Bean.User;
import com.example.treasure.Dao.PostDao;
import com.example.treasure.Dao.QuestionDao;
import com.example.treasure.Dao.UserDao;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AnswerActivity extends AppCompatActivity {

    Question q;
    TextView post_name, qu, qcontent, qTime;
    ImageView back;
    EditText ans;
    Button answer;
    User poster; //also current user
    User question_user;
    Post post;
    int lastPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        q=(Question)getIntent().getSerializableExtra("Question");
        lastPage=getIntent().getIntExtra("page",1);
        post_name=findViewById(R.id.post_title); //poster name
        qu=findViewById(R.id.qu);
        qcontent=findViewById(R.id.qcontent);
        qTime=findViewById(R.id.qTime);

        back=findViewById(R.id.Back_from_question);
        ans=findViewById(R.id.answerInput);
        answer=findViewById(R.id.addAns);

        getData();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AnswerActivity.this,QAActivity.class);
                intent.putExtra("post_id",post.getId());
                intent.putExtra("User",poster);
                intent.putExtra("page",lastPage);
                startActivity(intent);
                finish();
            }
        });

        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ans.getText().toString().trim().equals("")){
                    ans.setError("Answer something!");
                }
                else{
                    new Thread(){
                        public void run(){
                            QuestionDao questionDao =new QuestionDao();
                            Boolean rs=questionDao.answerQuestion(q.getId(),ans.getText().toString().trim(),new Date());
                            int msg=0;
                            if(!rs){
                                msg=2;
                            }
                            hand.sendEmptyMessage(msg);
                        }
                    }.start();
                }
            }
        });




    }

    public void getData(){
        new Thread(){
            public void run(){
                PostDao postDao=new PostDao();
                post=postDao.findPost(q.getPost_id());
                UserDao userDao=new UserDao();
                poster=userDao.findUserById(post.getPoster_id());
                question_user=userDao.findUserById(q.getUser_id());
                int msg=1;
                hand.sendEmptyMessage(msg);
            }
        }.start();
    }

    public void initData(){
        post_name.setText(post.getTitle());
        String name;
        if(question_user.getUse_nick()==1){
            name=question_user.getNick_name();
        }
        else{
            name=question_user.getStudent_name();
        }
        qu.setText(name);
        qcontent.setText(q.getQuestion());

        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        qTime.setText(dataFormat.format(q.getQuestion_time()));
    }

    @SuppressLint("HandlerLeak")
    final Handler hand = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.what==1){
                initData();
            }
            else if(msg.what==0){
                Toast.makeText(getApplicationContext(),"Answer successfully!", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(AnswerActivity.this,QAActivity.class);
                intent.putExtra("post_id",post.getId());
                intent.putExtra("User",poster);
                intent.putExtra("page",lastPage);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(getApplicationContext(),"Failed to answer", Toast.LENGTH_LONG).show();
            }

        }
    };

}