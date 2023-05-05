package com.example.treasure.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.treasure.Bean.Post;
import com.example.treasure.R;

import java.util.List;

public class PostsOderAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<Post> posts;

    public PostsOderAdapter(Context context, List<Post> posts ) {
        this.layoutInflater=LayoutInflater.from(context);
        this.posts=posts;
    }

    @Override
    public int getCount() {
        return posts.size();
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
        PostsOderAdapter.ViewHolder viewHolder;
        if (convertView==null){
            convertView=layoutInflater.inflate(R.layout.post_order_row,null);
            viewHolder=new PostsOderAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder=(PostsOderAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(posts.get(position).getTitle());


        return convertView;
    }

    public class ViewHolder {
        TextView title;

        public ViewHolder(@NonNull View itemView){
            title=itemView.findViewById(R.id.title);
        }
    }
}
