<?PHP
/**
 * Created by
 * User: Steve
 * Date: 11/11/13
 * Time: 17:44
 * Version: 1.0
 */
//pdo connection file to mysql database
include("../includes/connect.php");
//check if the uid is set = CARD LOGIN
if(!empty($_POST['uid'])){
// grab the uid value
$uid = htmlspecialchars($_POST["uid"]);
//prepare statement and execute
$query = $pdo->prepare("SELECT userName,userType FROM cards WHERE uid=?");
$query->bindValue(1,$uid);
$query->execute();
// if query has data
if($query->rowCount() > 0){
// fetch as an array [userName] => "Hill" etc
$data = $query->fetch(PDO::FETCH_ASSOC);
$jsonOutput = json_encode(array('user'=>array($data)));
echo $jsonOutput;
} else {
// user id is invalid therefore no data to display
echo "Error: username or password not set";
}

} elseif (!empty($_POST['username']) & !empty($_POST['password'])){ // check if the username is set = USERNAME LOGIN	
$userName =  $_POST['username'];
$passWord = sha1($_POST['password']);
// check login credentials 
$query = $pdo->prepare("SELECT userName,userType FROM cards WHERE userName=? AND passWord=?");
$query->bindValue(1,$userName);
$query->bindValue(2,$passWord);
$query->execute();
// if no data = wrong username or password
if ($query->rowCount() > 0){
//login success	
$data = $query->fetch(PDO::FETCH_ASSOC);
// returns array user, userName, userType
$jsonOutput = json_encode(array('user'=>array($data)));
echo $jsonOutput;
} else {
//username not set - login fail
echo "Error: username or password not set";
}
} else{
	// WHOLE DOESNT WORK
	echo "Error: username or password not set";

}


