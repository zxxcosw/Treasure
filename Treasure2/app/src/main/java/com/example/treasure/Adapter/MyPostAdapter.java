package com.example.treasure.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.treasure.Bean.Post;
import com.example.treasure.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyPostAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<Post> posts;
    ArrayList<String> user_name;

    public MyPostAdapter(Context context, List<Post> posts, ArrayList<String> user_name){
        this.layoutInflater=LayoutInflater.from(context);
        this.posts=posts;
        this.user_name=user_name;
    }

    @Override
    public int getCount() {
        return posts==null? 0: posts.size();
    }

    @Override
    public Object getItem(int position) {
        return posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyPostAdapter.ViewHolder viewHolder;
        if (convertView==null){
            convertView=layoutInflater.inflate(R.layout.main_row,null);
            viewHolder=new MyPostAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder=(MyPostAdapter.ViewHolder) convertView.getTag();
        }

        Post post=posts.get(position);

        viewHolder.title.setText(post.getTitle());
        viewHolder.poster.setText(user_name.get(position));
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
        viewHolder.date.setText(dataFormat.format(post.getPost_time()));

        if(post.getState()==1){
            viewHolder.duration.setText("Closed");
        }else{
            int left_date=differentDays(post.getPost_time(),post.getEnd_time());
            String leftDate;
            if(left_date<=1){
                leftDate=left_date+" day left";
            }
            else{
                leftDate=left_date+" days left";
            }
            viewHolder.duration.setText(leftDate);
        }

        viewHolder.img.setImageBitmap((Bitmap)getPicFromBytes(post.getImg(),null));
        return convertView;
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

    private static int differentDays(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1= cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if(year1 != year2) {
            int timeDistance = 0 ;
            for(int i = year1 ; i < year2 ; i ++)
            {
                if(i%4==0 && i%100!=0 || i%400==0)
                {
                    timeDistance += 366;
                }
                else
                {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2-day1) ;
        } else {
            //System.out.println("day2 - day1 : " + (day2-day1));
            return day2-day1;
        }
    }

    public class ViewHolder {
        TextView title,poster,duration,date;
        ImageView img;

        public ViewHolder(@NonNull View itemView){
            title=itemView.findViewById(R.id.main_title);
            poster=itemView.findViewById(R.id.main_user);
            duration=itemView.findViewById(R.id.main_leftDate);
            date=itemView.findViewById(R.id.main_date);
            img=itemView.findViewById(R.id.main_pic);

        }
    }
}
