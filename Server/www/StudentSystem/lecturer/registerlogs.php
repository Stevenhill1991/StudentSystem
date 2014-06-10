<?php
/**
 * Created by
 * User: Steve
 * Date: 10/11/13
 * Time: 13:44
 * Version: 1.0
 * Select all registers with att = 1 to show completed registers for logs section
 */
include("../includes/connect.php");
if(!empty($_POST['userName'])){
$userName = $_POST['userName'];
$att = 1;
$day = 1;
$query = $pdo->prepare("SELECT timetable.mid,modules.moduleName,timetable.startTime,timetable.endTime,timetable.registerData	
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
//$query->fetch(PDO::FETCH_ASSOC);
// returns array user, userName, userType
$jsonOutput = json_encode(array('lecture'=>$data));
//print_r($data);
echo($jsonOutput);
} else {
// no more lectures
echo "Error grabbing registers";
}
} 
else {
	echo "Error no post variables";
}
