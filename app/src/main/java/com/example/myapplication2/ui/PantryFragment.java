package com.example.myapplication2.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication2.databinding.FragmentPantryBinding;
import com.example.myapplication2.model.Ingredient;
import com.example.myapplication2.util.NavigationUtils;
import com.example.myapplication2.viewmodel.PantryViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PantryFragment extends Fragment {

    private PantryViewModel viewModel;
    private IngredientAdapter adapter;
    private FragmentPantryBinding binding;

    private List<Ingredient> fullIngredientList = new ArrayList<>();
    private String currentQuery = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPantryBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        RecyclerView recyclerView = binding.recyclerViewPantry;
        FloatingActionButton fab = binding.fabAddIngredient;

        adapter = new IngredientAdapter(new IngredientAdapter.IngredientDiff(), new IngredientAdapter.OnIngredientClickListener() {
            @Override
            public void onIngredientClick(Ingredient ingredient) {
                NavigationUtils.navigateToEditIngredient(getContext(), getActivity(), ingredient);
            }
            @Override
            public void onDeleteClick(Ingredient ingredient) {
                viewModel.delete(ingredient);
            }
        });

        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Ingredient ingredient = adapter.getIngredientAt(position);
                    viewModel.delete(ingredient);
                    Snackbar.make(binding.getRoot(), ingredient.getName() + " deleted", Snackbar.LENGTH_LONG)
                            .setAction("Undo", v -> viewModel.insert(ingredient))
                            .show();
                }
            }
        }).attachToRecyclerView(recyclerView);

        binding.searchViewPantry.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterIngredients(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterIngredients(newText);
                return true;
            }
        });

        viewModel = new ViewModelProvider(this).get(PantryViewModel.class);
        viewModel.getAllIngredients().observe(getViewLifecycleOwner(), ingredients -> {
            fullIngredientList = ingredients != null ? ingredients : new ArrayList<>();
            filterIngredients(currentQuery);
        });

        fab.setOnClickListener(v -> NavigationUtils.navigateToAddIngredient(getContext(), getActivity()));

        return view;
    }

    private void filterIngredients(String query) {
        currentQuery = query == null ? "" : query.trim().toLowerCase(Locale.getDefault());
        List<Ingredient> filteredList = new ArrayList<>();

        if (fullIngredientList != null) {
            if (currentQuery.isEmpty()) {
                filteredList.addAll(fullIngredientList);
            } else {
                for (Ingredient ingredient : fullIngredientList) {
                    if (ingredient.getName().toLowerCase(Locale.getDefault()).contains(currentQuery)) {
                        filteredList.add(ingredient);
                    }
                }
            }
        }

        adapter.submitList(filteredList);

        if (binding != null) {
            if (filteredList.isEmpty()) {
                binding.textEmptyPantry.setText(currentQuery.isEmpty() ? "Your pantry is empty!" : "No matching ingredients found");
                binding.textEmptyPantry.setVisibility(View.VISIBLE);
            } else {
                binding.textEmptyPantry.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
