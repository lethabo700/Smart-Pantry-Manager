package com.example.myapplication2.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import androidx.core.content.ContextCompat;

import com.example.myapplication2.R;
import com.example.myapplication2.databinding.ItemIngredientBinding;
import com.example.myapplication2.model.Ingredient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class IngredientAdapter extends ListAdapter<Ingredient, IngredientAdapter.IngredientViewHolder> {

    private OnIngredientClickListener listener;

    public interface OnIngredientClickListener {
        void onIngredientClick(Ingredient ingredient);
        void onDeleteClick(Ingredient ingredient);
    }

    public IngredientAdapter(@NonNull DiffUtil.ItemCallback<Ingredient> diffCallback, OnIngredientClickListener listener) {
        super(diffCallback);
        this.listener = listener;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemIngredientBinding binding = ItemIngredientBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new IngredientViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient current = getItem(position);
        holder.bind(current, listener);
    }

    public Ingredient getIngredientAt(int position) {
        return getItem(position);
    }

    static class IngredientViewHolder extends RecyclerView.ViewHolder {
        private final ItemIngredientBinding binding;

        public IngredientViewHolder(@NonNull ItemIngredientBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Ingredient ingredient, OnIngredientClickListener listener) {
            binding.textName.setText(ingredient.getName());
            binding.textQuantity.setText(ingredient.getQuantity() + " " + ingredient.getUnit());
            
            if (ingredient.getExpiryDate() > 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                binding.textExpiry.setText("Expires: " + sdf.format(new Date(ingredient.getExpiryDate())));
                binding.textExpiry.setVisibility(View.VISIBLE);
                
                // Expiry Alert Logic
                long currentTime = System.currentTimeMillis();
                long threeDaysInMillis = 3L * 24 * 60 * 60 * 1000;
                
                int colorRes;
                if (ingredient.getExpiryDate() < currentTime) {
                    colorRes = R.color.expiry_red; // Expired
                } else if (ingredient.getExpiryDate() < currentTime + threeDaysInMillis) {
                    colorRes = R.color.expiry_orange; // Expiring soon (within 3 days)
                } else {
                    colorRes = R.color.expiry_green; // Fresh
                }
                binding.expiryIndicator.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), colorRes));
            } else {
                binding.textExpiry.setVisibility(View.GONE);
                binding.expiryIndicator.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.expiry_green));
            }

            String category = ingredient.getCategory();
            if (category != null && !category.trim().isEmpty()) {
                binding.textCategory.setText("[" + category + "]");
                binding.textCategory.setVisibility(View.VISIBLE);
            } else {
                binding.textCategory.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(v -> listener.onIngredientClick(ingredient));
            binding.buttonDelete.setOnClickListener(v -> listener.onDeleteClick(ingredient));
        }
    }

    public static class IngredientDiff extends DiffUtil.ItemCallback<Ingredient> {
        @Override
        public boolean areItemsTheSame(@NonNull Ingredient oldItem, @NonNull Ingredient newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Ingredient oldItem, @NonNull Ingredient newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getQuantity() == newItem.getQuantity() &&
                    oldItem.getUnit().equals(newItem.getUnit()) &&
                    oldItem.getExpiryDate() == newItem.getExpiryDate() &&
                    Objects.equals(oldItem.getCategory(), newItem.getCategory());
        }
    }
}
