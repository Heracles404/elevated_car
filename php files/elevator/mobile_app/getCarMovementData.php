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

$query = "SELECT origin, destination FROM car_movement ORDER BY appID DESC LIMIT 1";
$result = mysqli_query($conn, $query);  // Use $conn here, not $con

if($result && mysqli_num_rows($result) > 0) {
    $data = mysqli_fetch_assoc($result);
    echo json_encode($data);
} else {
    echo json_encode(["error" => "Error fetching data"]);
}

$conn->close();
?>
