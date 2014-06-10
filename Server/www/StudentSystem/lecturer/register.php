<?php
/**
 * Created by
 * User: Steve
 * Version: 1.0
 * used to grab all unfilled registers on a certain day
 */
include("../includes/connect.php");
if(!empty($_POST['userName'])){
$userName = $_POST['userName'];
$att = 0; // get all registers with value 0.
$day = 1; // preset variable to make sure information is avaliable for poster EXPO demo.
$query = $pdo->prepare("SELECT timetable.mid,modules.moduleName,timetable.startTime,timetable.endTime	
FROM `timetable`
INNER JOIN `modules`
ON timetable.mid=modules.mid
INNER JOIN `teachers`
ON timetable.lid=teachers.lid
WHERE teachers.userName =?
AND timetable.att =?
AND timetable.day =?");
$query->bindValue(1,$userName);
$query->bindValue(2,$att);
$query->bindValue(3,$day);
$query->execute();
// if query has data
if ($query->rowCount() > 0){
$data = $query->fetchAll(PDO::FETCH_ASSOC);
$jsonOutput = json_encode(array('lecture'=>$data));
echo($jsonOutput);
} else {
// no more lectures
echo "Error grabbing registers";
}
} 
// no post variables
else {
	echo "Error no post variables";
}
