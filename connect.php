<?php
/**
 * Created by
 * User: Steve
 * Date: 14/11/13
 * Time: 01:44
 * Version: 1.0
 */
try {
    $pdo = new PDO('mysql:host=localhost;dbname=nfc', 'root', '');
} catch (PDOException $e) {
    exit('Database Error');
}
