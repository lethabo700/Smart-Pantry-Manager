package com.example.myapplication2.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.myapplication2.model.Recipe;
import com.example.myapplication2.model.RecipeIngredient;
import com.example.myapplication2.model.RecipeWithIngredients;

import java.util.List;

@Dao
public interface RecipeDao {
    @Insert
    long insertRecipe(Recipe recipe);

    @Insert
    void insertRecipeIngredient(RecipeIngredient recipeIngredient);

    @Query("SELECT * FROM recipes")
    LiveData<List<Recipe>> getAllRecipes();

    @Query("SELECT * FROM recipes")
    List<Recipe> getAllRecipesSync();

    @Transaction
    @Query("SELECT * FROM recipes")
    List<RecipeWithIngredients> getAllRecipesWithIngredientsSync();

    @Query("SELECT * FROM recipe_ingredients WHERE recipeId = :recipeId")
    List<RecipeIngredient> getIngredientsForRecipe(int recipeId);
    
    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    Recipe getRecipeById(int recipeId);
}
