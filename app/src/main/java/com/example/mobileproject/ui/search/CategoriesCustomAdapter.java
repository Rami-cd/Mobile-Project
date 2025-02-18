package com.example.mobileproject.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobileproject.BuildConfig;
import com.example.mobileproject.R;
import com.example.mobileproject.ui.search.repository.Category;

import java.util.List;

public class CategoriesCustomAdapter extends RecyclerView.Adapter<CategoriesCustomAdapter.ViewHolder> {
    private Context context;
    private List<Category> categories; // List of Category objects
    private String serverIp = BuildConfig.SERVER_IP;

    // Constructor to initialize context and categories list
    public CategoriesCustomAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    // Method to update the categories list
    public void updateCategories(List<Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoriesCustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesCustomAdapter.ViewHolder holder, int position) {
        // Get the current Category object from the list
        Category category = categories.get(position);

        // Set the data to the UI components
        holder.getNameView().setText(category.getCategory_name());
        holder.setCategoryId(category.getCategory_id());

        // Load the category image using Glide
        Glide.with(context)
                .load("http://" + serverIp + category.getCategory_image_url()) // Assuming URL is stored in the Category object
                .placeholder(R.drawable.shop_images_ph)
                .error(R.drawable.shop_images_ph)
                .into(holder.getCategoryImage());
    }

    @Override
    public int getItemCount() {
        return categories.size(); // Return the size of the categories list
    }

    // ViewHolder class to hold views for each item in the RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryName;
        private ImageView categoryImage;
        private int categoryId;

        public ViewHolder(View view) {
            super(view);
            categoryName = view.findViewById(R.id.category_name);
            categoryImage = view.findViewById(R.id.category_image);

            // Set a click listener for the item
            view.setOnClickListener(v -> {
                Toast.makeText(v.getContext(), categoryName.getText().toString(), Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                bundle.putInt("category_id", categoryId);

                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_SearchFragment_to_CategoryFragment, bundle);
            });
        }

        public TextView getNameView() {
            return categoryName;
        }

        public ImageView getCategoryImage() {
            return categoryImage;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }
    }
}
