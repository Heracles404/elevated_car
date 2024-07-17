package com.example.mobile_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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

public class ElevatorLevel2 extends AppCompatActivity {

    private static final String TAG = "ElevatorLevel2";
    private RadioGroup radioGroup_Origin_Elevator2;
    private RadioGroup radioGroup_Destination_Elevator2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_elevator_level2);

        Button move = findViewById(R.id.button_ConfirmFloor2);
        Button back = findViewById(R.id.button_NotFloor2);

        radioGroup_Origin_Elevator2 = findViewById(R.id.radioGroup_Origin_Elevator2);
        radioGroup_Destination_Elevator2 = findViewById(R.id.radioGroup_Destination_Elevator2);

        radioGroup_Origin_Elevator2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                if (radioButton != null) {
                    // Toast.makeText(ElevatorLevel2.this, radioButton.getText() + " is selected as Origin", Toast.LENGTH_SHORT).show();
                }
            }
        });

        radioGroup_Destination_Elevator2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                if (radioButton != null) {
//                    Toast.makeText(ElevatorLevel2.this, radioButton.getText() + " is selected as Destination", Toast.LENGTH_SHORT).show();
                }
            }
        });

        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the selected radio buttons
                int selectedOriginId = radioGroup_Origin_Elevator2.getCheckedRadioButtonId();
                int selectedDestinationId = radioGroup_Destination_Elevator2.getCheckedRadioButtonId();

                // Get the text of the selected radio buttons
                String origin = ((RadioButton) findViewById(selectedOriginId)).getText().toString();
                String destination = ((RadioButton) findViewById(selectedDestinationId)).getText().toString();

                // Retrieve the Android ID
                @SuppressLint("HardwareIds")
                String androidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                // Insert data into the PHP database
                new InsertDataTask().execute(androidID, origin, destination);

                // Start the appropriate activity based on the selected destination
                Intent intent;
                switch (destination) {
                    case "1":
                        intent = new Intent(ElevatorLevel2.this, ElevatorLevel1.class);
                        break;
                    case "2":
                        Toast.makeText(ElevatorLevel2.this, "You are already at Level 2!", Toast.LENGTH_SHORT).show();
                        return; // Exit early since no activity transition is needed
                    case "3":
                        intent = new Intent(ElevatorLevel2.this, ElevatorLevel3.class);
                        break;
                    default:
                        Toast.makeText(ElevatorLevel2.this, "Invalid destination selected!", Toast.LENGTH_SHORT).show();
                        return; // Exit early if the destination is invalid
                }
                startActivity(intent);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ElevatorLevel2.this, "Please Select a Level.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ElevatorLevel2.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private static class InsertDataTask extends AsyncTask<String, Void, String> {

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

                String postData = "appID=" + appID + "&origin=" + origin + "&destination=" + destination;
                OutputStream os = conn.getOutputStream();
                os.write(postData.getBytes());
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
            Log.d(TAG, "Insert Data Response: " + result);
        }
    }
}