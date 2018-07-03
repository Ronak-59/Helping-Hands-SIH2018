package com.sih2018.helpinghand;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.sih2018.helpinghand.Fragments.AboutFragment;
import com.sih2018.helpinghand.Fragments.ContactFragment;
import com.sih2018.helpinghand.Fragments.FAQFragment;
import com.sih2018.helpinghand.Fragments.FindVictimFragment;
import com.sih2018.helpinghand.Fragments.HelplineTipsFragment;
import com.sih2018.helpinghand.Fragments.LiveUpdatesFragment;
import com.sih2018.helpinghand.Fragments.NotificationFragment;
import com.sih2018.helpinghand.Fragments.ProfileFragment;
import com.sih2018.helpinghand.Fragments.RequestHelpFragment;
import com.sih2018.helpinghand.Fragments.SheltersFragment;
import com.sih2018.helpinghand.Fragments.TransportFragment;
import com.sih2018.helpinghand.Fragments.VolunteerFragment;

import org.w3c.dom.Text;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    private FirebaseAuth auth;
    TextView tvProName,tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RequestHelpFragment fragment = new RequestHelpFragment();
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
        toolbar.setTitle("Request Help");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        auth = FirebaseAuth.getInstance();
        tvProName=navigationView.getHeaderView(0).findViewById(R.id.proName);
        tvEmail=navigationView.getHeaderView(0).findViewById(R.id.email);


        if (auth.getCurrentUser() != null) {
            tvProName.setText(auth.getCurrentUser().getDisplayName());
            tvEmail.setText(auth.getCurrentUser().getEmail());
            Log.e("LoginStatus","Success");
        }


    }
    private void signOut(){
        Intent signOutIntent = new Intent(NavigationActivity.this, LoginActivity.class);
        startActivity(signOutIntent);
        finish();
    }
    private void displayMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.action_logout){
            AuthUI.getInstance()
                    .signOut(NavigationActivity.this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                signOut();
                            }else {
                                displayMessage("Signout Error");
                            }
                        }
                    });
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.request_help) {
            RequestHelpFragment fragment = new RequestHelpFragment();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
            toolbar.setTitle("Request Help");
        } else if (id == R.id.volunteer) {
            VolunteerFragment fragment = new VolunteerFragment();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
            toolbar.setTitle("Volunteer");

        } else if (id == R.id.shelters) {
            SheltersFragment fragment = new SheltersFragment();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
            toolbar.setTitle("Shelters");

        } else if (id == R.id.find_victim) {
            FindVictimFragment fragment = new FindVictimFragment();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
            toolbar.setTitle("Find Victim");

        } else if (id == R.id.live_updates) {
            LiveUpdatesFragment fragment = new LiveUpdatesFragment();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
            toolbar.setTitle("Live Updates");

        } else if (id == R.id.helpline_tips) {
            HelplineTipsFragment fragment = new HelplineTipsFragment();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
            toolbar.setTitle("Helpline and Tips");

        } else if (id == R.id.profile) {
            ProfileFragment fragment = new ProfileFragment();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
            toolbar.setTitle("Profile");

        } else if (id == R.id.ec) {
            FAQFragment fragment = new FAQFragment();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
            toolbar.setTitle("FAQ");

        } else if (id == R.id.contact) {
            ContactFragment fragment = new ContactFragment();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
            toolbar.setTitle("Contact Us");

        } else if (id == R.id.about) {
            AboutFragment fragment = new AboutFragment();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
            toolbar.setTitle("About Us");

        } else if (id == R.id.notifications) {
            NotificationFragment fragment = new NotificationFragment();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
            toolbar.setTitle("Notifications");

        }
        else if (id == R.id.transport) {
            TransportFragment fragment = new TransportFragment();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
            toolbar.setTitle("Transportation");

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
