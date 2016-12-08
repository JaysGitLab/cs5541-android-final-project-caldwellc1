package com.bignerdranch.android.foodcents;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.bignerdranch.android.foodcents.database.FoodBaseHelper;
import com.bignerdranch.android.foodcents.database.FoodCursorWrapper;
import com.bignerdranch.android.foodcents.database.FoodDbSchema.FoodTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



public class FoodLab {
    private static FoodLab sFoodLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static FoodLab get(Context context){
        if(sFoodLab == null){
            sFoodLab = new FoodLab(context);
        }
        return sFoodLab;
    }

    private FoodLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new FoodBaseHelper(mContext).getWritableDatabase();
    }

    public void addFood(Food c){
        ContentValues values = getContentValues(c);
        mDatabase.insert(FoodTable.NAME, null, values);
    }

    public void deleteFood(Food f){
        String uuidString = f.getId().toString();
        mDatabase.delete(FoodTable.NAME, FoodTable.Cols.UUID + " = ?", new String[] { uuidString });
    }


    public List<Food> getFoods(){
        List<Food> foods = new ArrayList<>();

        FoodCursorWrapper cursor = queryFoods(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                foods.add(cursor.getFood());
                cursor.moveToNext();
            }
        }finally{
            cursor.close();
        }

        return foods;
    }

    public List<Food> getFoodsTwo(){
        sortFoods();
        List<Food> foods = new ArrayList<>();

        FoodCursorWrapper cursor = queryFoods(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                foods.add(cursor.getFood());
                cursor.moveToNext();
            }
        }finally{
            cursor.close();
        }

        return foods;
    }

    public FoodCursorWrapper sortFoods(){
        Cursor cursor = mDatabase.query(FoodTable.NAME, null, null,
                null, null, null, FoodTable.Cols.TITLE + "ASC");
        return new FoodCursorWrapper(cursor);
    }

    public List<Food> searchFoods(String searchValue){
        List<Food> foods = new ArrayList<>();

        FoodCursorWrapper cursor = queryFoods(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                foods.add(cursor.getFood());
                cursor.moveToNext();
            }
        }finally{
            cursor.close();
        }

        return foods;
    }


    public Food getFood(UUID id){
        FoodCursorWrapper cursor = queryFoods(
                FoodTable.Cols.UUID + " = ?", new String[] {id.toString()});
        try{
            if (cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getFood();
        }
        finally {
            cursor.close();
        }
    }

    public File getPhotoFile(Food food){
        File externalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(externalFilesDir == null){
            return null;
        }
        return new File(externalFilesDir, food.getPhotoFilename());
    }

    public void updateFood(Food food){
        String uuidString = food.getId().toString();
        ContentValues values = getContentValues(food);

        mDatabase.update(FoodTable.NAME, values,
                FoodTable.Cols.UUID + " = ?", new String[] { uuidString });
    }

    private static ContentValues getContentValues(Food food){
        ContentValues values = new ContentValues();
        values.put(FoodTable.Cols.UUID, food.getId().toString());
        values.put(FoodTable.Cols.TITLE, food.getTitle());
        values.put(FoodTable.Cols.GOODP, food.isGood() ? 1 : 0);
        values.put(FoodTable.Cols.CURRENT, food.getCurrent());
        values.put(FoodTable.Cols.REGULAR, food.getRegular());
        values.put(FoodTable.Cols.DATE, food.getDate().getTime());


        return values;
    }

    private FoodCursorWrapper queryFoods(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(FoodTable.NAME, null, whereClause,
                whereArgs, null, null, null);
        return new FoodCursorWrapper(cursor);
    }
}
