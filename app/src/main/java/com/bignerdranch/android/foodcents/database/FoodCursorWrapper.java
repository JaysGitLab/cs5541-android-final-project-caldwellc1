package com.bignerdranch.android.foodcents.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import com.bignerdranch.android.foodcents.Food;
import com.bignerdranch.android.foodcents.database.FoodDbSchema.FoodTable;

import java.util.Date;
import java.util.UUID;


public class FoodCursorWrapper extends CursorWrapper{
    public FoodCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Food getFood(){
        String uuidString = getString(getColumnIndex(FoodTable.Cols.UUID));
        String title = getString(getColumnIndex(FoodTable.Cols.TITLE));
        String store = getString(getColumnIndex(FoodTable.Cols.STORE));
        int current = getInt(getColumnIndex(FoodTable.Cols.CURRENT));
        int regular = getInt(getColumnIndex(FoodTable.Cols.REGULAR));
        int isDeal = getInt(getColumnIndex(FoodTable.Cols.GOODP));
        long date = getLong(getColumnIndex(FoodTable.Cols.DATE));

        Food food = new Food(UUID.fromString(uuidString));
        food.setTitle(title);
        food.setDate(new Date(date));
        food.setStore(store);
        food.setCurrent(current);
        food.setRegular(regular);
        food.setGood(isDeal !=0);

        return food;
    }
}
