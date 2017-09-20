package com.example.repositoryfilms.utils;

import com.example.repositoryfilms.model.Character;

import java.util.List;

public interface LoadListener {
    void onCharactersLoaded(List<Character> characters);

    void onCharactersLoadingError(String errorMessage);
}
