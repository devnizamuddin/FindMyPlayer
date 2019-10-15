package com.example.findmyplayer;

import android.content.Intent;
import android.os.Bundle;

import com.example.findmyplayer.Auth.AuthActivity;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;

import com.example.findmyplayer.Navigation.AllPlayerFragment;
import com.example.findmyplayer.Navigation.EventFragment;
import com.example.findmyplayer.Navigation.FindMyPlayer.FindMyPlayerFragment;
import com.example.findmyplayer.Navigation.NewsFeedFragment;
import com.example.findmyplayer.Navigation.ProfileFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth firebaseAuth;
    ImageView profileImageView;
    TextView username_tv, email_tv;
    DrawerLayout drawer;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        drawer = findViewById(R.id.drawer_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);

        profileImageView = headerLayout.findViewById(R.id.profileImageView);
        username_tv = headerLayout.findViewById(R.id.username_tv);
        email_tv = headerLayout.findViewById(R.id.email_tv);


        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            userId = firebaseUser.getUid();
            username_tv.setText(firebaseUser.getDisplayName());
            email_tv.setText(firebaseUser.getEmail());
            Picasso.get().load(firebaseUser.getPhotoUrl()).into(profileImageView);
        } else {
            Toast.makeText(this, "No user found", Toast.LENGTH_SHORT).show();
        }


        changeFragment(new AllPlayerFragment());
        //****************************OnCreate :
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_logout:
                firebaseAuth.signOut();
                startActivity(new Intent(this, AuthActivity.class));
                break;

            case R.id.action_settings:

                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.nav_profile:

                changeFragment(ProfileFragment.getInstance(userId));
                break;

            case R.id.nav_all_player:

                changeFragment(new AllPlayerFragment());
                break;
            case R.id.nav_find_player:

                changeFragment(new FindMyPlayerFragment());
                break;
            case R.id.nav_events:

                changeFragment(new EventFragment());
                break;
            case R.id.nav_news_feed:

                changeFragment(new NewsFeedFragment());
                break;

                default:
                changeFragment(new AllPlayerFragment());
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    void changeFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_main, fragment);
        fragmentTransaction.commit();

    }
}
