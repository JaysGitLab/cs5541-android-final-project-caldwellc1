package com.bignerdranch.android.foodcents.database;


public class FoodDbSchema {
    public static final class FoodTable{
        public static final String NAME = "foods";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String STORE = "store";
            public static final String CURRENT = "current";
            public static final String REGULAR = "regular";
            public static final String GOODP = "goodprice";
            public static final String DATE = "date";
        }
    }
}
