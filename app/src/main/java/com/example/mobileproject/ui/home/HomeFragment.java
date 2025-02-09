package com.example.mobileproject.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mobileproject.ImageAdapter;
import com.example.mobileproject.R;
import com.example.mobileproject.databinding.FragmentHomeBinding;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ViewPager2 viewPager;
    private ImageAdapter imageAdapter;
    private DotsIndicator dotsIndicator;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize ViewPager2 and DotsIndicator
        viewPager = binding.viewPager;
        dotsIndicator = binding.dotsIndicator;

        // Observe ViewModel and set text
        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Image resources for the carousel
        List<Integer> images = Arrays.asList(
                R.drawable.image1,  // Replace with your actual image resources
                R.drawable.image2,
                R.drawable.image3
        );

        // Set up the adapter for ViewPager2
        imageAdapter = new ImageAdapter(images);
        viewPager.setAdapter(imageAdapter);

        // Attach the DotsIndicator to ViewPager2 using setViewPager2
        dotsIndicator.setViewPager2(viewPager);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
