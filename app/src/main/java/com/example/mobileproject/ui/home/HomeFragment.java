package com.example.mobileproject.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobileproject.BuildConfig;
import com.example.mobileproject.HomeShopsCustomAdapter;
import com.example.mobileproject.ImageAdapter;
import com.example.mobileproject.MainActivity;
import com.example.mobileproject.R;
import com.example.mobileproject.databinding.FragmentHomeBinding;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ViewPager2 viewPager;
    private ImageAdapter imageAdapter;
    private DotsIndicator dotsIndicator;
    private RecyclerView recyclerView;
    String serverIp = BuildConfig.SERVER_IP;
    int TIMEOUT_MS = 10000;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize ViewPager2 and DotsIndicator
        viewPager = binding.viewPager;
        dotsIndicator = binding.dotsIndicator;

        recyclerView = binding.homeRecView;

        // Image resources for the carousel
        List<Integer> images = Arrays.asList(
                R.drawable.sales_image2,  // Replace with your actual image resources
                R.drawable.sales_image1,
                R.drawable.sales_image2
        );

        // Set up the adapter for ViewPager2
        imageAdapter = new ImageAdapter(images);
        viewPager.setAdapter(imageAdapter);

        // Attach the DotsIndicator to ViewPager2 using setViewPager2
        dotsIndicator.setViewPager2(viewPager);

        String url = "http://"+serverIp+"/storage_bucket/get_all_shops.php";
        RequestQueue queue = Volley.newRequestQueue(requireContext());

            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    HomeShopsCustomAdapter adapter = new HomeShopsCustomAdapter(getContext(), response);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(adapter);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Volley", error.toString());
                }
            });

        request.setRetryPolicy(new DefaultRetryPolicy(
                TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(request);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
