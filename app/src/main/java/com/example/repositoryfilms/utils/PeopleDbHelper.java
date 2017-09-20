package com.example.repositoryfilms.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.repositoryfilms.model.Character;
import com.example.repositoryfilms.model.DBHelper;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

public class PeopleDbHelper {
    private DBHelper helper;
    private Gson gson;

    public PeopleDbHelper(DBHelper dbHelper, Gson gson) {
        this.helper = dbHelper;
        this.gson = gson;
    }

    public List<Character> getCharacters() {
        List<Character> characters = new ArrayList<>();
        SQLiteDatabase readableDatabase = helper.getReadableDatabase();
        Cursor cursor = readableDatabase.query(DBHelper.TABLE_PEOPLES, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int filmJsonIndex = cursor.getColumnIndex(DBHelper.KEY_PEOPLE_JSON);
            do {
                String characterJson = cursor.getString(filmJsonIndex);
                Character character = gson.fromJson(characterJson, Character.class);
                characters.add(character);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return characters;
    }

    public void saveCharacters(List<Character> characters) {
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        writableDatabase.delete(DBHelper.TABLE_PEOPLES, null, null);
        ContentValues cv = new ContentValues();
        writableDatabase.beginTransaction();
        try {
            for (int j = 0; j < characters.size(); j++) {
                Character character = characters.get(j);
                String characterJson = gson.toJson(character);
                cv.put(DBHelper.KEY_PEOPLE_JSON, characterJson);
                writableDatabase.insert(DBHelper.TABLE_PEOPLES, null, cv);
            }
            writableDatabase.setTransactionSuccessful();
        } finally {
            writableDatabase.endTransaction();
        }
    }
}
