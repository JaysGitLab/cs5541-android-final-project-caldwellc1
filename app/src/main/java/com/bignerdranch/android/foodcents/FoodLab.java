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
    private static FoodLab sCrimeLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static FoodLab get(Context context){
        if(sCrimeLab == null){
            sCrimeLab = new FoodLab(context);
        }
        return sCrimeLab;
    }

    private FoodLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new FoodBaseHelper(mContext).getWritableDatabase();
    }

    public void addCrime(Food c){
        ContentValues values = getContentValues(c);
        mDatabase.insert(FoodTable.NAME, null, values);
    }

    public List<Food> getCrimes(){
        List<Food> crimes = new ArrayList<>();

        FoodCursorWrapper cursor = queryCrimes(null, null);

        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                crimes.add(cursor.getFood());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return crimes;
    }

    public Food getCrime(UUID id){
        FoodCursorWrapper cursor = queryCrimes(
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

    public File getPhotoFile(Food crime){
        File externalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(externalFilesDir == null){
            return null;
        }
        return new File(externalFilesDir, crime.getPhotoFilename());
    }

    public void updateCrime(Food crime){
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);

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

    private FoodCursorWrapper queryCrimes(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(FoodTable.NAME, null, whereClause,
                whereArgs, null, null, null);
        return new FoodCursorWrapper(cursor);
    }
}
