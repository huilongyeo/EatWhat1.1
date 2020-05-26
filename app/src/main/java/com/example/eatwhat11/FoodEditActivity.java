package com.example.eatwhat11;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eatwhat11.Database.FoodDBHelper;
import androidx.appcompat.app.AppCompatActivity;
import com.example.eatwhat11.bean.Food;

import java.util.ArrayList;

/**
 * Created by huilongyeo on 20/5/2020
 */
public class FoodEditActivity extends AppCompatActivity implements OnClickListener{

    private EditText et_edit;
    private Bundle bundle;
    private FoodDBHelper mHelper;
    private Long rowid;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_edit);
        et_edit= findViewById(R.id.et_edit);
        findViewById(R.id.btn_edit).setOnClickListener(this);
        showHint();
    }

    /*@Override
    protected void onResume(){
        super.onResume();
        //get the object of FoodDBHelper and open Write Link获取数据库帮助器对象并打开写连接
        mHelper = FoodDBHelper.getInstance(this, 1);
        mHelper.openReadLink();
    }

    @Override
    protected void onPause(){
        super.onPause();
        //close read link关闭读连接
        mHelper.closeLink();
    }*/

    @Override
    public void onClick(View v){
        if(v.getId() == R.id.btn_edit){
            String name = et_edit.getText().toString();
            if(TextUtils.isEmpty(name)){
                Toast.makeText(this, "请输入食物名称",Toast.LENGTH_SHORT).show();
                return;
            }
            mHelper = FoodDBHelper.getInstance(this, 1);
            mHelper.openWriteLink();//不知为何，需要在此处读取写连接，否则滑动两个页面后会出现错误
            ArrayList<Food> infoArray = mHelper.query("rowid=" + rowid);
            for(Food info : infoArray){
                if(rowid == info.rowid){
                    Food newInfo = info;
                    newInfo.food_name = name;
                    mHelper.update(newInfo);
                    break;
                }
            }
            mHelper.closeLink();
            finish();
        }
    }

    private void showHint(){
       bundle = getIntent().getExtras();
       rowid = bundle.getLong("rowid");
       String name = bundle.getString("name");
       et_edit.setHint(name);
    }
}
