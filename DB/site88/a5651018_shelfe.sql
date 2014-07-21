-- phpMyAdmin SQL Dump
-- version 2.11.4
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jul 21, 2014 at 02:13 PM
-- Server version: 5.1.57
-- PHP Version: 5.2.17

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

--
-- Database: `a5651018_shelfe`
--

-- --------------------------------------------------------

--
-- Table structure for table `inventory`
--

CREATE TABLE `inventory` (
  `ItemId` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) COLLATE latin1_general_ci NOT NULL,
  `CategoryId` int(11) NOT NULL,
  `Price` decimal(10,2) NOT NULL,
  `InitialAmount` decimal(10,2) DEFAULT '-1.00',
  `CurrentAmount` decimal(10,2) DEFAULT '-1.00',
  `Barcode` int(10) unsigned zerofill DEFAULT NULL,
  `ShelfId` int(11) NOT NULL DEFAULT '-1',
  `ShelfRegion` int(11) NOT NULL DEFAULT '-1',
  `DateAdded` int(12) NOT NULL DEFAULT '0',
  `ExpiryDate` int(12) NOT NULL DEFAULT '0',
  `username` varchar(45) COLLATE latin1_general_ci NOT NULL,
  `Status` int(3) NOT NULL DEFAULT '1',
  PRIMARY KEY (`ItemId`),
  KEY `username_idx` (`username`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci AUTO_INCREMENT=63 ;

--
-- Dumping data for table `inventory`
--

INSERT INTO `inventory` VALUES(60, 'New', 5, 7.79, 100.00, 100.00, 0000465468, -1, -1, 1405964612, 1418342400, 'test', 1);
INSERT INTO `inventory` VALUES(62, 'New3', 1, 3.50, 150.00, 150.00, 0000000026, 1, 4, 1405964617, 1418342400, 'test', 0);
INSERT INTO `inventory` VALUES(61, 'New', 5, 7.79, 100.00, 100.00, 0000465468, -1, -1, 1405964612, 1418342400, 'test', 1);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `iduser` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) COLLATE latin1_general_ci NOT NULL,
  `password` varchar(45) COLLATE latin1_general_ci NOT NULL,
  PRIMARY KEY (`iduser`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci AUTO_INCREMENT=3 ;

--
-- Dumping data for table `user`
--

INSERT INTO `user` VALUES(1, 'test', 'test');
INSERT INTO `user` VALUES(2, 'test2', 'test2');
