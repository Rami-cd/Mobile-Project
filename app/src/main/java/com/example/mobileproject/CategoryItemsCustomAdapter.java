package com.example.mobileproject;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobileproject.datacart.Cart;
import com.example.mobileproject.ui.cart.CartFragment;
import com.example.mobileproject.ui.search.CategoriesCustomAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoryItemsCustomAdapter extends RecyclerView.Adapter<CategoryItemsCustomAdapter.ViewHolder>{
    JSONArray data;
    private static Context context = null;
    String serverIp = BuildConfig.SERVER_IP;

    public CategoryItemsCustomAdapter(Context context, JSONArray data){
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryItemsCustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_row, parent, false);
        return new CategoryItemsCustomAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryItemsCustomAdapter.ViewHolder holder, int position) {
        try {
            JSONObject obj = data.getJSONObject(position);
            Log.i("obj", String.valueOf(obj));
            holder.getNameView().setText(obj.getString("product_name"));
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
            holder.bindData(obj);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return data.length();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView productName;
        private final TextView productDesc;
        private final TextView productPrice;
        private final ImageView productImage;
        private JSONObject productData;
        public ViewHolder(@NonNull View view) {
            super(view);
            productName = (TextView) view.findViewById(R.id.productName);
            productDesc = (TextView) view.findViewById(R.id.productDescription);
            productImage = (ImageView) view.findViewById(R.id.productImage);
            productPrice = (TextView) view.findViewById(R.id.productPrice);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(v.getContext(), productName.getText().toString(), Toast.LENGTH_SHORT).show();
                    try {
                        int product_id = productData.getInt("product_id");
                        int shop_id = productData.getInt("shop_id");
                        String image_url = productData.getString("image_url");
                        String name = productData.getString("product_name");
                        double price = productData.getDouble("price");
                        String description = productData.getString("description");
                        int quantity = productData.getInt("quantity_in_stock");

                        Cart cart_item = new Cart();
                        cart_item.name = name;
                        cart_item.product_id = product_id;
                        Log.i("shop_id", shop_id+"");
                        cart_item.shop_id = shop_id;
                        cart_item.price = price;
                        cart_item.description = description;
                        cart_item.image_url = image_url;
                        cart_item.stock_quantity = quantity;

                        insertInBackground(cart_item);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }

        void insertInBackground(Cart cart) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    // Insert into the database
                    MainActivity.cartDatabase.cartdao().insertAll(cart);

                    // Ensure UI updates on the main thread
                    if (context != null) {
                        Handler mainHandler = new Handler(context.getMainLooper());
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                // UI updates after the background task completes
                                Toast.makeText(context, "Item added to cart", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
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
        public void bindData(JSONObject productData) {
            this.productData = productData;
        }
    }
}
