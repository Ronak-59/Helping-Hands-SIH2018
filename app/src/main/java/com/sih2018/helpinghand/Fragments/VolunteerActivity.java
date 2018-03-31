package com.sih2018.helpinghand.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.sih2018.helpinghand.R;
import com.sih2018.helpinghand.VolunteerShelterActivity;
import com.sih2018.helpinghand.data.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.sih2018.helpinghand.Fragments.VolunteerFragment.MyPREFERENCES;


public class VolunteerActivity extends AppCompatActivity {


    public static final String MyPREFERENCES = "VolunteerPref" ;
    SharedPreferences sharedpreferences;

    private VolunteerActivity.UserLoginTask mAuthTask = null;
    private FirebaseAuth auth;

    String shelName,facilities="";
    EditText editText;
    CheckBox food,water,cloth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_volunteer);

        //Creating Switch variable
        final Switch mSwitch=findViewById(R.id.switchButton);

        //Shared Preferences
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if(!sharedpreferences.contains("volunteer"))
        {
            mSwitch.setChecked(false);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean("volunteer", false);
            editor.commit();
            //Craeting Temp Linear Layout Variable
            LinearLayout linLayouttemp=findViewById(R.id.volunteerBtns);
            linLayouttemp.setVisibility(View.INVISIBLE);

        }
        else
        {
            mSwitch.setChecked(sharedpreferences.getBoolean("volunteer",false));
            //Craeting Temp Linear Layout Variable
            LinearLayout linLayouttemp=findViewById(R.id.volunteerBtns);
            if(sharedpreferences.getBoolean("volunteer",false)) {
                linLayouttemp.setVisibility(View.VISIBLE);
            }else{
                linLayouttemp.setVisibility(View.INVISIBLE);
            }
        }



        //Switch Change Listener
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(mSwitch.isChecked())
                {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean("volunteer",true);
                    editor.commit();
                    LinearLayout linLayout=findViewById(R.id.volunteerBtns);
                    linLayout.setVisibility(View.VISIBLE);
                }else
                {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean("volunteer",false);
                    editor.commit();
                    LinearLayout linLayout=findViewById(R.id.volunteerBtns);
                    linLayout.setVisibility(View.INVISIBLE);
                }
            }
        });

        Button mShelterBtn=findViewById(R.id.provideShelter);
        Button mTransportBtn=findViewById(R.id.provideTransport);

        if(sharedpreferences.contains("shelter")) {
            if (sharedpreferences.getBoolean("shelter",false))
            {
                mShelterBtn.setText("Check your Shelter");
                mShelterBtn.setBackgroundColor(Color.rgb(0,128,0));
            }
        }

        mShelterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sharedpreferences.contains("shelter")) {
                    if (sharedpreferences.getBoolean("shelter",false))
                    {
                        startActivity(new Intent(VolunteerActivity.this,VolunteerShelterActivity.class));
                    }
                }

                //Create a dialog box
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(VolunteerActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_shelter_reg, null);
                Button mDone = (Button) mView.findViewById(R.id.btnDone);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                if(!sharedpreferences.contains("shelter") || !sharedpreferences.getBoolean("shelter",false))
                {
                    dialog.show();
                }
                editText=mView.findViewById(R.id.shelter);
                water=mView.findViewById(R.id.water);
                food=mView.findViewById(R.id.food);
                cloth=mView.findViewById(R.id.cloth);

                mDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shelName=editText.getText().toString();
                        if(water.isChecked())
                        {
                            facilities=facilities+"Water,";
                        }
                        if(food.isChecked()){
                            facilities=facilities+"Food,";
                        }
                        if(cloth.isChecked())
                        {
                            facilities=facilities+"Cloth";
                        }


                        auth = FirebaseAuth.getInstance();
                        if(auth.getCurrentUser()!=null) {
                            mAuthTask = new UserLoginTask(auth.getCurrentUser().getEmail());
                            mAuthTask.execute((Void) null);
                        }
                        else
                        {

                        }

                    }
                });

            }
        });

        mTransportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    /**
     * Represents an asynchronous login/registration task used to Register
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        String jsonStr;

        UserLoginTask(String email) {
            mEmail=email;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            HttpHandler sh = new HttpHandler();
            String ipadd = getResources().getString(R.string.ipadd);
            String url = "/HHAPI/firstreg.php";
            String headurl = "http://";
            url=headurl+ipadd+url;
            Log.e("url",url);

            //Creating a Hashmap for storing parameters for POST Method.
            HashMap<String, String> postparam = new HashMap<>();

            // adding each child node to HashMap key => value\
            postparam.put("email",mEmail);
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
                    //If Status Returned true then user is registered already
                    HttpHandler sh1 = new HttpHandler();
                    String ipadd1 = getResources().getString(R.string.ipadd);
                    String url1 = "/HHAPI/shelter_reg.php";
                    String headurl1 = "http://";
                    url1=headurl1+ipadd1+url1;
                    Log.e("url",url1);

                    //Creating a Hashmap for storing parameters for POST Method.
                    HashMap<String, String> postparam1 = new HashMap<>();
                    JSONObject jsonObj1 = null;
                    try {
                        jsonObj1=jsonObj.getJSONObject("0");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // adding each child node to HashMap key => value\
                    postparam1.put("email",mEmail);
                    postparam1.put("name",shelName);
                    String u_id = null,lati = null,longi = null,mobile = null,facility = null,password = null;
                    try {
                        u_id=jsonObj1.getString("u_id");
                        lati=jsonObj1.getString("lati");
                        longi=jsonObj1.getString("longi");
                        mobile=jsonObj1.getString("mobile");
                        facility=facilities;
                        password="pass123";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    postparam1.put("lati",lati);
                    postparam1.put("longi",longi);
                    postparam1.put("u_id",u_id);
                    postparam1.put("mobile",mobile);
                    postparam1.put("facility",facility);
                    postparam1.put("password",password);

                    for (Map.Entry<String,String> entry : postparam.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        Log.e(key,value);
                    }

                    // Making a request to url and getting response
                    jsonStr = sh.makeServiceCallPost(url1,postparam1);
                    Log.e("Debugg(LoginAct)", "Response from url: " + jsonStr);
                    if (jsonStr != null) {
                        JSONObject jsonObj2 = null;
                        String resp2 = "";
                        try {
                            jsonObj = new JSONObject(jsonStr);
                            //Log.e("doInBackgroundl", jsonObj.getString("status"));
                            resp2 = jsonObj.getString("status");

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
                        if(resp2.equals("1")) {
                            return true;
                        }
                        else
                        {
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
                    }
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

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("shelter", true);
                editor.commit();
                //Already Registration done
                startActivity(new Intent(VolunteerActivity.this,VolunteerShelterActivity.class));

            } else {
                //Registeration Needed
                Toast.makeText(getApplicationContext(),
                        "Registration Failed",
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

