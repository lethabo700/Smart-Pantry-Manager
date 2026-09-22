package com.example.myapplication2.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication2.databinding.ActivityAddEditIngredientBinding;
import com.example.myapplication2.model.Ingredient;
import com.example.myapplication2.util.Constants;
import com.example.myapplication2.util.NavigationUtils;
import com.example.myapplication2.viewmodel.PantryViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEditIngredientActivity extends AppCompatActivity {

    private ActivityAddEditIngredientBinding binding;
    private long selectedExpiryDate = 0;
    private PantryViewModel viewModel;
    private int ingredientId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEditIngredientBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(PantryViewModel.class);

        if (getIntent().hasExtra(Constants.EXTRA_INGREDIENT_ID)) {
            ingredientId = getIntent().getIntExtra(Constants.EXTRA_INGREDIENT_ID, -1);
            binding.editTextName.setText(getIntent().getStringExtra(Constants.EXTRA_INGREDIENT_NAME));
            binding.editTextQuantity.setText(String.valueOf(getIntent().getDoubleExtra(Constants.EXTRA_INGREDIENT_QUANTITY, 0.0)));
            binding.editTextUnit.setText(getIntent().getStringExtra(Constants.EXTRA_INGREDIENT_UNIT));
            selectedExpiryDate = getIntent().getLongExtra(Constants.EXTRA_INGREDIENT_EXPIRY, 0);
            if (selectedExpiryDate > 0) {
                updateDateButton();
            }
        }

        binding.buttonPickDate.setOnClickListener(v -> showDatePicker());
        binding.buttonSave.setOnClickListener(v -> saveIngredient());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        if (selectedExpiryDate > 0) {
            calendar.setTimeInMillis(selectedExpiryDate);
        }
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            selectedExpiryDate = calendar.getTimeInMillis();
            updateDateButton();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateDateButton() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        binding.buttonPickDate.setText(sdf.format(new Date(selectedExpiryDate)));
    }

    private void saveIngredient() {
        String name = binding.editTextName.getText().toString().trim();
        String quantityStr = binding.editTextQuantity.getText().toString().trim();
        String unit = binding.editTextUnit.getText().toString().trim();

        if (name.isEmpty()) {
            binding.editTextName.setError("Name is required");
            return;
        }
        if (quantityStr.isEmpty()) {
            binding.editTextQuantity.setError("Quantity is required");
            return;
        }
        double quantity;
        try {
            quantity = Double.parseDouble(quantityStr);
        } catch (NumberFormatException e) {
            binding.editTextQuantity.setError("Invalid quantity");
            return;
        }
        if (unit.isEmpty()) {
            binding.editTextUnit.setError("Unit is required");
            return;
        }

        Ingredient ingredient = new Ingredient(name, quantity, unit, selectedExpiryDate);
        if (ingredientId != -1) {
            ingredient.setId(ingredientId);
            viewModel.update(ingredient);
            Toast.makeText(this, "Ingredient updated", Toast.LENGTH_SHORT).show();
        } else {
            viewModel.insert(ingredient);
            Toast.makeText(this, "Ingredient added", Toast.LENGTH_SHORT).show();
        }
        finish();
        NavigationUtils.applyBackwardTransition(this);
    }
}
