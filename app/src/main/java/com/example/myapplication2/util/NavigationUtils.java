package com.example.myapplication2.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.myapplication2.R;
import com.example.myapplication2.model.Ingredient;
import com.example.myapplication2.model.Recipe;
import com.example.myapplication2.ui.AddEditIngredientActivity;
import com.example.myapplication2.ui.RecipeDetailActivity;

public class NavigationUtils {

    private NavigationUtils() {
        // Private constructor to prevent instantiation
    }

    public static void navigateToAddIngredient(Context context, Activity hostActivity) {
        if (context == null) return;
        Intent intent = new Intent(context, AddEditIngredientActivity.class);
        context.startActivity(intent);
        applyForwardTransition(hostActivity);
    }

    public static void navigateToEditIngredient(Context context, Activity hostActivity, Ingredient ingredient) {
        if (context == null || ingredient == null) return;
        Intent intent = new Intent(context, AddEditIngredientActivity.class);
        intent.putExtra(Constants.EXTRA_INGREDIENT_ID, ingredient.getId());
        intent.putExtra(Constants.EXTRA_INGREDIENT_NAME, ingredient.getName());
        intent.putExtra(Constants.EXTRA_INGREDIENT_QUANTITY, ingredient.getQuantity());
        intent.putExtra(Constants.EXTRA_INGREDIENT_UNIT, ingredient.getUnit());
        intent.putExtra(Constants.EXTRA_INGREDIENT_EXPIRY, ingredient.getExpiryDate());
        intent.putExtra(Constants.EXTRA_INGREDIENT_CATEGORY, ingredient.getCategory());
        context.startActivity(intent);
        applyForwardTransition(hostActivity);
    }

    public static void navigateToRecipeDetail(Context context, Activity hostActivity, Recipe recipe) {
        if (context == null || recipe == null) return;
        Intent intent = new Intent(context, RecipeDetailActivity.class);
        intent.putExtra(Constants.EXTRA_RECIPE_ID, recipe.getId());
        context.startActivity(intent);
        applyForwardTransition(hostActivity);
    }

    public static void applyForwardTransition(Activity activity) {
        if (activity != null) {
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    public static void applyBackwardTransition(Activity activity) {
        if (activity != null) {
            activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }
}
