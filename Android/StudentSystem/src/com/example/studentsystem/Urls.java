package com.example.studentsystem;
/*
 * Author: Steven Hill
 * Date: 01/01/14
 * Version: 0.1
 * 
 */
public class Urls {
	// used to allow easy access to change the server for the program
	// allows this file to be updated only when needing server URLS.
	String ADDRESS_URL = "http://192.168.173.1/StudentSystem/";	
	// file system setup for php scripts
	String LOGIN_URL = "login/login.php";
	String LOGIN_IDLE_URL = "login/loginidle.php";
	String STUDENT_TIMETABLE_URL = "student/timetable.php";
	String TEACHER_REGISTER_URL = "lecturer/register.php";
	String TEACHER_REGISTER_NAMES_URL = "lecturer/registerlist.php";
	String TEACHER_REGISTER_UPDATE_URL = "lecturer/registerupdate.php";
	String TEACHER_REGISTER_LOGS_URL = "lecturer/registerlogs.php";
public Urls(){
	
}
/*
 * Returns Login URL
 */
public String getLoginURL(){
	return ADDRESS_URL+LOGIN_URL;
}
/*
 * Returns Login Idle Screen URL
 */
public String getLoginIdleURL(){
	return ADDRESS_URL+LOGIN_IDLE_URL;
}
/*
 *  Returns Timetable URL
 */
public String getTimetableURL(){
	return ADDRESS_URL+STUDENT_TIMETABLE_URL;
}
/*
 * Returns Register URL
 */
public String getRegisterURL(){
	return ADDRESS_URL+TEACHER_REGISTER_URL;
}
public String getRegisterNamesURL(){
	return ADDRESS_URL+TEACHER_REGISTER_NAMES_URL;
}
public String getRegisterUpdateURL(){	
	return ADDRESS_URL+TEACHER_REGISTER_UPDATE_URL;
}
public String getRegisterLogsURL(){
	return ADDRESS_URL+TEACHER_REGISTER_LOGS_URL;
}

}
