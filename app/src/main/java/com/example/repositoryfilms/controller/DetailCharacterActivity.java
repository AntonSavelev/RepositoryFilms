package com.example.repositoryfilms.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.repositoryfilms.R;
import com.example.repositoryfilms.managers.App;
import com.example.repositoryfilms.model.Character;
import com.example.repositoryfilms.utils.LoadDetailCharacterListener;
import com.example.repositoryfilms.utils.Loader;

public class DetailCharacterActivity extends AppCompatActivity implements LoadDetailCharacterListener {
    Loader loader;
    TextView tw_name;
    TextView tw_height;
    TextView tw_mass;
    TextView tw_height_color;
    TextView tw_skin_color;
    TextView tw_eye_color;
    TextView tw_birth_year;
    TextView tw_gender;
    TextView tw_homeworld;
    TextView tw_created;
    TextView tw_edited;
    TextView tw_url;
    Character character;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        loader = App.getLoader();
        findTextView();
        loadDetailCharacterInfo();
    }

    public void loadDetailCharacterInfo() {
        String characterName = getIntent().getStringExtra(App.getCharacterDetailInformation());
        character = loader.readCharacterDetailFromDb(characterName);
        setDetailInformation(character);
        if (!isCharacterDetail(character))
            character = loader.loadCharacterDetail(characterName);
    }

    public void findTextView() {
        tw_name = (TextView) findViewById(R.id.tw_name);
        tw_height = (TextView) findViewById(R.id.tw_height);
        tw_mass = (TextView) findViewById(R.id.tw_mass);
        tw_height_color = (TextView) findViewById(R.id.tw_hair_color);
        tw_skin_color = (TextView) findViewById(R.id.tw_skin_color);
        tw_eye_color = (TextView) findViewById(R.id.tw_eye_color);
        tw_birth_year = (TextView) findViewById(R.id.tw_birth_year);
        tw_gender = (TextView) findViewById(R.id.tw_gender);
        tw_homeworld = (TextView) findViewById(R.id.tw_homeworld);
        tw_created = (TextView) findViewById(R.id.tw_created);
        tw_edited = (TextView) findViewById(R.id.tw_edited);
        tw_url = (TextView) findViewById(R.id.tw_url);
    }

    public void setDetailInformation(Character character) {
        tw_name.setText(character.getName());
        tw_height.setText("Height:  " + character.getHeight());
        tw_mass.setText("Mass:  " + character.getMass());
        tw_height_color.setText("Height color:  " + character.getHairColor());
        tw_skin_color.setText("Skin color:  " + character.getSkinColor());
        tw_eye_color.setText("Eye color:  " + character.getEyeColor());
        tw_birth_year.setText("Birth year:  " + character.getBirthYear());
        tw_gender.setText("Gender:  " + character.getGender());
        tw_homeworld.setText("Homeworld:  " + character.getHomeworld());
        tw_created.setText("Created:  " + character.getCreated());
        tw_edited.setText("Edited:  " + character.getEdited());
        tw_url.setText("URL:  " + character.getUrl());
    }

    public boolean isCharacterDetail(Character character) {
        String url = character.getUrl();
        if (url == null) {
            return false;
        } else return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        loader.addLoadDetailListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        loader.removeLoadDetailListener(this);
    }

    @Override
    public void onDetailCharacterLoaded(Character character) {
        setDetailInformation(character);
        loader.saveCharacterDetailToDb(character);
    }

    @Override
    public void onDetailCharacterLoadingError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
