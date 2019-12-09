package com.example.namabarang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import server.ConfigUrl;

public class MainActivity extends AppCompatActivity {

  private RequestQueue mRequestQueue;
  private TextView txtdata;

  private EditText edtkodebarang, edtnamabarang, edtharga, edtjenis;
  private Button btnsimpan;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mRequestQueue = Volley.newRequestQueue(this);

    txtdata = (TextView) findViewById(R.id.txtdataku);

    edtkodebarang = (EditText) findViewById(R.id.edtkodebarang);
    edtnamabarang =(EditText) findViewById(R.id.edtnamabarang);
    edtharga = (EditText) findViewById(R.id.edtharga);
    edtjenis = (EditText) findViewById(R.id.edtjenis);

    btnsimpan = (Button) findViewById(R.id.btnsimpan);

    btnsimpan.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String strkodebarang = edtkodebarang.getText().toString();
        String strnamabarang = edtnamabarang.getText().toString();
        String strharga = edtharga.getText().toString();
        String strjenis = edtjenis.getText().toString();

        if(strkodebarang.isEmpty()){
          Toast.makeText(getApplicationContext(), "Kode tidak boleh kosong",
            Toast.LENGTH_LONG).show();
        } else if(strnamabarang.isEmpty()) {
          Toast.makeText(getApplicationContext(), "Nama Barang tidak boleh kosong",
            Toast.LENGTH_LONG).show();
        } else  if(strharga.isEmpty()) {
          Toast.makeText(getApplicationContext(), "Harga tidak boleh kosong",
            Toast.LENGTH_LONG).show();
        } else  if(strjenis.isEmpty()) {
          Toast.makeText(getApplicationContext(), "Jenis tidak boleh kosong",
            Toast.LENGTH_LONG).show();
        } else {
          inputData(strkodebarang, strnamabarang, strharga, strjenis);

          Intent a = new Intent(MainActivity.this,MainActivity.class);
          startActivity(a);
          finish();
          Toast.makeText(getApplicationContext(), "Input Data Tersimpan",
            Toast.LENGTH_LONG).show();
        }
      }
    });
    fetchJsonResponse();
  }


  private void fetchJsonResponse() {
    // Pass second argument as "null" for GET requests
    JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, ConfigUrl.getAllnamabarang, null,
      new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
          try {
            String result = response.getString("data");
            //Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
            //Log.v("ini data dari server",result.toString());
            JSONArray res = new JSONArray(result);
            for ( int i = 0; i < res.length(); i++){
              JSONObject jObj = res.getJSONObject(i);
              txtdata.append("Kode Barang = "+ jObj.getString("kodebarang") + "\n"+
                "Nama Barang = " + jObj.getString("namabarang") + "\n\n");
            }


          } catch (JSONException e) {
            e.printStackTrace();
          }
        }
      }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        VolleyLog.e("Error: ", error.getMessage());
      }
    });

    /* Add your Requests to the RequestQueue to execute */
    mRequestQueue.add(req);
  }



  private void inputData(String kodebarang, String namabarang, String harga, String jenis){
// Post params to be sent to the server
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("kodebarang", kodebarang);
    params.put("namabarang", namabarang);
    params.put("harga", harga);
    params.put("jenis", jenis);

    JsonObjectRequest req = new JsonObjectRequest(ConfigUrl.inputDatanambarang, new JSONObject(params),
      new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
          try {
            VolleyLog.v("Response:%n %s", response.toString(4));
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }
      }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        VolleyLog.e("Error: ", error.getMessage());
      }
    });

// add the request object to the queue to be executed
    //ApplicationController.getInstance().addToRequestQueue(req);
    mRequestQueue.add(req);
  }

}
