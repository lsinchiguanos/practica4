package com.example.practica4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.practica4.models.Country;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RequestQueue rque;
    String url = "https://api.covid19api.com/summary";
    List<String> data = new ArrayList<String>();
    ListView lstDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Inicializo mi Volly */
        rque = Volley.newRequestQueue(this);

        /* Llamado a la getApiData*/
        getApiData();

        lstDatos = (ListView) findViewById(R.id.lstDatos);
    }

    private void getApiData(){

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("Countries");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = new JSONObject(jsonArray.get(i).toString());
                                Country country = new Country();
                                country.setCountry(obj.getString("Country"));
                                country.setCountryCode(obj.getString("CountryCode").toString());
                                country.setSlug(obj.getString("Slug").toString());
                                country.setNewConfirmed(Integer.parseInt(obj.getString("NewConfirmed").toString()));
                                country.setTotalConfirmed(Integer.parseInt(obj.getString("TotalConfirmed").toString()));
                                country.setNewDeaths(Integer.parseInt(obj.getString("NewDeaths").toString()));
                                country.setTotalDeaths(Integer.parseInt(obj.getString("TotalDeaths").toString()));
                                country.setNewRecovered(Integer.parseInt(obj.getString("NewRecovered").toString()));
                                country.setTotalRecovered(Integer.parseInt(obj.getString("TotalRecovered").toString()));
                                country.setDate(obj.getString("Date").toString());

                                data.add(obj.get("Country").toString());

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, data);
                                lstDatos.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("HttpClient", "error: " + error.toString());
                    }
                }
        );
        rque.add(request);
    }

}