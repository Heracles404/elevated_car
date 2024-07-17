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

// Function to insert a new record
function insertRecord($conn, $appID, $origin, $destination) {
    $stmt = $conn->prepare("INSERT INTO car_movement (appID, origin, destination) VALUES (?, ?, ?)");
    if ($stmt === false) {
        error_log("Prepare failed: " . $conn->error);
        die("Prepare failed: " . $conn->error);
    }

    $stmt->bind_param("sss", $appID, $origin, $destination);

    // Execute the statement
    if ($stmt->execute()) {
        error_log("Record inserted successfully");
        echo "Record inserted successfully";
    } else {
        error_log("Execute failed: " . $stmt->error);
        echo "Error: " . $stmt->error;
    }

    // Close the statement
    $stmt->close();
}

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    if (isset($_POST['appID']) && isset($_POST['origin']) && isset($_POST['destination'])) {
        $appID = $_POST['appID'];
        $origin = $_POST['origin'];
        $destination = $_POST['destination'];
        error_log("Received data for insertion: appID=$appID, origin=$origin, destination=$destination");

        insertRecord($conn, $appID, $origin, $destination);
    } else {
        error_log("Required data not provided");
        echo "Required data not provided";
    }
} elseif ($_SERVER['REQUEST_METHOD'] == 'GET') {
    if (isset($_GET['appID']) && isset($_GET['origin']) && isset($_GET['destination'])) {
        $appID = $_GET['appID'];
        $origin = $_GET['origin'];
        $destination = $_GET['destination'];
        error_log("Received data for insertion: appID=$appID, origin=$origin, destination=$destination");

        insertRecord($conn, $appID, $origin, $destination);
    } else {
        error_log("Required data not provided");
        echo "Required data not provided";
    }
} else {
    error_log("Invalid request method");
    echo "Invalid request method";
}

// Close the connection
$conn->close();
?>
