package com.example.treasure;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.treasure.Bean.Post;
import com.example.treasure.Bean.User;
import com.example.treasure.Dao.PostDao;


import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class AddActivity extends AppCompatActivity {
    TextView title, content, end_date, amount, price, o_price;
    Button post;
    ImageView back,add_image;
    User user;
    byte[] img;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        title=findViewById(R.id.Add_postTitle);
        content=findViewById(R.id.Add_content);
        end_date=findViewById(R.id.Add_End_date);
        amount=findViewById(R.id.Add_Amount);
        price=findViewById(R.id.price);
        o_price=findViewById(R.id.oPrice);

        back=findViewById(R.id.Back_from_add);
        add_image=findViewById(R.id.add_image);

        post=findViewById(R.id.Add_Post);

        user=(User)getIntent().getSerializableExtra("User");
        img=null;



        end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();

                DatePickerDialog dpd = new DatePickerDialog( AddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String text = "You choose：" + year + "-" + (month + 1) + "-" + dayOfMonth;
                        Toast.makeText( AddActivity.this, text, Toast.LENGTH_SHORT ).show();
                        end_date.setText(String.format("%d-%d-%d",year,month+1,dayOfMonth));

                    }
                }
                        ,calendar.get(Calendar.YEAR)
                        ,calendar.get(Calendar.MONTH)
                        ,calendar.get(Calendar.DAY_OF_MONTH));
                dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dpd.show();
            }
        });

        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);

                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 2);
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.getText().toString().trim().equals("")){
                    title.setError("Title should not be null!");
                }else if(end_date.getText().toString().trim().equals("")){
                    end_date.setError("Expiration date should be given!");
                }else if(amount.getText().toString().trim().equals("")){
                    amount.setError("Number should be given!");
                }else if(price.getText().toString().trim().equals("")){
                    price.setError("Price should be given!");
                }else if(o_price.getText().toString().trim().equals("")){
                    o_price.setError("Original price should be given!");
                }else{
                    Post post=new Post();
                    post.setPoster_id(user.getId());
                    post.setTitle(title.getText().toString().trim());
                    post.setContent(content.getText().toString().trim());
                    post.setAmount(Integer.parseInt(amount.getText().toString().trim()));
                    post.setState(0);
                    post.setPrice(price.getText().toString().trim());
                    post.setO_price(o_price.getText().toString().trim());
                    post.setPost_time(new Date());
                    try {
                        post.setEnd_time(sdf.parse(end_date.getText().toString().trim()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(img==null){
                        Drawable pic=getDrawable(R.drawable.food);
                        Bitmap bitmap = ((BitmapDrawable)pic).getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        post.setImg(stream.toByteArray());
                    }
                    else{
                        post.setImg(img);
                    }

                    new Thread(){
                        public void run(){
                            PostDao postDao=new PostDao();
                            boolean result=postDao.addPost(post);
                            int msg=0;
                            if (!result){
                                msg=1;
                            }
                            hand.sendEmptyMessage(msg);
                        }

                    }.start();

                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddActivity.this, MainActivity.class);
                intent.putExtra("User",user);
                intent.putExtra("index",0);
                startActivity(intent);
                finish();
            }
        });

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            Log.e(this.getClass().getName(), "Result:" + data.toString());

            if (data != null) {
                Uri uri = data.getData();
                img=getBytesFromUri(uri);
                add_image.setImageURI(uri);
                Log.e(this.getClass().getName(), "Uri:" + String.valueOf(uri));

            }
        }
    }

    private byte[] getBytesFromUri(Uri uri){
        try
        {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            return baos.toByteArray();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }






    @SuppressLint("HandlerLeak")
    final Handler hand = new Handler()
    {
        public void handleMessage(Message msg) {
            if(msg.what==0){
                Toast.makeText(getApplicationContext(),"Post successfully!", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(AddActivity.this, MainActivity.class);
                intent.putExtra("User",user);
                intent.putExtra("index",0);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(getApplicationContext(),"Failed to post!", Toast.LENGTH_LONG).show();
            }
        }
    };
}