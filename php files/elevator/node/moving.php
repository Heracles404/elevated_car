<?php
include '../connect.php';

// Query to fetch the oldest record
$sql = "SELECT appID FROM car_movement ORDER BY appID ASC LIMIT 1";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    // Output the data of the oldest record
    $row = $result->fetch_assoc();
//    echo "Oldest record origin: " . $row['origin'];
    echo $row['appID'];
} else {
    echo "No records found";
}

$conn->close();
?>
