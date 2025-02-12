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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

public class ShopProductsCustomAdapter extends RecyclerView.Adapter<ShopProductsCustomAdapter.ViewHolder> {

    JSONArray data;
    private final Context context;
    String serverIp = BuildConfig.SERVER_IP;

    public ShopProductsCustomAdapter(Context context, JSONArray data){
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ShopProductsCustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopProductsCustomAdapter.ViewHolder holder, int position) {
        try {
            JSONObject obj = data.getJSONObject(position);
            holder.getNameView().setText(obj.getString("name"));
            holder.getProdDesc().setText(obj.getString("description"));
            double price = obj.getDouble("price");
            String formattedPrice = String.format(Locale.US, "%.2f$", price);
            holder.getProductPrice().setText(formattedPrice);
            Log.i("this", serverIp+obj.getString("image_url"));
            Glide.with(context)
                    .load("http://"+serverIp+obj.getString("image_url"))
                    .placeholder(R.drawable.shop_images_ph)
                    .error(R.drawable.shop_images_ph)
                    .into(holder.getProdImage());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return data.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView productName, productDesc, productPrice;
        private ImageView productImage;
        public ViewHolder(@NonNull View view) {
            super(view);
            productName = (TextView) view.findViewById(R.id.productName);
            productDesc = (TextView) view.findViewById(R.id.productDescription);
            productImage = (ImageView) view.findViewById(R.id.productImage);
            productPrice = (TextView) view.findViewById(R.id.productPrice);
                view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), productName.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        public TextView getNameView() {
            return productName;
        }
        public ImageView getProdImage() {
            return productImage;
        }
        public TextView getProdDesc() {
            return productDesc;
        }

        public TextView getProductPrice() {
            return productPrice;
        }
    }
}
