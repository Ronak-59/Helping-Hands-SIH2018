package com.sih2018.helpinghand;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class FirebaseAuthActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 0;
    private FirebaseAuth auth;
    List<AuthUI.IdpConfig> providers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        providers = new ArrayList<>();

        if (auth.getCurrentUser() != null) {
            Log.d("AUTH", auth.getCurrentUser().getEmail());
            startActivity(new Intent(this, NavigationActivity.class));
            finish();
            return;
        } else {
            providers.add(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());
            providers.add(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build());

            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setProviders(providers)
                            .setTheme(R.style.AuthBackground)
                            .build(),
                    RC_SIGN_IN);

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==RC_SIGN_IN){
            if(resultCode==RESULT_OK){
                //User logged in
                Log.d("AUTH", auth.getCurrentUser().getEmail());
                startActivity(new Intent(this, NavigationActivity.class));
                finish();
                return;
            }
            else if (resultCode == RESULT_CANCELED){
                //User not Authenticated
                Log.d("AUTH", "NOT AUTHENTICATED");
                finish();
            }

        }
    }
}
