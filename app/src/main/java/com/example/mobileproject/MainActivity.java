package com.example.mobileproject;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobileproject.datacart.AppDatabase;
import com.example.mobileproject.ui.search.repository.Category;
import com.example.mobileproject.ui.search.repository.CategoryDataBase;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mobileproject.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    AppBarConfiguration appBarConfiguration;
    public static CategoryDataBase categoryDataBase;
    public static AppDatabase cartDatabase;
    String serverIp = BuildConfig.SERVER_IP;
    int TIMEOUT_MS = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.mobileproject.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cartDatabase = Room.databaseBuilder(this,AppDatabase.class,"cart_db").build();

        // the action bar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        categoryDataBase = Room.databaseBuilder(getApplicationContext(),
                CategoryDataBase.class, "categories_db").build();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_cart, R.id.navigation_search, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        getSupportActionBar().setTitle("mobile project");

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            myToolbar.getMenu().clear();

            // this function checks the current fragment we are in
            // to change by inflating the search after removing the previous action
            // to prevent multiple action bars
            if (destination.getId() == R.id.navigation_search) {
                getMenuInflater().inflate(R.menu.search_action_bar, myToolbar.getMenu());

                // instead of menu from the argument like the pdf we can get the
                // current menu using the getMenu() function
//                MenuItem searchItem = myToolbar.getMenu().findItem(R.id.action_search);
//                SearchView searchView =
//                        (SearchView) searchItem.getActionView();
//                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                    @Override
//                    public boolean onQueryTextSubmit(String query) {
//                        Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
//                        return false;
//                    }
//                    @Override
//                    public boolean onQueryTextChange(String newText) {
//                        Toast.makeText(MainActivity.this, newText, Toast.LENGTH_SHORT).show();
//                        return false;
//                    }
//                });
            }
            else {
                getMenuInflater().inflate(R.menu.main_activity_action_bar, myToolbar.getMenu());
            }
        });

        fetchAndStoreCategories();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_location){
            Toast.makeText(this, "location action", Toast.LENGTH_SHORT).show();
            return true;
        }
        if(id == R.id.action_notification){
            Toast.makeText(this, "notification action", Toast.LENGTH_SHORT).show();
            return true;
        }
        if(id == R.id.action_search) {
            Toast.makeText(this, "searching", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // In MainActivity, fetch and insert categories
    void fetchAndStoreCategories() {
        String url = "http://"+serverIp+"/storage_bucket/get_all_categories.php";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    List<Category> categoryList = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            String name = obj.getString("name");
                            String image_url = obj.getString("image_url");
                            int sql_id = obj.getInt("category_id");
                            categoryList.add(new Category(name, image_url, sql_id));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d("MainActivity", "Inserting Categories: " + categoryList.toString());
                    // Insert categories into Room database after fetching
                    insertCategoriesInBackground(categoryList);
                },
                error -> {
                    error.printStackTrace();
                }
        );

        queue.add(jsonArrayRequest);
    }
    void insertCategoriesInBackground(List<Category> categories) {
        // Insert the categories into Room database in the background
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            MainActivity.categoryDataBase.categoryDao().insertCategories(categories);
            Log.d("MainActivity", "Categories inserted");

            // You can trigger your UI update here after insertion
            // Example: Once categories are inserted, reload them
            runOnUiThread(() -> {
                // Call your method to load data into the RecyclerView, or signal that the data is ready
                // Or refresh your fragment
                // You can make sure the RecyclerView is updated at this point.
            });
        });
    }

//    void loadCategoriesFromRoom() {
//        MainActivity.categoryDataBase.categoryDao().getAllCategories().observe(this, categories -> {
//            if (categories != null) {
//                // Update RecyclerView with new data
//                adapter.updateCategories(categories);
//            }
//        });
//    }
    void insertInBackground(Category category){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                MainActivity.categoryDataBase.categoryDao().insertAll(category);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }
        });
    }
}