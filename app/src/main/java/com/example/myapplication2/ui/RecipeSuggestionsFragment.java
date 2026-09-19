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
import com.example.myapplication2.databinding.FragmentRecipeSuggestionsBinding;
import com.example.myapplication2.model.Recipe;
import com.example.myapplication2.viewmodel.RecipeViewModel;

public class RecipeSuggestionsFragment extends Fragment {

    private RecipeViewModel viewModel;
    private RecipeAdapter adapter;
    private FragmentRecipeSuggestionsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRecipeSuggestionsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        RecyclerView recyclerView = binding.recyclerViewRecipes;
        TextView textNoMatches = binding.textNoMatches;

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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.updateSuggestions();
        }
    }
}
