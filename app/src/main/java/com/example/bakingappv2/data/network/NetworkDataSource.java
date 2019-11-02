package com.example.bakingappv2.data.network;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bakingappv2.AppExecutors;
import com.example.bakingappv2.data.model.Recipe;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkDataSource {

    private static final String LOG_TAG = NetworkDataSource.class.getSimpleName();
    private final Context mContext;

    @Inject
    ApiInterface apiService;

    // LiveData storing the latest downloaded Recipe
    private final MutableLiveData<List<Recipe>> mDownloadedRecipes;
    private final AppExecutors mExecutors;

    @Inject
    public NetworkDataSource(Context context, AppExecutors mExecutors) {
        mContext = context.getApplicationContext();
        this.mExecutors = mExecutors;
        mDownloadedRecipes = new MutableLiveData<>();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mDownloadedRecipes;
    }


    public void startFetchRecipeService() {
        Intent intentToFetch = new Intent(mContext, RecipeIntentService.class);
        mContext.startService(intentToFetch);
    }


    // fetch recipes from api using retrofit
    public void fetchRecipes() {
        mExecutors.networkIO().execute(() -> {
            Call<List<Recipe>> call = apiService.getRecipe();
            call.enqueue(new Callback<List<Recipe>>() {
                @Override
                public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                    int statusCode = response.code();
                    List<Recipe> recipes = response.body();
                    if (recipes != null) {
                        Log.d(LOG_TAG, "Received " + recipes.size() + " recipes");
                    }
                    mDownloadedRecipes.postValue(recipes);
                }
                @Override
                public void onFailure(Call<List<Recipe>> call, Throwable t) {
                    t.printStackTrace();
                    Log.d(LOG_TAG, "onFailure: " + t.getMessage());
                }
            });

        });
    }

}
