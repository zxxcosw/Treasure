package com.example.treasure.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.treasure.Bean.Order;

import com.example.treasure.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderReceivedAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    List<Order> orders;
    ArrayList<String> names; //orderer name

    public OrderReceivedAdapter(Context context, List<Order> orders, ArrayList<String> names){
        this.layoutInflater=LayoutInflater.from(context);
        this.orders=orders;
        this.names=names;
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int position) {
        return orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderReceivedAdapter.ViewHolder viewHolder;
        if (convertView==null){
            convertView=layoutInflater.inflate(R.layout.order_received_row,null);
            viewHolder=new OrderReceivedAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder=(OrderReceivedAdapter.ViewHolder) convertView.getTag();
        }
        Order order=orders.get(position);
        viewHolder.name.setText("Ordered by: "+ names.get(position));
        viewHolder.number.setText("Number: "+order.getNumber());
        String state;
        int status=order.getState();
        if(status==0){
            state="Ordered";
        }
        else if(status==1){
            state="Completed";
        }
        else{
            state="Cancelled";
        }
        viewHolder.state.setText(state);
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        viewHolder.date.setText(dft.format(order.getOrder_time()));
        return convertView;


    }

    public class ViewHolder{
        TextView name,number,state,date;
        public ViewHolder(@NonNull View itemView){
            name=itemView.findViewById(R.id.orderer);
            number=itemView.findViewById(R.id.num);
            state=itemView.findViewById(R.id.state);
            date=itemView.findViewById(R.id.main_date);
        }
    }
}
