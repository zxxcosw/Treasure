package com.example.treasure;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.treasure.Bean.User;
import com.example.treasure.Dao.UserDao;



public class LoginActivity extends AppCompatActivity {
    EditText UserId, Password;
    Button login, register;

    private ImageView show,delete;
    private Boolean isPswVisible = false;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        UserId=findViewById(R.id.LoginId);
        Password=findViewById(R.id.LoginPassword);
        login=findViewById(R.id.login);
        register=findViewById(R.id.register);
        show = findViewById(R.id.LoginShow);
        delete=findViewById(R.id.LoginDelete);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UserId.getText().toString().trim().equals("")){
                    UserId.setError("Null Student ID!");
                }
                else if(Password.getText().toString().trim().equals("")){
                    Password.setError("Null password!");
                }
                else{
                    new Thread(){
                        @Override
                        public void run() {
                            UserDao userDao = new UserDao();
                            user=userDao.findUser(UserId.getText().toString().trim());
                            int msg = userDao.login(UserId.getText().toString().trim(),Password.getText().toString().trim());
                            hand1.sendEmptyMessage(msg);
                        }
                    }.start();
                }


            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPswVisible();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserId.setText("");
            }
        });
    }

    private void setPswVisible() {
        isPswVisible = !isPswVisible;
        if (isPswVisible){
            show.setImageResource(R.drawable.eyeopen);
            HideReturnsTransformationMethod method = HideReturnsTransformationMethod.getInstance();
            Password.setTransformationMethod(method);
        }else {
            show.setImageResource(R.drawable.eyeclose);
            PasswordTransformationMethod method = PasswordTransformationMethod.getInstance();
            Password.setTransformationMethod(method);
        }
    }


    @SuppressLint("HandlerLeak")
    final Handler hand1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0){
                Toast.makeText(getApplicationContext(), "Failed to login", Toast.LENGTH_LONG).show();
            } else if (msg.what == 1) {
                Toast.makeText(getApplicationContext(), "Login successfully", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                intent.putExtra("User", user);
                intent.putExtra("index",0);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else if (msg.what == 2){
                Toast.makeText(getApplicationContext(), "Wrong password", Toast.LENGTH_LONG).show();
            } else if (msg.what == 3){
                Toast.makeText(getApplicationContext(), "This account does not exist", Toast.LENGTH_LONG).show();
            }
        }
    };
}