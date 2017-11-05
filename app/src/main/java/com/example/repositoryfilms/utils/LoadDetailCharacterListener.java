package com.example.repositoryfilms.utils;

import com.example.repositoryfilms.model.Character;

public interface LoadDetailCharacterListener {

    void onDetailCharacterLoaded(Character character);

    void onDetailCharacterLoadingError(String errorMessage);
}
