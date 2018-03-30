package com.sih2018.helpinghand.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.sih2018.helpinghand.FirebaseAuthActivity;
import com.sih2018.helpinghand.LoginActivity;
import com.sih2018.helpinghand.NavigationActivity;
import com.sih2018.helpinghand.R;
import com.sih2018.helpinghand.RegisterActivity;
import com.sih2018.helpinghand.VolunteerShelterActivity;
import com.sih2018.helpinghand.data.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VolunteerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VolunteerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VolunteerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static final String MyPREFERENCES = "VolunteerPref" ;
    SharedPreferences sharedpreferences;

    private VolunteerFragment.UserLoginTask mAuthTask = null;
    private FirebaseAuth auth;

    String shelName,facilities="";
    EditText editText;
    CheckBox food,water,cloth;

    public VolunteerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VolunteerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VolunteerFragment newInstance(String param1, String param2) {
        VolunteerFragment fragment = new VolunteerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_volunteer, container, false);

        //Creating Switch variable
        final Switch mSwitch=view.findViewById(R.id.switchButton);

        //Shared Preferences
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if(!sharedpreferences.contains("volunteer"))
        {
            mSwitch.setChecked(false);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean("volunteer", false);
            editor.commit();
            //Craeting Temp Linear Layout Variable
            LinearLayout linLayouttemp=view.findViewById(R.id.volunteerBtns);
            linLayouttemp.setVisibility(View.INVISIBLE);

        }
        else
        {
            mSwitch.setChecked(sharedpreferences.getBoolean("volunteer",false));
            //Craeting Temp Linear Layout Variable
            LinearLayout linLayouttemp=view.findViewById(R.id.volunteerBtns);
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
                    LinearLayout linLayout=view.findViewById(R.id.volunteerBtns);
                    linLayout.setVisibility(View.VISIBLE);
                }else
                {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean("volunteer",false);
                    editor.commit();
                    LinearLayout linLayout=view.findViewById(R.id.volunteerBtns);
                    linLayout.setVisibility(View.INVISIBLE);
                }
            }
        });

        Button mShelterBtn=view.findViewById(R.id.provideShelter);
        Button mTransportBtn=view.findViewById(R.id.provideTransport);

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
                        startActivity(new Intent(getActivity(),VolunteerShelterActivity.class));
                    }
                }

                //Create a dialog box
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
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
                        mAuthTask = new UserLoginTask(auth.getCurrentUser().getEmail());
                        mAuthTask.execute((Void) null);
                    }
                });

            }
        });

        mTransportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity().getApplicationContext(),
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
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity().getApplicationContext(),
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
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity().getApplicationContext(),
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(),
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
                startActivity(new Intent(getActivity(),VolunteerShelterActivity.class));

            } else {
                //Registeration Needed
                Toast.makeText(getActivity().getApplicationContext(),
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


