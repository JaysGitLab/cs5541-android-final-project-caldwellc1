package com.bignerdranch.android.foodcents;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.text.DecimalFormat;
import java.util.UUID;


public class FoodFragment extends Fragment{

    private static final String ARG_FOOD_ID = "food_id";
    private static final int REQUEST_PHOTO = 0;

    private Food mFood;
    private EditText mTitleField;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private File mPhotoFile;
    private String goods;

    public static FoodFragment newInstance(UUID foodId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_FOOD_ID, foodId);

        FoodFragment fragment = new FoodFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        UUID foodId = (UUID) getArguments().getSerializable(ARG_FOOD_ID);
        mFood = FoodLab.get(getActivity()).getFood(foodId);
        mPhotoFile = FoodLab.get(getActivity()).getPhotoFile(mFood);
    }

    @Override
    public void onPause(){
        super.onPause();

        FoodLab.get(getActivity()).updateFood(mFood);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_food, container, false);
        mTitleField = (EditText)v.findViewById(R.id.food_title);
        mTitleField.setText(mFood.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mFood.setTitle(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mTitleField = (EditText)v.findViewById(R.id.food_reg);
        mTitleField.setText(String.valueOf(mFood.getRegular()));
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String sss = s.toString();
                mFood.setRegular(Integer.parseInt(sss));
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mTitleField = (EditText)v.findViewById(R.id.food_current);
        mTitleField.setText(String.valueOf(mFood.getCurrent()));
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String sss = s.toString();
                mFood.setCurrent(Integer.parseInt(sss));
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mTitleField = (EditText)v.findViewById(R.id.food_store);
        mTitleField.setText(mFood.getStore());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mFood.setStore(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mTitleField = (EditText)v.findViewById(R.id.food_good);
        if (mFood.isGood()){ goods="Y";}
        else{goods="N";}
        mTitleField.setText(goods);
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s=="Y"){mFood.setGood(true);}
                else{mFood.setGood(false);}
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mPhotoButton = (ImageButton) v.findViewById(R.id.food_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null;
        mPhotoButton.setEnabled(canTakePhoto);

        if(canTakePhoto){
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        mPhotoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView) v.findViewById(R.id.food_photo);
        updatePhotoView();

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_PHOTO){
            updatePhotoView();
        }
    }



    private void updatePhotoView(){
        if(mPhotoFile == null || !mPhotoFile.exists()){
            mPhotoView.setImageDrawable(null);
        }
        else{
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }
}
