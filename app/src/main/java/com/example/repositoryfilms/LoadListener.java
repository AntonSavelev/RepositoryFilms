package com.example.repositoryfilms;

import java.util.List;

public interface LoadListener {
    void onCharactersLoaded(List<Character> characters);

    void onCharactersLoadingError(String errorMessage);
}
