
<?php
include '../connect.php';

sleep(2);

if (isset($_GET['destination'])) {
    $destination = $_GET['destination'];
    $sql = "DELETE FROM car_movement WHERE origin='0' AND destination='$destination'";
    if ($conn->query($sql) === TRUE) {
        echo "Record deleted successfully";
    } else {
        echo "Error: " . $sql . "<br>" . $conn->error;
    }
} else {
    echo "destination not set";
}

$conn->close();

//<!--http://localhost/elevator/arrived.php?destination=3-->
?>
