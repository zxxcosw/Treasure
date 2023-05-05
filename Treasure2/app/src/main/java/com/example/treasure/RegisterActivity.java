package com.example.treasure;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.treasure.Bean.User;
import com.example.treasure.Dao.UserDao;

public class RegisterActivity extends AppCompatActivity {

    EditText ID, Password, nickName, realName;
    Button register;

    RadioGroup Gender;
    RadioButton female,male;
    String UserGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ID=findViewById(R.id.RegisterId);
        Password=findViewById(R.id.RegisterPassword);
        nickName=findViewById(R.id.RegisterNickName);
        realName=findViewById(R.id.RegisterRealName);
        register=findViewById(R.id.Register);

        Gender = findViewById(R.id.rGroup);
        female = findViewById(R.id.F);
        male = findViewById(R.id.M);

        male.setChecked(true);
        UserGender="Male";

        Gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==female.getId()){
                    UserGender="Female";
                }else{
                    UserGender="Male";
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ID.getText().toString().trim().equals("")){
                    ID.setError("Student ID should not be null!");
                }else if(Password.getText().toString().trim().equals("")){
                    Password.setError("Password should not be null!");
                }else if(realName.getText().toString().trim().equals("")){
                    realName.setError("Real name should not be null!");
                }
                else{
                    User user = new User();
                    user.setStudent_id(ID.getText().toString().trim());
                    user.setPassword(Password.getText().toString().trim());
                    if (nickName.getText().toString().trim().equals("")){
                        user.setNick_name("Student");
                    }
                    else{
                        user.setNick_name(nickName.getText().toString().trim());
                    }
                    user.setStudent_name(realName.getText().toString().trim());
                    user.setGender(UserGender);

                    new Thread(){
                        @Override
                        public void run() {

                            int msg = 0;

                            UserDao userDao = new UserDao();

                            User uu = userDao.findUser(user.getStudent_id());
                            if(uu != null){
                                msg = 1;
                            }
                            else{
                                boolean flag = userDao.register(user);
                                if(flag){
                                    msg = 2;
                                }
                            }
                            hand.sendEmptyMessage(msg);

                        }
                    }.start();

                }


            }
        });
    }

    @SuppressLint("HandlerLeak")
    final Handler hand = new Handler()
    {
        public void handleMessage(Message msg) {
            if(msg.what == 0) {
                Toast.makeText(getApplicationContext(),"Failed to register", Toast.LENGTH_LONG).show();
            } else if(msg.what == 1) {
                Toast.makeText(getApplicationContext(),"This student ID has been registered",Toast.LENGTH_LONG).show();
            } else if(msg.what == 2) {
                Toast.makeText(getApplicationContext(), "Register successfully", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    };

}