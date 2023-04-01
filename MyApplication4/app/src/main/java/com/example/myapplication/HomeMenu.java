package com.example.myapplication;



import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;


public class HomeMenu extends AppCompatActivity {
    private String token = "MzgwOTM4ODQ1Mzk06T1Wbh";
    private static final String api_address = "https://cr-test-ribu2uaqea-ey.a.run.app/";
    private Button Buttoncatalog ;
    private Button Buttonexit;
    private Button Buttonusers;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_menu);

        // Отримуємо токен з попередньої вкладки
        Bundle extras = getIntent().getExtras();
        token = extras.getString("token");

        // Знаходимо кнопки та додаємо обробники подій
        Buttonusers = findViewById(R.id.users);
        Buttonusers.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getUserInfo();
        }
    });


    Buttoncatalog = findViewById(R.id.catalog);
        Buttoncatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectToCatalogAPI(token);
                openCatalogBrandsScreen();
            }
        });

        Buttonexit = findViewById(R.id.exit);
        Buttonexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeMenu.this, LogOut.class);
                startActivity(intent);
            }
        });
    }

    private void getUserInfo() {
            String url = api_address + "/users/?token=" + token;

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Обробка JSON-відповіді
                            try {
                                int userId = response.getInt("userld");
                                String firstName = response.getString("firstname");
                                String lastName = response.getString("lastname");
                                // інші поля
                                // ...
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Обробка помилки
                        }
                    });
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(request);
        }
    private void connectToCatalogAPI(String token) {
        String url = api_address + "/home/menu/items/?token=" + token;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Обробка JSON-відповіді
                        try {
                            JSONObject items = response.getJSONObject("items");
                            int itemId = items.getInt("itemld");
                            String itemName = items.getString("itemName");
                            String itemImage = items.getString("itemImage");
                            // ...
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Обробка помилки
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(request);
    }
    public void openCatalogBrandsScreen() {
        Intent intent = new Intent(this, CatalogBrand1.class);
        startActivity(intent);
    }
}