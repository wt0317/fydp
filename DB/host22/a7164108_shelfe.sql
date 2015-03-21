-- phpMyAdmin SQL Dump
-- version 2.11.4
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Mar 20, 2015 at 08:32 PM
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
  `ItemId` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) COLLATE latin1_general_ci NOT NULL,
  `ShelfId` int(128) NOT NULL,
  `Name` varchar(45) COLLATE latin1_general_ci NOT NULL,
  `CategoryId` int(11) NOT NULL DEFAULT '0',
  `Price` decimal(10,2) NOT NULL DEFAULT '0.00',
  `InitialAmount` decimal(10,2) NOT NULL,
  `CurrentAmount` decimal(10,2) NOT NULL,
  `Barcode` int(15) NOT NULL DEFAULT '0',
  `ShelfRegion` int(11) NOT NULL,
  `DateAdded` int(12) NOT NULL DEFAULT '0',
  `ExpiryDate` int(12) NOT NULL DEFAULT '0',
  `Status` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`ItemId`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci AUTO_INCREMENT=694 ;

--
-- Dumping data for table `inventory`
--

INSERT INTO `inventory` VALUES(693, 'rahoque', 123, 'San_Benedetto_Natural_Watter', 5, 1.00, 1.00, 1.00, 2147483647, 1, 1426889648, 1489982400, 0);
INSERT INTO `inventory` VALUES(692, 'Wilson', 2015, 'apple', 1, 2.00, 0.37, 0.16, 1234567888, 36, 1426857034, 1553054400, 0);
INSERT INTO `inventory` VALUES(691, 'Wilson', 2015, 'apple', 1, 2.00, 0.37, 0.16, 1234567888, 35, 1426857034, 1553054400, 0);
INSERT INTO `inventory` VALUES(690, 'Wilson', 2015, 'apple', 1, 2.00, 0.37, 0.16, 1234567888, 31, 1426857034, 1553054400, 0);
INSERT INTO `inventory` VALUES(688, 'Wilson', 2015, 'chicken', 1, 1.00, 0.24, 0.20, 1234567888, 0, 1426857020, 1553054400, 0);
INSERT INTO `inventory` VALUES(689, 'Wilson', 2015, 'mushroom', 1, 1.00, 0.16, 0.20, 1234567888, 2, 1426856969, 1521518400, 0);

-- --------------------------------------------------------

--
-- Table structure for table `log`
--

CREATE TABLE `log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `message` varchar(45) COLLATE latin1_general_ci NOT NULL,
  `date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci AUTO_INCREMENT=244 ;

--
-- Dumping data for table `log`
--

