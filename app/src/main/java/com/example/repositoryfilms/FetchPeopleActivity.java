package com.example.repositoryfilms;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;

public class FetchPeopleActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    GridLayoutManager layoutManager;
    AllPeople allPeople;
    MyAdapter adapter;
    ApiInterface apiInterface;
    ArrayList<People> peoples;
    Button btn;
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    Gson gson;
    Cursor cursor;
    ContentValues cv;
    boolean isLoading = true;
    static int peoplesCount;
    static int peoplesSum = 0;
    public static int i = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        dbHelper = new DBHelper(this);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        gson = new GsonBuilder().create();
        cursor = sqLiteDatabase.query(DBHelper.TABLE_PEOPLES, null, null, null, null, null, null);
        peoples = new ArrayList<>();
        btn = (Button) findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i == 1)
                peoples.clear();
                    new MyAsyncTask().execute();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.r_view);
        layoutManager = new GridLayoutManager(FetchPeopleActivity.this, 1);
        recyclerView.setLayoutManager(layoutManager);

        if(cursor != null){
            peoples.clear();
            readDB();
            setDataInAdapter();
        }

    }

    public void readDB(){
        Toast.makeText(this, "SQLite", Toast.LENGTH_SHORT).show();
        if(cursor.moveToFirst()){
            int filmJsonIndex = cursor.getColumnIndex(DBHelper.KEY_PEOPLE_JSON);
            do{
                peoples.add(gson.fromJson(cursor.getString(filmJsonIndex), People.class));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void writeDB(){
        sqLiteDatabase.delete(DBHelper.TABLE_PEOPLES, null, null);
        Toast.makeText(this, "Web", Toast.LENGTH_SHORT).show();
        cv = new ContentValues();
        for (int j = 0; j < peoples.size(); j++){
            cv.put(DBHelper.KEY_PEOPLE_JSON, gson.toJson(peoples.get(j)));
            sqLiteDatabase.insert(DBHelper.TABLE_PEOPLES, null, cv);
        }
    }

    public void setDataInAdapter(){
        adapter = new MyAdapter(this, peoples);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(scrollListener);
        recyclerView.getAdapter().notifyDataSetChanged();

    }

    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItems = layoutManager.findFirstVisibleItemPosition();
            int lastVisibleItems = layoutManager.findLastVisibleItemPosition();

            if (isLoading) {
                if ((visibleItemCount + firstVisibleItems) >= totalItemCount) {

                    if (i <= (peoplesCount / 10)) {
                        new MyAsyncTask().execute();
                    }else Toast.makeText(FetchPeopleActivity.this, "Error", Toast.LENGTH_SHORT).show();
// Как правильно реализовать работу с флагом (поэтапная прокрутка)?
// Как сделать так, чтобы при добавлении новых элементов при прокруке ни выбрасывало на начало списка?
                    isLoading = false;
                }
                }

            }


    };

    private class MyAsyncTask extends AsyncTask<Void, Void, AllPeople>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            apiInterface = ApiClient.getClient().create(ApiInterface.class);
        }

        @Override
        protected void onPostExecute(AllPeople allPeople) {
            super.onPostExecute(allPeople);

                peoples.addAll(allPeople.getResults());
                setDataInAdapter();
                writeDB();
                i++;
                peoplesCount = Integer.parseInt(allPeople.getCount());
                peoplesSum = peoplesSum + allPeople.getResults().size();

        }

        @Override
        protected AllPeople doInBackground(Void... voids) {
            Call<AllPeople> call = apiInterface.getAllPeoples(i);
                try {
                    allPeople = call.execute().body();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            return allPeople;
        }
    }
}
