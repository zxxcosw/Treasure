package com.example.treasure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.treasure.Bean.User;
import com.example.treasure.Fragment.HomeFragment;
import com.example.treasure.Fragment.MessageFragment;
import com.example.treasure.Fragment.MineFragment;
import com.example.treasure.Fragment.OrderFragment;



public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {

    private static final int TIME_EXIT=2000;
    private long mBackPressed;
    private BottomNavigationBar bottom;
    private HomeFragment homeFragment=new HomeFragment();
    private OrderFragment orderFragment=new OrderFragment();
    private MessageFragment messageFragment=new MessageFragment();
    private MineFragment mineFragment=new MineFragment();
    User user;
    int currentId;
    Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user=(User)getIntent().getSerializableExtra("User");
        bundle=new Bundle();
        bundle.putSerializable("User",user);
        currentId=getIntent().getIntExtra("index",0);


        bottom = findViewById(R.id.Bottom);
        initView();

        setFragment();


        bottom.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0:
                        currentId=0;
                        homeFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, homeFragment).commitAllowingStateLoss();
                        break;
                    case 1:
                        currentId=1;
                        orderFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, orderFragment).commitAllowingStateLoss();
                        break;
                    case 2:
                        Intent intent= new Intent(MainActivity.this,AddActivity.class);
                        intent.putExtra("User",user);
                        startActivity(intent);
                        finish();
                        break;
                    case 3:
                        currentId=3;
                        messageFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, messageFragment).commitAllowingStateLoss();
                        break;
                    case 4:
                        currentId=4;
                        mineFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, mineFragment).commitAllowingStateLoss();
                        break;
                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }


        });

    }

    public void setFragment(){
        if(currentId==0){
            homeFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, homeFragment).commitAllowingStateLoss();
        }
        else if(currentId==1){
            orderFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, orderFragment).commitAllowingStateLoss();
        }
        else if(currentId==3){
            messageFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, messageFragment).commitAllowingStateLoss();
        }
        else if(currentId==4){
            mineFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, mineFragment).commitAllowingStateLoss();
        }
    }

    @Override
    public void onBackPressed(){
        if(mBackPressed+TIME_EXIT>System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }else{
            Toast.makeText(this,"Click again to exist app", Toast.LENGTH_SHORT
            ).show();
            mBackPressed=System.currentTimeMillis();
        }
    }




    private void initView() {

        initBottomNavigationBar();
    }

    private void initBottomNavigationBar() {


        bottom.setTabSelectedListener(this);
        bottom.clearAll();
        bottom.setMode(BottomNavigationBar.MODE_FIXED);
        bottom.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_DEFAULT);
        bottom.setBarBackgroundColor(R.color.white)
                .setActiveColor(R.color.orange)
                .setInActiveColor(R.color.black);

        bottom.addItem(new BottomNavigationItem(R.drawable.home_in,"Home").setInactiveIconResource(R.drawable.home))
                .addItem(new BottomNavigationItem(R.drawable.recipe_in,"Order").setInactiveIconResource(R.drawable.recipe))
                .addItem(new BottomNavigationItem(R.drawable.add_in,"Post").setInactiveIconResource(R.drawable.add))
                .addItem(new BottomNavigationItem(R.drawable.message_in,"Msg").setInactiveIconResource(R.drawable.message))
                .addItem(new BottomNavigationItem(R.drawable.me_in,"Mine").setInactiveIconResource(R.drawable.me))
                .setFirstSelectedPosition(currentId)
                .initialise();

    }





    @Override
    public void onTabSelected(int position) {

    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

  //  @Override
  //  protected void onResume() {
   //     super.onResume();
   //     bottom.selectTab(currentId);
  //      setFragment();
 //   }




}