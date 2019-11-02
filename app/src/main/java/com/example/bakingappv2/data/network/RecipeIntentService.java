package com.example.bakingappv2.data.network;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.example.bakingappv2.BakingApp;

import javax.inject.Inject;

public class RecipeIntentService extends IntentService {

    @Inject
    NetworkDataSource networkDataSource;
    public RecipeIntentService() {
        super("RecipeIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        BakingApp.getComponent(this).inject(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        networkDataSource.fetchRecipes();
    }

}
