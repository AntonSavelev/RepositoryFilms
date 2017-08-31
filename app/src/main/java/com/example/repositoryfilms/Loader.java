package com.example.repositoryfilms;

import android.util.Log;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Loader {
    private SwapiService service;
    private PeopleDbHelper peopleDbHelper;

    private Set<LoadListener> listeners = new HashSet<>();

    public Loader(SwapiService service, PeopleDbHelper peopleDbHelper) {
        this.service = service;
        this.peopleDbHelper = peopleDbHelper;
    }

    public void loadMore() {

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
            Log.e("Loader", "removeLoadListener, no such listener");
        }
    }


//    private class MyAsyncTask extends AsyncTask<Void, Void, AllPeople> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            swapiService = ApiClient.getSwapiService().create(SwapiService.class);
//        }
//
//        @Override
//        protected void onPostExecute(AllPeople allPeople) {
//            super.onPostExecute(allPeople);
//            characters.addAll(allPeople.getResults());
//            setDataInAdapter();
//            writeDB();
//            i++;
//            peoplesCount = Integer.parseInt(allPeople.getCount());
//            peoplesSum = peoplesSum + allPeople.getResults().size();
//            sqLiteDatabase = dbHelper.getWritableDatabase();
//            gson = new GsonBuilder().create();
//            if (cursor != null) {
//                characters.clear();
//                readDB();
//                setDataInAdapter();
//            }
//        }
//
//        @Override
//        protected AllPeople doInBackground(Void... voids) {
//            Call<AllPeople> call = swapiService.getAllPeoples(i);
//            try {
//                allPeople = call.execute().body();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return allPeople;
//        }
//    }
}
