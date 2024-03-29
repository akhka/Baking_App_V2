package com.example.bakingappv2.di.modules;

import android.app.Application;

import androidx.room.Room;

import com.example.bakingappv2.data.database.AppDatabase;
import com.example.bakingappv2.data.database.RecipeDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RoomModule {

    private AppDatabase appDatabase;

    public RoomModule(Application application) {
        appDatabase = Room.databaseBuilder(application, AppDatabase.class, AppDatabase.DB_NAME).build();
    }

    @Singleton
    @Provides
    AppDatabase provideDatabase() {
        return appDatabase;
    }

    @Singleton
    @Provides
    RecipeDao provideDao() {
        return appDatabase.recipeDao();
    }

}
