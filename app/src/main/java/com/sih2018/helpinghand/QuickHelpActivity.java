package com.sih2018.helpinghand;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.sih2018.helpinghand.Fragments.*;
import com.sih2018.helpinghand.data.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.sih2018.helpinghand.Fragments.VolunteerFragment.MyPREFERENCES;

public class QuickHelpActivity extends AppCompatActivity {

    ImageView RequestHelp;
    ImageView FindShelter;
    ImageView FindSomeone;
    ImageView BeVolunteer;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_help);


        RequestHelp = findViewById(R.id.requesthelp);
        FindShelter = findViewById(R.id.findshelter);
        FindSomeone = findViewById(R.id.findsomeone);
        BeVolunteer = findViewById(R.id.bevolunteer);
        sharedPreferences=getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String mPhone=null;
        if(sharedPreferences.contains("Quick"))
        {
            mPhone=sharedPreferences.getString("Quick",null);
        }

        RequestHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuickHelpActivity.this, TransportsActivity.class));
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
                startActivity(new Intent(QuickHelpActivity.this,VolunteerActivity.class));
            }
        });

       TextView tvMobileno=findViewById(R.id.mobileno);
       tvMobileno.setText(mPhone);

       TextView tvLogout=findViewById(R.id.logout);
       tvLogout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               sharedPreferences.edit().remove("Quick").commit();
               startActivity(new Intent(QuickHelpActivity.this,LoginActivity.class));
           }
       });
    }


    @Override
    public void onBackPressed() {

    }

}
