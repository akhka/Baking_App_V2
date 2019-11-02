package com.example.bakingappv2.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.bakingappv2.data.model.Recipe;

@Database(entities = Recipe.class, version = 2, exportSchema = false)
@TypeConverters({IngredientListConverter.class, StepListConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public static final String DB_NAME = "recipe_db";

    public abstract RecipeDao recipeDao();

}
