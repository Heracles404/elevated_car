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

public class ElevatorLevel1 extends AppCompatActivity {

    private static final String TAG = "ElevatorLevel1";
    private RadioGroup radioGroup_Origin_Elevator1;
    private RadioGroup radioGroup_Destination_Elevator1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_elevator_level1);

        Button move = findViewById(R.id.button_ConfirmFloor1);
        Button back = findViewById(R.id.button_GoBackFromStart);

        radioGroup_Origin_Elevator1 = findViewById(R.id.radioGroup_Origin_Elevator1);
        radioGroup_Destination_Elevator1 = findViewById(R.id.radioGroup_Destination_Elevator1);



        radioGroup_Origin_Elevator1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                if (radioButton != null) {
                    // Toast.makeText(ElevatorLevel1.this, radioButton.getText() + " is selected as Origin", Toast.LENGTH_SHORT).show();

                    RadioButton origin1 = findViewById(R.id.OriginLevel1_Elevator1);
                    RadioButton origin2 = findViewById(R.id.OriginLevel2_Elevator1);
                    RadioButton origin3 = findViewById(R.id.OriginLevel3_Elevator1);

                    RadioButton destination1 = findViewById(R.id.DestinationLevel1_Elevator1);
                    RadioButton destination2 = findViewById(R.id.DestinationLevel2_Elevator1);
                    RadioButton destination3 = findViewById(R.id.DestinationLevel3_Elevator1);

                    // Reset all DestinationLevel buttons to enabled first
                    destination1.setEnabled(true);
                    destination2.setEnabled(true);
                    destination3.setEnabled(true);

                    // Disable corresponding DestinationLevel button
                    if (checkedId == origin1.getId()) {
                        destination1.setEnabled(false);
                    } else if (checkedId == origin2.getId()) {
                        destination2.setEnabled(false);
                    } else if (checkedId == origin3.getId()) {
                        destination3.setEnabled(false);
                    }
                }
            }
        });

        radioGroup_Destination_Elevator1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                if (radioButton != null) {
                    // Toast.makeText(ElevatorLevel1.this, radioButton.getText() + " is selected as Destination", Toast.LENGTH_SHORT).show();
                }
            }
        });

        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the selected radio buttons
                int selectedOriginId = radioGroup_Origin_Elevator1.getCheckedRadioButtonId();
                int selectedDestinationId = radioGroup_Destination_Elevator1.getCheckedRadioButtonId();

                // Get the text of the selected radio buttons
                String origin = ((RadioButton) findViewById(selectedOriginId)).getText().toString();
                String destination = ((RadioButton) findViewById(selectedDestinationId)).getText().toString();

                // Toast.makeText(ElevatorLevel1.this, "You will shortly arrive at Level " + destination + "!", Toast.LENGTH_SHORT).show();

                // Retrieve the Android ID
                @SuppressLint("HardwareIds")
                String androidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                // Insert data into the PHP database
                new InsertDataTask().execute(androidID, origin, destination);

                // Start the appropriate activity based on the selected destination
                Intent intent;
                switch (destination) {
                    case "1":
                        intent = new Intent(ElevatorLevel1.this, ElevatorStatus.class);
                        break;
                    case "2":
                        intent = new Intent(ElevatorLevel1.this, ElevatorStatus.class);
                        break;
                    case "3":
                        intent = new Intent(ElevatorLevel1.this, ElevatorStatus.class);
                        break;
                    default:
                        Toast.makeText(ElevatorLevel1.this, "Invalid destination selected!", Toast.LENGTH_SHORT).show();
                        return; // Exit early if the destination is invalid
                }
                startActivity(intent);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ElevatorLevel1.this, "Thank you for using our service!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ElevatorLevel1.this, MainActivity.class);
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