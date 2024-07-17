<?php
// Include database connection
include '../connect.php';

// Check if appID is provided in the URL
if (isset($_GET['appID'])) {
    $appID = $_GET['appID'];

    sleep(1);
    
    // Update the record with origin='0'
    $sql_update = "UPDATE car_movement SET origin='0' WHERE appID='$appID'";
    if ($conn->query($sql_update) === TRUE) {
    // Successfully updated
    } else {
        echo "Error updating record: " . $conn->error;
    }

    // Select the destination column based on the provided appID
    $sql_select = "SELECT destination FROM car_movement WHERE appID='$appID'";
    $result = $conn->query($sql_select);

    if ($result->num_rows > 0) {
        // Output data of the first row (assuming appID is unique)
        $row = $result->fetch_assoc();
        echo $row['destination'];
    } else {
        echo "0 results";
    }

} else {
    echo "AppID not set";
}

// Close the database connection
$conn->close();
?>
