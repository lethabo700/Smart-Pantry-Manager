package com.example.myapplication2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication2.R;
import com.example.myapplication2.model.Recipe;
import com.example.myapplication2.viewmodel.RecipeViewModel;

public class RecipeSuggestionsFragment extends Fragment {

    private RecipeViewModel viewModel;
    private RecipeAdapter adapter;
    private TextView textNoMatches;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_suggestions, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_recipes);
        textNoMatches = view.findViewById(R.id.text_no_matches);

        adapter = new RecipeAdapter(new RecipeAdapter.RecipeDiff(), recipe -> {
            Intent intent = new Intent(getContext(), RecipeDetailActivity.class);
            intent.putExtra("recipe_id", recipe.getId());
            startActivity(intent);
            if (getActivity() != null) {
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        viewModel.getSuggestedRecipes().observe(getViewLifecycleOwner(), recipes -> {
            adapter.submitList(recipes);
            textNoMatches.setVisibility(recipes.isEmpty() ? View.VISIBLE : View.GONE);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.updateSuggestions();
        }
    }
}
