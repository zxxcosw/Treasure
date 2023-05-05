package com.example.treasure;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.treasure.Adapter.QAdapter;
import com.example.treasure.Bean.Post;
import com.example.treasure.Bean.Question;
import com.example.treasure.Bean.User;
import com.example.treasure.Dao.PostDao;
import com.example.treasure.Dao.QuestionDao;
import com.example.treasure.Dao.UserDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QAActivity extends AppCompatActivity {

    User user,poster;
    Post post;
    ListView questionList;
    int post_id,lastPage;

    Boolean isPoster;
    List<Question> questions;
    ArrayList<String> user_name; //questions users' names
    QAdapter qAdapter;
    Button ask;

    TextView poste_name;
    ImageView back;
    EditText question;
    RelativeLayout Bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qaactivity);

        post_id=getIntent().getIntExtra("post_id",0);
        user=(User)getIntent().getSerializableExtra("User");
        lastPage=getIntent().getIntExtra("page",1);

        questionList=findViewById(R.id.QA_list);

        question=findViewById(R.id.post_q);
        ask=findViewById(R.id.post_ask);

        back=findViewById(R.id.Back_from_qa);

        poste_name=findViewById(R.id.post_title);
        Bottom=findViewById(R.id.post_bottom);

        getData();



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    public void getData(){
        new Thread(){
            public void run(){
                PostDao postDao= new PostDao();
                post=postDao.findPost(post_id);

                QuestionDao questionDao=new QuestionDao();
                try {
                    questions=new ArrayList<>();
                    questions=questionDao.selectQuestionByPost(post_id);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                UserDao userDao=new UserDao();
                poster=userDao.findUserById(post.getPoster_id());
                user_name=new ArrayList<>();
                for(int i=0;i<questions.size();i++){
                    User quser=userDao.findUserById(questions.get(i).getUser_id());
                    String name;
                    if(quser.getUse_nick()==1){
                        name=quser.getNick_name();
                    }else{
                        name=quser.getStudent_name();
                    }
                    user_name.add(name);
                }
                int msg=1;
                hand.sendEmptyMessage(msg);
            }
        }.start();


    }

    public void initData(){
        poste_name.setText(post.getTitle());
        if(user.getId()!=poster.getId()){
            Bottom.setVisibility(View.VISIBLE);
            ask.setVisibility(View.VISIBLE);
            question.setVisibility(View.VISIBLE);

            ask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(question.getText().toString().trim().equals("")){
                        question.setError("Ask some questions here!");
                    }
                    else{
                        Question q=new Question();
                        q.setPost_id(post.getId());
                        q.setUser_id(user.getId());
                        q.setQuestion(question.getText().toString().trim());
                        q.setQuestion_time(new Date());
                        new Thread(){
                            public void run(){
                                QuestionDao questionDao=new QuestionDao();
                                boolean result=questionDao.addQuestion(q);
                                int msg=0;
                                if(!result){
                                    msg=2;
                                }
                                hand.sendEmptyMessage(msg);

                            }
                        }.start();

                    }

                }
            });
        }
        else{
            Bottom.setVisibility(View.GONE);
            ask.setVisibility(View.GONE);
            question.setVisibility(View.GONE);
        }

    }

    @SuppressLint("HandlerLeak")
    final Handler hand = new Handler()
    {
        public void handleMessage(Message msg) {
            if(msg.what==1){
                initData();
                isPoster=post.getPoster_id()==user.getId();

                //Show question list
                qAdapter=new QAdapter(getApplicationContext(),questions,user_name);
                questionList.setAdapter(qAdapter);
                questionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Question q=questions.get(position);
                        if(isPoster&&q.getAnswer()==null&&post.getState()!=1){
                            Intent intent=new Intent(QAActivity.this, AnswerActivity.class);
                            intent.putExtra("Question",q);
                            intent.putExtra("page",lastPage);
                            startActivity(intent);
                            finish();
                        }
                        else{

                        }
                    }
                });


            }else if(msg.what==0){
                Toast.makeText(getApplicationContext(),"Ask successfully!", Toast.LENGTH_LONG).show();
                getData();
                question.setText("");

            }
            else{
                Toast.makeText(getApplicationContext(),"Failed to ask!", Toast.LENGTH_LONG).show();
            }

        }
    };


}