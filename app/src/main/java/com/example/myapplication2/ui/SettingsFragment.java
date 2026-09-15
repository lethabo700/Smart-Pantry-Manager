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
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsFragment extends Fragment {

    private SwitchMaterial switchAlerts;
    private RadioGroup radioGroupUnits;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        switchAlerts = view.findViewById(R.id.switch_expiry_alerts);
        radioGroupUnits = view.findViewById(R.id.radio_group_units);
        
        sharedPreferences = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);

        // Load settings
        switchAlerts.setChecked(sharedPreferences.getBoolean("expiry_alerts", true));
        String unitPref = sharedPreferences.getString("unit_pref", "metric");
        if (unitPref.equals("metric")) {
            ((RadioButton)view.findViewById(R.id.radio_metric)).setChecked(true);
        } else {
            ((RadioButton)view.findViewById(R.id.radio_imperial)).setChecked(true);
        }

        // Save settings
        switchAlerts.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean("expiry_alerts", isChecked).apply();
        });

        radioGroupUnits.setOnCheckedChangeListener((group, checkedId) -> {
            String pref = (checkedId == R.id.radio_metric) ? "metric" : "imperial";
            sharedPreferences.edit().putString("unit_pref", pref).apply();
        });

        return view;
    }
}
