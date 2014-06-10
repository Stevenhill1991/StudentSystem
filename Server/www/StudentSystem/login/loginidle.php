<?php
/**
 * Created by
 * User: Steve
 * Date: 10/11/13
 * Time: 13:44
 * Version: 1.0
 * Grab top 3 upcomming lectures for a selected room based on the time
 */
//pdo connection file to mysql database
include("../includes/connect.php");
//if(!empty($_POST['roomname'])){
//get time of request 10:20:00 etc
$localtime = localtime();
$lta = localtime(time(), true);
//$time = $lta['tm_hour'].":".$lta['tm_min'].":".$lta['tm_sec'];
$time="08:59:00"; // TEST VARIABLE
//0 to 6- SUNDAY TO SATURDAY
//$day = $lta['tm_wday'];
$day="1"; // TEST VARIABLE
// get roomName
//$roomname = htmlspecialchars($_POST["roomname"]);
$roomname = "SLT";
//get next 3 module in current room;

$query = $pdo->prepare("SELECT
timetable.startTime,modules.moduleName
FROM `timetable` 
INNER JOIN `rooms`
ON timetable.rid=rooms.rid 
INNER JOIN `modules`
ON timetable.mid=modules.mid
WHERE day =? AND rooms.roomName =? AND  startTime > ?
LIMIT 3");
$query->bindValue(1,$day);
$query->bindValue(2,$roomname);
$query->bindValue(3,$time);
$query->execute();
// if no data = no more lectures
if ($query->rowCount() > 0){
//login success	
$data = $query->fetchAll(PDO::FETCH_ASSOC);
//$query->fetch(PDO::FETCH_ASSOC);
// returns array user, userName, userType
$jsonOutput = json_encode(array('lecture'=>$data));
//print_r($data);
echo($jsonOutput);
} else {
// no more lectures
echo "No more lectures";
}
/*
} else {
	echo "Post error";
}
*/