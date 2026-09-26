package com.example.myapplication2.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.myapplication2.dao.PantryDao;
import com.example.myapplication2.dao.RecipeDao;
import com.example.myapplication2.model.Ingredient;
import com.example.myapplication2.model.Recipe;
import com.example.myapplication2.model.RecipeIngredient;
import com.example.myapplication2.model.RecipeWithIngredients;

import java.util.List;

public class IngredientRepository {
    private PantryDao pantryDao;
    private RecipeDao recipeDao;
    private LiveData<List<Ingredient>> allIngredients;
    private LiveData<List<Recipe>> allRecipes;

    public IngredientRepository(Application application) {
        PantryDatabase db = PantryDatabase.getDatabase(application);
        pantryDao = db.pantryDao();
        recipeDao = db.recipeDao();
        allIngredients = pantryDao.getAllIngredients();
        allRecipes = recipeDao.getAllRecipes();
    }

    public LiveData<List<Ingredient>> getAllIngredients() { return allIngredients; }
    public LiveData<List<Recipe>> getAllRecipes() { return allRecipes; }

    public void insert(Ingredient ingredient) {
        PantryDatabase.databaseWriteExecutor.execute(() -> pantryDao.insert(ingredient));
    }

    public void update(Ingredient ingredient) {
        PantryDatabase.databaseWriteExecutor.execute(() -> pantryDao.update(ingredient));
    }

    public void delete(Ingredient ingredient) {
        PantryDatabase.databaseWriteExecutor.execute(() -> pantryDao.delete(ingredient));
    }

    public List<Ingredient> getAllIngredientsSync() { return pantryDao.getAllIngredientsSync(); }
    public List<Recipe> getAllRecipesSync() { return recipeDao.getAllRecipesSync(); }
    public List<RecipeWithIngredients> getAllRecipesWithIngredientsSync() { return recipeDao.getAllRecipesWithIngredientsSync(); }
    public List<RecipeIngredient> getIngredientsForRecipe(int recipeId) { return recipeDao.getIngredientsForRecipe(recipeId); }
    public Recipe getRecipeById(int recipeId) { return recipeDao.getRecipeById(recipeId); }
}
