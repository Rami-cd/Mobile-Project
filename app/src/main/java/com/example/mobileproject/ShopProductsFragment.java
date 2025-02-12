package com.example.mobileproject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobileproject.databinding.FragmentShopProductsBinding;

import org.json.JSONArray;

public class ShopProductsFragment extends Fragment {

    FragmentShopProductsBinding binding;
    String serverIp = BuildConfig.SERVER_IP;
    int TIMEOUT_MS = 10000;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentShopProductsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.productsRecView;

        Bundle arguments = getArguments();
        if (arguments != null) {
            int shop_id = arguments.getInt("shop_id");
//            binding.first.setText(""+value);
            String url = "http://"+serverIp+"/storage_bucket/get_shop_items.php?id="+shop_id;
            RequestQueue queue = Volley.newRequestQueue(requireContext());

            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            if(response.length()==0) {
                                binding.first.setText("No Products in this shop");
                            }
                            ShopProductsCustomAdapter adapter = new ShopProductsCustomAdapter(getContext(), response);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(adapter);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("here", "1");
                            Log.e("Volley", error.toString());
                        }
                    });

            request.setRetryPolicy(new DefaultRetryPolicy(
                    TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(request);
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Ensure that the Up button is disabled when the fragment is destroyed
        if (requireActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) requireActivity();
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }


}

