package com.lyam.mymemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class SQLdbHelper extends SQLiteOpenHelper {
    public static final int DB_VERSION = 6;
    public static final String DB_NAME = "memo.db";

    public static SQLdbHelper inst;

    private SQLiteDatabase db;
    private Context context;

    public SQLdbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        db = this.getWritableDatabase();
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FeedContract.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(FeedContract.SQL_DELETE);
        onCreate(db);
    }

    public static SQLdbHelper getInst(Context context) {
        if (inst == null)
            inst = new SQLdbHelper(context);
        return inst;
    }

    public Cursor getAllData() {
        String query = "SELECT * FROM " + FeedContract.FeedEntry.TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        return c;
    }


    public long insertMemo(String data, int color) {
        ContentValues values = new ContentValues();
        values.put(FeedContract.FeedEntry.COLUMN_MEMO, data);
        values.put(FeedContract.FeedEntry.COLOR_CODE, color);
        Toast.makeText(context, "저장되었습니다!", Toast.LENGTH_SHORT).show();
        return db.insert(FeedContract.FeedEntry.TABLE_NAME, null, values);
    }

    public void deleteMemo(String data) {
        System.out.println("삭제하기 " + data);
        long id = getID_fromData(data);
        db.delete(FeedContract.FeedEntry.TABLE_NAME, FeedContract.FeedEntry.COLUMN_MEMO + "=? and "
                + FeedContract.FeedEntry._ID + "=?", new String[]{data, String.valueOf(id)});
        Toast.makeText(context, "삭제되었습니다!", Toast.LENGTH_SHORT).show();
    }

    public void updateMemo(String new_memo, String old_memo, int color) {
        System.out.println("수정하기 " + old_memo + " -> " + new_memo + " / " + color);
        long id = getID_fromData(old_memo);
        ContentValues values = new ContentValues();
        values.put(FeedContract.FeedEntry.COLUMN_MEMO, new_memo);
        values.put(FeedContract.FeedEntry.COLOR_CODE, color);
        db.update(FeedContract.FeedEntry.TABLE_NAME, values, FeedContract.FeedEntry.COLUMN_MEMO + " = ? AND "
                + FeedContract.FeedEntry._ID + " = ? ", new String[]{old_memo, String.valueOf(id)});
        Toast.makeText(context, "수정되었습니다!", Toast.LENGTH_SHORT).show();
    }

    public long getID_fromData(String data) {
        System.out.println("삭제 또는 수정할 내용은: " + data);
        Cursor cursor = db.query(FeedContract.FeedEntry.TABLE_NAME, new String[]{FeedContract.FeedEntry._ID}, FeedContract.FeedEntry.COLUMN_MEMO + "=?", new String[]{String.valueOf(data)}, null, null, null);
        long id = 0;
        while (cursor.moveToNext()) {
            int column = cursor.getColumnIndex(FeedContract.FeedEntry._ID);
            id = cursor.getLong(column);
            System.out.println(data + " 의 id는 " + id);
        }
        cursor.close();
        return id;
    }
}