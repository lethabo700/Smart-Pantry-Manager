package com.example.myapplication2.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "ingredients")
public class Ingredient {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String name;
    private double quantity;
    private String unit;
    private long expiryDate; // Timestamp

    @ColumnInfo(defaultValue = "Pantry")
    private String category;

    public Ingredient() {
        // Default constructor for Room
    }

    @Ignore
    public Ingredient(String name, double quantity, String unit, long expiryDate, String category) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
        this.category = category != null && !category.trim().isEmpty() ? category : "Pantry";
    }

    @Ignore
    public Ingredient(String name, double quantity, String unit, long expiryDate) {
        this(name, quantity, unit, expiryDate, "Pantry");
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public long getExpiryDate() { return expiryDate; }
    public void setExpiryDate(long expiryDate) { this.expiryDate = expiryDate; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
