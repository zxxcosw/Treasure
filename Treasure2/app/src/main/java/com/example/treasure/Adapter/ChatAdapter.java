package com.example.treasure.Adapter;

import com.example.treasure.Bean.Msg;
import com.example.treasure.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.List;

public class ChatAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<Msg> msgs;
    private String chatName;
    private String userName;
    private int userId;

    public ChatAdapter(Context context, List<Msg> msgs, String chatName, String userName, int userId){
        this.layoutInflater=LayoutInflater.from(context);
        this.msgs = msgs;
        this.chatName=chatName;
        this.userName=userName;
        this.userId=userId;
    }
    @Override
    public int getCount() {
        return msgs.size();
    }

    @Override
    public Object getItem(int position) {
        return msgs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatAdapter.ViewHolder viewHolder;
        if (convertView==null){
            convertView=layoutInflater.inflate(R.layout.chat_row,null);
            viewHolder=new ChatAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder=(ChatAdapter.ViewHolder) convertView.getTag();
        }

        Msg msg= msgs.get(position);
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(msg.getSender_id()==userId) {
            viewHolder.user1.setVisibility(View.GONE);
            viewHolder.name1.setVisibility(View.GONE);
            viewHolder.text1.setVisibility(View.GONE);
            viewHolder.time1.setVisibility(View.GONE);

            viewHolder.user2.setVisibility(View.VISIBLE);
            viewHolder.name2.setVisibility(View.VISIBLE);
            viewHolder.text2.setVisibility(View.VISIBLE);
            viewHolder.time2.setVisibility(View.VISIBLE);

            viewHolder.name2.setText(userName);
            viewHolder.text2.setText(msg.getText());
            viewHolder.time2.setText(dataFormat.format(msg.getTime()));
        }
        else{
            viewHolder.user2.setVisibility(View.GONE);
            viewHolder.name2.setVisibility(View.GONE);
            viewHolder.text2.setVisibility(View.GONE);
            viewHolder.time2.setVisibility(View.GONE);

            viewHolder.user1.setVisibility(View.VISIBLE);
            viewHolder.name1.setVisibility(View.VISIBLE);
            viewHolder.text1.setVisibility(View.VISIBLE);
            viewHolder.time1.setVisibility(View.VISIBLE);

            viewHolder.name1.setText(chatName);
            viewHolder.text1.setText(msg.getText());
            viewHolder.time1.setText(dataFormat.format(msg.getTime()));
        }

        return convertView;
    }

    public class ViewHolder{
        RelativeLayout user1,user2;
        TextView name1,name2,text1,text2,time1,time2;

        public ViewHolder(@NonNull View itemView){
            user1=itemView.findViewById(R.id.user1);
            user2=itemView.findViewById(R.id.user2);
            name1=itemView.findViewById(R.id.name1);
            name2=itemView.findViewById(R.id.name2);
            text1=itemView.findViewById(R.id.text1);
            text2=itemView.findViewById(R.id.text2);
            time1=itemView.findViewById(R.id.time1);
            time2=itemView.findViewById(R.id.time2);

        }
    }
}
