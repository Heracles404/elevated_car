<?php
include '../connect.php';

// Check if 'appID' is set in the GET parameters
if (isset($_GET['appID'])) {
    $appID = $_GET['appID']; // Assign the value from GET to $appID
} else {
    die("No 'appID' parameter provided");
}

// Query to fetch the oldest record
$sql = "SELECT origin FROM car_movement WHERE appID='$appID'";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    // Output the data of the oldest record
    $row = $result->fetch_assoc();
    echo $row['origin'];

} else {
    echo "No records found for appID: $appID";
}

$conn->close();
?>
