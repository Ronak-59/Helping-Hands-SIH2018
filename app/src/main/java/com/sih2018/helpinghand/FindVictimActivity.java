package com.sih2018.helpinghand;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sih2018.helpinghand.data.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FindVictimActivity extends AppCompatActivity implements VictimAdapter.VictimAdapterOnClickHandler, View.OnClickListener {
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private RecyclerView mRecyclerView;
    private VictimAdapter mVictimAdapter;
    private CardView mResultCard;
    private EditText mSearchField;
    private String TAG = FindVictimActivity.class.getSimpleName();
    private GetVictim mTask;
    // URL to get contacts JSON
    private  String url = "/HHAPI/select.php";
    private String headurl = "http://";
    String ipadd;
    ArrayList<HashMap<String, String>> shelterList;
    Button imgup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_find_victim);
        shelterList = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_shelter);
        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = (TextView) findViewById(R.id.error_mess);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.loadbar);
        mSearchField =(EditText)findViewById(R.id.search_field);
        mResultCard=(CardView)findViewById(R.id.card_viewresult);
        imgup = (Button) findViewById(R.id.imgup);

        imgup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Clicked","Yes");
                Intent intentnext = new Intent(FindVictimActivity.this, UploadActivity.class);
                startActivity(intentnext);
            }
        });

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);


        mVictimAdapter = new VictimAdapter(this);

        mRecyclerView.setAdapter(mVictimAdapter);

        mSearchField.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                        showShelterDataView();
                        Log.e("TestCode","Working");
                        if(mSearchField.getText().toString().length()>0)
                        new GetVictim().execute(mSearchField.getText().toString());
                    }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


    }

    private void showShelterDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
        mResultCard.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(String weatherForDay) {
        Intent intentnext = new Intent(getApplicationContext(), FindVictimActivity.class);
        intentnext.putExtra("formname",weatherForDay);
        startActivity(intentnext);
    }

    @Override
    public void onClick(View v) {

    }

    private class GetVictim extends AsyncTask<String, Void, Void> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(String... strings) {
            ipadd = getApplicationContext().getResources().getString(R.string.ipadd);
            HttpHandler sh = new HttpHandler();

            url = "/HHAPI/select.php?search="+strings[0];
            headurl = "http://";
            url=headurl+ipadd+url;
            Log.e("url",url);
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr); //The Output of First Page

            if (jsonStr != null) {
                try {

                    // Getting JSON Array node
                    JSONArray names = new JSONArray(jsonStr);
                    shelterList.clear();
                    // looping through All Contacts
                    for (int i = 0; i < names.length(); i++) {
                        JSONObject c = names.getJSONObject(i);

                        String name = c.getString("name");

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value\
                        contact.put("name", name);
                        //Log.e("Names",contact.get("name").toString());
                        // adding contact to contact list

                        shelterList.add(contact);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
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



            } else {
                Log.e(TAG, "Couldn't get json from server.");
                /*getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();

                    }
                });*/
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            /**
             * Updating parsed JSON data into ListView
             * */

            mLoadingIndicator.setVisibility(View.INVISIBLE);
            showShelterDataView();
            /*ListAdapter adapter = new SimpleAdapter(
                    SheltersActivity.this, shelterList,
                    R.layout.list_item, new String[]{"name", "email",
                    "mobile"}, new int[]{R.id.name,
                    R.id.email, R.id.mobile});

            lv.setAdapter(adapter);*/
            if(shelterList.size()>0) {
                HashMap<String, String> m = shelterList.get(0);
                String strArr[] = new String[shelterList.size() * m.size()];
                int i = 0;
                for (HashMap<String, String> hash : shelterList) {
                    for (String current : hash.values()) {
                        strArr[i] = current;
                        i++;
                    }
                }
                mVictimAdapter.setShelterData(strArr);
            }else {
                showErrorMessage();
            }
        }

    }
}
