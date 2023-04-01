package com.example.myapplication;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;



import java.net.*;
import java.io.*;
import org.json.*;

public class CatalogBrand1 extends AppCompatActivity {
    private String api_address = "https://cr-test-ribu2uaqea-ey.a.run.app/";
    private String token = "MzgwOTM4ODQ1Mzk06T1Wbh";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog_brand1);

        // Отримання токену з попередньої активності
        token = getIntent().getStringExtra("token");

        // Підключення до API і отримання даних
        try {
            String url = api_address + "/catalog/brands/?token=" + token;
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonObject = new JSONObject(response.toString());
            JSONObject brands = jsonObject.getJSONObject("brands");
            JSONArray brandIds = brands.names();

            // Отримання списку брендів з JSON-відповіді і відображення їх на екрані
            LinearLayout layout = (LinearLayout) findViewById(R.id.recyclerView);
            for (int i = 0; i < brandIds.length(); i++) {
                int brandId = Integer.parseInt(brandIds.get(i).toString());
                JSONObject brand = brands.getJSONObject(Integer.toString(brandId));

                TextView textView = new TextView(this);
                textView.setText(brand.getString("brandName"));
                layout.addView(textView);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
