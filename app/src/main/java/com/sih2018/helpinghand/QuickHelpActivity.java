package com.sih2018.helpinghand;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.sih2018.helpinghand.Fragments.FindVictimFragment;
import com.sih2018.helpinghand.Fragments.RequestHelpFragment;
import com.sih2018.helpinghand.Fragments.SheltersFragment;
import com.sih2018.helpinghand.Fragments.VolunteerFragment;

public class QuickHelpActivity extends AppCompatActivity {

    ImageView RequestHelp;
    ImageView FindShelter;
    ImageView FindSomeone;
    ImageView BeVolunteer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_help);

        RequestHelp = findViewById(R.id.requesthelp);
        FindShelter = findViewById(R.id.findshelter);
        FindSomeone = findViewById(R.id.findsomeone);
        BeVolunteer = findViewById(R.id.bevolunteer);

        RequestHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestHelpFragment fragment = new RequestHelpFragment();
                FragmentTransaction fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
            }
        });
        FindShelter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SheltersFragment fragment = new SheltersFragment();
                FragmentTransaction fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
            }
        });
        FindSomeone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindVictimFragment fragment = new FindVictimFragment();
                FragmentTransaction fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
            }
        });
        BeVolunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VolunteerFragment fragment = new VolunteerFragment();
                FragmentTransaction fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
            }
        });
    }
}
