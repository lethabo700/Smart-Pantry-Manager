package com.example.myapplication2.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication2.R;
import com.example.myapplication2.databinding.FragmentSettingsBinding;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        
        sharedPreferences = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);

        // Load settings
        binding.switchExpiryAlerts.setChecked(sharedPreferences.getBoolean("expiry_alerts", true));
        String unitPref = sharedPreferences.getString("unit_pref", "metric");
        if (unitPref.equals("metric")) {
            binding.radioMetric.setChecked(true);
        } else {
            binding.radioImperial.setChecked(true);
        }

        // Save settings
        binding.switchExpiryAlerts.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean("expiry_alerts", isChecked).apply();
        });

        binding.radioGroupUnits.setOnCheckedChangeListener((group, checkedId) -> {
            String pref = (checkedId == R.id.radio_metric) ? "metric" : "imperial";
            sharedPreferences.edit().putString("unit_pref", pref).apply();
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
