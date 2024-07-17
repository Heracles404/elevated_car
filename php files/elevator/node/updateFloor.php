<?php
include '../connect.php';

if (isset($_GET['floor'])) {
    $floor = $_GET['floor'];
    // Update the single record in the current_floor table
    $sql = "UPDATE current_floor SET floor='$floor'";
    if ($conn->query($sql) === TRUE) {
        echo "Floor updated successfully";
    } else {
        echo "Error: " . $sql . "<br>" . $conn->error;
    }
} else {
    echo "floor not set";
}
// <!--http://localhost/elevator/updateFloor.php?floor=2-->

$conn->close();
?>
