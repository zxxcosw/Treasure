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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.treasure.Bean.User;
import com.example.treasure.Dao.UserDao;


public class InfoActivity extends AppCompatActivity {
    User user;
    EditText ID, Password, nickName, realName;
    RadioGroup Gender, useName;
    RadioButton female, male,nick,real;
    String UserGender;
    int UseName;
    Button save;
    ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        user = (User) getIntent().getSerializableExtra("User");
        ID = findViewById(R.id.Id);
        Password = findViewById(R.id.Password);
        nickName = findViewById(R.id.NickName);
        realName = findViewById(R.id.RealName);
        Gender = findViewById(R.id.rGroup);
        useName=findViewById(R.id.uGroup);
        female = findViewById(R.id.F);
        male = findViewById(R.id.M);
        nick=findViewById(R.id.n);
        real=findViewById(R.id.r);
        save = findViewById(R.id.save);
        back = findViewById(R.id.Back_from_info);

        ID.setText(user.getStudent_id());
        Password.setText(user.getPassword());
        nickName.setText(user.getNick_name());
        realName.setText(user.getStudent_name());

        if (user.getGender() == "Male") {
            male.setChecked(true);
            UserGender = "Male";
        } else {
            female.setChecked(true);
            UserGender = "Female";
        }

        if(user.getUse_nick()==1){
            nick.setChecked(true);
            UseName=1;
        }else{
            real.setChecked(true);
            UseName=0;
        }

        Gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == female.getId()) {
                    UserGender = "Female";
                } else {
                    UserGender = "Male";
                }
            }
        });

        useName.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==nick.getId()){
                    UseName=1;

                }else{
                    UseName=0;
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoActivity.this, MainActivity.class);
                intent.putExtra("User", user);
                intent.putExtra("index", 4);
                startActivity(intent);
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user.setStudent_id(ID.getText().toString().trim());
                user.setPassword(Password.getText().toString().trim());
                user.setStudent_name(realName.getText().toString().trim());
                user.setNick_name(nickName.getText().toString().trim());
                user.setGender(UserGender);
                user.setUse_nick(UseName);
                new Thread() {
                    public void run() {
                        UserDao userDao = new UserDao();
                        boolean rs = userDao.editInfo(user);
                        int msg = 0;
                        if (!rs) {
                            msg = 1;
                        }
                        hand.sendEmptyMessage(msg);

                    }
                }.start();

            }
        });


    }

    @SuppressLint("HandlerLeak")
    final Handler hand = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.what==0){
                Toast.makeText(InfoActivity.this,"Save successfully!", Toast.LENGTH_LONG).show();
            }

        }
    };
}