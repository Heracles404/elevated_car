# Project Setup Instructions

## Materials / Components Used
1. Arduino
2. NodeMCU
3. Servo Motor (1)
4. Relay Switch (2)
5. Momentary Button (2)
6. Connecting Wires

## IDEs / Text Editors
1. Arduino IDE
2. Android Studio
3. Notepad ++
4. PHP Storm (optional - for php files)
5. IDLE (for flask)

## Hardware Setup
1. **Diagram Reference**: Refer to the diagram in the `hardware` folder for setup instructions.
2. **IP Address Configuration**:
   - Open `node.ino` and `arduino.ino` files.
   - Change the IP addresses as required.
3. **Code Upload**:
   - Upload `node.ino` to your NodeMCU or other ESP32 devices.
   - Upload `arduino.ino` to your Arduino board.
4. **Connections**:
   - Connect RX of NodeMCU/ESP32 to TX of Arduino.
   - Connect TX of NodeMCU/ESP32 to RX of Arduino.

## Database Setup
1. **Import SQL File**:
   - Open `localhost/phpmyadmin`.
   - Import the `elevator.sql` file.

## PHP Files Setup
1. **Copy Files**:
   - Locate your XAMPP `htdocs` directory.
   - Copy the `elevator` folder, which contains the PHP files, into the `htdocs` directory.

## Mobile App Setup
1. **IP Address Configuration**:
   - Open the Java files in your mobile app project.
   - Change the IP addresses as needed.

## Flask Web Server Version
1. **Run Flask Server**:
   - Open a terminal in the `flask-webserver` directory.
   - Run the following command:
     ```sh
     python run.py
     ```
