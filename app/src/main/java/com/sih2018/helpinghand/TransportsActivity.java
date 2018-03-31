package com.sih2018.helpinghand;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sih2018.helpinghand.data.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TransportsActivity extends AppCompatActivity implements TransAdapter.TransAdapterOnClickHandler{

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private RecyclerView mRecyclerView;
    private TransAdapter mTransAdapter;
    private String TAG = FindVictimActivity.class.getSimpleName();
    private TransportsActivity.GetTrans mTask;
    // URL to get contacts JSON
    private  String url = "/HHAPI/select2.php";
    private String headurl = "http://";
    String ipadd;
    ArrayList<HashMap<String, String>> transList;
    String [][]Transdet = new String[10][4];
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_transport);
        transList = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_shelter);
        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = (TextView) findViewById(R.id.error_mess);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.loadbar);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);


        mTransAdapter = new TransAdapter(this);
        mRecyclerView.setAdapter(mTransAdapter);
        showTransDataView();
        new GetTrans().execute("nostring");
    }


    private void showTransDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(String data) {
        Intent intentnext = new Intent(TransportsActivity.this, com.sih2018.helpinghand.TransportActivity.class);
        intentnext.putExtra("did",data);
        startActivity(intentnext);
    }


    private class GetTrans extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(String... strings) {
            ipadd = getResources().getString(R.string.ipadd);
            HttpHandler sh = new HttpHandler();

            url = "/HHAPI/selecttrans.php";
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
                    transList.clear();
                    // looping through All Contacts
                    for (int i = 0; i < names.length(); i++) {
                        JSONObject c = names.getJSONObject(i);

                        String name = c.getString("name");
                        String id = c.getString("id");
                        String sname = c.getString("sname");
                        String dname = c.getString("dname");

                        // tmp hash map for single contact
                        HashMap<String, String> shelname = new HashMap<>();

                        // adding each child node to HashMap key => value\
                        shelname.put("name", name);
                        //Log.e("Names",contact.get("name").toString());
                        // adding contact to contact list

                        transList.add(shelname);
                        Transdet[i][0]=id;
                        Transdet[i][1]=name;
                        Transdet[i][2]=sname;
                        Transdet[i][3]=dname;
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
                Log.e("set","yes");


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
            showTransDataView();
            /*ListAdapter adapter = new SimpleAdapter(
                    TranssActivity.this, transList,
                    R.layout.list_item, new String[]{"name", "email",
                    "mobile"}, new int[]{R.id.name,
                    R.id.email, R.id.mobile});

            lv.setAdapter(adapter);*/
            if(transList.size()>0) {
                HashMap<String, String> m = transList.get(0);
                String strArr[][] = new String[transList.size() * m.size()][4];
                for (int i=0;i<transList.size() * m.size();i++) {
                    strArr[i][0] = Transdet[i][0];
                    strArr[i][1] = Transdet[i][1];
                    strArr[i][2] = Transdet[i][2];
                    strArr[i][3] = Transdet[i][3];
                }
                mTransAdapter.setTransData(strArr);
            }else {
                showErrorMessage();
            }
        }

    }
}