INSERT INTO `log` VALUES(1, 'addItem', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(2, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(3, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(4, 'addItem', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(5, 'addItem', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(6, 'addItem', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(7, 'addItem', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(8, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(9, 'addItem', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(10, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(11, 'addItem', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(12, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(13, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(14, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(15, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(16, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(17, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(18, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(19, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(20, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(21, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(22, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(23, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(24, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(25, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(26, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(27, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(28, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(29, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(30, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(31, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(32, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(33, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(34, 'No items exist in provided regions.', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(35, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(36, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(37, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(38, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(39, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(40, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(41, 'addItem', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(42, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(43, 'No items exist in provided regions.', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(44, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(45, 'addItem', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(46, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(47, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(48, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(49, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(50, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(51, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(52, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(53, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(54, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(55, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(56, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(57, 'addItem', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(58, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(59, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(60, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(61, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(62, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(63, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(64, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(65, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(66, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(67, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(68, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(69, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(70, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(71, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(72, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(73, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(74, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(75, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(76, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(77, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(78, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(79, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(80, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(81, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(82, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(83, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(84, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(85, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(86, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(87, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(88, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(89, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(90, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(91, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(92, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(93, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(94, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(95, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(96, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(97, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(98, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(99, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(100, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(101, 'addItem', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(102, 'addItem', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(103, 'addItem', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(104, 'addItem', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(105, 'addItem', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(106, 'addItem', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(107, 'addItem', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(108, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(109, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(110, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(111, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(112, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(113, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(114, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(115, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(116, 'No items exist in provided regions.', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(117, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(118, 'No items exist in provided regions.', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(119, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(120, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(121, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(122, 'No items are currently checked out.', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(123, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(124, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(125, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(126, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(127, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(128, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(129, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(130, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(131, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(132, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(133, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(134, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(135, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(136, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(137, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(138, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(139, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(140, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(141, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(142, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(143, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(144, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(145, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(146, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(147, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(148, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(149, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(150, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(151, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(152, 'No items exist in provided regions.', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(153, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(154, 'No items are currently checked out.', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(155, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(156, 'No items exist in provided regions.', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(157, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(158, 'No items are currently checked out.', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(159, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(160, 'No items are currently checked out.', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(161, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(162, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(163, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(164, 'No items are currently checked out.', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(165, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(166, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(167, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(168, 'No items are currently checked out.', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(169, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(170, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(171, 'No items exist in provided regions.', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(172, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(173, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(174, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(175, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(176, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(177, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(178, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(179, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(180, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(181, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(182, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(183, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(184, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(185, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(186, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(187, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(188, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(189, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(190, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(191, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(192, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(193, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(194, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(195, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(196, 'addItem', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(197, 'addItem', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(198, 'addItem', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(199, 'addItem', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(200, 'addItem', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(201, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(202, 'addItem', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(203, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(204, 'addItem', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(205, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(206, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(207, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(208, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(209, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(210, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(211, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(212, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(213, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(214, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(215, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(216, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(217, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(218, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(219, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(220, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(221, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(222, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(223, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(224, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(225, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(226, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(227, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(228, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(229, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(230, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(231, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(232, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(233, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(234, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(235, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(236, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(237, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(238, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(239, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(240, 'itemOut', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(241, 'itemIn', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(242, 'addItem', '0000-00-00 00:00:00');
INSERT INTO `log` VALUES(243, 'itemIn', '0000-00-00 00:00:00');

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
  `Barcode` int(15) NOT NULL DEFAULT '0',
  `DateAdded` int(12) NOT NULL DEFAULT '0',
  `ExpiryDate` int(12) NOT NULL DEFAULT '0',
  `Status` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`pendingItemId`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci AUTO_INCREMENT=81 ;

--
-- Dumping data for table `pendingInventory`
--


-- --------------------------------------------------------

--
-- Table structure for table `shelf`
--

CREATE TABLE `shelf` (
  `idshelf` int(11) NOT NULL AUTO_INCREMENT,
  `ShelfId` int(128) NOT NULL,
  `password` varchar(45) COLLATE latin1_general_ci NOT NULL,
  PRIMARY KEY (`idshelf`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci AUTO_INCREMENT=4 ;

--
-- Dumping data for table `shelf`
--

INSERT INTO `shelf` VALUES(1, 123, 'thebank');
INSERT INTO `shelf` VALUES(2, 2015, 'ACY778zUguizA');
INSERT INTO `shelf` VALUES(3, 0, 'ACY778zUguizA');

-- --------------------------------------------------------

--
-- Table structure for table `shelfUserMapping`
--

CREATE TABLE `shelfUserMapping` (
  `idmapping` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) COLLATE latin1_general_ci NOT NULL,
  `ShelfId` int(128) NOT NULL,
  PRIMARY KEY (`idmapping`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci AUTO_INCREMENT=7 ;

--
-- Dumping data for table `shelfUserMapping`
--

INSERT INTO `shelfUserMapping` VALUES(1, 'rahoque', 123);
INSERT INTO `shelfUserMapping` VALUES(2, 'Wilson', 2015);
INSERT INTO `shelfUserMapping` VALUES(4, 'test', 2015);
INSERT INTO `shelfUserMapping` VALUES(5, 'test', 2015);
INSERT INTO `shelfUserMapping` VALUES(6, 'test', 2015);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `iduser` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) COLLATE latin1_general_ci NOT NULL,
  `password` varchar(45) COLLATE latin1_general_ci NOT NULL,
  `email` varchar(75) COLLATE latin1_general_ci NOT NULL,
  `status` varchar(10) COLLATE latin1_general_ci NOT NULL DEFAULT 'no',
  PRIMARY KEY (`iduser`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci AUTO_INCREMENT=6 ;

--
-- Dumping data for table `user`
--

INSERT INTO `user` VALUES(1, 'test', 'test', 'test@test.com', '1');
INSERT INTO `user` VALUES(2, 'r1', 'test', 'rakinhoque@gmail.com', '1');
INSERT INTO `user` VALUES(3, 'saroach', 'pass', 'email@email.com', 'jj6iAa');
INSERT INTO `user` VALUES(4, 'rahoque', 'thebank', 'sac.roach@gmail.com', '1');
INSERT INTO `user` VALUES(5, 'Wilson', 'test', 'wt.0317@gmail.com', '1');
