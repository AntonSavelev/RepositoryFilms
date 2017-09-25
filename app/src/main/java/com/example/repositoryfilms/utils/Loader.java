package com.example.repositoryfilms.utils;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.example.repositoryfilms.model.AllPeople;
import com.example.repositoryfilms.model.Character;
import com.example.repositoryfilms.network.SwapiService;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import retrofit2.Call;

public class Loader implements Serializable {

    private static final int AMOUNT_OF_ITEM_ON_PAGE = 10;
    private static int numberOfRequest;
    final List<Character> characters = new ArrayList<>();
    private SwapiService service;
    private PeopleDbHelper peopleDbHelper;
    private Set<LoadListener> listeners = new HashSet<>();
    private Handler handler;
    private int total = 0;

    public Loader(SwapiService service, PeopleDbHelper peopleDbHelper) {
        this.service = service;
        this.peopleDbHelper = peopleDbHelper;
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                characters.addAll((List<Character>) msg.obj);
                Loader.this.peopleDbHelper.saveCharacters(characters);
                total = msg.arg1;
                notifyDataLoaded(characters);
            }
        };
    }

    public void loadMore() {
        numberOfRequest = 1 + characters.size() / AMOUNT_OF_ITEM_ON_PAGE;
        if (characters.size() < total || total == 0) {
            new MyAsyncTask().execute(numberOfRequest);
        }
    }

    public void readDB() {
        if (peopleDbHelper.getCharacters().size() != 0) {
            characters.clear();
            characters.addAll(peopleDbHelper.getCharacters());
            notifyDataLoaded(characters);
            numberOfRequest = 1 + peopleDbHelper.getCharacters().size() / AMOUNT_OF_ITEM_ON_PAGE;
        }
    }

    private void notifyDataLoaded(List<Character> characters) {
        Iterator<LoadListener> iterator = listeners.iterator();
        if (iterator.hasNext()) {
            LoadListener listener = iterator.next();
            listener.onCharactersLoaded(characters);
        }
    }

    private void notifyDataLoadingError(String exception) {
        Iterator<LoadListener> iterator = listeners.iterator();
        if (iterator.hasNext()) {
            LoadListener listener = iterator.next();
            listener.onCharactersLoadingError(exception);
        }
    }

    public boolean addLoadListener(LoadListener loadListener) {
        return listeners.add(loadListener);
    }

    public void removeLoadListener(LoadListener loadListener) {
        if (!listeners.remove(loadListener)) {
        }
    }

    private class MyAsyncTask extends AsyncTask<Integer, Void, String> {
        @Override
        protected String doInBackground(Integer... params) {
            String exception = null;
            List<Character> characters = new ArrayList<>();
            Call<AllPeople> call = service.getAllPeoples(params[0]);
            try {
                retrofit2.Response<AllPeople> response = call.execute();
                if (response.isSuccessful()) {
                    AllPeople allPeople = response.body();
                    List<Character> results = allPeople.getResults();
                    if (results != null) {
                        characters.addAll(results);
                    }
                    Message msg = handler.obtainMessage(0, allPeople.getCount(), 0, characters);
                    handler.sendMessage(msg);
                } else {
                    switch (response.code()) {
                        case 404:
                            exception = "Not Found";
                            break;
                        case 408:
                            exception = "Request Timeout";
                            break;
                        case 500:
                            exception = "Server Error";
                            break;
                        default:
                            exception = "Response failed";
                            break;
                    }
                }
            } catch (IOException e) {
                exception = "Request failed";
            }
            return exception;
        }

        @Override
        protected void onPostExecute(String ex) {
            if (ex != null) {
                notifyDataLoadingError(ex);
            }
        }
    }
}
