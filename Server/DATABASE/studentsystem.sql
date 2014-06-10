-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Apr 30, 2014 at 01:19 PM
-- Server version: 5.5.24-log
-- PHP Version: 5.4.3

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `studentsystem`
--

-- --------------------------------------------------------

--
-- Table structure for table `buildings`
--

CREATE TABLE IF NOT EXISTS `buildings` (
  `bid` int(6) NOT NULL AUTO_INCREMENT COMMENT 'building id',
  `buildingName` varchar(32) NOT NULL,
  PRIMARY KEY (`bid`),
  UNIQUE KEY `bid` (`bid`),
  UNIQUE KEY `buildingName` (`buildingName`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `buildings`
--

INSERT INTO `buildings` (`bid`, `buildingName`) VALUES
(1, 'Dean Street'),
(2, 'Main Arts');

-- --------------------------------------------------------

--
-- Table structure for table `courses`
--

CREATE TABLE IF NOT EXISTS `courses` (
  `cid` varchar(6) NOT NULL COMMENT 'courseid',
  `courseName` varchar(32) NOT NULL,
  `courseYear` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `courses`
--

INSERT INTO `courses` (`cid`, `courseName`, `courseYear`) VALUES
('1', 'Computer Science BSc', 3);

-- --------------------------------------------------------

--
-- Table structure for table `modules`
--

CREATE TABLE IF NOT EXISTS `modules` (
  `mid` varchar(10) NOT NULL COMMENT 'module id number',
  `cid` varchar(6) NOT NULL COMMENT 'course id number',
  `moduleName` varchar(100) NOT NULL,
  `lid` varchar(6) NOT NULL COMMENT 'lecturer id',
  UNIQUE KEY `mid` (`mid`),
  UNIQUE KEY `moduleName` (`moduleName`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `modules`
--

INSERT INTO `modules` (`mid`, `cid`, `moduleName`, `lid`) VALUES
('ICP-3011-0', '1', 'Computer & Network Security 201314', '1'),
('ICP-3036-0', '1', 'Computer Graphics 3 Rendering 201314', '1'),
('ICP-3038-0', '1', 'Computer Vision 201314', '1'),
('ICP-3083-0', '1', 'Pattn Recogn & Neural Networks 201314', '1'),
('ICP-3099-0', '1', 'Individual Project-Computing 201314', '1');

-- --------------------------------------------------------

--
-- Table structure for table `rooms`
--

CREATE TABLE IF NOT EXISTS `rooms` (
  `rid` int(6) NOT NULL AUTO_INCREMENT COMMENT 'roomid',
  `bid` varchar(6) NOT NULL COMMENT 'building id',
  `roomName` varchar(32) NOT NULL,
  PRIMARY KEY (`rid`),
  UNIQUE KEY `roomName` (`roomName`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6 ;

--
-- Dumping data for table `rooms`
--

INSERT INTO `rooms` (`rid`, `bid`, `roomName`) VALUES
(1, '1', '319'),
(2, '1', 'MLT'),
(3, '1', 'SLT'),
(4, '1', '207'),
(5, '1', 'S-6');

-- --------------------------------------------------------

--
-- Table structure for table `students`
--

CREATE TABLE IF NOT EXISTS `students` (
  `userName` varchar(6) NOT NULL,
  `firstName` varchar(32) NOT NULL,
  `lastName` varchar(32) NOT NULL,
  `courseName` varchar(32) NOT NULL,
  `year` int(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `students`
--

INSERT INTO `students` (`userName`, `firstName`, `lastName`, `courseName`, `year`) VALUES
('eeue1f', 'Steven', 'Hill', 'Computer Science BSc', 3),
('eeue2f', 'Andrew', 'Boardman', 'Computer Science BSc', 3),
('eeue3f', 'Wayne', 'Ferrin', 'Computer Science BSc', 3);

-- --------------------------------------------------------

--
-- Table structure for table `teachers`
--

CREATE TABLE IF NOT EXISTS `teachers` (
  `lid` int(6) NOT NULL,
  `userName` varchar(6) NOT NULL,
  `firstName` varchar(32) NOT NULL,
  `lastName` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `teachers`
--

INSERT INTO `teachers` (`lid`, `userName`, `firstName`, `lastName`) VALUES
(1, 'eeuesa', 'david', 'jones');

-- --------------------------------------------------------

--
-- Table structure for table `timetable`
--

CREATE TABLE IF NOT EXISTS `timetable` (
  `tid` int(6) NOT NULL COMMENT 'timetable id',
  `startTime` time NOT NULL,
  `endTime` time NOT NULL,
  `weeks` varchar(32) NOT NULL COMMENT 'room id',
  `day` int(1) NOT NULL,
  `mid` varchar(10) NOT NULL COMMENT 'module id',
  `group` varchar(5) NOT NULL DEFAULT '<all>',
  `type` varchar(10) NOT NULL DEFAULT 'Lect',
  `rid` int(6) NOT NULL,
  `lid` int(6) NOT NULL,
  `att` int(1) NOT NULL DEFAULT '0' COMMENT 'attendance taken or not',
  `registerData` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `timetable`
--

INSERT INTO `timetable` (`tid`, `startTime`, `endTime`, `weeks`, `day`, `mid`, `group`, `type`, `rid`, `lid`, `att`, `registerData`) VALUES
(1, '09:00:00', '10:00:00', '1', 1, 'ICP-3011-0', '<all>', 'Lect', 3, 1, 1, 'a100'),
(2, '10:00:00', '11:00:00', '1', 1, 'ICP-3038-0', '<all>', 'Lect', 3, 1, 1, 'a100'),
(3, '11:00:00', '12:00:00', '1', 1, 'ICP-3036-0', '<all>', 'Lect', 3, 1, 1, 'a001'),
(4, '13:00:00', '14:00:00', '1', 1, 'ICP-3099-0', '<all>', 'Lect', 3, 1, 1, 'a100'),
(5, '14:00:00', '16:00:00', '1', 2, 'ICP-3083-0', '<all>', 'Lect', 3, 1, 0, ''),
(6, '15:00:00', '17:00:00', '1', 7, 'ICP-3011-0', '<all>', 'Lect', 3, 1, 0, ''),
(7, '09:00:00', '11:00:00', '1', 5, 'ICP-3099-0', '<all>', 'Lect', 3, 1, 0, '');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `uid` varchar(8) NOT NULL COMMENT 'user id of NFC card',
  `userName` varchar(6) NOT NULL COMMENT 'username of user',
  `passWord` varchar(40) NOT NULL COMMENT 'password of user',
  `userType` int(1) NOT NULL DEFAULT '2' COMMENT 'usertype of user, 0 = admin, 1 = teacher, 2 = student',
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uid` (`uid`),
  UNIQUE KEY `username` (`userName`),
  UNIQUE KEY `username_2` (`userName`),
  UNIQUE KEY `username_3` (`userName`),
  UNIQUE KEY `username_4` (`userName`),
  UNIQUE KEY `username_5` (`userName`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Stores the NFC card id, username, password and usertype of a user';

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`uid`, `userName`, `passWord`, `userType`) VALUES
('03BA544F', 'admin', '5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8', 0),
('431B574F', 'eeuesa', '5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8', 1),
('E320574F', 'eeue1f', '5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8', 2),
('E330574F', 'eeue2f', '5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8', 2),
('E340574F', 'eeue3f', '5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8', 2);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
