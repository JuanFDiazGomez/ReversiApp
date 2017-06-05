<?php
	$array;
	$conn = new mysqli("localhost","root","","News");
	if($conn->connect_error){
		die("Connection failed: " . $conn->connect_error);
	}
	$sql = "SELECT * FROM News";
	$result = $conn->query($sql);
	#if we start the array from 0, the key of each collection will disappear ma o menos
	$numeroNoticia=0;
	if($result->num_rows > 0){
		while($row = $result->fetch_assoc()){
			$array[$numeroNoticia]=$row;
			$numeroNoticia++;
		}
	}
	echo json_encode($array);
?>