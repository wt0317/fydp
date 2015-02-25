-- phpMyAdmin SQL Dump
-- version 2.11.4
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Feb 25, 2015 at 04:59 PM
-- Server version: 5.1.57
-- PHP Version: 5.2.17

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

--
-- Database: `a7164108_shelfe`
--

-- --------------------------------------------------------

--
-- Table structure for table `inventory`
--

CREATE TABLE `inventory` (
  `ItemId` int(11) NOT NULL,
  `username` varchar(45) COLLATE latin1_general_ci NOT NULL,
  `ShelfId` int(128) NOT NULL,
  `Name` varchar(45) COLLATE latin1_general_ci NOT NULL,
  `CategoryId` int(11) NOT NULL DEFAULT '0',
  `Price` decimal(10,2) NOT NULL DEFAULT '0.00',
  `InitialAmount` decimal(10,2) NOT NULL,
  `CurrentAmount` decimal(10,2) NOT NULL,
  `Barcode` int(10) NOT NULL DEFAULT '0',
  `ShelfRegion` int(11) NOT NULL,
  `DateAdded` int(12) NOT NULL DEFAULT '0',
  `ExpiryDate` int(12) NOT NULL DEFAULT '0',
  `Status` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`ItemId`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

--
-- Dumping data for table `inventory`
--


-- --------------------------------------------------------

--
-- Table structure for table `pendingInventory`
--

CREATE TABLE `pendingInventory` (
  `pendingItemId` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) COLLATE latin1_general_ci NOT NULL,
  `Name` varchar(45) COLLATE latin1_general_ci NOT NULL,
  `CategoryId` int(11) NOT NULL,
  `Price` decimal(10,2) NOT NULL DEFAULT '0.00',
  `Barcode` int(10) NOT NULL DEFAULT '0',
  `DateAdded` int(12) NOT NULL DEFAULT '0',
  `ExpiryDate` int(12) NOT NULL DEFAULT '0',
  `Status` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`pendingItemId`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci AUTO_INCREMENT=18 ;

--
-- Dumping data for table `pendingInventory`
--


-- --------------------------------------------------------

--
-- Table structure for table `shelf`
--

CREATE TABLE `shelf` (
  `idshelf` int(11) NOT NULL,
  `ShelfId` int(128) NOT NULL,
  `password` varchar(45) COLLATE latin1_general_ci NOT NULL,
  PRIMARY KEY (`idshelf`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

--
-- Dumping data for table `shelf`
--


-- --------------------------------------------------------

--
-- Table structure for table `shelfUserMapping`
--

CREATE TABLE `shelfUserMapping` (
  `idmapping` int(11) NOT NULL,
  `username` varchar(45) COLLATE latin1_general_ci NOT NULL,
  `ShelfId` int(128) NOT NULL,
  PRIMARY KEY (`idmapping`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

--
-- Dumping data for table `shelfUserMapping`
--


-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `iduser` int(11) NOT NULL,
  `username` varchar(45) COLLATE latin1_general_ci NOT NULL,
  `password` varchar(45) COLLATE latin1_general_ci NOT NULL,
  `email` varchar(75) COLLATE latin1_general_ci NOT NULL,
  `status` varchar(10) COLLATE latin1_general_ci NOT NULL DEFAULT 'no',
  PRIMARY KEY (`iduser`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` VALUES(0, 'test', 'test', 'test@test.com', '1');
