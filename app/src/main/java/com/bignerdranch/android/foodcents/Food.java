package com.bignerdranch.android.foodcents;

import java.util.Date;
import java.util.UUID;


public class Food {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private int mRegular;
    private int mCurrent;
    private boolean mGood;
    private String mStore;

    public Food(){
        //Generate unique identifier
        this(UUID.randomUUID());
    }

    public Food(UUID id){
        mId = id;
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public int getRegular() {
        return mRegular;
    }

    public void setRegular(int reg) {
        mRegular = reg;
    }

    public int getCurrent() {
        return mCurrent;
    }

    public void setCurrent(int curr) {
        mCurrent = curr;
    }

    public boolean isGood() {
        return mGood;
    }

    public void setGood(boolean g) {
        mGood = g;
    }

    public String getStore(){
        return mStore;
    }

    public void setStore(String store){
        mStore = store;
    }

    public String getPhotoFilename(){
        return "IMG_" + getId().toString() + ".jpg";
    }
}
