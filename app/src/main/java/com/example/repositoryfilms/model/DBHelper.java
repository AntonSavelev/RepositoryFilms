package com.example.repositoryfilms.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "cashDb";
    public static final String TABLE_PEOPLES = "characters";

    public static final String KEY_ID = "_id";
    public static final String KEY_PEOPLE_NAME = "name";
    public static final String KEY_PEOPLE_HEIGHT = "height";
    public static final String KEY_PEOPLE_MASS = "mass";
    public static final String KEY_PEOPLE_HAIR_COLOR = "hair_color";
    public static final String KEY_PEOPLE_SKIN_COLOR = "skin_color";
    public static final String KEY_PEOPLE_EYE_COLOR = "eye_color";
    public static final String KEY_PEOPLE_BIRTH_YEAR = "birth_year";
    public static final String KEY_PEOPLE_GENDER = "gender";
    public static final String KEY_PEOPLE_HOMEWORLD = "homeworld";
    public static final String KEY_PEOPLE_CREATED = "created";
    public static final String KEY_PEOPLE_EDITED = "edited";
    public static final String KEY_PEOPLE_URL = "url";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_PEOPLES + "(" + KEY_ID
                + " integer primary key," + KEY_PEOPLE_NAME + " text," +
                KEY_PEOPLE_HEIGHT + " text," + KEY_PEOPLE_MASS + " text," +
                KEY_PEOPLE_HAIR_COLOR + " text," + KEY_PEOPLE_SKIN_COLOR + " text," +
                KEY_PEOPLE_EYE_COLOR + " text," + KEY_PEOPLE_BIRTH_YEAR + " text," +
                KEY_PEOPLE_GENDER + " text," + KEY_PEOPLE_HOMEWORLD + " text," +
                KEY_PEOPLE_CREATED + " text," + KEY_PEOPLE_EDITED + " text," + KEY_PEOPLE_URL + " text" +")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_PEOPLES);
        onCreate(sqLiteDatabase);
    }
}
