package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static String API_URL = "https://cr-test-ribu2uaqea-ey.a.run.app/routes/";

    private static final String APP_NAME = "cuton";
    private static final int APP_VERSION = 36;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Display the InitializeScreen
        Intent initializeIntent = new Intent(this, MainActivity.class);
        startActivity(initializeIntent);

        // Check for internet connectivity
        if (isConnected()) {
            // Connect to the API to get the api_address
            getApiAddress();
        } else {
            // Display error message if there is no internet connectivity
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }

        // Open the AuthorizationScreen
        Intent authorizationIntent = new Intent(this, Autorisation.class);
        startActivity(authorizationIntent);
    }



    private boolean isConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void getApiAddress() {
        String url = API_URL + "?appName=" + APP_NAME + "&v=" + APP_VERSION;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);
                API_URL = jsonObject.getString("route");
            } else {
                Toast.makeText(this, "Failed to get api_address", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
