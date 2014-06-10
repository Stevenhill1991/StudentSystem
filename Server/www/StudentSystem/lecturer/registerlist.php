<?php
/**
 * Created by
 * User: Steve
 * Version: 1.0
 * Select users for a certain unmarked register
 */
include("../includes/connect.php");
$moduleName="ICP-3011-0";
$startTime="09:00:00";
$endTime="10:00:00";
$query = $pdo->prepare("SELECT users.uid,students.firstName,students.lastName	
FROM `timetable`
INNER JOIN `modules`
ON modules.mid=timetable.mid
INNER JOIN `courses`
ON modules.cid=courses.cid
INNER JOIN `students`
ON courses.courseName=students.courseName
INNER JOIN `users`
ON students.userName=users.userName
WHERE modules.mid =? AND startTime=? AND endTime=?");
$query->bindValue(1,$moduleName);
$query->bindValue(2,$startTime);
$query->bindValue(3,$endTime);
$query->execute();
// if query has data
if ($query->rowCount() > 0){
$data = $query->fetchAll(PDO::FETCH_ASSOC);

$jsonOutput = json_encode(array('register'=>$data));

echo($jsonOutput);
} else {
// no more lectures
echo "Error grabbing registers";
}
