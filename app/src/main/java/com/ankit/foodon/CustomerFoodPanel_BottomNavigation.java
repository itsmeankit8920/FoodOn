package com.ankit.foodon;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CustomerFoodPanel_BottomNavigation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_food_panel_bottom_navigation);

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);

        // Set listener using new setOnItemSelectedListener method
        navigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return onNavigationItemSelected(item);  // Handle item selection
            }
        });

        // Load the default fragment only if there is no saved instance
        if (savedInstanceState == null) {
            loadFragment(new CustomerHomeFragment());
        }
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        // Replacing switch-case with if-else
        if (item.getItemId() == R.id.cust_Home) {
            fragment = new CustomerHomeFragment();
        } else if (item.getItemId() == R.id.cart) {
            fragment = new CustomerCartFragment();
        } else if (item.getItemId() == R.id.cust_profile) {
            fragment = new CustomerProfileFragment();
        } else if (item.getItemId() == R.id.Cust_order) {
            fragment = new CustomerOrdersFragment();
        } else if (item.getItemId() == R.id.track) {
            fragment = new CustomerTrackFragment();
        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
