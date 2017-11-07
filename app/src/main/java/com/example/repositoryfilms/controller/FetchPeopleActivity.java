package com.example.repositoryfilms.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.repositoryfilms.utils.LoadListener;
import com.example.repositoryfilms.utils.Loader;
import com.example.repositoryfilms.adapter.MyAdapter;
import com.example.repositoryfilms.R;
import com.example.repositoryfilms.managers.App;
import com.example.repositoryfilms.model.Character;

import java.util.List;

public class FetchPeopleActivity extends AppCompatActivity implements LoadListener {

    final String SAVED_TOTAL_CHARACTERS = "saved_total_characters";
    SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences sPref;
    private MyAdapter adapter;
    private Loader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initSwipeRefreshLayout();
        initRecyclerView();
        loader = App.getLoader();
        if (loader.isDbExist()) {
            loadPref();
        }
    }

    public void initSwipeRefreshLayout() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestNextItems();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.r_view);
        final GridLayoutManager layoutManager = new GridLayoutManager(FetchPeopleActivity.this, 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setListener(new MyAdapter.Listener() {
            @Override
            public void onClick(String characterName) {
                Intent intent = new Intent(FetchPeopleActivity.this, DetailCharacterActivity.class);
                intent.putExtra(App.getCharacterDetailInformation(), characterName);
                startActivity(intent);
            }
        });
        RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItems = layoutManager.findLastVisibleItemPosition();
                if (!swipeRefreshLayout.isRefreshing() && totalItemCount < loader.getTotalCharacters()) {
                    if (totalItemCount <= lastVisibleItems + 5) {
                        swipeRefreshLayout.setRefreshing(true);
                        if (loader != null) {
                            requestNextItems();
                        }
                    }
                }
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
    }

    public void setDataInAdapter(List<Character> characters) {
        adapter.setData(characters);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loader.addLoadListener(this);
        loader.readListCharactersFromDb();
    }

    @Override
    protected void onStop() {
        super.onStop();
        loader.removeLoadListener(this);
        savePref();
    }

    private void requestNextItems() {
        loader.loadMore();
    }

    public void savePref() {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putInt(SAVED_TOTAL_CHARACTERS, loader.getTotalCharacters());
        ed.apply();
    }

    public void loadPref() {
        sPref = getPreferences(MODE_PRIVATE);
        loader.setTotalCharacters(sPref.getInt(SAVED_TOTAL_CHARACTERS, 0));
    }

    @Override
    public void onCharactersLoaded(List<Character> characters) {
        setDataInAdapter(characters);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onCharactersLoadingError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
