package com.example.myapplication;


import android.content.Intent;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Autorisation extends AppCompatActivity {


    private EditText phoneEditText;
    private EditText passwordEditText;
    private String token;
    private static final String TAG = "AuthorizationScreen";
    private static final String api_address = "https://cr-test-ribu2uaqea-ey.a.run.app/";
    private static final String appName = "cuton";
    private static final int v = 36;

    private String devman;
    private String devmod;
    private String devavs;
    private String devaid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autorisation);

        // Отримання даних про пристрій
        devman = Build.MANUFACTURER;
        devmod = Build.MODEL;
        devavs = Build.VERSION.RELEASE;
        devaid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        // Підключення до API api_address/app/version/latest/ для перевірки версії додатку
        HttpUrl.Builder urlBuilder = HttpUrl.parse(api_address + "/app/version/latest/").newBuilder();
        urlBuilder.addQueryParameter("v", "36");
        String url = urlBuilder.build().toString();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                JSONObject jsonData = new JSONObject(response.body().string());
                int answer = jsonData.getInt("answer");
                if (answer == 2) {
                    Toast.makeText(this, "Версія додатку застаріла, авторизація неможлива", Toast.LENGTH_SHORT).show();
                } else if (answer == 1) {
                    Toast.makeText(this, "Є більш нова версія додатку", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Не вдалося підключитися до API", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException | JSONException e) {
            Log.e("AuthorizationScreen", e.getMessage(), e);
        }


        // Отримання посилань на елементи екрану
        phoneEditText = findViewById(R.id.phone);
        passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login);

        // Додавання обробника події на кнопку "Логін"
        Button login = findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = "+380501234567"; // введене користувачем значення телефону
                String password = "123456"; // введене користувачем значення пароля

                // Створення об'єкту для виконання запитів до API
                OkHttpClient client = new OkHttpClient();

                // Формування запиту для логіну
                JSONObject requestBody = new JSONObject();
                try {
                    requestBody.put("login", phone);
                    requestBody.put("password", password);
                    requestBody.put("devman", devman);
                    requestBody.put("devmod", devmod);
                    requestBody.put("devavs", devavs);
                    requestBody.put("devaid", devaid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }



                String api_address = "https://cr-test-ribu2uaqea-ey.a.run.app/routes/";
                Request request = new Request.Builder()
                        .url(api_address + "/users/login/")
                        .post(RequestBody.create(MediaType.parse("application/json"), requestBody.toString()))
                        .build();

                // Виконання запиту до API
                try {
                    Response response = client.newCall(request).execute();
                    String responseBody = response.body().string();

                    // Обробка відповіді від API
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(responseBody);
                        token = jsonObject.getString("token");

                        // Перехід на екран HomeMenu
                        Intent intent = new Intent(Autorisation.this, HomeMenu.class);
                        startActivity(intent);
                    } else {
                        // Виведення повідомлення про помилку
                        Toast.makeText(Autorisation.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}


