<?PHP
// check if the uid is set
if(isset($_GET['uid'])){
// grab the uid value
$uid = htmlspecialchars($_GET["uid"]);
//pdo connection file to mysql database
include("connect.php");
//prepare statement and execute
$query = $pdo->prepare("SELECT * FROM cards WHERE uid = ?");
$query->bindValue(1,$uid);
$query->execute();
// if query has data
if($query->rowCount() > 0){
// fetch as an array [lastName] => "Hill" etc
$data = $query->fetch(PDO::FETCH_ASSOC);
$jsonOutput = json_encode(array('data'=>array($data)));
echo $jsonOutput;
} else {
// user id is invalid therefore no data to display
 echo "Page not found: Please notify an administrator if you followed a valid link";
}
} else {
// not allowed access to data no parameter found
echo 'Access Denied';
}
