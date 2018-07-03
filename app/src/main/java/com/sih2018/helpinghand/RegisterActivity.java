package com.sih2018.helpinghand;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.sih2018.helpinghand.data.HttpHandler;
import com.sih2018.helpinghand.location.GPSTracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private UserLoginTask mAuthTask = null;
    private FirebaseAuth auth;

    GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Firebase Authentication Initialization
        auth = FirebaseAuth.getInstance();

        //Displaying Name and Email
        EditText mName=(EditText)findViewById(R.id.signup_input_name);
        EditText mEmail=(EditText)findViewById(R.id.signup_input_email);
        if (auth.getCurrentUser() != null) {
            mName.setText(auth.getCurrentUser().getDisplayName());
            mEmail.setText(auth.getCurrentUser().getEmail());
        }

        Button btnReg=(Button)findViewById(R.id.btn_signup);

        //Getting Location
        gpsTracker = new GPSTracker(RegisterActivity.this);
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

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Declaring Storing variables
                EditText mPhone=(EditText)findViewById(R.id.signup_input_phone);
                EditText mProfession=(EditText)findViewById(R.id.signup_input_protype);
                EditText mAge=(EditText)findViewById(R.id.signup_input_age);
                RadioGroup mGender=(RadioGroup)findViewById(R.id.gender_radio_group);
                EditText mAadhaar=(EditText)findViewById(R.id.signup_input_aadhaar);

                //Storing Inputs into the variables
                String phone=mPhone.getText().toString();
                String profession=mProfession.getText().toString();
                String age=mAge.getText().toString();
                String aadhaar=mAadhaar.getText().toString();
                int selectedId = mGender.getCheckedRadioButtonId();
                RadioButton tempRadioButton=findViewById(selectedId);
                String gender=tempRadioButton.getText().toString();
                String name=auth.getCurrentUser().getDisplayName();
                String email=auth.getCurrentUser().getEmail();

                //Starting the async task
                mAuthTask = new RegisterActivity.UserLoginTask(name,email,phone,profession,age,gender,aadhaar,finalStringLatitude,finalStringLongitude);
                mAuthTask.execute((Void) null);
                startActivity(new Intent(RegisterActivity.this,NavigationActivity.class));

            }
        });
    }



    /**
     * Represents an asynchronous login/registration task used to Register
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mName;
        private final String mEmail;
        private final String mPhone;
        private final String mProfession;
        private final String mAge;
        private final String mGender;
        private final String mAadhaar;
        private final String mLati;
        private final String mLongi;
        String jsonStr;

        UserLoginTask(String name,String email,String phone,String profession, String age, String gender,String aadhaar,String lati,String longi) {
            mName = name;
            mEmail= email;
            mPhone=phone;
            mProfession=profession;
            mAge=age;
            mGender=gender;
            mAadhaar=aadhaar;
            mLati=lati;
            mLongi=longi;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            HttpHandler sh = new HttpHandler();
            String ipadd = getResources().getString(R.string.ipadd);
            String url = "/HHAPI/fullregister.php";
            String headurl = "http://";
            url=headurl+ipadd+url;
            Log.e("url",url);

            //Creating a Hashmap for storing parameters for POST Method.
            HashMap<String, String> postparam = new HashMap<>();

            // adding each child node to HashMap key => value\
            postparam.put("name", mName);
            postparam.put("email", mEmail);
            postparam.put("phone", mPhone);
            postparam.put("profession", mProfession);
            postparam.put("age", mAge);
            postparam.put("gender", mGender);
            postparam.put("aadhaar",mAadhaar);
            postparam.put("lati",mLati);
            postparam.put("longi",mLongi);

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
                //if sucessful login then go to main activity
                Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
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
