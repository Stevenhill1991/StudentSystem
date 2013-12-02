<?php
/**
 * Created by
 * User: Steve
 * Date: 10/11/13
 * Time: 13:44
 * Version: 1.0
 */
try {
    $pdo = new PDO('mysql:host=localhost;dbname=nfc', 'root', '');
} catch (PDOException $e) {
    exit('Database Error');
}
