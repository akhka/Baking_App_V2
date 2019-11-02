package com.example.bakingappv2.data;

import androidx.lifecycle.LiveData;

import com.example.bakingappv2.data.model.Recipe;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RecipeRepository {

    private final RecipeDao mRecipeDao;
    private final NetworkDataSource mNetworkDataSource;
    private AppExecutors mExecutors;
    private boolean mInitialized = false;

    @Inject
    public RecipeRepository(RecipeDao recipeDao, NetworkDataSource networkDataSource, AppExecutors executors) {
        mRecipeDao = recipeDao;
        mNetworkDataSource = networkDataSource;
        mExecutors = executors;
        // As long as the repository exists, observe the network LiveData.
        // If that LiveData changes, update the database.
        LiveData<List<Recipe>> networkData = mNetworkDataSource.getRecipes();
        networkData.observeForever(newRecipes -> mExecutors.diskIO().execute(() -> {
            // delete old data
            deleteOldData();
            // Insert new data
            mRecipeDao.bulkInsert(newRecipes);
        }));
    }

    private synchronized void initializeData() {
        // Only perform initialization once per app lifetime. If initialization has already been
        // performed, we have nothing to do in this method.
        if (mInitialized) return;
        mInitialized = true;
        startFetchRecipeService();
    }

    // get all Recipes
    public LiveData<List<Recipe>> getAllRecipes() {
        initializeData();
        return mRecipeDao.getAllRecipes();
    }

    public void retryFetch() {
        mNetworkDataSource.fetchRecipes();
    }

    // Delete old data
    private void deleteOldData() {
        mRecipeDao.deleteAllRecipes();
    }

    // get recipe by Id
    public Recipe getRecipe(int id) {
        return mRecipeDao.getSelectedRecipe(id);
    }

    // Fetch recipes in the background
    private void startFetchRecipeService() {
        mExecutors.diskIO().execute(mNetworkDataSource::startFetchRecipeService);
    }

}
