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
            int characterNameIndex = cursor.getColumnIndex(DBHelper.KEY_PEOPLE_NAME);
            int characterHeightIndex = cursor.getColumnIndex(DBHelper.KEY_PEOPLE_HEIGHT);
            int characterMassIndex = cursor.getColumnIndex(DBHelper.KEY_PEOPLE_MASS);
            do {
                String characterName = cursor.getString(characterNameIndex);
                String characterHeight = cursor.getString(characterHeightIndex);
                String characterMass = cursor.getString(characterMassIndex);
                Character character = new Character(characterName, characterHeight, characterMass);
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
                String characterName = character.getName();
                String characterHeight = character.getHeight();
                String characterMass = character.getMass();
                cv.put(DBHelper.KEY_PEOPLE_NAME, characterName);
                cv.put(DBHelper.KEY_PEOPLE_HEIGHT, characterHeight);
                cv.put(DBHelper.KEY_PEOPLE_MASS, characterMass);
                writableDatabase.insert(DBHelper.TABLE_PEOPLES, null, cv);
            }
            writableDatabase.setTransactionSuccessful();
        } finally {
            writableDatabase.endTransaction();
        }
    }

    public void saveCharacter(Character character) {
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        writableDatabase.beginTransaction();
        try {
            String characterName = character.getName();
            String characterHeight = character.getHeight();
            String characterMass = character.getMass();
            String characterHairColor = character.getHairColor();
            String characterSkinColor = character.getSkinColor();
            String characterEyeColor = character.getEyeColor();
            String characterBirthYear = character.getBirthYear();
            String characterGender = character.getGender();
            String characterHomeWorld = character.getHomeworld();
            String characterCreated = character.getCreated();
            String characterEdited = character.getEdited();
            String characterURL = character.getUrl();
            cv.put(DBHelper.KEY_PEOPLE_NAME, characterName);
            cv.put(DBHelper.KEY_PEOPLE_HEIGHT, characterHeight);
            cv.put(DBHelper.KEY_PEOPLE_MASS, characterMass);
            cv.put(DBHelper.KEY_PEOPLE_HAIR_COLOR, characterHairColor);
            cv.put(DBHelper.KEY_PEOPLE_SKIN_COLOR, characterSkinColor);
            cv.put(DBHelper.KEY_PEOPLE_EYE_COLOR, characterEyeColor);
            cv.put(DBHelper.KEY_PEOPLE_BIRTH_YEAR, characterBirthYear);
            cv.put(DBHelper.KEY_PEOPLE_GENDER, characterGender);
            cv.put(DBHelper.KEY_PEOPLE_HOMEWORLD, characterHomeWorld);
            cv.put(DBHelper.KEY_PEOPLE_CREATED, characterCreated);
            cv.put(DBHelper.KEY_PEOPLE_EDITED, characterEdited);
            cv.put(DBHelper.KEY_PEOPLE_URL, characterURL);
            writableDatabase.update(DBHelper.TABLE_PEOPLES, cv, DBHelper.KEY_PEOPLE_NAME + " = ?", new String[]{characterName});
            writableDatabase.setTransactionSuccessful();
        } finally {
            writableDatabase.endTransaction();
        }
    }

    public Character getCharacter(String characterName) {
        SQLiteDatabase readableDatabase = helper.getReadableDatabase();
        String selection = DBHelper.KEY_PEOPLE_NAME + " = ?";
        Cursor cursor = readableDatabase.query(DBHelper.TABLE_PEOPLES, null, selection, new String[]{characterName}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        int characterNameIndex = cursor.getColumnIndex(DBHelper.KEY_PEOPLE_NAME);
        int characterHeightIndex = cursor.getColumnIndex(DBHelper.KEY_PEOPLE_HEIGHT);
        int characterMassIndex = cursor.getColumnIndex(DBHelper.KEY_PEOPLE_MASS);
        int characterHeirColorIndex = cursor.getColumnIndex(DBHelper.KEY_PEOPLE_HAIR_COLOR);
        int characterSkinColorIndex = cursor.getColumnIndex(DBHelper.KEY_PEOPLE_SKIN_COLOR);
        int characterEyeColorIndex = cursor.getColumnIndex(DBHelper.KEY_PEOPLE_EYE_COLOR);
        int characterBirthYearIndex = cursor.getColumnIndex(DBHelper.KEY_PEOPLE_BIRTH_YEAR);
        int characterGenderIndex = cursor.getColumnIndex(DBHelper.KEY_PEOPLE_GENDER);
        int characterHomeworldIndex = cursor.getColumnIndex(DBHelper.KEY_PEOPLE_HOMEWORLD);
        int characterCreatedIndex = cursor.getColumnIndex(DBHelper.KEY_PEOPLE_CREATED);
        int characterEditedIndex = cursor.getColumnIndex(DBHelper.KEY_PEOPLE_EDITED);
        int characterUrlIndex = cursor.getColumnIndex(DBHelper.KEY_PEOPLE_URL);
        String name = cursor.getString(characterNameIndex);
        String height = cursor.getString(characterHeightIndex);
        String mass = cursor.getString(characterMassIndex);
        String heirColor = cursor.getString(characterHeirColorIndex);
        String skinColor = cursor.getString(characterSkinColorIndex);
        String eyeColor = cursor.getString(characterEyeColorIndex);
        String birthYear = cursor.getString(characterBirthYearIndex);
        String gender = cursor.getString(characterGenderIndex);
        String homeworld = cursor.getString(characterHomeworldIndex);
        String created = cursor.getString(characterCreatedIndex);
        String edited = cursor.getString(characterEditedIndex);
        String url = cursor.getString(characterUrlIndex);
        Character character = new Character(name, height, mass, heirColor, skinColor, eyeColor, birthYear, gender, homeworld, created, edited, url);
        return character;
    }
}
