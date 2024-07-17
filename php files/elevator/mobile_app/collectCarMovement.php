<?php
// Database configuration
$servername = "localhost";
$username = "root";  // Default XAMPP username
$password = "";      // Default XAMPP password
$dbname = "elevator";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    error_log("Connection failed: " . $conn->connect_error);
    die("Connection failed: " . $conn->connect_error);
} else {
    error_log("Connected successfully to the database");
}

// Check if temp and humidity are set in the GET request
if (isset($_GET['origin']) && isset($_GET['destination'])) {
    $t = mysqli_real_escape_string($con, $_GET['origin']);
    $h = mysqli_real_escape_string($con, $_GET['destination']);

    // Use a prepared statement to avoid SQL injection
    $stmt = $con->prepare("INSERT INTO car_movement (origin, destination) VALUES (?, ?)");
    $stmt->bind_param("ss", $t, $h); // Assuming both fields are treated as strings

    // Execute the statement and check for errors
    if ($stmt->execute()) {
        echo "Data inserted successfully!";
    } else {
        echo "Error inserting data: " . $stmt->error;
    }

    $stmt->close();
} else {
    echo "origin and destination must be provided.";
}

$con->close();
?>