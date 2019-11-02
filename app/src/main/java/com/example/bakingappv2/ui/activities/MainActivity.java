package com.example.bakingappv2.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.bakingappv2.R;
import com.example.bakingappv2.ui.fragments.RecipeFragment;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // Add fragment to activity
        FragmentManager fManager = getSupportFragmentManager();
        RecipeFragment recipeFragment = fManager.findFragmentByTag("com.example.bakingappv2.ui.fragments.")
    }
}
