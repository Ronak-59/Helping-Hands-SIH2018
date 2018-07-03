package com.sih2018.helpinghand;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.sih2018.helpinghand.data.HttpHandler;
import com.sih2018.helpinghand.location.GPSTracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.sih2018.helpinghand.Fragments.VolunteerFragment.MyPREFERENCES;

public class LoginActivity extends AppCompatActivity {

    Button QuickHelp;
    Button Login;
    private UserLoginTask mAuthTask = null;
    EditText mPhone;
    GPSTracker gpsTracker;
    private FirebaseAuth auth;
    String gPhone;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        sharedPreferences=getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this,NavigationActivity.class));
        }
        if(sharedPreferences.contains("Quick"))
        {
            startActivity(new Intent(LoginActivity.this,QuickHelpActivity.class));
        }

        QuickHelp = findViewById(R.id.quick_help);
        Login = findViewById(R.id.login);

        QuickHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create a dialog box
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoginActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_login, null);
                Button mLogin = (Button) mView.findViewById(R.id.btnLogin);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                //using mView for Edit Button in the dialog box
                mPhone = mView.findViewById(R.id.phone);

                //Getting Location
                gpsTracker = new GPSTracker(LoginActivity.this);
                String stringLatitude="0";
                String stringLongitude="0";
                if (gpsTracker.getIsGPSTrackingEnabled())
                {
                    //Getting Latitude and Longitude
                    stringLatitude = String.valueOf(gpsTracker.latitude);
                    stringLongitude = String.valueOf(gpsTracker.longitude);


                    //Error Checking and Substring to make it ready to store it in the database
                    if(stringLatitude.length()>5) {
                        stringLatitude = stringLatitude.substring(0, 7);
                        stringLongitude = stringLongitude.substring(0, 7);
                    }
                    else
                    {
                        stringLatitude="26.1444";
                        stringLongitude="91.7364";
                    }
                    Toast.makeText(getApplicationContext(),
                            "lat: "+stringLatitude+"long: "+stringLongitude,
                            Toast.LENGTH_LONG)
                            .show();
                }
                else
                {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gpsTracker.showSettingsAlert();
                }

                //Convert latitude and longitude to final variable to use it ini inner class
                final String finalStringLatitude = stringLatitude;
                final String finalStringLongitude = stringLongitude;

                mLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String phone=mPhone.getText().toString();
                        gPhone=phone;
                        if(!phone.isEmpty())
                        {
                            //Starting the async task
                            mAuthTask = new UserLoginTask(phone, finalStringLatitude, finalStringLongitude);
                            mAuthTask.execute((Void) null);
                            //startActivity(new Intent(LoginActivity.this,QuickHelpActivity.class));
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),
                                    "Please enter your Mobile No",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,FirebaseAuthActivity.class));
            }
        });
    }



    /**
     * Represents an asynchronous login/registration task used to Register
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mPhone;
        private final String mLati;
        private final String mlongi;
        String jsonStr;

        UserLoginTask(String phone,String lati,String longi) {
            mPhone = phone;
            mLati=lati;
            mlongi=longi;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            HttpHandler sh = new HttpHandler();
            String ipadd = getResources().getString(R.string.ipadd);
            String url = "/HHAPI/register.php";
            String headurl = "http://";
            url=headurl+ipadd+url;
            Log.e("url",url);

            //Creating a Hashmap for storing parameters for POST Method.
            HashMap<String, String> postparam = new HashMap<>();

            // adding each child node to HashMap key => value\
            postparam.put("phone", mPhone);
            postparam.put("lati", mLati);
            postparam.put("longi", mlongi);
            for (Map.Entry<String,String> entry : postparam.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                Log.e(key,value);
            }

            // Making a request to url and getting response
            jsonStr = sh.makeServiceCallPost(url,postparam);
            Log.e("Debugg(LoginAct)", "Response from url: " + jsonStr); //The Output of First Page

            //JSON Parsing
            if (jsonStr != null) {
                JSONObject jsonObj = null;
                String resp="";
                try {
                    jsonObj = new JSONObject(jsonStr);
                    //Log.e("doInBackgroundl", jsonObj.getString("status"));
                    resp=jsonObj.getString("status");

                } catch (final JSONException e) {
                    Log.e("Error", "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
                if(resp.equals("1"))
                {
                    //If Status Returned true then registration is successful
                    return true;
                }

            } else {
                Log.e("Error", "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();

                    }
                });
            }

            //If Status returned false then registration error
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (success) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Quick",gPhone);
                editor.commit();
                //if sucessful login then go to main activity
                Intent intent = new Intent(getApplicationContext(), QuickHelpActivity.class);
                intent.putExtra("JSONStr", jsonStr);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(),
                        "Registration Unsuccessful",
                        Toast.LENGTH_LONG)
                        .show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}
