package com.bignerdranch.android.foodcents;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

public class FoodPagerActivity extends AppCompatActivity {
    private static final String EXTRA_FOOD_ID =
            "com.bignerdranch.android.foodcents.food_id";

    private ViewPager mViewPager;
    private List<Food> mFoods;

    public static Intent newIntent(Context packageContext, UUID crimeId){
        Intent intent = new Intent(packageContext, com.bignerdranch.android.foodcents.FoodPagerActivity.class);
        intent.putExtra(EXTRA_FOOD_ID, crimeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_pager);

        UUID foodId = (UUID) getIntent().getSerializableExtra(EXTRA_FOOD_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_food_pager_view_pager);

        mFoods = FoodLab.get(this).getFoods();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Food food = mFoods.get(position);
                return FoodFragment.newInstance(food.getId());
            }

            @Override
            public int getCount() {
                return mFoods.size();
            }
        });
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                Food food = mFoods.get(position);
                if (food.getTitle() != null) {
                    setTitle(food.getTitle());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });

        for(int i=0;i<mFoods.size();i++){
            if(mFoods.get(i).getId().equals(foodId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

}
