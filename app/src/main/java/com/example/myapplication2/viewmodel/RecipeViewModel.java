package com.example.myapplication2.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication2.db.IngredientRepository;
import com.example.myapplication2.logic.MatchingEngine;
import com.example.myapplication2.model.Ingredient;
import com.example.myapplication2.model.Recipe;
import com.example.myapplication2.model.RecipeIngredient;
import com.example.myapplication2.model.RecipeWithIngredients;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecipeViewModel extends AndroidViewModel {
    private IngredientRepository repository;
    private MutableLiveData<List<Recipe>> suggestedRecipes = new MutableLiveData<>();
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public RecipeViewModel(Application application) {
        super(application);
        repository = new IngredientRepository(application);
    }

    public LiveData<List<Recipe>> getSuggestedRecipes() { return suggestedRecipes; }

    public void updateSuggestions() {
        executor.execute(() -> {
            List<Ingredient> pantry = repository.getAllIngredientsSync();
            List<RecipeWithIngredients> recipesWithIngredients = repository.getAllRecipesWithIngredientsSync();
            List<Recipe> matches = new ArrayList<>();

            if (pantry != null && recipesWithIngredients != null) {
                for (RecipeWithIngredients item : recipesWithIngredients) {
                    if (item != null && item.recipe != null && item.ingredients != null) {
                        if (MatchingEngine.canMakeRecipe(item.ingredients, pantry)) {
                            matches.add(item.recipe);
                        }
                    }
                }
            }
            suggestedRecipes.postValue(matches);
        });
    }
    
    public Recipe getRecipeById(int id) {
        return repository.getRecipeById(id);
    }
    
    public List<RecipeIngredient> getIngredientsForRecipe(int id) {
        return repository.getIngredientsForRecipe(id);
    }
}
