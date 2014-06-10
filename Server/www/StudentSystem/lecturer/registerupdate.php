<?php
/**
 * Created by
 * User: Steve
 * Version: 1.0
 * Update a register to completed and submit register data to database
 */
include("../includes/connect.php");
if(!empty($_POST['registerData'])){

$registerData = $_POST['registerData'];
$mid = $_POST['mid'];
$startTime = $_POST['startTime'];
$endTime = $_POST['endTime'];

$query = $pdo->prepare("UPDATE `timetable`	
SET registerData=?,att=?
WHERE mid=? AND startTime=? AND endTime=?");
$query->bindValue(1,$registerData);
$query->bindValue(2,1);
$query->bindValue(3,$mid);
$query->bindValue(4,$startTime);
$query->bindValue(5,$endTime);
$status = $query->execute(); 
if($status){
//success
$data = array(
    "registerData" => $registerData,
    "mid" => $mid,
	"startTime" => $startTime,
	"endTime" => $endTime,	
);
$jsonOutput = json_encode(array('register'=>$data));
echo $jsonOutput;
} else {
//failure
echo "Error";
}

} else {
echo "Error no post variables";
}
