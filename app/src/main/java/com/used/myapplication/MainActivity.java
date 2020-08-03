package com.used.myapplication;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.used.myapplication.Fragments.ExploreFragment;
import com.used.myapplication.Fragments.HomeFragment;
import com.used.myapplication.Fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    /**
     *
     * @author Кырыкбай Ерасыл
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemReselectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();


    }


    /**
     * Обработчик нажатий на BottomNavigationView
     */
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemReselectedListener = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selected = null;
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selected = new HomeFragment();
                            break;
                        case R.id.nav_favorite:
                            selected = new ExploreFragment();
                            break;
                        case R.id.nav_profile:
                            selected = new ProfileFragment();
                            break;
                    }
                    assert selected != null;
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
                    return true;
                }
            };


    /**
     * Override метода on back pressed для activity и фрагментов
     */
    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            moveTaskToBack(true);
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }
}