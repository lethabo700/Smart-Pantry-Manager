package com.example.myapplication2.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication2.R;
import com.example.myapplication2.model.Recipe;
import com.example.myapplication2.model.RecipeIngredient;
import com.example.myapplication2.viewmodel.RecipeViewModel;
import com.example.myapplication2.db.PantryDatabase;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView textName, textIngredients, textInstructions;
    private RecipeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        textName = findViewById(R.id.text_detail_name);
        textIngredients = findViewById(R.id.text_detail_ingredients);
        textInstructions = findViewById(R.id.text_detail_instructions);

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
                    textName.setText(recipe.getName());
                    textInstructions.setText(recipe.getInstructions());
                    
                    StringBuilder sb = new StringBuilder();
                    for (RecipeIngredient ri : ingredients) {
                        sb.append("• ").append(ri.getIngredientName())
                          .append(": ").append(ri.getRequiredQuantity())
                          .append(" ").append(ri.getUnit()).append("\n");
                    }
                    textIngredients.setText(sb.toString());
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
