package com.conductor.appconductor.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.conductor.appconductor.Clases.Cambio;
import com.conductor.appconductor.Clases.VolleySingleton;
import com.conductor.appconductor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MonedasActivity extends AppCompatActivity {


    Button btn;

    JsonObjectRequest jsonObjectRequest;
    TextView  txtnumero;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monedas);

        txtnumero=(TextView)findViewById(R.id.txtnumero);
        btn=(Button)findViewById(R.id.btn1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscar();

            //    Ingresar();
            }
        });
    }

    private void buscar() {
        final String fecha ="2020-08-13";
        String URL2="https://api.sunat.cloud/cambio/"+fecha;
        final RequestQueue requestQueue2= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest1 =new StringRequest(Request.Method.GET,URL2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responses) {
                        try {
                            JSONObject jsonObject2=new JSONObject(responses);


                        //    JSONObject jsonObject3 =new JSONObject(jsonObject2);
                            String dato =jsonObject2.getString("cambio");

                            JSONObject jsonjObject = new JSONObject(responses);
                            String valorLlave = jsonjObject.getString("cambio");

                        String array []=responses.split(":");
                            Log.e("datos",dato);
                        //    JSONArray jsonArray=jsonObject2.getJSONArray(fecha);

                    /*
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                JSONObject jsonVideoId=jsonObject1.getJSONObject("compra");
                                JSONObject jsonsnippet= jsonObject1.getJSONObject("venta");

                                Toast.makeText(MonedasActivity.this, jsonVideoId.toString(), Toast.LENGTH_SHORT).show();
                            }


                     */

                           // JSONArray json=responses.(fecha);

                        //    String nombre=jsonObject2.getString("compra");
                           // Toast.makeText(MonedasActivity.this, nombre, Toast.LENGTH_SHORT).show();
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(MonedasActivity.this, "Err "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest1.setRetryPolicy(policy);
        requestQueue2.add(stringRequest1);
    }

    private  void lista1(){
/*
        String booksJson="asdfsd";
        JSONObject root = new JSONObject(booksJson);
        JSONArray booksArray = root.getJSONArray("books");
        JSONObject firstBook = booksArray.getJSONObject(0);
        String title = firstBook.getString("title");
        int timesSold = firstBook.getInt("times_sold");

 */
    }

    private void Ingresar() {

        final String fecha ="2020-08-13";
        String URL2="https://api.sunat.cloud/cambio/"+fecha;


        //final String ip=getString(R.string.ip);
       // String URL2=ip;
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, URL2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
             //   pDialog.hide();

                Cambio miUsuario=new Cambio();

                JSONArray json=response.optJSONArray(fecha);
                JSONObject jsonObject=null;

                try {
                    jsonObject=json.getJSONObject(2);
                    miUsuario.setCompra(jsonObject.optString("compra"));
                    miUsuario.setVenta(jsonObject.optString("venta"));

                    Toast.makeText(MonedasActivity.this, miUsuario.getCompra(), Toast.LENGTH_SHORT).show();




                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
                System.out.println();
            //    pDialog.hide();
                Log.d("ERROR: ", error.toString());
            }
        });

        // request.add(jsonObjectRequest);
        VolleySingleton.getIntanciaVolley(getBaseContext()).addToRequestQueue(jsonObjectRequest);

    }
}
