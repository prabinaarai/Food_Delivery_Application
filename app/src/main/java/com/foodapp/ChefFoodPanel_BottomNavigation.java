package com.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.foodapp.ChefFoodPanel.ChefHomeFragment;
import com.foodapp.ChefFoodPanel.ChefPendingOrdersFragment;
import com.foodapp.ChefFoodPanel.ChefProfileFragment;
import com.foodapp.ChefFoodPanel.ChefOrderFragment;

import com.foodapp.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class ChefFoodPanel_BottomNavigation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_food_panel__bottom_navigation);

        BottomNavigationView navigationView = findViewById(R.id.chef_bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);

        UpdateToken();

        String name = getIntent().getStringExtra("PAGE");
        if (name != null) {
            loadInitialFragment(name);
        } else {
            loadcheffragment(new ChefHomeFragment());
        }
    }

    /**
     * Loads the initial fragment based on the intent extra "PAGE"
     * @param pageName The name of the page to load
     */
    private void loadInitialFragment(String pageName) {
        Fragment fragment = null;
        switch (pageName.toLowerCase()) {
            case "orderpage":
                fragment = new ChefPendingOrdersFragment();
                break;
            case "confirmpage":
                fragment = new ChefOrderFragment();
                break;
            case "acceptorderpage":
            case "deliveredpage":
                fragment = new ChefHomeFragment();
                break;
            default:
                fragment = new ChefHomeFragment();
                break;
        }
        loadcheffragment(fragment);
    }

    /**
     * Updates the Firebase Cloud Messaging token for the current user
     */
    private void UpdateToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    String token = task.getResult();
                    FirebaseDatabase.getInstance().getReference("Tokens")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(token);
                }
            }
        });
    }

    /**
     * Replaces the current fragment with the provided fragment
     * @param fragment The fragment to load
     * @return true if the fragment was loaded successfully, false otherwise
     */
    private boolean loadcheffragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    /**
     * Handles navigation item selection
     * @param menuItem The selected menu item
     * @return true if the event was handled, false otherwise
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        int itemId = menuItem.getItemId(); // Get the item ID

        // Replace switch-case with if-else
        if (itemId == R.id.chefHome) {
            fragment = new ChefHomeFragment();
        } else if (itemId == R.id.PendingOrders) {
            fragment = new ChefPendingOrdersFragment();
        } else if (itemId == R.id.Orders) {
            fragment = new ChefOrderFragment();
        } else if (itemId == R.id.chefProfile) {
            fragment = new ChefProfileFragment();
        }

        return loadcheffragment(fragment); // Load the selected fragment
    }

}
