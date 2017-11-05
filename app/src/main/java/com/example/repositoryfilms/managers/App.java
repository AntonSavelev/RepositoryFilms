package com.example.repositoryfilms.managers;

import android.app.Application;

import com.example.repositoryfilms.utils.Loader;
import com.example.repositoryfilms.model.DBHelper;
import com.example.repositoryfilms.utils.PeopleDbHelper;
import com.example.repositoryfilms.network.ApiClient;
import com.google.gson.Gson;


public class App extends Application {
    private static final String CHARACTER_NAME = "characterName";
    private static App instance;
    private static Loader loader;

    public static String getCharacterDetailInformation() {
        return CHARACTER_NAME;
    }


    public synchronized static Loader getLoader() {
        if (loader == null) {
            Gson gson = new Gson();
            DBHelper dbHelper = new DBHelper(instance);
            PeopleDbHelper peopleDbHelper = new PeopleDbHelper(dbHelper, gson);
            loader = new Loader(ApiClient.getSwapiService(gson), peopleDbHelper);
        }
        return loader;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
