package com.sih2018.helpinghand.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.sih2018.helpinghand.FindVictimActivity;
import com.sih2018.helpinghand.TransAdapter;
import com.sih2018.helpinghand.R;
import com.sih2018.helpinghand.TransportActivity;
import com.sih2018.helpinghand.data.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TransportFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TransportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransportFragment extends Fragment implements  TransAdapter.TransAdapterOnClickHandler{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private RecyclerView mRecyclerView;
    private TransAdapter mTransAdapter;
    private String TAG = FindVictimActivity.class.getSimpleName();
    private GetTrans mTask;
    // URL to get contacts JSON
    private  String url = "/HHAPI/select2.php";
    private String headurl = "http://";
    String ipadd;
    ArrayList<HashMap<String, String>> transList;
    String [][]Transdet = new String[10][4];
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TransportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TransportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TransportFragment newInstance(String param1, String param2) {
        TransportFragment fragment = new TransportFragment();
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
        View view=inflater.inflate(R.layout.fragment_transport, container, false);
        transList = new ArrayList<>();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_shelter);
        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = (TextView) view.findViewById(R.id.error_mess);
        mLoadingIndicator = (ProgressBar) view.findViewById(R.id.loadbar);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);


        mTransAdapter = new TransAdapter(this);
        mRecyclerView.setAdapter(mTransAdapter);
        showTransDataView();
        new GetTrans().execute("nostring");
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
        Intent intentnext = new Intent(getActivity(), TransportActivity.class);
        intentnext.putExtra("did",data);
        startActivity(intentnext);
    }
    
    
    private class GetTrans extends AsyncTask<String, Void, Void>  {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(String... strings) {
            ipadd = getActivity().getResources().getString(R.string.ipadd);
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),
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