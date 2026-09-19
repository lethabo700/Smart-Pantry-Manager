package com.example.myapplication2.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication2.R;
import com.example.myapplication2.databinding.ActivityRecipeDetailBinding;
import com.example.myapplication2.model.Recipe;
import com.example.myapplication2.model.RecipeIngredient;
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

        int recipeId = getIntent().getIntExtra("recipe_id", -1);
        if (recipeId != -1) {
            loadRecipeDetails(recipeId);
        }
    }

    private void loadRecipeDetails(int recipeId) {
        PantryDatabase.databaseWriteExecutor.execute(() -> {
            Recipe recipe = viewModel.getRecipeById(recipeId);
            List<RecipeIngredient> ingredients = viewModel.getIngredientsForRecipe(recipeId);

            runOnUiThread(() -> {
                if (recipe != null) {
                    binding.textDetailName.setText(recipe.getName());
                    binding.textDetailInstructions.setText(recipe.getInstructions());
                    
                    StringBuilder sb = new StringBuilder();
                    for (RecipeIngredient ri : ingredients) {
                        sb.append("• ").append(ri.getIngredientName())
                          .append(": ").append(ri.getRequiredQuantity())
                          .append(" ").append(ri.getUnit()).append("\n");
                    }
                    binding.textDetailIngredients.setText(sb.toString());
                }
            });
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
