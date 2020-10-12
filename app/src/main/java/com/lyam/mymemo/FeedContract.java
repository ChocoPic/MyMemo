package com.lyam.mymemo;

import android.provider.BaseColumns;

public class FeedContract {
    private FeedContract(){}

    public static class FeedEntry implements BaseColumns{
        public static final String TABLE_NAME = "memo_table";
        public static final String COLUMN_MEMO = "memo";
        public static final String COLOR_CODE = "color_code";
    }

    public static final String SQL_CREATE = "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
            FeedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            FeedEntry.COLUMN_MEMO + " TEXT," +
            FeedEntry.COLOR_CODE + " INTEGER )";

    public static final String SQL_DELETE = "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

}
