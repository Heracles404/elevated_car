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
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button move = findViewById(R.id.button_ElevatorLevel1);
        Button moveAboutUs = findViewById(R.id.moveAboutUs);

        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ElevatorLevel1.class);
                startActivity(intent);

                // Toast.makeText(MainActivity.this, "You can now Select Origin and Destination!", Toast.LENGTH_SHORT).show();

                // Retrieve the Android ID
                @SuppressLint("HardwareIds")
                String androidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                // Insert the origin and destination in the PHP database
                //new InsertOriginDestinationTask().execute(androidID, "empty", "empty"); // Replace "1" and "3" with the actual origin and destination values you want to set

                // Send the Android ID to the server
                //sendAndroidIDToServer(androidID);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        moveAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(MainActivity.this, "Welcome to About us!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, AboutUsPage.class);
                startActivity(intent);
            }
        });
    }

    private void sendAndroidIDToServer(String androidID) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://192.168.110.196/elevator/mobile_app/register_device.php");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                    String data = "appID=" + androidID;
                    OutputStream os = conn.getOutputStream();
                    os.write(data.getBytes());
                    os.flush();
                    os.close();

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    Log.d(TAG, "Server Response: " + response.toString());
                } catch (Exception e) {
                    Log.e(TAG, "Error sending Android ID to server", e);
                }
            }
        }).start();
    }

    private static class InsertOriginDestinationTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String appID = params[0];
            String origin = params[1];
            String destination = params[2];
            String urlString = "http://192.168.110.196/elevator/mobile_app/insertOriginDestination.php";
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String data = "appID=" + appID + "&origin=" + origin + "&destination=" + destination;
                OutputStream os = conn.getOutputStream();
                os.write(data.getBytes());
                os.flush();
                os.close();

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
                    return "POST request failed";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // Handle the result (e.g., update UI)
            Log.d(TAG, "Insert Origin and Destination Response: " + result);
        }
    }
}
