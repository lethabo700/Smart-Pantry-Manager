package com.example.myapplication2.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication2.databinding.ActivityRecipeDetailBinding;
import com.example.myapplication2.model.Recipe;
import com.example.myapplication2.model.RecipeIngredient;
import com.example.myapplication2.util.Constants;
import com.example.myapplication2.util.NavigationUtils;
import com.example.myapplication2.viewmodel.RecipeViewModel;
import com.example.myapplication2.db.PantryDatabase;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    private ActivityRecipeDetailBinding binding;
    private RecipeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipeDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(RecipeViewModel.class);

        int recipeId = getIntent().getIntExtra(Constants.EXTRA_RECIPE_ID, -1);
        if (recipeId != -1) {
            loadRecipeDetails(recipeId);
        }
    }

    private void loadRecipeDetails(int recipeId) {
        PantryDatabase.databaseWriteExecutor.execute(() -> {
            Recipe recipe = viewModel.getRecipeById(recipeId);
            List<RecipeIngredient> ingredients = viewModel.getIngredientsForRecipe(recipeId);

            runOnUiThread(() -> {
                if (binding != null && recipe != null) {
                    binding.textDetailName.setText(recipe.getName() != null ? recipe.getName() : "");
                    binding.textDetailInstructions.setText(recipe.getInstructions() != null ? recipe.getInstructions() : "");
                    
                    StringBuilder sb = new StringBuilder();
                    if (ingredients != null) {
                        for (RecipeIngredient ri : ingredients) {
                            if (ri != null) {
                                sb.append("• ").append(ri.getIngredientName() != null ? ri.getIngredientName() : "")
                                  .append(": ").append(ri.getRequiredQuantity())
                                  .append(" ").append(ri.getUnit() != null ? ri.getUnit() : "").append("\n");
                            }
                        }
                    }
                    binding.textDetailIngredients.setText(sb.toString());
                }
            });
        });
    }

    @Override
    public void finish() {
        super.finish();
        NavigationUtils.applyBackwardTransition(this);
    }
}
