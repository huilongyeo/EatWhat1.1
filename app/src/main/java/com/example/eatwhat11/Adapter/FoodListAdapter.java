package com.example.eatwhat11.Adapter;

import java.util.ArrayList;

import com.example.eatwhat11.FoodEditActivity;
import com.example.eatwhat11.MainActivity;
import com.example.eatwhat11.R;
import com.example.eatwhat11.bean.Food;
import com.example.eatwhat11.Database.FoodDBHelper;
import com.example.eatwhat11.fragment.FoodFragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("DefaultLocale")
public class FoodListAdapter extends BaseAdapter implements OnItemClickListener, OnItemLongClickListener {
    private static final String TAG = "ScheduleListAdapter";
    private Context mContext;//declare a object of context声明上下文对象
    private ArrayList<Food> mFoodArray;
    FoodDBHelper mHelper;

    public FoodListAdapter(Context context, ArrayList<Food> foodArray){
        mContext = context;
        mFoodArray = foodArray;
    }
    @Override
    public int getCount() {
        return mFoodArray.size();
    }

    @Override
    public Object getItem(int position) {
        return mFoodArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list, null);
            holder.tv_food_name = convertView.findViewById(R.id.tv_food_name);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_food_name.setText(mFoodArray.get(position).food_name);
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //get the case of Application获得Application的实例
        Food info = mFoodArray.get(position);
        Intent intent = new Intent(mContext, FoodEditActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("name", info.food_name);
        bundle.putLong("rowid",info.rowid);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    public class ViewHolder{
        TextView tv_food_name;
    }

    @Override
    public boolean onItemLongClick(final AdapterView<?> parent, final View view, final int position, long id) {
        final Food info = mFoodArray.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("删除");
        builder.setMessage(String.format("删除%s吗", info.food_name));
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mHelper = FoodDBHelper.getInstance(mContext, 1);
                mHelper.openWriteLink();
                mHelper.delete(String.format("rowid='%d'",info.rowid));
                mHelper.closeLink();
                view.setVisibility(View.GONE);
            }
        });
        builder.setNegativeButton("取消", null);
        AlertDialog alert = builder.create();
        alert.show();
        return true;
    }
}
