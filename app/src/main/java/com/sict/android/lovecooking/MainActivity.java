package com.sict.android.lovecooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sict.android.lovecooking.Fragment.DishFragment;
import com.sict.android.lovecooking.Fragment.UserFragment;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    MaterialToolbar materialToolbar;
    SharedPreferences sharedPreferences;
    private String fullname,username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);
        materialToolbar = (MaterialToolbar)findViewById(R.id.search);

        fullname = sharedPreferences.getString("full_name","Love Cooking");
        username = sharedPreferences.getString("user_name","Admin");

        loadFragment(new DishFragment());

        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_search:
                        Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                        finish();startActivity(intent);
                        return true;
                    case R.id.action_user:
                        Intent intent2 = new Intent(MainActivity.this,UserInfoActivity.class);
                        finish();startActivity(intent2);
                        return true;
                    default: return false;
                }
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        materialToolbar.setTitle(R.string.app_name);
                        loadFragment(new DishFragment());
                        return true;
                    case R.id.userpage:
                        materialToolbar.setTitle(fullname +" - "+username);
                        loadFragment(new UserFragment());
                        return true;
                    default: return false;
                }
            }
        });
    }


    private void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}