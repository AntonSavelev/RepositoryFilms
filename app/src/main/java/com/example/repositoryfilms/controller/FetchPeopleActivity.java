package com.example.repositoryfilms.controller;

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

    private MyAdapter adapter;
    private Loader loader;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        if (savedInstanceState != null) {
            loader = (Loader) savedInstanceState.getSerializable("Loader");
        } else {
            loader = App.getLoader();
        }

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestNextItems();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.r_view);
        final GridLayoutManager layoutManager = new GridLayoutManager(FetchPeopleActivity.this, 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);
        RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItems = layoutManager.findLastVisibleItemPosition();
                if (!isLoading) {
                    if (totalItemCount <= lastVisibleItems + 5) {
                        isLoading = true;
                        if (loader != null) {
                            requestNextItems();
                        }
                    }
                }
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("Loader", loader);
    }

    public void setDataInAdapter(List<Character> characters) {
        adapter.setData(characters);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loader.addLoadListener(this);
        loader.readDB();
    }

    @Override
    protected void onStop() {
        super.onStop();
        loader.removeLoadListener(this);
    }

    private void requestNextItems() {
        loader.loadMore();
    }

    @Override
    public void onCharactersLoaded(List<Character> characters) {
        setDataInAdapter(characters);
        isLoading = false;
    }

    @Override
    public void onCharactersLoadingError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
