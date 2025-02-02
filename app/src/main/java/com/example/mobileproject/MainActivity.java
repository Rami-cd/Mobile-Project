package com.example.mobileproject;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mobileproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.mobileproject.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // the action bar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
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
                MenuItem searchItem = myToolbar.getMenu().findItem(R.id.action_search);
                SearchView searchView =
                        (SearchView) searchItem.getActionView();
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        Toast.makeText(MainActivity.this, newText, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
            }
            else {
                getMenuInflater().inflate(R.menu.main_activity_action_bar, myToolbar.getMenu());
            }
        });
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
}