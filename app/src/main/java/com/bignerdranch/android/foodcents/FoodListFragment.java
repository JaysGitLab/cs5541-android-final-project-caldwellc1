package com.bignerdranch.android.foodcents;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class FoodListFragment extends Fragment {
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private boolean mSubtitleVisible;

    @Override
    public void onCreate(Bundle savedInstancesState){
        super.onCreate(savedInstancesState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_food_list, container, false);
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(savedInstanceState != null){
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        updateUI();
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_food_list, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_item_new_food:
                Food food = new Food();
                FoodLab.get(getActivity()).addFood(food);
                Intent intent = FoodPagerActivity.newIntent(getActivity(), food.getId());
                startActivity(intent);
                return true;
            case R.id.menu_item_del:
                return true;
            case R.id.menu_item_search:
                return true;
            case R.id.menu_item_sort:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI(){
        FoodLab foodLab = FoodLab.get(getActivity());
        List<Food> foods = foodLab.getFoods();
        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(foods);
            mCrimeRecyclerView.setAdapter(mAdapter);
        }
        else{
            mAdapter.setFoods(foods);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTitleTextView;
        private TextView mCurrentTextView;
        private Food mFood;

        public CrimeHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_food_title_text_view);
            mCurrentTextView = (TextView) itemView.findViewById(R.id.list_item_food_lowest_text_view);

            }

        public void bindCrime(Food crime) {
            mFood = crime;
            mTitleTextView.setText(mFood.getTitle());
            //mCurrentTextView.setText(mFood.toString(mFood.getCurrent()));
        }

        @Override
        public void onClick(View v){
            Intent intent = FoodPagerActivity.newIntent(getActivity(), mFood.getId());
            startActivity(intent);
        }

    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{
        private List<Food> mFoods;

        public CrimeAdapter(List<Food> crimes){
            mFoods = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_food, parent, false);
            return new CrimeHolder(view);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position){
            Food crime = mFoods.get(position);
            holder.bindCrime(crime);
        }

        @Override
        public int getItemCount(){
            return mFoods.size();
        }

        public void setFoods(List<Food> foods){
            mFoods = foods;
        }
    }
}
