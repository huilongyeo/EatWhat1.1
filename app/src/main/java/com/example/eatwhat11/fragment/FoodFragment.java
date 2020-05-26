package com.example.eatwhat11.fragment;

import java.util.ArrayList;

import com.example.eatwhat11.R;
import com.example.eatwhat11.Adapter.FoodListAdapter;
import com.example.eatwhat11.bean.Food;
import com.example.eatwhat11.Database.FoodDBHelper;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
//import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

@SuppressLint(value={"SimpleDateFormat", "DefaultLocale"})
public class FoodFragment extends Fragment{
    private static final String TAG = "FoodFragment";
    protected View mView;//declare object of view声明视图对象
    protected Context mContext;//declare object of context声明上下对象
    private int foodType;
    public ListView lv_food;//declare object of ListView声明列表对象
    private ArrayList<Food> foodArray = new ArrayList<>();
    private FoodDBHelper mFoodHelper;//declare a database helper声明食物数据库帮助器

    //get the case of fragment获得碎片的实例
    public static FoodFragment newInstance(int foodType){
        FoodFragment fragment = new FoodFragment();//create a case of fragment创建一个碎片实例
        Bundle bundle = new Bundle();
        bundle.putInt("foodType", foodType);
        fragment.setArguments(bundle);
        return fragment;
    }

    //create the view of fragment创建碎片视图
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mContext = getActivity();//get the context of activity获取活动页面的上下文
        if(getArguments() != null){
            foodType = getArguments().getInt("foodType", 0);
        }
        mView = inflater.inflate(R.layout.fragment_food, container, false);
        lv_food = mView.findViewById(R.id.lv_food);
        return mView;
    }

    @Override
    public void onStart(){
        super.onStart();
        mFoodHelper = FoodDBHelper.getInstance(mContext, 1);
        mFoodHelper.openReadLink();
        ArrayList<Food> infoArray = mFoodHelper.query("1=1");
        ArrayList<Food> arrayList = new ArrayList<>();
        for(int i = 0; i < infoArray.size(); i++){
            if(foodType == infoArray.get(i).food_type){
                arrayList.add(infoArray.get(i));
            }
        }
        final FoodListAdapter listAdapter = new FoodListAdapter(mContext, arrayList);
        lv_food.setAdapter(listAdapter);
        lv_food.setOnItemClickListener(listAdapter);
        lv_food.setOnItemLongClickListener(listAdapter);

    }

    @Override
    public void onStop(){
        super.onStop();
        mFoodHelper.closeLink();
    }
}
