package com.example.bakingappv2.ui.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingappv2.R;
import com.example.bakingappv2.data.model.Recipe;
import com.example.bakingappv2.utils.Utils;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class RecipeFragment extends Fragment implements RecipeAdapter.RecipeClickListener {

    public static final String LIST_STATE_KEY = "list_state";
    private RecipeAdapter recipeAdapter;
    private RecyclerView recipeRecycler;
    private ShimmerFrameLayout shimmer;
    private Parcelable listState;
    private boolean isSet;
    private GridLayoutManager gridLayoutManager;
    private FrameLayout frameLayout;
    private RecipeViewModel recipeViewModel;
    private FragmentRecipeBinding binding;

    @Inject
    RecipeViewModelFactory factory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BakingApp.getComponent(Objects.requireNonNull(getActivity())).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentRecipeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        initViews();
        checkOrientation();
        setupViewModel();
        return view;
    }


    // Initialize views
    public void initViews(){
        frameLayout = binding.mainFrame;
        shimmer = binding.shimmer;
        recipeRecycler = binding.mainRecyclerView;
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recipeRecycler.setLayoutManager(gridLayoutManager);
        recipeRecycler.setHasFixedSize(true);
        recipeRecycler.setItemAnimator(new DefaultItemAnimator());
        recipeRecycler.setNestedScrollingEnabled(true);
        recipeAdapter = new RecipeAdapter(this);
        recipeRecycler.setAdapter(recipeAdapter);
    }

    // ViewModel Setup
    public void setupViewModel(){
        recipeViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), factory).get(RecipeViewModel.class);
        recipeViewModel.getAllRecipes().observe(getContext(), this::setRecipesToAdapter);
    }


    // Get recipes data from database to the adapter
    public void setRecipesToAdapter(List<Recipe>recipes){
        if (recipes != null  &&  recipes.size() != 0){
            recipeAdapter.setRcipeList(recipes);
            if (listState != null){
                gridLayoutManager.onRestoreInstanceState(listState);
            }
            showData();
            shimmer.stopShimmer();
            shimmer.setVisibility(View.GONE);
        }
        else{
            showEmpty();
        }
    }


    // Handling no network condition when empty
    public void showEmpty(){
        if (!isConnected()){
            showIdlePic();
        }
        else{
            shimmer.startShimmer();
        }
        isSet = true;
    }


    // When data available, show recipes
    public void showData(){
        if (isSet){
            shimmer.setVisibility(View.GONE);
            shimmer.stopShimmer();
            isSet = false;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        shimmer.startShimmer();
    }

    @Override
    public void onPause() {
        super.onPause();
        shimmer.stopShimmer();
    }



    // Save recyclerview scroll state
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LIST_STATE_KEY, gridLayoutManager.onSaveInstanceState());
    }

    // Retrieve recyclerview scroll state

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            listState = savedInstanceState.getParcelable(LIST_STATE_KEY);
        }
    }


    // Checking network dtatus
    public boolean isConnected(){
        return Utils.isConnected(Objects.requireNonNull(getActivity()));
    }

    // Show idle image when no network or empty data
    public void showIdlePic(){
        IdlePic.make(frameLayout, R.string.no_internet, IdlePic.LENGTH_INDEFINITE)
                .setAction(R.string.retry, view -> {
                    if (isSet) {
                        recipeViewModel.retryFetch();
                    }
                    showEmpty();
                }).show();
    }

    // Expand more items in landscape mode
    public void checkOrientation(){
        if (recipeRecycler.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            gridLayoutManager = new GridLayoutManager(getContext(), 2);
            recipeRecycler.setLayoutManager(gridLayoutManager);
        }
        else if (recipeRecycler.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            gridLayoutManager = new GridLayoutManager(getContext(), 4);
            recipeRecycler.setLayoutManager(gridLayoutManager);
        }
    }


    @Override
    public void onRecipeClick(Recipe recipe) {
        Intent in = new Intent(getContext(), StepListActivity.class);
        in.putExtra(StepListActivity.INTENT_EXTRA, recipe);
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(in);
    }

}
