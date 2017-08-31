package com.example.repositoryfilms;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class FetchPeopleActivity extends AppCompatActivity implements LoadListener {

    private MyAdapter adapter;
    private Loader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestNextItems();
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
                if (totalItemCount <= lastVisibleItems + 5) {
                    requestNextItems();
                }
            }


        };
        recyclerView.addOnScrollListener(scrollListener);
        loader = App.getLoader();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void setDataInAdapter(List<Character> characters) {
        adapter.setData(characters);
    }


    @Override
    protected void onStart() {
        super.onStart();
        loader.addLoadListener(this);
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
    }

    @Override
    public void onCharactersLoadingError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
