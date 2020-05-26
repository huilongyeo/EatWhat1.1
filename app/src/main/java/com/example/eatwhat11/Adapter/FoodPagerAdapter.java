package com.example.eatwhat11.Adapter;

import com.example.eatwhat11.fragment.FoodFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.Fragment;

public class FoodPagerAdapter extends FragmentStatePagerAdapter{

    public FoodPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return FoodFragment.newInstance(position);
    }

    //get the count of fragment, breakfast, lunch and dinner获取碎片个数，早餐、午餐 和晚餐
    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position){
        String[] foodType = {"早餐", "午餐", "晚餐"};
        return foodType[position];
    }
}
