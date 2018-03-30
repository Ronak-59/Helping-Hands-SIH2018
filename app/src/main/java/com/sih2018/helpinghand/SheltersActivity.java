package com.sih2018.helpinghand;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sih2018.helpinghand.Fragments.SheltersFragment;
import com.sih2018.helpinghand.data.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SheltersActivity extends AppCompatActivity implements  NearShelterAdapter.NearShelterAdapterOnClickHandler {
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private RecyclerView mRecyclerView;
    private NearShelterAdapter mNearShelterAdapter;
    private CardView mResultCard;
    private EditText mSearchField;
    private String TAG = FindVictimActivity.class.getSimpleName();
    private GetShelter mTask;
    // URL to get contacts JSON
    private String url = "/HHAPI/select2.php";
    private String headurl = "http://";
    String ipadd;
    ArrayList<HashMap<String, String>> shelterList;
    String[][] shelterdet = new String[10][3];
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_shelters);
        shelterList = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_shelter);
        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = (TextView) findViewById(R.id.error_mess);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.loadbar);
        mSearchField = (EditText) findViewById(R.id.search_field);
        mResultCard = (CardView) findViewById(R.id.card_viewresult);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);


        mNearShelterAdapter = new NearShelterAdapter(this);
        mRecyclerView.setAdapter(mNearShelterAdapter);
        showShelterDataView();
        new GetShelter().execute("nostring");
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
    public void onClick(String ProfileData) {
        Intent intentnext = new Intent(this, FindVictimActivity.class);
        intentnext.putExtra("username",ProfileData);
        startActivity(intentnext);
    }


    private class GetShelter extends AsyncTask<String, Void, Void> implements OnMapReadyCallback {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(String... strings) {
            ipadd = getResources().getString(R.string.ipadd);
            HttpHandler sh = new HttpHandler();

            url = "/HHAPI/select2.php";
            headurl = "http://";
            url = headurl + ipadd + url;
            Log.e("url", url);
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

           Log.e(TAG, "Response from url: " + jsonStr); //The Output of First Page

            if (jsonStr != null) {
                try {

                    JSONArray names = new JSONArray(jsonStr);
                    shelterList.clear();
                        // looping through All Contacts
                        for (int j = 0; j < names.length(); j++) {

                            JSONObject idjsonobj = names.getJSONObject(j);
                            String name = idjsonobj.getString("name");
                            String lati = idjsonobj.getString("lati");
                            String longi = idjsonobj.getString("longi");

                            // tmp hash map for single contact
                            HashMap<String, String> shelname = new HashMap<>();

                            // adding each child node to HashMap key => value\
                            shelname.put("name", name);
                            //Log.e("Names",contact.get("name").toString());
                            // adding contact to contact list

                            shelterList.add(shelname);
                            shelterdet[j][0] = name;
                            shelterdet[j][1] = lati;
                            shelterdet[j][2] = longi;
                        }

            } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }
                Log.e("set", "yes");


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
            if (shelterList.size() > 0) {
                HashMap<String, String> m = shelterList.get(0);
                String strArr[] = new String[shelterList.size() * m.size()];
                int i = 0;
                for (HashMap<String, String> hash : shelterList) {
                    for (String current : hash.values()) {
                        strArr[i] = current;
                        i++;
                    }
                }
                mNearShelterAdapter.setShelterData(strArr);
            } else {
                showErrorMessage();
            }
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {

            map = googleMap;
            boolean success = map.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json)));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
            LatLng sheltermark = new LatLng(26.2006, 92.9376);
            Log.e("call", String.valueOf(shelterdet.length));
            if (shelterdet.length > 0) {
                for (int i = 0; i < shelterdet.length; i++) {
                    Log.e("name", "N-" + shelterdet[i][0]);
                    sheltermark = new LatLng(Double.parseDouble(shelterdet[i][2]), Double.parseDouble(shelterdet[i][1]));
                    map.addMarker(new MarkerOptions().position(sheltermark).title(shelterdet[i][0]));
                }
            }
            map.moveCamera(CameraUpdateFactory.newLatLng(sheltermark));
            map.animateCamera(CameraUpdateFactory.zoomTo(8.0f));
        }
    }

}