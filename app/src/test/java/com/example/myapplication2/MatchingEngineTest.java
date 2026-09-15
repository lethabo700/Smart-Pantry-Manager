package com.example.myapplication2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.myapplication2.logic.MatchingEngine;
import com.example.myapplication2.model.Ingredient;
import com.example.myapplication2.model.RecipeIngredient;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MatchingEngineTest {

    @Test
    public void testStrictMatching_Success() {
        List<RecipeIngredient> required = new ArrayList<>();
        required.add(new RecipeIngredient(1, "Eggs", 2, "pcs"));
        required.add(new RecipeIngredient(1, "Salt", 0.5, "tsp"));

        List<Ingredient> pantry = new ArrayList<>();
        pantry.add(new Ingredient("Eggs", 5, "pcs", 0));
        pantry.add(new Ingredient("Salt", 1, "tsp", 0));

        assertTrue(MatchingEngine.canMakeRecipe(required, pantry));
    }

    @Test
    public void testStrictMatching_MissingIngredient() {
        List<RecipeIngredient> required = new ArrayList<>();
        required.add(new RecipeIngredient(1, "Eggs", 2, "pcs"));
        required.add(new RecipeIngredient(1, "Salt", 0.5, "tsp"));

        List<Ingredient> pantry = new ArrayList<>();
        pantry.add(new Ingredient("Eggs", 5, "pcs", 0));
        // Missing Salt

        assertFalse(MatchingEngine.canMakeRecipe(required, pantry));
    }

    @Test
    public void testStrictMatching_InsufficientQuantity() {
        List<RecipeIngredient> required = new ArrayList<>();
        required.add(new RecipeIngredient(1, "Eggs", 2, "pcs"));

        List<Ingredient> pantry = new ArrayList<>();
        pantry.add(new Ingredient("Eggs", 1, "pcs", 0));

        assertFalse(MatchingEngine.canMakeRecipe(required, pantry));
    }

    @Test
    public void testNormalization_Plural() {
        List<RecipeIngredient> required = new ArrayList<>();
        required.add(new RecipeIngredient(1, "Tomato", 2, "pcs"));

        List<Ingredient> pantry = new ArrayList<>();
        pantry.add(new Ingredient("Tomatoes", 5, "pcs", 0));

        assertTrue(MatchingEngine.canMakeRecipe(required, pantry));
    }
}
