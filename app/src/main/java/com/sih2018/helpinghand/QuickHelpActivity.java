package com.sih2018.helpinghand;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
                startActivity(new Intent(QuickHelpActivity.this,SheltersActivity.class));
            }
        });
        FindSomeone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuickHelpActivity.this,FindVictimActivity.class));
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
