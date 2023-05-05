package com.example.treasure;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class EditPostActivity extends AppCompatActivity {
    ImageView back,postImg;
    TextView title, content, end_date, amount,price, o_price;
    Button save;
    Post post;
    int post_id;
    int lastPage;
    User user;
    byte[] img;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        post_id=getIntent().getIntExtra("post_id",0);
        user=(User)getIntent().getSerializableExtra("User");
        lastPage=getIntent().getIntExtra("page",1);

        back=findViewById(R.id.Back_from_editPost);
        postImg=findViewById(R.id.Edit_image);
        title=findViewById(R.id.Edit_postTitle);
        content=findViewById(R.id.Edit_content);
        end_date=findViewById(R.id.Edit_End_date);
        amount=findViewById(R.id.Edit_Amount);
        price=findViewById(R.id.price);
        o_price=findViewById(R.id.oPrice);
        save=findViewById(R.id.Save_Post);


        new Thread(){
            public void run(){
                PostDao postDao= new PostDao();
                post=postDao.findPost(post_id);
                int msg=2;
                hand.sendEmptyMessage(msg);
            }
        }.start();



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EditPostActivity.this, PostActivity.class);
                intent.putExtra("User",user);
                intent.putExtra("post_id",post.getId());
                intent.putExtra("page",lastPage);
                startActivity(intent);
                finish();
            }
        });

        end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();

                DatePickerDialog dpd = new DatePickerDialog( EditPostActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String text = "You choose：" + year + "-" + (month + 1) + "-" + dayOfMonth;
                        Toast.makeText( EditPostActivity.this, text, Toast.LENGTH_SHORT ).show();
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

        postImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 2);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
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
                    post.setTitle(title.getText().toString().trim());
                    post.setContent(content.getText().toString().trim());
                    post.setAmount(Integer.parseInt(amount.getText().toString().trim()));
                    post.setPrice(price.getText().toString().trim());
                    post.setO_price(o_price.getText().toString().trim());
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
                            boolean result=postDao.editPost(post);
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


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            //Log.e(this.getClass().getName(), "Result:" + data.toString());

            if (data != null) {
                Uri uri = data.getData();
                img=getBytesFromUri(uri);
                postImg.setImageURI(uri);
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

    public static Bitmap getPicFromBytes(byte[] bytes, BitmapFactory.Options opts) {
        if (bytes != null)
            if (opts != null)
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
                        opts);
            else
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;
    }

    public void initData(){
        title.setText(post.getTitle());
        content.setText(post.getContent());
        end_date.setText(sdf.format(post.getEnd_time()));
        amount.setText(String.valueOf(post.getAmount()));
        postImg.setImageBitmap((Bitmap)getPicFromBytes(post.getImg(),null));
        img=post.getImg();
        price.setText(post.getPrice());
        o_price.setText(post.getO_price());
    }

    @SuppressLint("HandlerLeak")
    final Handler hand = new Handler()
    {
        public void handleMessage(Message msg) {
            if(msg.what==0){
                Toast.makeText(getApplicationContext(),"Edit successfully!", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(EditPostActivity.this, PostActivity.class);
                intent.putExtra("User",user);
                intent.putExtra("post_id",post.getId());
                intent.putExtra("page",lastPage);
                startActivity(intent);
                finish();
            }
            else if(msg.what==2){
                initData();
            }
            else{
                Toast.makeText(getApplicationContext(),"Failed to edit!", Toast.LENGTH_LONG).show();
            }
        }
    };
}