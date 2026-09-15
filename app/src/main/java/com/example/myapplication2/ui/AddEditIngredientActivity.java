package com.example.myapplication2.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication2.R;
import com.example.myapplication2.model.Ingredient;
import com.example.myapplication2.viewmodel.PantryViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEditIngredientActivity extends AppCompatActivity {

    private TextInputEditText editName, editQuantity, editUnit;
    private Button buttonPickDate, buttonSave;
    private long selectedExpiryDate = 0;
    private PantryViewModel viewModel;
    private int ingredientId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        editName = findViewById(R.id.edit_text_name);
        editQuantity = findViewById(R.id.edit_text_quantity);
        editUnit = findViewById(R.id.edit_text_unit);
        buttonPickDate = findViewById(R.id.button_pick_date);
        buttonSave = findViewById(R.id.button_save);

        viewModel = new ViewModelProvider(this).get(PantryViewModel.class);

        if (getIntent().hasExtra("id")) {
            ingredientId = getIntent().getIntExtra("id", -1);
            editName.setText(getIntent().getStringExtra("name"));
            editQuantity.setText(String.valueOf(getIntent().getDoubleExtra("quantity", 0.0)));
            editUnit.setText(getIntent().getStringExtra("unit"));
            selectedExpiryDate = getIntent().getLongExtra("expiry", 0);
            if (selectedExpiryDate > 0) {
                updateDateButton();
            }
        }

        buttonPickDate.setOnClickListener(v -> showDatePicker());
        buttonSave.setOnClickListener(v -> saveIngredient());
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
        buttonPickDate.setText(sdf.format(new Date(selectedExpiryDate)));
    }

    private void saveIngredient() {
        String name = editName.getText().toString().trim();
        String quantityStr = editQuantity.getText().toString().trim();
        String unit = editUnit.getText().toString().trim();

        if (name.isEmpty()) {
            editName.setError("Name is required");
            return;
        }
        if (quantityStr.isEmpty()) {
            editQuantity.setError("Quantity is required");
            return;
        }
        double quantity;
        try {
            quantity = Double.parseDouble(quantityStr);
        } catch (NumberFormatException e) {
            editQuantity.setError("Invalid quantity");
            return;
        }
        if (unit.isEmpty()) {
            editUnit.setError("Unit is required");
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
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
