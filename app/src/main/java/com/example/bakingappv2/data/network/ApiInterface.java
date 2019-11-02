package com.example.bakingappv2.data.network;

import com.example.bakingappv2.data.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("baking.json")
    Call<List<Recipe>> getRecipe();

}
