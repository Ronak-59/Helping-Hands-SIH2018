package com.sih2018.helpinghand;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.sih2018.helpinghand.data.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VolunteerShelterActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private VolunteerShelterActivity.UserLoginTask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_shelter);

        Button btnReg=findViewById(R.id.btn_signup);
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText mPhone=findViewById(R.id.signup_input_phone);
                String phone=mPhone.getText().toString();
                EditText mProfession=findViewById(R.id.signup_input_protype);
                String protype=mProfession.getText().toString();
                EditText mName=findViewById(R.id.signup_input_name);
                String name=mName.getText().toString();
                mAuthTask = new UserLoginTask(phone, protype, name);
                mAuthTask.execute((Void) null);
            }
        });
    }



    /**
     * Represents an asynchronous login/registration task used to Register
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mPhone;
        private final String mProfesion;
        private final String mName;
        String jsonStr;

        UserLoginTask(String phone,String profession,String name) {
            mPhone = phone;
            mProfesion=profession;
            mName=name;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            HttpHandler sh = new HttpHandler();
            String ipadd = getResources().getString(R.string.ipadd);
            String url = "/HHAPI/shel_entry.php";
            String headurl = "http://";
            url=headurl+ipadd+url;
            Log.e("url",url);

            //Creating a Hashmap for storing parameters for POST Method.
            HashMap<String, String> postparam = new HashMap<>();

            // adding each child node to HashMap key => value\
            postparam.put("phone", mPhone);
            postparam.put("name", mName);
            postparam.put("protype", mProfesion);
            auth = FirebaseAuth.getInstance();
            String email = null;
            if (auth.getCurrentUser() != null) {
                email=auth.getCurrentUser().getEmail();
            }
            postparam.put("email",email);



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
                Toast.makeText(getApplicationContext(),
                        "Registration Successful",
                        Toast.LENGTH_LONG)
                        .show();
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
