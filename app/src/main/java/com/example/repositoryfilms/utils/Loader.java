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

    private SwapiService service;
    private PeopleDbHelper peopleDbHelper;

    private Set<LoadListener> listeners = new HashSet<>();
    private Handler h;
    final List<Character> characters = new ArrayList<>();
    private int total = 0;
    private static int nuberOfRequest;
    private static final int AMOUNT_OF_ITEM_ON_PAGE = 10;

    public Loader(SwapiService service, PeopleDbHelper peopleDbHelper) {
        this.service = service;
        this.peopleDbHelper = peopleDbHelper;
    }

    public void loadMore() {
        nuberOfRequest = 1 + characters.size() / AMOUNT_OF_ITEM_ON_PAGE;
        if (characters.size() < total || total == 0) {
            h = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    characters.addAll((List<Character>) msg.obj);
                    peopleDbHelper.saveCharacters(characters);
                    total = msg.arg1;
                    notifyDataLoaded(characters);
                }
            };
            new MyAsyncTask().execute(nuberOfRequest);
        }
    }

    public void readDB() {
        if (peopleDbHelper.getCharacters().size() != 0) {
            characters.clear();
            characters.addAll(peopleDbHelper.getCharacters());
            notifyDataLoaded(characters);
            nuberOfRequest = 1 + peopleDbHelper.getCharacters().size() / AMOUNT_OF_ITEM_ON_PAGE;
        }
    }

    private void notifyDataLoaded(List<Character> characters) {
        Iterator<LoadListener> iterator = listeners.iterator();
        if (iterator.hasNext()) {
            LoadListener listener = iterator.next();
            listener.onCharactersLoaded(characters);
        }
    }

    public boolean addLoadListener(LoadListener loadListener) {
        return listeners.add(loadListener);
    }

    public void removeLoadListener(LoadListener loadListener) {
        if (!listeners.remove(loadListener)) {
        }
    }

    private class MyAsyncTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... integers) {
            Message msg;
            AllPeople allPeople;
            List<Character> characters = new ArrayList<>();
            Call<AllPeople> call = service.getAllPeoples(integers[0]);
            try {
                allPeople = call.execute().body();
                characters.addAll(allPeople.getResults());
                msg = h.obtainMessage(0, Integer.parseInt(allPeople.getCount()), 0, characters);
                h.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
