package com.example.eatwhat11;

import com.example.eatwhat11.Adapter.FoodPagerAdapter;
import com.example.eatwhat11.Database.FoodDBHelper;
import com.example.eatwhat11.bean.Food;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AlertDialog;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by huilongyeo on 17/5/2020
 */
public class MainActivity extends AppCompatActivity implements OnClickListener{
    public static String ACTION_FRAGMENT_SELECTED = "com.example.eatwhat11.ACTION_FRAGMENT_SELECTED";
    public static String EXTRA_FOOD_TYPE = "food_type";
    private ViewPager vp_food;
    private int mFoodType = 0;
    private EditText et_add;
    private FoodDBHelper mHelper;
    private PagerTabStrip pts_food;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_add = findViewById(R.id.et_add);
        findViewById(R.id.btn_add).setOnClickListener(this);
        findViewById(R.id.btn_whatever).setOnClickListener(this);
        pts_food = findViewById(R.id.pts_food);
        vp_food = findViewById(R.id.vp_food);
        FoodPagerAdapter adapter = new FoodPagerAdapter(getSupportFragmentManager());
        vp_food.setAdapter(adapter);
        vp_food.setCurrentItem(0);
        vp_food.addOnPageChangeListener(new FoodChangedListener());
    }

    @Override
    protected void onResume(){
        super.onResume();
        mHelper = FoodDBHelper.getInstance(this,1);
        mHelper.openReadLink();
        sendBroadcast(mFoodType);
    }

    @Override
    protected void onPause(){
        super.onPause();
        mHelper.closeLink();
    }

    private Handler mHandler = new Handler();

    private Runnable mFirst = new Runnable() {
        @Override
        public void run() {
            sendBroadcast(mFoodType);
        }
    };

    private void sendBroadcast(int foodType){
        Intent intent = new Intent(ACTION_FRAGMENT_SELECTED);
        intent.putExtra(EXTRA_FOOD_TYPE, foodType);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_add){
            String name = et_add.getText().toString();
            if(TextUtils.isEmpty(name)){
                Toast.makeText(this, "请输入食物名称",Toast.LENGTH_SHORT).show();
                return;
            }
            Food info = new Food();
            info.food_name = name;
            info.food_type = mFoodType;
            et_add.setText("");
            mHelper.openWriteLink();//不知为何，需要在此处读取写连接，否则滑动两个页面后会出现错误
            mHelper.insert(info);
            mHelper.closeLink();
            vp_food = findViewById(R.id.vp_food);//刷新Pager碎片
            FoodPagerAdapter adapter = new FoodPagerAdapter(getSupportFragmentManager());
            vp_food.setAdapter(adapter);
            vp_food.setCurrentItem(mFoodType);
            vp_food.addOnPageChangeListener(new FoodChangedListener());
            sendBroadcast(mFoodType);
        }else if(v.getId() == R.id.btn_whatever){
            mHelper.openReadLink();//此处也需要打开都连接
            ArrayList<Food> infoArray = mHelper.query("type=" + mFoodType);
            if(infoArray.size()>0){
                int random = (int)(Math.random() * 10) % infoArray.size();
                try{
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    final String food_name = infoArray.get(random).food_name;
                    builder.setTitle("帮你决定");
                    builder.setMessage(String.format("%s吃%s吗",foodTitle[mFoodType],food_name));
                    builder.setPositiveButton("就吃这个", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            RelativeLayout rl_food = findViewById(R.id.rl_food);
                            rl_food.setVisibility(View.GONE);
                            TextView tv_food = findViewById(R.id.tv_food);
                            tv_food.setVisibility(View.VISIBLE);
                            tv_food.setText(food_name);
                        }
                    });
                    builder.setNegativeButton("我再想想", null);
                    AlertDialog alert = builder.create();
                    alert.show();
                }catch(ArrayIndexOutOfBoundsException e){
                    Toast.makeText(this,"Error:" + e, Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "没有" +foodTitle[mFoodType],Toast.LENGTH_SHORT).show();
            }
            mHelper.closeLink();
        }
    }

    private String[] foodTitle = {"早餐", "午餐", "晚餐"};

    public class FoodChangedListener implements OnPageChangeListener{

        public void onPageSelected(int position){
            mFoodType = position;
            sendBroadcast(mFoodType);
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
        public void onPageScrollStateChanged(int arg0) {}
    }
}
