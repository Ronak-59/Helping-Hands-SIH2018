package com.sih2018.helpinghand;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sih2018.helpinghand.data.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UploadActivity extends AppCompatActivity implements View.OnClickListener{

    private Button UploadBn,ChooseBn;
    private EditText NAME;
    TextView simiresult;
    private ImageView imgView;
    CardView cv;
    private final int IMG_REQUEST=1;
    private Bitmap bitmap;
    GetRekogResult getRekogResult;
<<<<<<< HEAD
    private String uploadUrl = "http://192.168.43.143/imager/updateinfo.php";
=======
    private String uploadUrl = "http://192.168.137.76/imager/updateinfo.php";
>>>>>>> 9a56d640d3cd6afceb099c038e75eadeff5b6dd6
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private  String url = "/imager/updateinfo.php";
    private String headurl = "http://";
    String ipadd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        UploadBn=(Button)findViewById(R.id.uploadBn);
        ChooseBn=(Button)findViewById(R.id.chooseBn);
        cv=(CardView) findViewById(R.id.top_card);
        simiresult=(TextView)findViewById(R.id.simiresult);
        NAME=(EditText)findViewById(R.id.name);
        imgView=(ImageView)findViewById(R.id.imageView);
        ChooseBn.setOnClickListener(this);
        UploadBn.setOnClickListener(this);
        mErrorMessageDisplay = (TextView) findViewById(R.id.error_mess);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.loadbar);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.chooseBn:
                selectImage();
                break;
            case R.id.uploadBn:
                uploadImage();
                break;
        }
    }

    private void selectImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMG_REQUEST && resultCode==RESULT_OK && data!=null)
        {
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                imgView.setImageBitmap(bitmap);
                NAME.setVisibility(View.VISIBLE);
                imgView.setVisibility(View.VISIBLE);
                cv.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, uploadUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response",response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String Response = jsonObject.getString("response");
                            Toast.makeText(UploadActivity.this,Response,Toast.LENGTH_LONG).show();
                            imgView.setImageResource(0);
                            imgView.setVisibility(View.GONE);
                            NAME.setText("");
                            NAME.setVisibility(View.GONE);
                            cv.setVisibility(View.GONE);
                            new GetRekogResult().execute();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("name","1");
                params.put("image",imageToString(bitmap));
                return params;
            }
        };
        MySingleton.getmInstance(UploadActivity.this).addToRequestQue(stringRequest);
    }

    private String imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgBytes =  byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes,Base64.DEFAULT);
    }


    private void showShelterDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data *
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }


    private class GetRekogResult extends AsyncTask<Void, Void, String[]> {


        @Override
        protected String[] doInBackground(Void... voids) {
            ipadd = "192.168.43.143";
            HttpHandler sh = new HttpHandler();
            String percent[]=new String[2];
            url = "/imager/compare-faces.php";
            headurl = "http://";
            url=headurl+ipadd+url;
            Log.e("url",url);
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e("Uploadactivity", "Response from url: " + jsonStr); //The Output of First Page

            if (jsonStr != null) {
                try {
                    // Getting JSON Array node
                    JSONObject obj = new JSONObject(jsonStr);
                    percent[0]=obj.getString("simi");
                    percent[1]=obj.getString("tid");

                } catch (final JSONException e) {


                }



            } else {

            }

            return percent;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }



        @Override
        protected void onPostExecute(String result[]) {
            super.onPostExecute(result);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            showShelterDataView();
<<<<<<< HEAD
            if(result[0].equals("0")) {
=======
            if(result.equals("0")) {
>>>>>>> 9a56d640d3cd6afceb099c038e75eadeff5b6dd6
                simiresult.setText("No Match Found");
            }
            else {
                simiresult.setText(result[0] + "% Similarity | Name: "+result[1]+" | Shelter: Goreshwar High School");
            }

        }

    }
}
