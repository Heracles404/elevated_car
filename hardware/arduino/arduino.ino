#include <Servo.h> // Include the Servo library

#define rocker1 8
#define rocker2 9

int btn1, btn2;
Servo myServo; // Create a Servo object
int pos = 0; // Initial position of the servo

void setup() {
  Serial.begin(9600);
  pinMode(rocker1, INPUT);
  pinMode(rocker2, INPUT);
  myServo.attach(7); // Attach the servo to pin 7 (change if needed)
  myServo.write(pos); // Set initial position of the servo
}

void loop() {
  btn1 = digitalRead(rocker1);
  if (btn1 == HIGH && btn2 == LOW) {
    if (pos > 0) { // Ensure the position does not go below 0 degrees
      pos -= 10;
      if (pos < 0) pos = 0; // Ensure pos does not go below 0
      myServo.write(pos); // Decrease the servo position by 10
      Serial.println("Decreasing degree: " + String(pos));
      delay(200); // Add a small delay for debouncing
    }
  }

  btn2 = digitalRead(rocker2);
  if (btn2 == HIGH && btn1 == LOW) {
    if (pos < 180) { // Ensure the position does not exceed 180 degrees
      pos += 10;
      if (pos > 180) pos = 180; // Ensure pos does not exceed 180
      myServo.write(pos); // Increase the servo position by 10
      Serial.println("Increasing degree: " + String(pos));
      delay(200); // Add a small delay for debouncing
    }
  }

  // Check for user input from Serial Monitor
  if (Serial.available() > 0) {
    int floor = Serial.parseInt(); // Read the input number
    moveToFloor(floor);
  }

}

void moveToFloor(int floor) {
  switch (floor) {
    case 1:
      moveToFirstFloor();
      // Send this back to node
      Serial.write("a");
      Serial.println("");
      delay(3000);
      break;
    case 2:
      moveToSecondFloor();
      // Send this back to node
      Serial.write("b");
      Serial.println("");
      delay(3000);
      break;
    case 3:
      moveToThirdFloor();
     // Send this back to node
      Serial.write("c");
      Serial.println("");
      delay(3000);
      break;
    default:
      delay(2000);
  }
}

void moveToFirstFloor() {
  pos = 0; // position for the first floor
  myServo.write(pos);
  Serial.println("Moved to First Floor: " + String(pos));
}

void moveToSecondFloor() {
  pos = 50; // position for the second floor
  myServo.write(pos);
  Serial.println("Moved to Second Floor: " + String(pos));
}

void moveToThirdFloor() {
  pos = 180; // position for the third floor
  myServo.write(pos);
  Serial.println("Moved to Third Floor: " + String(pos));
}
