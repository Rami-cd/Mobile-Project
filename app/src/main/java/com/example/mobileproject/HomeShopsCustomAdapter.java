package com.example.mobileproject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeShopsCustomAdapter extends RecyclerView.Adapter<HomeShopsCustomAdapter.ViewHolder> {
    private static Context context;
    JSONArray data;
    String serverIp = BuildConfig.SERVER_IP;

    public HomeShopsCustomAdapter(Context context, JSONArray data){
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public HomeShopsCustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_fragment_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            JSONObject obj = data.getJSONObject(position);
            holder.getNameView().setText(obj.getString("name"));
            holder.getShopNumber().setText(obj.getString("contact_number"));
            holder.setShopId(obj.getInt("shop_id"));
            Log.i("path check", serverIp+obj.getString("image_url"));
            Glide.with(context)
                    // you can remove or edit based on the address you are using
                    .load("http://"+serverIp+obj.getString("image_url"))
                    .placeholder(R.drawable.shop_images_ph)
                    .error(R.drawable.shop_images_ph)
                    .into(holder.getShopImage());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return data.length();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView shopName, shopNumber;
        private ImageView shopImage;

        private int shopId;
        public ViewHolder(View view) {
            super(view);
            shopName = (TextView) view.findViewById(R.id.shop_name);
            shopNumber = (TextView) view.findViewById(R.id.shop_number);
            shopImage = (ImageView) view.findViewById(R.id.shop_image);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), shopName.getText().toString(), Toast.LENGTH_SHORT).show();

                    // Create a Bundle to pass data
                    Bundle bundle = new Bundle();
                    bundle.putInt("shop_id", shopId);

                    // Get the NavController
                    NavController navController = Navigation.findNavController(v);

                    // Navigate to the ProductsFragment and pass the data
                    navController.navigate(R.id.action_HomeFragment_to_ProductsFragment, bundle);
                }
            });
        }
        public TextView getNameView() {
            return shopName;
        }
        public ImageView getShopImage() {
            return shopImage;
        }
        public TextView getShopNumber() {
            return shopNumber;
        }
        public void setShopId(int shopId) {
            this.shopId = shopId;
        }
    }
}
