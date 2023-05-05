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

import com.example.treasure.Bean.Post;
import com.example.treasure.Bean.User;
import com.example.treasure.Dao.PostDao;
import com.example.treasure.Dao.UserDao;
import com.example.treasure.PostActivity;
import com.example.treasure.R;
import com.example.treasure.SearchActivity;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener{

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



    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        listView=view.findViewById(R.id.home_list);


        user=(User)getArguments().getSerializable("User");

        ImageView search=view.findViewById(R.id.Home_Search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("User",user);
                startActivity(intent);
                getActivity().finish();
            }
        });


        // Realize ListView
        final Handler hand=new Handler(){
            @SuppressLint("HandlerLeak")
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                if(msg.what==1){
                    List<Map<String,Object>>  data= (List<Map<String, Object>>) msg.obj;
                    simpleAdapter=new SimpleAdapter(getActivity(),data,R.layout.main_row,new String[]{"pic","title","user","leftDate","post_date","id"},
                            new int[]{R.id.main_pic,R.id.main_title,R.id.main_user,R.id.main_leftDate,R.id.main_date,R.id.main_id});

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

        Thread thread=new Thread(new Runnable(){
            List<Map<String,Object>> data=new ArrayList<>();
            @Override
            public void run() {
                PostDao postDao = new PostDao();
                List<Post> list = new ArrayList<>();
                try {
                    list = postDao.selectMainPost();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < list.size(); i++) {
                    Map map = new HashMap<String,Object>();
                    Post post = list.get(i);

                    map.put("pic", getPicFromBytes(post.getImg(),null));
                    map.put("title", post.getTitle());


                    UserDao userDao=new UserDao();
                    User poster=userDao.findUserById(post.getPoster_id());
                    String name;
                    if(poster.getUse_nick()==1){
                        name=poster.getNick_name();
                    }else{
                        name=poster.getStudent_name();
                    }
                    map.put("user", name);


                    int left_date=differentDays(post.getPost_time(),post.getEnd_time());
                    String leftDate;
                    if(left_date<=1){
                        leftDate=left_date+" day left";
                    }
                    else{
                        leftDate=left_date+" days left";
                    }
                    map.put("leftDate", leftDate);

                    SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
                    map.put("post_date",dft.format(post.getPost_time()));
                    map.put("id",post.getId());

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


    private static int differentDays(Date date1,Date date2) {
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
        int post_id=Integer.parseInt(main_id.getText().toString().trim());
        Intent intent=new Intent(getActivity(), PostActivity.class);
        intent.putExtra("post_id",post_id);
        intent.putExtra("User",user);
        intent.putExtra("page",1);
        startActivity(intent);
        getActivity().finish();

    }




}