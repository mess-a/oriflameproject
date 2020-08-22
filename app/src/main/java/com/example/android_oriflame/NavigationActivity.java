package com.example.android_oriflame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class NavigationActivity extends AppCompatActivity {
    FirebaseAuth fAuth;

    public String page_name;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        page_name = "Home";

        this.bootstrapNav();
    }

    public void bootstrapNav() {
        // Get instance
        this.fAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();

        // Redirect user if not logged in
        if (fAuth.getCurrentUser() == null) {
            Toast.makeText(getApplicationContext(), "You need to be logged in to view this page.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            finish();
        }

        // Navigation toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(this.page_name);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(NavigationActivity.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        final View navView = navigationView.inflateHeaderView(R.layout.navigation_header);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                UserMenuSelector(item);
                return false;
            }
        });

        // Check admin user
        if (Helper.isAdmin()) {
            navigationView.getMenu().findItem(R.id.admin_menu).setVisible(true);
        }

        // Profile Email
        final TextView pName = navView.findViewById(R.id.profile_name);
        pName.setText(fAuth.getCurrentUser().getEmail());
    }

    private void UserMenuSelector(MenuItem item) {
        String url;
        Uri uri;
        Intent intent;

        switch (item.getItemId()) {
            case R.id.homePage:
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
                break;

            case R.id.facebookPage:
                url = "http://www.facebook.com"; // Facebook link dahna tur
                uri = Uri.parse(url);
                intent = new Intent(Intent.ACTION_VIEW, uri);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                break;

            case R.id.instagramPage:
                url = "http://www.intagram.com"; // Instagram link dahna tur
                uri = Uri.parse(url);
                intent = new Intent(Intent.ACTION_VIEW, uri);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                break;

            case R.id.logout:
                // Signout
                fAuth.signOut();
                // Redirect
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                finish();
                break;

            case R.id.all_products:
                startActivity(new Intent(getApplicationContext(), AllProductsActivity.class));
                finish();
                break;

            case R.id.add_product:
                startActivity(new Intent(getApplicationContext(), AddProductActivity.class));
                finish();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
