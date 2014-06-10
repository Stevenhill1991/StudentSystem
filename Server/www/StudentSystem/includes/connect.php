<?php
/**
 * Created by
 * User: Steve
 * Date: 10/11/13
 * Time: 13:44
 * Version: 1.0
 * Connection file, replace with your connection settings to database
 */
try {
	// host name, databasename, username , password 
   $pdo = new PDO('mysql:host=localhost;dbname=studentsystem', 'root', ''); 
} catch (PDOException $e) {
    exit('Database Error');
}
