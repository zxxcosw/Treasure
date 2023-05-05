package com.example.treasure;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.treasure.Adapter.ChatAdapter;
import com.example.treasure.Bean.Msg;

import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.treasure.Bean.User;
import com.example.treasure.Dao.MessageDao;
import com.example.treasure.Dao.UserDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    ImageView back;
    TextView chatName;
    ListView listView;
    User user,chatUser;
    int chatId;
    List<Msg> list=new ArrayList<Msg>();
    ChatAdapter chatAdapter;
    EditText text;
    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        user=(User)getIntent().getSerializableExtra("User");
        chatId=getIntent().getIntExtra("chatId",0);
        listView=findViewById(R.id.list);

        back=findViewById(R.id.Back);
        chatName=findViewById(R.id.chatName);
        text=findViewById(R.id.Input);
        send=findViewById(R.id.send);

        getData();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChatActivity.this, MainActivity.class);
                intent.putExtra("User",user);
                intent.putExtra("index",3);
                startActivity(intent);
                finish();

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    public void run(){
                        Msg msg=new Msg();
                        msg.setSender_id(user.getId());
                        msg.setReceiver_id(chatId);
                        msg.setText(text.getText().toString());
                        msg.setTime(new Date());
                        MessageDao messageDao=new MessageDao();
                        Boolean rs=messageDao.sendMessage(msg);
                        if(messageDao.findChat(user.getId(),chatUser.getId())==0){

                            messageDao.addChat(user.getId(), chatUser.getId());
                        }
                        int m=0;
                        if(!rs){
                            m=2;
                        }
                        hand.sendEmptyMessage(m);
                    }
                }.start();
            }
        });


    }

    public void initData(){
        String name;
        if(chatUser.getUse_nick()==1){
            name=chatUser.getNick_name();
        }else{
            name=chatUser.getStudent_name();
        }
        chatName.setText(name);
        String myName;
        if(user.getUse_nick()==1){
            myName=user.getNick_name();
        }
        else{
            myName=user.getStudent_name();
        }
        chatAdapter=new ChatAdapter(getApplicationContext(),list,name,myName,user.getId());
        listView.setAdapter(chatAdapter);




    }

    public void getData(){
        new Thread(){
            public void run(){
                MessageDao messageDao=new MessageDao();
                list=messageDao.selectMessageByUsersId(user.getId(),chatId);
                UserDao userDao=new UserDao();
                chatUser=userDao.findUserById(chatId);
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
            }else if(msg.what==0){
                getData();
                text.setText("");
            }


        }
    };


}