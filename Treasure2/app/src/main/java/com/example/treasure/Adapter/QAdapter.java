package com.example.treasure.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.ImageView;

import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.example.treasure.Bean.Question;
import com.example.treasure.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class QAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<Question> questions;
    ArrayList<String> user_name;


    public QAdapter(Context context, List<Question> questions, ArrayList<String> user_name){
        this.layoutInflater=LayoutInflater.from(context);
        this.questions=questions;
        this.user_name=user_name;
    }

    @Override
    public int getCount() {
        return questions==null? 0: questions.size();
    }

    @Override
    public Object getItem(int i) {
        return questions.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            convertView=layoutInflater.inflate(R.layout.qa_row,null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder) convertView.getTag();
        }
        Question question=(Question)getItem(position);

        viewHolder.q.setVisibility(View.VISIBLE);
        viewHolder.q.setImageResource(R.drawable.q);
        viewHolder.qU.setText(user_name.get(position));
        viewHolder.qContent.setText(question.getQuestion());
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        viewHolder.qTime.setText(dataFormat.format(question.getQuestion_time()));

        //No answer
        if(question.getAnswer()==null){
            viewHolder.answer.setVisibility(View.GONE);
        }

        //Have answered
        else{
            viewHolder.answer.setVisibility(View.VISIBLE);
            viewHolder.a.setVisibility(View.VISIBLE);
            viewHolder.a.setImageResource(R.drawable.a);
            viewHolder.aContent.setText(question.getAnswer());
            viewHolder.aTime.setText(dataFormat.format(question.getAnswer_time()));
        }
        return convertView;
    }

    public class ViewHolder{
        TextView qU, qContent, qTime, aContent, aTime;
        RelativeLayout answer;
        ImageView q,a;
        public ViewHolder(@NonNull View itemView){
            qU=itemView.findViewById(R.id.qu);
            qContent=itemView.findViewById(R.id.qcontent);
            qTime=itemView.findViewById(R.id.qTime);
            q=itemView.findViewById(R.id.q);

            answer=itemView.findViewById(R.id.answer);
            aContent=itemView.findViewById(R.id.acontent);
            aTime=itemView.findViewById(R.id.aTime);
            a=itemView.findViewById(R.id.a);



        }



    }
}
