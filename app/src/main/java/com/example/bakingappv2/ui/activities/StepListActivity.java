package com.example.bakingappv2.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingappv2.R;
import com.example.bakingappv2.data.model.Ingredient;
import com.example.bakingappv2.data.model.Recipe;
import com.example.bakingappv2.data.model.Step;
import com.example.bakingappv2.databinding.ActivityStepListBinding;
import com.example.bakingappv2.ui.fragments.StepDetailFragment;
import com.example.bakingappv2.utils.adapters.DetailAdapter;
import com.example.bakingappv2.widget.RecipeWidgetProvider;

import java.util.ArrayList;
import java.util.List;

public class StepListActivity extends AppCompatActivity implements DetailAdapter.StepClickListener, StepDetailFragment.OnStepClickListener {

    public static final String PREF = "Preferences";
    public static final String INTENT_EXTRA = "recipe";
    public static final String RECIPE_ID = "id";
    public static final String RECIPE_NAME = "name";
    private boolean mTwoPane;
    private int mRecipeId;
    private List<Step> mStepList;
    private String mRecipeName;
    ActivityStepListBinding binding;
    Toolbar toolbar;
    DetailAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_step_list);
        toolbar = binding.stepListToolbar;
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ArrayList<Object> mBakingObjects = new ArrayList<>();

        //Get intent extras
        Intent in = getIntent();
        if (in == null) {
            closeOnError();
        }
        assert in != null;
        if (in.hasExtra(INTENT_EXTRA)) {
            Recipe mRecipe = getIntent().getParcelableExtra(INTENT_EXTRA);
            mRecipeId = mRecipe.getId();
            mRecipeName = mRecipe.getName();
            List<Ingredient> mIngredientList = mRecipe.getIngredients();
            mStepList = mRecipe.getSteps();
            String mRecipeName = mRecipe.getName();
            mBakingObjects.addAll(mIngredientList);
            mBakingObjects.addAll(mStepList);
            setTitle(mRecipeName);
        }

        if (findViewById(R.id.step_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        RecyclerView mRecyclerView = findViewById(R.id.step_list);
        assert mRecyclerView != null;
        mAdapter = new DetailAdapter(mBakingObjects, this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void saveToPref() {
        //Save clicked recipe to shared preferences for the widget
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(RECIPE_ID, mRecipeId);
        editor.putString(RECIPE_NAME, mRecipeName);
        editor.apply();

        //update the widget with current recipe details
        RecipeWidgetProvider.updateWidget(this);
        IdlePic.make(binding.getRoot(), getString(R.string.widget_info, mRecipeName), Snackbar.LENGTH_LONG).show();
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.no_recipe_data, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        } else if (id == R.id.widget_menu) {
            saveToPref();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStepClick(Step step) {
        if (step != null) {
            if (mTwoPane) {
                StepDetailFragment fragment = StepDetailFragment.newInstance(step);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.step_detail_container, fragment)
                        .commit();
            } else {
                Intent intent = new Intent(this, StepDetailActivity.class);
                intent.putExtra(StepDetailActivity.EXTRA, step);
                intent.putExtra(StepDetailActivity.EXTRA_NAME, mRecipeName);
                intent.putParcelableArrayListExtra(StepDetailActivity.EXTRA_LIST, (ArrayList<? extends Parcelable>) mStepList);
                startActivity(intent);
            }

        }

    }

    @Override
    public void onPrevStepClick(Step step) {
    }

    @Override
    public void onNextStepClick(Step step) {
    }

}
