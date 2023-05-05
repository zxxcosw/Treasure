package com.example.treasure.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.treasure.Bean.Msg;
import com.example.treasure.Bean.User;
import com.example.treasure.ChatActivity;
import com.example.treasure.Dao.MessageDao;
import com.example.treasure.Dao.UserDao;
import com.example.treasure.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageFragment extends Fragment implements AdapterView.OnItemClickListener {

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

    public MessageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessageFragment newInstance(String param1, String param2) {
        MessageFragment fragment = new MessageFragment();
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
        View view=inflater.inflate(R.layout.fragment_message, container, false);
        listView=view.findViewById(R.id.list);
        // Inflate the layout for this fragment
        user=(User)getArguments().getSerializable("User");

        final Handler hand=new Handler() {
            @SuppressLint("HandlerLeak")
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    List<Map<String,Object>>  data= (List<Map<String, Object>>) msg.obj;
                    simpleAdapter=new SimpleAdapter(getActivity(),data,R.layout.message_row,new String[]{"name","text","time","chatId"},
                            new int[]{R.id.Name,R.id.text,R.id.time,R.id.id});
                    listView.setAdapter(simpleAdapter);
                }
            }
        };

        Thread thread=new Thread(new Runnable() {
            List<Map<String,Object>> data=new ArrayList<>();

            @Override
            public void run() {
                MessageDao messageDao=new MessageDao();
                List<Msg> list = new ArrayList<>();
                list=messageDao.selectMessageByUserId(user.getId());


                for (int i = 0; i < list.size(); i++) {
                    Map map = new HashMap<String,Object>();
                    Msg msg=list.get(i);
                    UserDao userDao=new UserDao();
                    String name;
                    User chatU;

                    if(user.getId()==msg.getReceiver_id()){
                        chatU=userDao.findUserById(msg.getSender_id());

                    }else{
                        chatU=userDao.findUserById(msg.getReceiver_id());

                    }


                    if(chatU.getUse_nick()==1){
                        name=chatU.getNick_name();
                    }
                    else{
                        name=chatU.getStudent_name();
                    }

                    map.put("name",name) ;
                    map.put("text",msg.getText());
                    SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    map.put("time",dft.format(msg.getTime()));
                    map.put("chatId",chatU.getId());
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView chatid=view.findViewById(R.id.id);
        int chat_id=Integer.parseInt(chatid.getText().toString());
        Intent intent=new Intent(getActivity(), ChatActivity.class);
        intent.putExtra("User",user);
        intent.putExtra("chatId",chat_id);
        startActivity(intent);
        getActivity().finish();
    }
}