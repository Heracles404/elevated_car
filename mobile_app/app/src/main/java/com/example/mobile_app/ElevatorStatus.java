package com.example.mobile_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class ElevatorStatus extends AppCompatActivity {

    private TextView textOrigin, textDestination, textWelcome;
    private RequestQueue requestQueue;
    private Button move;
    private Handler handler;
    private Runnable runnable;
    private static final int UPDATE_INTERVAL = 5000; // 5 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elevator_status);

        textOrigin = findViewById(R.id.LevelStatus); // Assuming this is the ID for the TextView for origin
        textDestination = findViewById(R.id.LevelLocation); // Assuming this is the ID for the TextView for destination
        textWelcome = findViewById(R.id.TextView_Location);
        move = findViewById(R.id.proceedHome);
        move.setVisibility(View.INVISIBLE); // Initially hide the button

        requestQueue = Volley.newRequestQueue(this);

        // Initial data fetch
        fetchData();

        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ElevatorStatus.this, "Thank you for using our service!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ElevatorStatus.this, MainActivity.class);
                startActivity(intent);
            }
        });


        // Initialize handler and runnable for periodic update
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                fetchData();
                handler.postDelayed(this, UPDATE_INTERVAL);
            }
        };

        // Start periodic update
        handler.post(runnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop the periodic update when the activity is destroyed
        handler.removeCallbacks(runnable);
    }

    private void fetchData() {
        String url = "http://192.168.110.196/elevator/mobile_app/getCarMovementData.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(

                Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.has("origin") && response.has("destination")) {
                            String destination = response.getString("destination");
                            textDestination.setText("LEVEL " + destination);

                            String origin = response.getString("origin");
                            updateLevelStatus(origin);

                            // Check if origin is "Arrived" to show the button
                            if (origin.equals("ARRIVED")) {
                                move.setVisibility(View.VISIBLE); // Show the button
                            } else {
                                move.setVisibility(View.INVISIBLE); // Hide the button if not arrived
                            }
                        } else if (response.has("error")) {
                            textOrigin.setText("ARRIVED");
                            textDestination.setText(" Welcome! ");
                            textWelcome.setText(" You can now press 'Proceed' ");
                            move.setVisibility(View.VISIBLE); // Show the button when error occurs
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("ElevatorStatus", "Error parsing data");
                        textDestination.setText("Error parsing data");
                    }
                },
                error -> {
                    error.printStackTrace();
                    Log.d("ElevatorStatus", "Error fetching data");
                    textDestination.setText("Error fetching data");
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void updateLevelStatus(String origin) {
        switch (origin) {
            case "1":
            case "2":
            case "3":
                textOrigin.setText("WAITING");
                break;
            case "0":
                textOrigin.setText("ON BOARDING");
                break;
            default:
                textOrigin.setText("ARRIVED");
                break;
        }
    }
}
