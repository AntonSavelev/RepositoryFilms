package com.example.repositoryfilms.utils;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.example.repositoryfilms.model.AllCharacters;
import com.example.repositoryfilms.model.Character;
import com.example.repositoryfilms.network.SwapiService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Loader {

    private static final int AMOUNT_OF_ITEM_ON_PAGE = 10;
    private static int pageNumber;
    final List<Character> characters = new ArrayList<>();
    private int totalCharacters = 0;
    private SwapiService service;
    private PeopleDbHelper peopleDbHelper;
    private Set<LoadListener> listeners = new HashSet<>();
    private Set<LoadDetailCharacterListener> detailCharacterListeners = new HashSet<>();
    private Handler h;

    public Loader(SwapiService service, PeopleDbHelper peopleDbHelper) {
        this.service = service;
        this.peopleDbHelper = peopleDbHelper;
    }

    public int getTotalCharacters() {
        return totalCharacters;
    }

    public void setTotalCharacters(int totalCharacters) {
        this.totalCharacters = totalCharacters;
    }

    public void loadMore() {
        pageNumber = 1 + characters.size() / AMOUNT_OF_ITEM_ON_PAGE;
        if (characters.size() < totalCharacters || totalCharacters == 0) {
            h = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    characters.addAll((List<Character>) msg.obj);
                    peopleDbHelper.saveCharacters(characters);
                    totalCharacters = msg.arg1;
                    notifyDataLoaded(characters);
                }
            };
            if (pageNumber <= 1 + (totalCharacters / AMOUNT_OF_ITEM_ON_PAGE)) {
                new MyAsyncTask().execute(pageNumber);
            }
        }
    }

    public void readListCharactersFromDb() {
        int charactersSize = peopleDbHelper.getCharacters().size();
        if (charactersSize != 0) {
            characters.clear();
            characters.addAll(peopleDbHelper.getCharacters());
            notifyDataLoaded(characters);
            pageNumber = 1 + charactersSize / AMOUNT_OF_ITEM_ON_PAGE;
        }
    }

    public boolean isDbExist() {
        int charactersSize = peopleDbHelper.getCharacters().size();
        if (charactersSize != 0) {
            return true;
        } else return false;
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

    public void saveCharacterDetailToDb(Character character) {
        peopleDbHelper.saveCharacter(character);
    }

    public Character readCharacterDetailFromDb(String characterName) {
        Character character = peopleDbHelper.getCharacter(characterName);
        return character;
    }

    private void notifyDetailDataLoaded(Character character) {
        Iterator<LoadDetailCharacterListener> iterator = detailCharacterListeners.iterator();
        if (iterator.hasNext()) {
            LoadDetailCharacterListener detailCharacterListener = iterator.next();
            detailCharacterListener.onDetailCharacterLoaded(character);
        }
    }

    private void notifyDetailDataLoadingError(String exception) {
        Iterator<LoadDetailCharacterListener> iterator = detailCharacterListeners.iterator();
        if (iterator.hasNext()) {
            LoadDetailCharacterListener detailCharacterListener = iterator.next();
            detailCharacterListener.onDetailCharacterLoadingError(exception);
        }
    }

    public boolean addLoadDetailListener(LoadDetailCharacterListener loadDetailCharacterListener) {
        return detailCharacterListeners.add(loadDetailCharacterListener);
    }

    public void removeLoadDetailListener(LoadDetailCharacterListener loadDetailCharacterListener) {
        if (!detailCharacterListeners.remove(loadDetailCharacterListener)) {
        }
    }

    public Character loadCharacterDetail(String characterName) {
        final Character[] character = {null};
        final String[] exception = {null};
        service.getPeopleSearch(characterName).enqueue(new Callback<AllCharacters>() {
            @Override
            public void onResponse(Call<AllCharacters> call, Response<AllCharacters> response) {
                if (response.isSuccessful()) {
                    AllCharacters allCharacters = response.body();
                    character[0] = allCharacters.getResults().get(0);
                    notifyDetailDataLoaded(character[0]);
                } else {
                    switch (response.code()) {
                        case 404:
                            exception[0] = "Not Found";
                            break;
                        case 408:
                            exception[0] = "Request Timeout";
                            break;
                        case 500:
                            exception[0] = "Server Error";
                            break;
                        default:
                            exception[0] = "Response failed";
                            break;
                    }
                    notifyDetailDataLoadingError(exception[0]);
                }
            }

            @Override
            public void onFailure(Call<AllCharacters> call, Throwable t) {
                notifyDataLoadingError(t.getMessage());
            }
        });
        return character[0];
    }

    private class MyAsyncTask extends AsyncTask<Integer, Void, String> {
        @Override
        protected String doInBackground(Integer... integers) {
            Message msg;
            AllCharacters allCharacters;
            String exception = null;
            List<Character> characters = new ArrayList<>();
            List<Character> results;
            Call<AllCharacters> call = service.getAllPeoples(integers[0]);
            try {
                retrofit2.Response<AllCharacters> response = call.execute();
                if (response.isSuccessful()) {
                    allCharacters = response.body();
                    results = allCharacters.getResults();
                    characters.addAll(results);
                    int charactersQuantity = Integer.parseInt(allCharacters.getCount());
                    msg = h.obtainMessage(0, charactersQuantity, 0, characters);
                    h.sendMessage(msg);
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
        protected void onPostExecute(String exeption) {
            super.onPostExecute(exeption);
            if (exeption != null) {
                notifyDataLoadingError(exeption);
            }
        }
    }
}
