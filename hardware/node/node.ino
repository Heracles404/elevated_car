#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>

#define ldr1 D1
#define ldr2 D2
#define ldr3 D3

const char* ssid = "IoT";
const char* password = "AccessPoint.2024";

String pickedParam, destParam;


void setup() {
  Serial.begin(9600);
  WiFi.begin(ssid, password);

  Serial.print("Connecting to WiFi");
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println();
  Serial.println("Connected to WiFi");
}

void loop() {
  pickup();
}

void pickup() {
  String getOrigin = "http://192.168.110.196/elevator/node/moving.php";
  if (WiFi.status() == WL_CONNECTED) { // Check if we're connected to WiFi
    HTTPClient http;  // Object of class HTTPClient
    WiFiClient wifi;
    http.begin(wifi, getOrigin);  // Specify request destination
    
    int httpCode = http.GET(); // Send the request

    if (httpCode > 0) { // Check the returning code
      String payload = http.getString(); // Get the request response payload

      if (payload == "No records found") {
        Serial.println("Trying again... ");
        // Convert String to const char* and then print it / must write for movement of servo
        delay(5000); // Optional: wait for 1 second before retrying
        pickup(); // Go back to the beginning of the while loop
      }else{
        //Serial.print("Pickup: ");
        // Convert String to const char* and then print it / must write for movement of servo
        //Serial.write(payload.c_str());
        Serial.println("");
        
        //appID
        origin(payload);
      }
    } else {
      Serial.println("Error on HTTP request");
    }

    http.end(); // Close connection
  } else {
    Serial.println("WiFi not connected");
  }
}

void origin(String appids){
  String getOrigin = "http://192.168.110.196/elevator/node/origin.php?appID=" + appids;
  if (WiFi.status() == WL_CONNECTED) { // Check if we're connected to WiFi
    HTTPClient http;  // Object of class HTTPClient
    WiFiClient wifi;
    http.begin(wifi, getOrigin);  // Specify request destination
    
    int httpCode = http.GET(); // Send the request

    if (httpCode > 0) { // Check the returning code
      String payload = http.getString(); // Get the request response payload

      

      Serial.print("Origin: ");
      // Print the response / origin / must write for movement of servo
      Serial.write(payload.c_str());
      Serial.println("");

      proceed();


      // appID
      checkDestination(appids);

    } else {
      Serial.println("Error on HTTP request");
    }

    http.end(); // Close connection
  } else {
    Serial.println("WiFi not connected");
  }
}

void checkDestination(String dest){
  destParam = dest;
  String destination = "http://192.168.110.196/elevator/node/checkDestination.php?appID=" + destParam;

  if (WiFi.status() == WL_CONNECTED) { // Check if we're connected to WiFi
    HTTPClient http;  // Object of class HTTPClient
    WiFiClient wifi;
    
    http.begin(wifi, destination);  
    
    int httpCode = http.GET(); // Send the request

    if (httpCode > 0) {
      String payload = http.getString(); // Get the request response payload

      Serial.print("Destination: ");
      // Print the response / appID / must write for movement of servo
      Serial.write(payload.c_str());
      Serial.println("");

      proceed();

      arrived(payload);

    } else {
      Serial.println("Error on HTTP request");
    }

    http.end(); // Close connection
  } else {
    Serial.println("WiFi not connected");
  }
}

void arrived(String toRemove){
  String delSQL = "http://192.168.110.196/elevator/node/arrived.php?destination=" + toRemove;

  if (WiFi.status() == WL_CONNECTED) { // Check if we're connected to WiFi
    HTTPClient http;  // Object of class HTTPClient
    WiFiClient wifi;
    
    http.begin(wifi, delSQL);  
    
    int httpCode = http.GET(); // Send the request

    if (httpCode > 0) {
      String payload = http.getString(); // Get the request response payload

      Serial.println("Arrived in " + toRemove);
    } else {
      Serial.println("Error on HTTP request");
    }

    http.end(); // Close connection
  } else {
    Serial.println("WiFi not connected");
  }
}

void proceed() {
  String done;
  do {
    while (Serial.available() <= 0) {
      // Waiting for input
    }
    done = Serial.readStringUntil('\n'); // Read the input until newline character
    done.trim(); // Remove any trailing newline or whitespace
  } while (!(done.equals("a") || done.equals("b") || done.equals("c"))); // Keep waiting until "a", "b", or "c" is received

  String doneSQL = "http://192.168.110.196/elevator/node/updateFloor.php?floor=" + done;

  if (WiFi.status() == WL_CONNECTED) { // Check if we're connected to WiFi
    HTTPClient http;  // Object of class HTTPClient
    WiFiClient wifi;
    
    http.begin(wifi, doneSQL);  
    
    int httpCode = http.GET(); // Send the request

    if (httpCode > 0) {
      String payload = http.getString(); // Get the request response payload
      Serial.println(payload);
    } else {
      Serial.println("Error on HTTP request");
    }

    http.end(); // Close connection
  } else {
    Serial.println("WiFi not connected");
  }

  Serial.println(done);
}


