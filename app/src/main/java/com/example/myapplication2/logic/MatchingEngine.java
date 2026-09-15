package com.example.myapplication2.logic;

import com.example.myapplication2.model.Ingredient;
import com.example.myapplication2.model.RecipeIngredient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchingEngine {

    /**
     * Checks if a recipe can be made with the current pantry.
     * Strict matching: every required ingredient must be present in sufficient quantity.
     */
    public static boolean canMakeRecipe(List<RecipeIngredient> required, List<Ingredient> pantry) {
        Map<String, Double> pantryMap = new HashMap<>();
        for (Ingredient item : pantry) {
            String normalizedName = normalize(item.getName());
            pantryMap.put(normalizedName, pantryMap.getOrDefault(normalizedName, 0.0) + item.getQuantity());
        }

        for (RecipeIngredient req : required) {
            String normalizedReqName = normalize(req.getIngredientName());
            Double availableQuantity = pantryMap.get(normalizedReqName);

            if (availableQuantity == null || availableQuantity < req.getRequiredQuantity()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Normalizes ingredient names to handle simple pluralization and case differences.
     */
    public static String normalize(String name) {
        if (name == null) return "";
        String normalized = name.trim().toLowerCase();
        
        // Simple plural handling (very basic)
        if (normalized.endsWith("es")) {
            return normalized.substring(0, normalized.length() - 2);
        } else if (normalized.endsWith("s")) {
            return normalized.substring(0, normalized.length() - 1);
        }
        
        return normalized;
    }
}
