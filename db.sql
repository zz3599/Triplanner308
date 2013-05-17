-- phpMyAdmin SQL Dump
-- version 3.4.10.1deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: May 17, 2013 at 11:38 AM
-- Server version: 5.5.29
-- PHP Version: 5.3.10-1ubuntu3.6

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `TriPlanner308`
--

-- --------------------------------------------------------

--
-- Table structure for table `events`
--

CREATE TABLE IF NOT EXISTS `events` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tripid` int(11) DEFAULT NULL,
  `tripdayid` int(11) NOT NULL,
  `starttime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `endtime` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  `startlocation` varchar(45) NOT NULL,
  `endlocation` varchar(100) DEFAULT NULL,
  `eventtype` int(11) NOT NULL,
  `comment` text,
  PRIMARY KEY (`id`),
  KEY `fk_events_1` (`tripdayid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=11 ;

--
-- Dumping data for table `events`
--

INSERT INTO `events` (`id`, `tripid`, `tripdayid`, `starttime`, `endtime`, `startlocation`, `endlocation`, `eventtype`, `comment`) VALUES
(1, 1, 3, '2013-05-24 18:38:00', '2013-05-24 19:38:00', 'Fremont, OH', '', 0, 'Chilling'),
(2, 1, 2, '2013-05-23 12:00:00', '2013-05-23 12:30:00', 'Salt Lake City, UT', '', 0, 'See the Mormons'),
(3, 3, 9, '2013-05-22 15:00:00', '2013-05-22 16:00:00', 'Sioux Falls, SD', '', 0, 'Burger King feast'),
(4, 3, 10, '2013-05-23 08:00:00', '2013-05-23 08:32:00', 'Salina, KS', '', 0, 'Tree sighting'),
(5, 4, 12, '2013-05-30 07:00:00', '2013-05-30 07:17:00', 'Jackson Heights, Queens, NY', '', 0, 'Kill'),
(6, 4, 12, '2013-05-30 14:00:00', '2013-05-30 14:11:00', 'Brooklyn, NY', '', 0, 'Buffet'),
(7, 6, 22, '2013-05-17 20:00:00', '2013-05-17 21:00:00', 'Fallingwater Road, Mill Run, PA', '', 0, 'something'),
(8, 6, 22, '2013-05-17 20:00:00', '2013-05-17 21:00:00', 'Fallingwater Road, Mill Run, PA', '', 0, 'Fallingwater'),
(9, 6, 22, '2013-05-17 20:00:00', '2013-05-17 21:00:00', 'Fallingwater Road, Mill Run, PA', '', 0, 'Fallingwater'),
(10, 6, 22, '2013-05-17 16:00:00', '2013-05-17 21:00:00', 'Williamsport, PA', 'Gettysburg, PA', 0, 'Gettysburgh');

-- --------------------------------------------------------

--
-- Table structure for table `friendrequests`
--

CREATE TABLE IF NOT EXISTS `friendrequests` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user1` int(11) NOT NULL,
  `user2` int(11) NOT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_friendrequests_1` (`user1`),
  KEY `fk_friendrequests_2` (`user2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `friends`
--

CREATE TABLE IF NOT EXISTS `friends` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `friend1` int(11) DEFAULT NULL,
  `friend2` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_friends_1` (`friend1`),
  KEY `fk_friends_2` (`friend2`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `friends`
--

INSERT INTO `friends` (`id`, `friend1`, `friend2`) VALUES
(1, 1, 2),
(2, 1, 2),
(3, 1, 2);

-- --------------------------------------------------------

--
-- Table structure for table `hotels`
--

CREATE TABLE IF NOT EXISTS `hotels` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tripid` int(11) NOT NULL,
  `tripdayid` int(11) NOT NULL,
  `location` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `tripid_idx` (`tripid`),
  KEY `fk_hotels_1` (`tripid`),
  KEY `fk_hotels_2` (`tripdayid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `hotels`
--

INSERT INTO `hotels` (`id`, `tripid`, `tripdayid`, `location`) VALUES
(1, 1, 2, 'The Hotel / Club Elevate, West 200 South, Salt Lake City, UT'),
(2, 4, 12, 'The ghetto');

-- --------------------------------------------------------

--
-- Table structure for table `photos`
--

CREATE TABLE IF NOT EXISTS `photos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(128) NOT NULL,
  `userid` int(11) NOT NULL,
  `tripid` int(11) NOT NULL,
  `tripdayid` int(11) DEFAULT NULL,
  `eventid` int(11) DEFAULT NULL,
  `originalname` varchar(100) DEFAULT NULL,
  `comment` text,
  `uploadtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_photos_1` (`userid`),
  KEY `fk_photos_2` (`tripid`),
  KEY `fk_photos_3` (`tripdayid`),
  KEY `fk_photos_4` (`eventid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=12 ;

--
-- Dumping data for table `photos`
--

INSERT INTO `photos` (`id`, `url`, `userid`, `tripid`, `tripdayid`, `eventid`, `originalname`, `comment`, `uploadtime`) VALUES
(1, '../uploads/2/244648735f7e46a8a73f4f7e39463e94.jpg', 2, 1, NULL, NULL, 'sf.jpg', 'Golden Gate', '2013-05-15 20:05:09'),
(2, '../uploads/2/43a6e3e0a16b4cc4bb2e59b0b135ecf1.jpg', 2, 1, 2, 2, 'mormon.jpg', 'Mormon', '2013-05-15 22:34:22'),
(3, '../uploads/2/3745f4be36a54e10869589da35121a4f.jpg', 2, 3, 9, 3, 'bk.jpg', 'BK', '2013-05-15 22:39:16'),
(4, '../uploads/2/e204b00618aa4bfbad1371604b791bcb.jpg', 2, 3, 10, 4, 'imgtree.jpg', 'Tree sighting', '2013-05-15 22:41:44'),
(5, '../uploads/1/2f8e4cecaf974c0988204b4183bb6394.jpg', 1, 4, NULL, NULL, 'mormon.jpg', 'Mormon photo', '2013-05-15 23:39:34'),
(6, '../uploads/1/ff2d1a25602446db9d7f00dad963f1bb.jpg', 1, 4, 12, 5, 'sf.jpg', '', '2013-05-15 23:42:12'),
(7, '../uploads/2/53dc1a18ab2c4625827e21d9f01fa33c.jpg', 2, 1, 1, NULL, 'sf.jpg', '', '2013-05-16 00:34:34'),
(8, '../uploads/3/73130561822144b38554856a742286d8.jpg', 3, 6, 25, NULL, 'CrawfordNebraska.jpg', 'Crawford', '2013-05-16 00:56:03'),
(9, '../uploads/3/3bb8890d27fd4d0bb48af70037ad6c4b.jpg', 3, 6, 25, NULL, 'EnteringCrawford.jpg', 'Entering Crawford', '2013-05-16 00:56:25'),
(10, '../uploads/3/e7b71cb70e7544e181df8c015e17a36c.jpg', 3, 6, 26, NULL, 'BuffaloBill.jpg', 'Buffalo Bill', '2013-05-16 01:00:32'),
(11, '../uploads/3/66a3fa8db74a4ccca682680a3e28abba.jpg', 3, 6, 26, NULL, 'ChimneyRock.jpg', 'Chimney Rock', '2013-05-16 01:00:45');

-- --------------------------------------------------------

--
-- Table structure for table `tripdays`
--

CREATE TABLE IF NOT EXISTS `tripdays` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tripid` int(11) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `startlocation` varchar(100) DEFAULT NULL,
  `endlocation` varchar(100) DEFAULT NULL,
  `comment` text,
  `daynum` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_tripdays_1` (`tripid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=30 ;

--
-- Dumping data for table `tripdays`
--

INSERT INTO `tripdays` (`id`, `tripid`, `date`, `startlocation`, `endlocation`, `comment`, `daynum`) VALUES
(1, 1, '2013-05-22 04:00:00', 'San Francisco, CA', 'Las Vegas, NV', 'Going to lose some weight', 0),
(2, 1, '2013-05-23 04:00:00', 'Las Vegas, NV', 'Columbus, OH', 'Cavs', 1),
(3, 1, '2013-05-24 04:00:00', 'Columbus, OH', 'Cleveland, OH', 'Lebron is coming back', 2),
(4, 2, '2013-05-22 04:00:00', 'Boston, MA', 'Chicago, IL', 'Sox game', 0),
(5, 2, '2013-05-23 04:00:00', 'Chicago, IL', NULL, NULL, 1),
(6, 2, '2013-05-24 04:00:00', 'Boston, MA', NULL, NULL, 2),
(7, 2, '2013-05-25 04:00:00', 'Boston, MA', NULL, NULL, 3),
(8, 2, '2013-05-26 04:00:00', 'Boston, MA', NULL, NULL, 4),
(9, 3, '2013-05-22 04:00:00', 'Minneapolis, MN', 'La Crosse, KS', 'Nothing', 0),
(10, 3, '2013-05-23 04:00:00', 'La Crosse, KS', 'St. Louis, MO', '', 1),
(11, 3, '2013-05-24 04:00:00', 'St. Louis, MO', NULL, NULL, 2),
(12, 4, '2013-05-30 04:00:00', 'New york, NY', 'Queens, NY', '', 0),
(13, 4, '2013-05-31 04:00:00', 'Queens, NY', 'Philadelphia, PA', '', 1),
(14, 5, '2013-05-17 12:00:00', 'Stony Brook, NY', 'Pittsburgh, PA', '', 0),
(15, 5, '2013-05-18 04:00:00', 'Pittsburgh, PA', NULL, NULL, 1),
(16, 5, '2013-05-19 04:00:00', 'Stony Brook, NY', NULL, NULL, 2),
(17, 5, '2013-05-20 04:00:00', 'Stony Brook, NY', NULL, NULL, 3),
(18, 5, '2013-05-21 04:00:00', 'Stony Brook, NY', NULL, NULL, 4),
(19, 5, '2013-05-22 04:00:00', 'Stony Brook, NY', NULL, NULL, 5),
(20, 5, '2013-05-23 04:00:00', 'Stony Brook, NY', NULL, NULL, 6),
(21, 5, '2013-05-24 04:00:00', 'Stony Brook, NY', NULL, NULL, 7),
(22, 6, '2013-05-17 04:00:00', 'Stony Brook, NY', 'Pittsburgh, PA', '', 0),
(23, 6, '2013-05-18 04:00:00', 'Pittsburgh, PA', 'Chicago, IL', '', 1),
(24, 6, '2013-05-19 04:00:00', 'Chicago, IL', 'Crawford, NE', '', 2),
(25, 6, '2013-05-20 04:00:00', 'Crawford, NE', 'Liberal, KS', '', 3),
(26, 6, '2013-05-21 04:00:00', 'Liberal, KS', 'Dallas, TX', '', 4),
(27, 6, '2013-05-22 04:00:00', 'Dallas, TX', NULL, NULL, 5),
(28, 6, '2013-05-23 04:00:00', 'Stony Brook, NY', NULL, NULL, 6),
(29, 6, '2013-05-24 04:00:00', 'Stony Brook, NY', NULL, NULL, 7);

-- --------------------------------------------------------

--
-- Table structure for table `trips`
--

CREATE TABLE IF NOT EXISTS `trips` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) DEFAULT NULL,
  `title` varchar(100) DEFAULT NULL,
  `description` text,
  `starttime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `endtime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `startlocation` varchar(100) NOT NULL,
  `endlocation` varchar(100) DEFAULT NULL,
  `shared` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_trips_1` (`userid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=7 ;

--
-- Dumping data for table `trips`
--

INSERT INTO `trips` (`id`, `userid`, `title`, `description`, `starttime`, `endtime`, `startlocation`, `endlocation`, `shared`) VALUES
(1, 2, 'Michael''s First Trip', 'Michael had fun', '2013-05-22 04:00:00', '2013-05-24 04:00:00', 'San Francisco, CA', 'Cleveland, OH', 2),
(2, 1, 'Brian''s First Trip', 'Brian had more fun', '2013-05-22 04:00:00', '2013-05-26 04:00:00', 'Boston, MA', 'Chicago, IL', 0),
(3, 2, 'Michael''s Second Trip', 'I got bored', '2013-05-22 04:00:00', '2013-05-24 04:00:00', 'Minneapolis, MN', 'Chicago, IL', 1),
(4, 1, 'Brian''s Second Trip', 'I suck', '2013-05-30 04:00:00', '2013-05-31 04:00:00', 'New york, NY', 'Philadelphia, PA', 0),
(5, 3, 'Tornado Alley', 'Blah', '2013-05-17 12:00:00', '2013-05-24 04:00:00', 'Stony Brook, NY', 'Dallas, TX', 0),
(6, 3, 'Tornado Alley 2', 'Tornado ', '2013-05-17 04:00:00', '2013-05-24 04:00:00', 'Stony Brook, NY', 'Dallas, TX', 0);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(45) NOT NULL,
  `firstname` varchar(45) NOT NULL,
  `lastname` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `authority` int(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `email`, `firstname`, `lastname`, `password`, `authority`) VALUES
(1, 'brian', 'Brian', 'Scala', 'brian', 1),
(2, 'michael', 'Michael', 'Johnson', 'michael', 1),
(3, 'joeschmo@gmail.com', 'Joe', 'Schmo', 'joe', 1);

-- --------------------------------------------------------

--
-- Table structure for table `waypoints`
--

CREATE TABLE IF NOT EXISTS `waypoints` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tripid` int(11) NOT NULL,
  `tripdayid` int(11) NOT NULL,
  `location` varchar(100) NOT NULL,
  `pointnum` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `dayid_idx` (`tripdayid`),
  KEY `tripid_idx` (`tripid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=9 ;

--
-- Dumping data for table `waypoints`
--

INSERT INTO `waypoints` (`id`, `tripid`, `tripdayid`, `location`, `pointnum`) VALUES
(1, 1, 1, 'Fresno, CA', 0),
(2, 1, 1, 'Los Angeles, CA', 1),
(3, 2, 4, 'Buffalo, NY', 0),
(4, 3, 9, 'Rochester, KS', 0),
(5, 1, 1, 'Salinas, CA', 2),
(7, 6, 25, 'Scotts Bluff, NE', 0),
(8, 6, 25, 'North Platte, NE', 1);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `events`
--
ALTER TABLE `events`
  ADD CONSTRAINT `fk_events_1` FOREIGN KEY (`tripdayid`) REFERENCES `tripdays` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `friendrequests`
--
ALTER TABLE `friendrequests`
  ADD CONSTRAINT `fk_friendrequests_1` FOREIGN KEY (`user1`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_friendrequests_2` FOREIGN KEY (`user2`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `friends`
--
ALTER TABLE `friends`
  ADD CONSTRAINT `fk_friends_1` FOREIGN KEY (`friend1`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_friends_2` FOREIGN KEY (`friend2`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `hotels`
--
ALTER TABLE `hotels`
  ADD CONSTRAINT `fk_hotels_1` FOREIGN KEY (`tripid`) REFERENCES `trips` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_hotels_2` FOREIGN KEY (`tripdayid`) REFERENCES `tripdays` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `photos`
--
ALTER TABLE `photos`
  ADD CONSTRAINT `fk_photos_1` FOREIGN KEY (`userid`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_photos_2` FOREIGN KEY (`tripid`) REFERENCES `trips` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_photos_3` FOREIGN KEY (`tripdayid`) REFERENCES `tripdays` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_photos_4` FOREIGN KEY (`eventid`) REFERENCES `events` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `tripdays`
--
ALTER TABLE `tripdays`
  ADD CONSTRAINT `fk_tripdays_1` FOREIGN KEY (`tripid`) REFERENCES `trips` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `trips`
--
ALTER TABLE `trips`
  ADD CONSTRAINT `fk_trips_1` FOREIGN KEY (`userid`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `waypoints`
--
ALTER TABLE `waypoints`
  ADD CONSTRAINT `fk_dayid` FOREIGN KEY (`tripdayid`) REFERENCES `tripdays` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_tripid` FOREIGN KEY (`tripid`) REFERENCES `trips` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
