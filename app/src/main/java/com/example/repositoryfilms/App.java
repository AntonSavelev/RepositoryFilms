package com.example.repositoryfilms;

import android.app.Application;

import com.google.gson.Gson;


public class App extends Application {
    private static App instance;
    private static Loader loader;

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
