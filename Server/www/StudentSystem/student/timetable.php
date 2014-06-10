<?php
/**
 * Created by
 * User: Steve
 * Version: 1.0
 * grab day or week timetable based on input
 */
//pdo connection file to mysql database
include("../includes/connect.php");
if(!empty($_POST['userName']) && !empty($_POST['timetable'])){
$userName = $_POST['userName'];
$timetable = $_POST['timetable'];
// this day timetable
if($timetable == "1"){

$query = $pdo->prepare("SELECT timetable.day,timetable.weeks,modules.moduleName,timetable.startTime,timetable.endTime,
	timetable.group,rooms.roomName,teachers.firstName,teachers.lastName
	FROM `timetable`
INNER JOIN `rooms`
ON timetable.rid=rooms.rid 
INNER JOIN `modules`
ON timetable.mid=modules.mid
INNER JOIN `teachers`
ON timetable.lid=teachers.lid
INNER JOIN `courses`
ON modules.cid=courses.cid
INNER JOIN `students`
ON courses.courseName=students.courseName
WHERE students.userName = ?
AND timetable.day = ?");
$query->bindValue(1,$userName);
$query->bindValue(2,1);
$query->execute();
// if query has data
if ($query->rowCount() > 0){

$data = $query->fetchAll(PDO::FETCH_ASSOC);
//$query->fetch(PDO::FETCH_ASSOC);
// returns array user, userName, userType
$jsonOutput = json_encode(array('lecture'=>$data));
//print_r($data);
echo($jsonOutput);
} else {
// no more lectures
echo "Error grabbing timetable";
}
} else if ($timetable == "7"){	
$query = $pdo->prepare("SELECT timetable.day,modules.moduleName,timetable.startTime,timetable.endTime,
	timetable.group,rooms.roomName,teachers.firstName,teachers.lastName
	FROM `timetable`
INNER JOIN `rooms`
ON timetable.rid=rooms.rid 
INNER JOIN `modules`
ON timetable.mid=modules.mid
INNER JOIN `teachers`
ON timetable.lid=teachers.lid
INNER JOIN `courses`
ON modules.cid=courses.cid
INNER JOIN `students`
ON courses.courseName=students.courseName
WHERE students.userName = ?");
$query->bindValue(1,$userName);
$query->execute();
// if query has data
if ($query->rowCount() > 0){
$data = $query->fetchAll();
$jsonOutput = json_encode(array('lecture'=>$data));

echo($jsonOutput);
} else{
	echo "error";
}
} else {
	echo "Error no timetable selected";
}
} else {
	echo "Error no post variables";
}
