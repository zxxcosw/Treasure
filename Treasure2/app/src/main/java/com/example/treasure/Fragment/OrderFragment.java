package com.example.treasure.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.treasure.Bean.Order;
import com.example.treasure.Bean.Post;
import com.example.treasure.Bean.User;
import com.example.treasure.Dao.OrderDao;
import com.example.treasure.Dao.PostDao;
import com.example.treasure.OrderDetailActivity;
import com.example.treasure.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment implements AdapterView.OnItemClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ListView listView;

    SimpleAdapter simpleAdapter;
    User user;

    public OrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_order, container, false);
        listView=view.findViewById(R.id.order_list);

        user=(User)getArguments().getSerializable("User");

        final Handler hand=new Handler(){
            @SuppressLint("HandlerLeak")
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                if(msg.what==1){
                    List<Map<String,Object>>  data= (List<Map<String, Object>>) msg.obj;
                    simpleAdapter=new SimpleAdapter(getActivity(),data,R.layout.order_row,new String[]{"pic","title","number","state","date","id"},
                            new int[]{R.id.main_pic,R.id.main_title,R.id.num,R.id.state,R.id.main_date,R.id.main_id});

                    simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {

                        @Override
                        public boolean setViewValue(View view, Object data,
                                                    String textRepresentation) {
                            if (view instanceof ImageView && data instanceof Bitmap) {
                                ImageView iv = (ImageView) view;
                                iv.setImageBitmap((Bitmap) data);
                                return true;
                            }
                            return false;
                        }
                    });
                    listView.setAdapter(simpleAdapter);}

            }
        };


        Thread thread=new Thread(new Runnable() {
            List<Map<String,Object>> data=new ArrayList<>();

            @Override
            public void run() {
                OrderDao orderDao=new OrderDao();
                List<Order> list = new ArrayList<>();
                list=orderDao.selectOrderByUser(user.getId());
                for (int i = 0; i < list.size(); i++) {
                    Map map = new HashMap<String,Object>();
                    Order order=list.get(i);
                    PostDao postDao=new PostDao();
                    Post post=postDao.findPost(order.getPost_id());
                    map.put("pic",getPicFromBytes(post.getImg(),null));
                    map.put("title",post.getTitle());
                    map.put("number","Number: "+order.getNumber());
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
                    map.put("state",state);
                    SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    map.put("date",dft.format(order.getOrder_time()));
                    map.put("id",order.getId());
                    data.add(map);
                }
                Message message=Message.obtain();
                message.what=1;
                message.obj=data;
                hand.sendMessage(message);

            }
        });
        thread.start();
        listView.setOnItemClickListener(this);
        return view;
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView main_id=view.findViewById(R.id.main_id);
        int order_id=Integer.parseInt(main_id.getText().toString().trim());
        Intent intent=new Intent(getActivity(), OrderDetailActivity.class);
        intent.putExtra("order_id",order_id);
        intent.putExtra("User",user);
        startActivity(intent);
        getActivity().finish();
    }
}