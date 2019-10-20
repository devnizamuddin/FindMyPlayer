package com.example.findmyplayer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.findmyplayer.Auth.AuthActivity;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;

import com.example.findmyplayer.Auth.LoginFragment;
import com.example.findmyplayer.Navigation.AboutFragment;
import com.example.findmyplayer.Navigation.AllPlayerFragment;
import com.example.findmyplayer.Navigation.Events.EventFragment;
import com.example.findmyplayer.Navigation.FindMyPlayer.FindMyPlayerFragment;
import com.example.findmyplayer.Navigation.NewsFeedFragment;
import com.example.findmyplayer.Navigation.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    Toolbar toolbar;
    FirebaseUser firebaseUser;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        drawer = findViewById(R.id.drawer_layout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
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


         firebaseUser = firebaseAuth.getCurrentUser();

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
        } else if (fragmentManager.getBackStackEntryCount()>0){
            super.onBackPressed();

        }
        else{
            buildClosingAppDialog();
        }
    }

    private void buildClosingAppDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Close App !");
        builder.setMessage("Do you really want to close the app ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

            }
        });
        builder.show();

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
                finish();
                startActivity(new Intent(this, AuthActivity.class));
                break;

            case R.id.action_password_reset:

                passwordReset();

                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void passwordReset() {

        if (firebaseUser !=null){

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you really want to reset your password ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    //Password reset
                    firebaseAuth.sendPasswordResetEmail(firebaseUser.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){

                                Toast.makeText(MainActivity.this, "Email send", Toast.LENGTH_SHORT).show();

                            }
                            else {
                                Toast.makeText(MainActivity.this, ""+task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.setCancelable(false);
            builder.show();

        }
        else {
            Toast.makeText(this, "Your are not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.nav_profile:
                toolbar.setTitle("Profile");
                changeFragment(ProfileFragment.getInstance(userId));
                break;

            case R.id.nav_all_player:

                toolbar.setTitle("All Player");
                changeFragment(new AllPlayerFragment());
                break;
            case R.id.nav_find_player:

                toolbar.setTitle("Find Player");
                changeFragment(new FindMyPlayerFragment());
                break;
            case R.id.nav_events:

                toolbar.setTitle("Event");
                changeFragment(new EventFragment());
                break;
            case R.id.nav_news_feed:

                toolbar.setTitle("NewsFeed");
                changeFragment(new NewsFeedFragment());
                break;

            case R.id.nav_about:

                toolbar.setTitle("About");
                changeFragment(new AboutFragment());
                break;

            case R.id.nav_exit:

                finish();
                break;

            default:
                toolbar.setTitle("All Player");
                changeFragment(new AllPlayerFragment());
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    void changeFragment(Fragment fragment) {

         fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_main, fragment);
        fragmentTransaction.commit();

    }


}
