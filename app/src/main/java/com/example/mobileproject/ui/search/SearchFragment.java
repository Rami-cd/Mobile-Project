package com.example.mobileproject.ui.search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileproject.BuildConfig;
import com.example.mobileproject.MainActivity;
import com.example.mobileproject.R;
import com.example.mobileproject.databinding.FragmentSearchBinding;

public class SearchFragment extends Fragment {
    private static final String SEARCH_QUERY_KEY = "search_query_key";

    private String currentSearchQuery = "";
    private RecyclerView recyclerView;
    private FragmentSearchBinding binding;
    private CategoriesCustomAdapter adapter;
    private SearchViewModel searchViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.categoryRecycler;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Check if there's a saved query from previous navigation
        if (savedInstanceState != null) {
            currentSearchQuery = savedInstanceState.getString(SEARCH_QUERY_KEY, "");
        }

        // Observe all categories from the database
        MainActivity.categoryDataBase.categoryDao().getAllCategories().observe(getViewLifecycleOwner(), categories -> {
            if (adapter == null) {
                adapter = new CategoriesCustomAdapter(getContext(), categories);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.updateCategories(categories);
            }
        });

        // Access the toolbar from the MainActivity
        Toolbar myToolbar = requireActivity().findViewById(R.id.my_toolbar);
        if (myToolbar != null) {
            // Set up the SearchView from the toolbar
            MenuItem searchItem = myToolbar.getMenu().findItem(R.id.action_search);
            SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setQuery(currentSearchQuery, false);  // Restore the last query

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    currentSearchQuery = query; // Store the query on submit
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    currentSearchQuery = newText; // Update the current search query
                    // When query changes, update LiveData from the DAO
                    MainActivity.categoryDataBase.categoryDao().searchCategories(newText)
                            .observe(getViewLifecycleOwner(), categories -> {
                                if (adapter == null) {
                                    adapter = new CategoriesCustomAdapter(getContext(), categories);
                                    recyclerView.setAdapter(adapter);
                                } else {
                                    adapter.updateCategories(categories);
                                }
                            });
                    return false;
                }
            });
        }

        return root;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SEARCH_QUERY_KEY, currentSearchQuery);  // Save the current query
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
