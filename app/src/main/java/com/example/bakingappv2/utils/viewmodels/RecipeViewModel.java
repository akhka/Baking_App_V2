package com.example.bakingappv2.utils.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.bakingappv2.data.RecipeRepository;
import com.example.bakingappv2.data.model.Recipe;

import java.util.List;

import javax.inject.Inject;

public class RecipeViewModel extends ViewModel {

    private RecipeRepository mRecipeRepository;

    private final LiveData<List<Recipe>> mAllRecipes;

    @Inject
    RecipeViewModel(RecipeRepository repository) {
        mRecipeRepository = repository;
        mAllRecipes = repository.getAllRecipes();
    }

    LiveData<List<Recipe>> getAllRecipes() {
        return mAllRecipes;
    }

    void retryFetch() {
        mRecipeRepository.retryFetch();
    }

}
