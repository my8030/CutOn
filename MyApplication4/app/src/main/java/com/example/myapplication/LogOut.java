package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;


public class LogOut extends AppCompatActivity {

        private String apiAddress = "https://cr-test-ribu2uaqea-ey.a.run.app/";
        private String token  = "MzgwOTM4ODQ1Mzk06T1Wbh";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_log_out);

            // Отримати token з попередньої активності
            Intent intent = getIntent();
            token = intent.getStringExtra("TOKEN");

            Button yesButton = findViewById(R.id.yes);
            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Виконати запит DELETE до API
                    String url = apiAddress + "/users/?token=" + token;
                    RequestQueue queue = Volley.newRequestQueue(LogOut.this);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        int answer = response.getInt("answer");
                                        if (answer == 1) {
                                            Toast.makeText(LogOut.this, "Ви вийшли з додатку", Toast.LENGTH_SHORT).show();
                                            finishAffinity();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(LogOut.this, "Помилка виходу з додатку", Toast.LENGTH_SHORT).show();
                                    error.printStackTrace();
                                }
                            });

                    queue.add(jsonObjectRequest);
                }
            });

            Button noButton = findViewById(R.id.no);
            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }
