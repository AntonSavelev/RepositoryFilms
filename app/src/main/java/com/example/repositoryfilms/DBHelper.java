package com.example.repositoryfilms;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "cashDb";
    public static final String TABLE_PEOPLES = "peoples";

    public static final String KEY_ID = "_id";
    public static final String KEY_PEOPLE_JSON = "film_json";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_PEOPLES + "(" + KEY_ID
                + " integer primary key," + KEY_PEOPLE_JSON + " text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_PEOPLES);
        onCreate(sqLiteDatabase);
    }
}
