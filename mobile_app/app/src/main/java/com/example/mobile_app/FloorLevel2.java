package com.example.mobile_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FloorLevel2 extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_floor_level2);

        Button move = findViewById(R.id.button_Floor2Down);
        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FloorLevel2.this, ElevatorLevel2.class);
                startActivity(intent);

                // Retrieve the Android ID
                @SuppressLint("HardwareIds")
                String androidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                // Update the origin in the PHP database
                new FloorLevel2.UpdateDestinationTask().execute(androidID, "0"); // Replace "1"

                // Update the origin in the PHP database
                new UpdateOriginTask().execute(androidID, "2"); // Replace "1" with the actual origin value you want to set
            }
        });

        Button move2 = findViewById(R.id.button_Floor2Up);
        move2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FloorLevel2.this, ElevatorLevel2.class);
                startActivity(intent);

                // Retrieve the Android ID
                @SuppressLint("HardwareIds")
                String androidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                // Update the origin in the PHP database
                new FloorLevel2.UpdateDestinationTask().execute(androidID, "0"); // Replace "1" with the actual origin value you want to set

                // Update the origin in the PHP database
                new UpdateOriginTask().execute(androidID, "2"); // Replace "1" with the actual origin value you want to set
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private static class UpdateOriginTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String appID = params[0];
            String origin = params[1];
            String urlString = "http://192.168.110.196/elevator/mobile_app/newOrigin.php?appID=" + appID + "&origin=" + origin;
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    return response.toString();
                } else {
                    return "GET request failed";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // Handle the result (e.g., update UI)
            Log.d(TAG, "Update Origin Response: " + result);
        }
    }
    private static class UpdateDestinationTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String appID = params[0];
            String destination = params[1];
            String urlString = "http://192.168.110.196/elevator/mobile_app/newDestination.php?appID=" + appID + "&destination=" + destination;
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    return response.toString();
                } else {
                    return "GET request failed";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // Handle the result (e.g., update UI)
            Log.d(TAG, "Update Destination Response: " + result);
        }
    }
}