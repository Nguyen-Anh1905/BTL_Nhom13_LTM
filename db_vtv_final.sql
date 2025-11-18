CREATE DATABASE  IF NOT EXISTS `gamevtv` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `gamevtv`;
-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: localhost    Database: gamevtv
-- ------------------------------------------------------
-- Server version	8.0.39

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `dictionary`
--

DROP TABLE IF EXISTS `dictionary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dictionary` (
  `word_id` int NOT NULL AUTO_INCREMENT,
  `word` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Từ tiếng Việt có dấu, viết HOA',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `letter_id` int DEFAULT NULL,
  `meaning` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`word_id`),
  UNIQUE KEY `unique_word` (`word`),
  KEY `fk_diction_letter` (`letter_id`),
  CONSTRAINT `fk_diction_letter` FOREIGN KEY (`letter_id`) REFERENCES `letters` (`letter_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Từ điển tiếng Việt để kiểm tra từ hợp lệ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dictionary`
--

LOCK TABLES `dictionary` WRITE;
/*!40000 ALTER TABLE `dictionary` DISABLE KEYS */;
INSERT INTO `dictionary` VALUES (1,'bóngđá','2025-11-11 18:42:22',1,'bóng đá'),(2,'bồhóng','2025-11-11 18:42:22',1,'bồ hóng'),(3,'đồnghồ','2025-11-11 18:42:22',1,'đồng hồ'),(4,'đábóng','2025-11-11 18:42:22',1,'đá bóng'),(5,'đồhồng','2025-11-11 19:02:44',1,'đồ hồng'),(6,'gánhđồ','2025-11-11 19:02:44',1,'gánh đồ'),(7,'lươngthực','2025-11-16 18:49:50',2,'lương thực'),(8,'lươngthảo','2025-11-16 18:49:50',2,'lương thảo'),(9,'thươngcảm','2025-11-16 18:49:50',2,'thương cảm'),(10,'cảmthương','2025-11-16 18:49:50',2,'cảm thương'),(11,'thựơnglưu','2025-11-16 18:49:50',2,'thượng lưu'),(12,'cươngtrực','2025-11-16 18:49:50',2,'cương trực'),(13,'bóngbay','2025-11-16 18:49:50',3,'bóng bay'),(14,'bônghoa','2025-11-16 18:49:50',3,'bông hoa'),(15,'hoagiấy','2025-11-16 18:49:50',3,'hoa giấy'),(16,'giấynhớ','2025-11-16 18:49:50',3,'giấy nhớ'),(17,'giấybaó','2025-11-16 18:49:50',3,'giấy báo');
/*!40000 ALTER TABLE `dictionary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `leaderboard_by_points`
--

DROP TABLE IF EXISTS `leaderboard_by_points`;
/*!50001 DROP VIEW IF EXISTS `leaderboard_by_points`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `leaderboard_by_points` AS SELECT 
 1 AS `user_id`,
 1 AS `username`,
 1 AS `full_name`,
 1 AS `total_points`,
 1 AS `total_wins`,
 1 AS `total_draws`,
 1 AS `total_losses`,
 1 AS `total_matches`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `leaderboard_by_wins`
--

DROP TABLE IF EXISTS `leaderboard_by_wins`;
/*!50001 DROP VIEW IF EXISTS `leaderboard_by_wins`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `leaderboard_by_wins` AS SELECT 
 1 AS `user_id`,
 1 AS `username`,
 1 AS `full_name`,
 1 AS `total_wins`,
 1 AS `total_points`,
 1 AS `total_draws`,
 1 AS `total_losses`,
 1 AS `total_matches`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `letters`
--

DROP TABLE IF EXISTS `letters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `letters` (
  `letter_id` int NOT NULL AUTO_INCREMENT,
  `letter_detail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `length_word` int DEFAULT NULL,
  `time_round` int DEFAULT NULL,
  PRIMARY KEY (`letter_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `letters`
--

LOCK TABLES `letters` WRITE;
/*!40000 ALTER TABLE `letters` DISABLE KEYS */;
INSERT INTO `letters` VALUES (1,'b,á,đ,ó,g,n,h,ồ,ồ',6,60),(2,'l,c,ư,ả,ơ,o,n,m,g,ự,t,c,h,u,r',9,90),(3,'ớ, ấ,b,i,ó,o,n,h,g,a,y,b',7,60);
/*!40000 ALTER TABLE `letters` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `match_details`
--

DROP TABLE IF EXISTS `match_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `match_details` (
  `detail_id` int NOT NULL AUTO_INCREMENT,
  `match_id` int NOT NULL,
  `round_number` int NOT NULL COMMENT 'Vòng 1, 2, hoặc 3',
  `letter_id` int DEFAULT NULL,
  `player1_words_count` int DEFAULT '0' COMMENT 'Số từ đúng của player 1',
  `player2_words_count` int DEFAULT '0' COMMENT 'Số từ đúng của player 2',
  `round_status` enum('waiting','playing','completed') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'waiting',
  `started_at` datetime DEFAULT NULL,
  `ended_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `player1_dic` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `player2_dic` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`detail_id`),
  UNIQUE KEY `unique_match_round` (`match_id`,`round_number`),
  KEY `idx_match_id` (`match_id`),
  KEY `idx_round_number` (`round_number`),
  KEY `fk_match_letter` (`letter_id`),
  CONSTRAINT `fk_match_letter` FOREIGN KEY (`letter_id`) REFERENCES `letters` (`letter_id`),
  CONSTRAINT `match_details_ibfk_1` FOREIGN KEY (`match_id`) REFERENCES `matches` (`match_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=219 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Chi tiết từng vòng đấu trong trận';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `match_details`
--

LOCK TABLES `match_details` WRITE;
/*!40000 ALTER TABLE `match_details` DISABLE KEYS */;
INSERT INTO `match_details` VALUES (1,1,1,1,1,2,'completed',NULL,NULL,'2025-11-08 16:58:30','1','1,2'),(2,1,2,2,1,2,'completed',NULL,NULL,'2025-11-08 16:58:30','3,4','4'),(3,1,3,3,2,1,'completed',NULL,NULL,'2025-11-08 16:58:30','5,6','5'),(4,2,1,1,2,1,'completed',NULL,NULL,'2025-11-08 16:58:30','1,2','1'),(5,2,2,2,2,1,'completed',NULL,NULL,'2025-11-08 16:58:30','3,4','4'),(6,2,3,3,2,1,'completed',NULL,NULL,'2025-11-08 16:58:30','5,6','5'),(7,3,1,1,2,1,'completed',NULL,NULL,'2025-11-11 18:59:31','1,2','1'),(8,3,2,2,2,1,'completed',NULL,NULL,'2025-11-11 18:59:31','3,4','4'),(9,3,3,3,2,1,'completed',NULL,NULL,'2025-11-11 18:59:31','5,6','5'),(13,5,1,2,1,0,'completed','2025-11-16 01:26:27',NULL,'2025-11-16 01:26:27','3',''),(15,5,3,1,2,1,'completed','2025-11-16 01:28:17',NULL,'2025-11-16 01:28:17','1,2','1'),(28,10,1,2,0,0,'completed','2025-11-16 02:28:30',NULL,'2025-11-16 02:28:30','',''),(29,10,2,3,0,0,'completed','2025-11-16 02:29:20',NULL,'2025-11-16 02:29:20','',''),(30,10,3,1,0,0,'completed','2025-11-16 02:30:30',NULL,'2025-11-16 02:30:30','',''),(31,11,1,1,1,2,'completed','2025-11-16 02:36:40',NULL,'2025-11-16 02:36:40','1','1,2'),(32,11,2,2,1,1,'completed','2025-11-16 02:37:39',NULL,'2025-11-16 02:37:39','3','3'),(33,11,3,3,1,1,'completed','2025-11-16 02:38:26',NULL,'2025-11-16 02:38:26','6','5'),(34,12,1,2,1,0,'completed','2025-11-16 02:45:45',NULL,'2025-11-16 02:45:45','3',''),(35,12,2,1,2,0,'completed','2025-11-16 02:46:48',NULL,'2025-11-16 02:46:48','1,2',''),(36,13,1,1,0,2,'completed','2025-11-16 18:51:25',NULL,'2025-11-16 18:51:25','','1,2'),(37,13,2,2,1,1,'completed','2025-11-16 18:52:25',NULL,'2025-11-16 18:52:25','8','7'),(38,13,3,3,0,1,'completed','2025-11-16 18:53:07',NULL,'2025-11-16 18:53:07','','16'),(39,14,1,1,0,4,'completed','2025-11-16 18:56:37',NULL,'2025-11-16 18:56:37','','1,2,3,4'),(40,14,2,2,0,1,'completed','2025-11-16 18:57:47',NULL,'2025-11-16 18:57:47','','7'),(41,15,1,3,2,0,'completed','2025-11-16 19:07:27',NULL,'2025-11-16 19:07:27','16,15',''),(42,15,2,2,2,0,'completed','2025-11-16 19:08:37',NULL,'2025-11-16 19:08:37','7,8',''),(43,16,1,1,0,0,'completed','2025-11-16 19:37:50',NULL,'2025-11-16 19:37:50','',''),(44,16,2,2,0,0,'completed','2025-11-16 19:39:30',NULL,'2025-11-16 19:39:30','',''),(45,16,3,3,0,0,'completed','2025-11-16 19:40:40',NULL,'2025-11-16 19:40:40','',''),(46,17,1,1,0,0,'completed','2025-11-16 19:49:36',NULL,'2025-11-16 19:49:36','',''),(47,17,2,2,0,0,'completed','2025-11-16 19:51:17',NULL,'2025-11-16 19:51:17','',''),(48,17,3,3,0,0,'completed','2025-11-16 19:52:27',NULL,'2025-11-16 19:52:27','',''),(49,18,1,1,0,0,'completed','2025-11-16 20:06:51',NULL,'2025-11-16 20:06:51','',''),(50,18,2,2,0,0,'completed','2025-11-16 20:08:31',NULL,'2025-11-16 20:08:31','',''),(51,18,3,3,0,0,'completed','2025-11-16 20:09:42',NULL,'2025-11-16 20:09:42','',''),(52,20,1,3,1,0,'completed','2025-11-16 20:14:04',NULL,'2025-11-16 20:14:04','16',''),(53,21,1,1,3,0,'completed','2025-11-17 14:37:37',NULL,'2025-11-17 14:37:37','1,2,4',''),(54,21,2,2,0,0,'completed','2025-11-17 14:39:17',NULL,'2025-11-17 14:39:17','',''),(55,21,3,3,1,0,'completed','2025-11-17 14:40:21',NULL,'2025-11-17 14:40:21','15',''),(56,22,1,1,1,3,'completed','2025-11-17 14:42:28',NULL,'2025-11-17 14:42:28','3','1,3,4'),(57,22,2,2,0,4,'completed','2025-11-17 14:44:04',NULL,'2025-11-17 14:44:04','','7,8,11,12'),(58,24,1,2,0,0,'completed','2025-11-17 14:52:05',NULL,'2025-11-17 14:52:05','',''),(59,24,2,3,0,0,'completed','2025-11-17 14:53:15',NULL,'2025-11-17 14:53:15','',''),(60,24,3,1,0,0,'completed','2025-11-17 14:54:19',NULL,'2025-11-17 14:54:19','',''),(61,25,1,3,0,0,'completed','2025-11-17 14:57:24',NULL,'2025-11-17 14:57:24','',''),(62,25,2,2,0,0,'completed','2025-11-17 14:59:04',NULL,'2025-11-17 14:59:04','',''),(63,25,3,1,0,0,'completed','2025-11-17 15:00:15',NULL,'2025-11-17 15:00:15','',''),(64,26,1,2,0,0,'completed','2025-11-17 15:03:57',NULL,'2025-11-17 15:03:57','',''),(65,26,2,1,0,0,'completed','2025-11-17 15:05:07',NULL,'2025-11-17 15:05:07','',''),(66,26,3,3,0,0,'completed','2025-11-17 15:06:17',NULL,'2025-11-17 15:06:17','',''),(67,29,1,1,0,0,'completed','2025-11-17 15:12:45',NULL,'2025-11-17 15:12:45','',''),(68,29,2,3,0,0,'completed','2025-11-17 15:13:55',NULL,'2025-11-17 15:13:55','',''),(69,29,3,2,0,0,'completed','2025-11-17 15:15:35',NULL,'2025-11-17 15:15:35','',''),(70,30,1,3,0,0,'completed','2025-11-17 15:24:44',NULL,'2025-11-17 15:24:44','',''),(71,30,2,2,0,0,'completed','2025-11-17 15:26:24',NULL,'2025-11-17 15:26:24','',''),(72,30,3,1,0,0,'completed','2025-11-17 15:27:34',NULL,'2025-11-17 15:27:34','',''),(73,31,1,3,0,0,'completed','2025-11-17 15:31:53',NULL,'2025-11-17 15:31:53','',''),(74,32,1,3,0,0,'completed','2025-11-17 15:34:00',NULL,'2025-11-17 15:34:00','',''),(75,32,2,2,0,1,'completed','2025-11-17 15:35:36',NULL,'2025-11-17 15:35:36','','7'),(76,32,3,1,1,1,'completed','2025-11-17 15:36:46',NULL,'2025-11-17 15:36:46','4','1'),(77,33,1,1,0,1,'completed','2025-11-17 15:41:18',NULL,'2025-11-17 15:41:18','','1'),(78,33,2,3,0,0,'completed','2025-11-17 15:42:28',NULL,'2025-11-17 15:42:28','',''),(79,33,3,2,0,0,'completed','2025-11-17 15:44:08',NULL,'2025-11-17 15:44:08','',''),(80,35,1,2,0,0,'completed','2025-11-17 15:49:35',NULL,'2025-11-17 15:49:35','',''),(81,35,2,3,0,0,'completed','2025-11-17 15:50:45',NULL,'2025-11-17 15:50:45','',''),(82,35,3,1,0,0,'completed','2025-11-17 15:51:55',NULL,'2025-11-17 15:51:55','',''),(83,36,1,2,0,0,'completed','2025-11-17 15:55:21',NULL,'2025-11-17 15:55:21','',''),(84,36,2,1,0,0,'completed','2025-11-17 15:55:41',NULL,'2025-11-17 15:55:41','',''),(85,36,3,3,0,0,'completed','2025-11-17 15:56:01',NULL,'2025-11-17 15:56:01','',''),(86,37,1,2,0,0,'completed','2025-11-17 16:11:40',NULL,'2025-11-17 16:11:40','',''),(87,37,2,3,0,0,'completed','2025-11-17 16:12:00',NULL,'2025-11-17 16:12:00','',''),(88,37,3,1,0,0,'completed','2025-11-17 16:12:20',NULL,'2025-11-17 16:12:20','',''),(89,38,1,3,0,0,'completed','2025-11-17 16:12:58',NULL,'2025-11-17 16:12:58','',''),(90,38,2,2,0,0,'completed','2025-11-17 16:13:19',NULL,'2025-11-17 16:13:19','',''),(91,38,3,1,0,0,'completed','2025-11-17 16:13:39',NULL,'2025-11-17 16:13:39','',''),(92,39,1,3,0,0,'completed','2025-11-17 16:16:59',NULL,'2025-11-17 16:16:59','',''),(93,39,2,2,0,0,'completed','2025-11-17 16:17:19',NULL,'2025-11-17 16:17:19','',''),(94,39,3,1,0,0,'completed','2025-11-17 16:17:39',NULL,'2025-11-17 16:17:39','',''),(95,40,1,1,0,1,'completed','2025-11-17 16:18:18',NULL,'2025-11-17 16:18:18','','1'),(96,40,2,2,0,0,'completed','2025-11-17 16:18:38',NULL,'2025-11-17 16:18:38','',''),(97,40,3,3,0,0,'completed','2025-11-17 16:18:58',NULL,'2025-11-17 16:18:58','',''),(98,41,1,3,0,0,'completed','2025-11-17 16:28:20',NULL,'2025-11-17 16:28:20','',''),(99,41,2,1,0,1,'completed','2025-11-17 16:28:35',NULL,'2025-11-17 16:28:35','','1'),(100,41,3,2,0,0,'completed','2025-11-17 16:28:55',NULL,'2025-11-17 16:28:55','',''),(101,42,1,1,0,1,'completed','2025-11-17 16:30:47',NULL,'2025-11-17 16:30:47','','1'),(102,42,2,2,0,0,'completed','2025-11-17 16:31:07',NULL,'2025-11-17 16:31:07','',''),(103,42,3,3,0,0,'completed','2025-11-17 16:31:27',NULL,'2025-11-17 16:31:27','',''),(104,43,1,1,0,2,'completed','2025-11-17 16:36:41',NULL,'2025-11-17 16:36:41','','1,4'),(105,43,2,2,0,0,'completed','2025-11-17 16:37:02',NULL,'2025-11-17 16:37:02','',''),(106,43,3,3,0,0,'completed','2025-11-17 16:37:22',NULL,'2025-11-17 16:37:22','',''),(107,44,1,3,0,1,'completed','2025-11-17 16:41:33',NULL,'2025-11-17 16:41:33','','15'),(108,44,2,2,0,0,'completed','2025-11-17 16:41:53',NULL,'2025-11-17 16:41:53','',''),(109,44,3,1,0,0,'completed','2025-11-17 16:42:13',NULL,'2025-11-17 16:42:13','',''),(110,45,1,2,0,0,'completed','2025-11-17 16:55:53',NULL,'2025-11-17 16:55:53','',''),(111,45,2,3,0,1,'completed','2025-11-17 16:56:12',NULL,'2025-11-17 16:56:12','','16'),(112,45,3,1,0,2,'completed','2025-11-17 16:56:32',NULL,'2025-11-17 16:56:32','','1,4'),(113,46,1,1,0,2,'completed','2025-11-17 16:59:39',NULL,'2025-11-17 16:59:39','','1,4'),(114,46,2,3,0,1,'completed','2025-11-17 16:59:59',NULL,'2025-11-17 16:59:59','','15'),(115,47,1,1,0,0,'completed','2025-11-17 17:09:38',NULL,'2025-11-17 17:09:38','',''),(116,47,2,3,0,0,'completed','2025-11-17 17:09:58',NULL,'2025-11-17 17:09:58','',''),(117,47,3,2,0,0,'completed','2025-11-17 17:10:18',NULL,'2025-11-17 17:10:18','',''),(118,48,1,3,0,0,'completed','2025-11-17 17:11:43',NULL,'2025-11-17 17:11:43','',''),(119,48,2,2,0,0,'completed','2025-11-17 17:12:03',NULL,'2025-11-17 17:12:03','',''),(120,48,3,1,0,0,'completed','2025-11-17 17:12:23',NULL,'2025-11-17 17:12:23','',''),(121,49,1,1,0,2,'completed','2025-11-17 17:13:46',NULL,'2025-11-17 17:13:46','','1,4'),(122,49,2,2,0,0,'completed','2025-11-17 17:14:06',NULL,'2025-11-17 17:14:06','',''),(123,49,3,3,0,0,'completed','2025-11-17 17:14:26',NULL,'2025-11-17 17:14:26','',''),(124,50,1,1,0,2,'completed','2025-11-17 17:17:52',NULL,'2025-11-17 17:17:52','','1,4'),(125,50,2,2,0,0,'completed','2025-11-17 17:18:12',NULL,'2025-11-17 17:18:12','',''),(126,50,3,3,0,0,'completed','2025-11-17 17:18:32',NULL,'2025-11-17 17:18:32','',''),(127,51,1,1,0,2,'completed','2025-11-17 17:20:36',NULL,'2025-11-17 17:20:36','','1,3'),(128,51,2,3,0,0,'completed','2025-11-17 17:20:56',NULL,'2025-11-17 17:20:56','',''),(129,51,3,2,0,0,'completed','2025-11-17 17:21:16',NULL,'2025-11-17 17:21:16','',''),(130,52,1,1,0,0,'completed','2025-11-17 17:23:22',NULL,'2025-11-17 17:23:22','',''),(131,52,2,2,0,0,'completed','2025-11-17 17:23:42',NULL,'2025-11-17 17:23:42','',''),(132,52,3,3,0,0,'completed','2025-11-17 17:24:03',NULL,'2025-11-17 17:24:03','',''),(133,53,1,3,0,0,'completed','2025-11-17 17:28:49',NULL,'2025-11-17 17:28:49','',''),(134,53,2,1,0,0,'completed','2025-11-17 17:29:09',NULL,'2025-11-17 17:29:09','',''),(135,53,3,2,0,0,'completed','2025-11-17 17:29:29',NULL,'2025-11-17 17:29:29','',''),(136,54,1,2,0,0,'completed','2025-11-17 17:43:47',NULL,'2025-11-17 17:43:47','',''),(137,54,2,1,0,0,'completed','2025-11-17 17:43:58',NULL,'2025-11-17 17:43:58','',''),(138,54,3,3,0,0,'completed','2025-11-17 17:44:18',NULL,'2025-11-17 17:44:18','',''),(139,55,1,2,0,0,'completed','2025-11-17 17:48:23',NULL,'2025-11-17 17:48:23','',''),(140,55,2,1,0,0,'completed','2025-11-17 17:48:43',NULL,'2025-11-17 17:48:43','',''),(141,55,3,3,0,0,'completed','2025-11-17 17:49:03',NULL,'2025-11-17 17:49:03','',''),(142,56,1,3,0,0,'completed','2025-11-17 17:49:22',NULL,'2025-11-17 17:49:22','',''),(143,56,2,2,0,0,'completed','2025-11-17 17:49:34',NULL,'2025-11-17 17:49:34','',''),(144,56,3,1,0,0,'completed','2025-11-17 17:49:54',NULL,'2025-11-17 17:49:54','',''),(145,57,1,2,0,0,'completed','2025-11-17 17:53:03',NULL,'2025-11-17 17:53:03','',''),(146,57,2,1,0,0,'completed','2025-11-17 17:53:14',NULL,'2025-11-17 17:53:14','',''),(147,57,3,3,0,0,'completed','2025-11-17 17:53:35',NULL,'2025-11-17 17:53:35','',''),(148,58,1,2,0,0,'completed','2025-11-17 17:57:53',NULL,'2025-11-17 17:57:53','',''),(149,58,2,3,0,0,'completed','2025-11-17 17:58:05',NULL,'2025-11-17 17:58:05','',''),(150,58,3,1,0,0,'completed','2025-11-17 17:58:25',NULL,'2025-11-17 17:58:25','',''),(151,59,1,1,0,0,'completed','2025-11-17 18:02:30',NULL,'2025-11-17 18:02:30','',''),(152,59,2,3,0,0,'completed','2025-11-17 18:02:46',NULL,'2025-11-17 18:02:46','',''),(153,59,3,2,0,0,'completed','2025-11-17 18:02:58',NULL,'2025-11-17 18:02:58','',''),(154,61,1,1,0,0,'completed','2025-11-17 18:32:49',NULL,'2025-11-17 18:32:49','',''),(155,62,1,2,0,0,'completed','2025-11-17 18:37:04',NULL,'2025-11-17 18:37:04','',''),(156,62,2,3,0,0,'completed','2025-11-17 18:37:24',NULL,'2025-11-17 18:37:24','',''),(157,62,3,1,0,0,'completed','2025-11-17 18:37:44',NULL,'2025-11-17 18:37:44','',''),(158,63,1,3,0,0,'completed','2025-11-17 18:38:23',NULL,'2025-11-17 18:38:23','',''),(159,63,2,1,0,0,'completed','2025-11-17 18:38:43',NULL,'2025-11-17 18:38:43','',''),(160,63,3,2,0,0,'completed','2025-11-17 18:39:03',NULL,'2025-11-17 18:39:03','',''),(161,64,1,1,0,0,'completed','2025-11-17 18:48:52',NULL,'2025-11-17 18:48:52','',''),(162,64,2,2,0,0,'completed','2025-11-17 18:49:12',NULL,'2025-11-17 18:49:12','',''),(163,64,3,3,0,0,'completed','2025-11-17 18:49:32',NULL,'2025-11-17 18:49:32','',''),(164,65,1,2,0,0,'completed','2025-11-17 18:54:03',NULL,'2025-11-17 18:54:03','',''),(165,65,2,3,0,0,'completed','2025-11-17 18:54:23',NULL,'2025-11-17 18:54:23','',''),(166,65,3,1,0,0,'completed','2025-11-17 18:54:43',NULL,'2025-11-17 18:54:43','',''),(167,66,1,3,0,0,'completed','2025-11-17 18:56:52',NULL,'2025-11-17 18:56:52','',''),(168,66,2,2,0,0,'completed','2025-11-17 18:57:12',NULL,'2025-11-17 18:57:12','',''),(169,66,3,1,0,0,'completed','2025-11-17 18:57:32',NULL,'2025-11-17 18:57:32','',''),(170,67,1,1,0,0,'completed','2025-11-17 18:57:48',NULL,'2025-11-17 18:57:48','',''),(171,67,2,3,0,0,'completed','2025-11-17 18:58:08',NULL,'2025-11-17 18:58:08','',''),(172,67,3,2,0,0,'completed','2025-11-17 18:58:28',NULL,'2025-11-17 18:58:28','',''),(173,68,1,3,0,0,'completed','2025-11-17 19:00:10',NULL,'2025-11-17 19:00:10','',''),(174,68,2,1,0,0,'completed','2025-11-17 19:00:30',NULL,'2025-11-17 19:00:30','',''),(175,68,3,2,0,0,'completed','2025-11-17 19:00:50',NULL,'2025-11-17 19:00:50','',''),(176,69,1,3,0,0,'completed','2025-11-17 19:03:51',NULL,'2025-11-17 19:03:51','',''),(177,69,2,2,0,0,'completed','2025-11-17 19:04:11',NULL,'2025-11-17 19:04:11','',''),(178,71,1,3,0,0,'completed','2025-11-17 19:07:13',NULL,'2025-11-17 19:07:13','',''),(179,73,1,2,0,0,'completed','2025-11-17 19:17:31',NULL,'2025-11-17 19:17:31','',''),(180,73,2,3,0,0,'completed','2025-11-17 19:17:51',NULL,'2025-11-17 19:17:51','',''),(181,73,3,1,2,0,'completed','2025-11-17 19:18:04',NULL,'2025-11-17 19:18:04','1,4',''),(182,75,1,2,0,0,'completed','2025-11-17 22:43:30',NULL,'2025-11-17 22:43:30','',''),(183,75,2,1,1,0,'completed','2025-11-17 22:44:40',NULL,'2025-11-17 22:44:40','4',''),(184,75,3,3,1,0,'completed','2025-11-17 22:45:50',NULL,'2025-11-17 22:45:50','15',''),(185,76,1,1,1,0,'completed','2025-11-17 22:55:00',NULL,'2025-11-17 22:55:00','4',''),(186,83,1,2,1,0,'completed','2025-11-17 23:38:13',NULL,'2025-11-17 23:38:13','7',''),(187,83,2,1,0,0,'completed','2025-11-17 23:39:21',NULL,'2025-11-17 23:39:21','',''),(188,83,3,3,0,0,'completed','2025-11-17 23:40:31',NULL,'2025-11-17 23:40:31','',''),(189,84,1,1,0,1,'completed','2025-11-17 23:42:45',NULL,'2025-11-17 23:42:45','','1'),(190,84,2,2,0,0,'completed','2025-11-17 23:44:25',NULL,'2025-11-17 23:44:25','',''),(191,84,3,3,0,0,'completed','2025-11-17 23:45:35',NULL,'2025-11-17 23:45:35','',''),(192,85,1,2,1,2,'completed','2025-11-17 23:49:02',NULL,'2025-11-17 23:49:02','12','7,8'),(193,88,1,2,2,1,'completed','2025-11-18 00:34:00',NULL,'2025-11-18 00:34:00','7,8','12'),(194,88,2,3,0,0,'completed','2025-11-18 00:35:09',NULL,'2025-11-18 00:35:09','',''),(195,88,3,1,2,3,'completed','2025-11-18 00:36:19',NULL,'2025-11-18 00:36:19','1,5','3,4,6'),(196,91,1,3,1,1,'completed','2025-11-18 01:11:00',NULL,'2025-11-18 01:11:00','13','15'),(197,91,2,2,0,3,'completed','2025-11-18 01:12:36',NULL,'2025-11-18 01:12:36','','7,8,12'),(198,92,1,3,0,0,'completed','2025-11-18 23:01:00',NULL,'2025-11-18 23:01:00','',''),(199,92,2,2,0,0,'completed','2025-11-18 23:02:40',NULL,'2025-11-18 23:02:40','',''),(200,92,3,1,2,1,'completed','2025-11-18 23:03:42',NULL,'2025-11-18 23:03:42','1,3','4'),(201,94,1,2,1,0,'completed','2025-11-19 01:25:20',NULL,'2025-11-19 01:25:20','7',''),(202,94,2,1,0,0,'completed','2025-11-19 01:26:23',NULL,'2025-11-19 01:26:23','',''),(203,94,3,3,0,0,'completed','2025-11-19 01:27:33',NULL,'2025-11-19 01:27:33','',''),(204,95,1,3,1,0,'completed','2025-11-19 01:40:52',NULL,'2025-11-19 01:40:52','13',''),(205,95,2,1,0,0,'completed','2025-11-19 01:41:55',NULL,'2025-11-19 01:41:55','',''),(206,95,3,2,0,0,'completed','2025-11-19 01:43:35',NULL,'2025-11-19 01:43:35','',''),(207,96,1,1,1,0,'completed','2025-11-19 02:03:52',NULL,'2025-11-19 02:03:52','1',''),(208,96,2,3,0,0,'completed','2025-11-19 02:05:02',NULL,'2025-11-19 02:05:02','',''),(209,96,3,2,0,0,'completed','2025-11-19 02:06:42',NULL,'2025-11-19 02:06:42','',''),(210,97,1,3,0,1,'completed','2025-11-19 02:12:21',NULL,'2025-11-19 02:12:21','','13'),(211,97,2,1,0,0,'completed','2025-11-19 02:13:31',NULL,'2025-11-19 02:13:31','',''),(212,97,3,2,0,0,'completed','2025-11-19 02:15:11',NULL,'2025-11-19 02:15:11','',''),(213,100,1,2,0,0,'completed','2025-11-19 02:27:54','2025-11-19 02:27:54','2025-11-19 02:27:54','',''),(214,100,2,1,0,0,'completed','2025-11-19 02:28:58','2025-11-19 02:28:59','2025-11-19 02:28:58','',''),(215,100,3,3,0,0,'completed','2025-11-19 02:30:09','2025-11-19 02:30:09','2025-11-19 02:30:09','',''),(216,101,1,3,0,0,'completed','2025-11-19 02:33:05','2025-11-19 02:34:05','2025-11-19 02:33:05','',''),(217,101,2,1,0,0,'completed','2025-11-19 02:34:15','2025-11-19 02:35:15','2025-11-19 02:34:15','',''),(218,101,3,2,0,0,'completed','2025-11-19 02:35:21','2025-11-19 02:36:51','2025-11-19 02:35:21','','');
/*!40000 ALTER TABLE `match_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `match_history`
--

DROP TABLE IF EXISTS `match_history`;
/*!50001 DROP VIEW IF EXISTS `match_history`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `match_history` AS SELECT 
 1 AS `match_id`,
 1 AS `player1_id`,
 1 AS `player1_name`,
 1 AS `player2_id`,
 1 AS `player2_name`,
 1 AS `total_rounds`,
 1 AS `player1_rounds_won`,
 1 AS `player2_rounds_won`,
 1 AS `winner_id`,
 1 AS `winner_name`,
 1 AS `result`,
 1 AS `started_at`,
 1 AS `ended_at`,
 1 AS `duration_minutes`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `matches`
--

DROP TABLE IF EXISTS `matches`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `matches` (
  `match_id` int NOT NULL AUTO_INCREMENT,
  `player1_id` int NOT NULL COMMENT 'Người chơi 1',
  `player2_id` int NOT NULL COMMENT 'Người chơi 2',
  `match_status` enum('waiting','playing','completed','cancelled') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'playing',
  `total_rounds` int DEFAULT '0' COMMENT 'Số vòng đã chơi (1, 2, hoặc 3)',
  `player1_rounds_won` int DEFAULT '0' COMMENT 'Số vòng player 1 thắng',
  `player2_rounds_won` int DEFAULT '0' COMMENT 'Số vòng player 2 thắng',
  `winner_id` int DEFAULT NULL COMMENT 'Người thắng trận (NULL nếu hòa)',
  `result` enum('player1_win','player2_win','draw','cancelled') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `started_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `ended_at` datetime DEFAULT NULL,
  `draw` int DEFAULT NULL,
  PRIMARY KEY (`match_id`),
  KEY `player2_id` (`player2_id`),
  KEY `winner_id` (`winner_id`),
  KEY `idx_players` (`player1_id`,`player2_id`),
  KEY `idx_status` (`match_status`),
  KEY `idx_started` (`started_at` DESC),
  CONSTRAINT `matches_ibfk_1` FOREIGN KEY (`player1_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `matches_ibfk_2` FOREIGN KEY (`player2_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `matches_ibfk_3` FOREIGN KEY (`winner_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Quản lý trận đấu - thông tin tổng quan';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `matches`
--

LOCK TABLES `matches` WRITE;
/*!40000 ALTER TABLE `matches` DISABLE KEYS */;
INSERT INTO `matches` VALUES (1,1,2,'completed',3,1,2,2,'player2_win','2025-11-07 23:44:53','2025-11-07 23:48:53',0),(2,1,3,'completed',3,3,0,1,'player1_win','2025-11-08 16:53:36','2025-11-08 16:57:36',0),(3,1,4,'completed',3,2,1,1,'player1_win','2025-11-08 17:53:36','2025-11-08 17:58:36',0),(5,1,2,'completed',3,2,0,1,'player1_win','2025-11-16 01:25:37','2025-11-16 01:28:17',1),(6,1,2,'playing',3,0,0,NULL,NULL,'2025-11-16 01:29:13',NULL,3),(7,1,2,'playing',3,0,0,NULL,NULL,'2025-11-16 01:36:57',NULL,3),(8,2,1,'playing',3,0,0,NULL,NULL,'2025-11-16 02:12:50',NULL,NULL),(9,2,1,'playing',3,0,0,NULL,NULL,'2025-11-16 02:20:45',NULL,NULL),(10,2,1,'completed',3,3,3,NULL,'draw','2025-11-16 02:27:40','2025-11-16 02:30:30',3),(11,1,2,'completed',3,2,3,2,'player2_win','2025-11-16 02:35:40','2025-11-16 02:38:26',2),(12,1,2,'completed',3,2,0,1,'player1_win','2025-11-16 02:44:55','2025-11-16 02:46:48',0),(13,2,1,'completed',3,1,3,1,'player2_win','2025-11-16 18:50:44','2025-11-16 18:53:07',1),(14,6,4,'completed',3,0,2,4,'player2_win','2025-11-16 18:55:37','2025-11-16 18:57:47',0),(15,4,6,'completed',3,2,0,4,'player1_win','2025-11-16 19:06:27','2025-11-16 19:08:37',0),(16,6,4,'completed',3,0,0,6,'player1_win','2025-11-16 19:36:49','2025-11-16 19:48:21',3),(17,6,4,'completed',3,0,0,4,'player2_win','2025-11-16 19:48:36','2025-11-16 20:05:45',3),(18,4,6,'completed',3,0,0,6,'player2_win','2025-11-16 20:05:51','2025-11-16 20:10:34',3),(19,6,4,'completed',3,0,3,4,'player2_win','2025-11-16 20:11:23','2025-11-16 20:12:40',0),(20,4,1,'completed',3,0,3,1,'player2_win','2025-11-16 20:13:04','2025-11-16 20:18:03',0),(21,4,6,'completed',3,3,1,4,'player1_win','2025-11-17 14:36:37','2025-11-17 14:40:21',1),(22,4,6,'completed',3,0,2,6,'player2_win','2025-11-17 14:41:28','2025-11-17 14:44:04',0),(23,4,6,'completed',3,3,0,4,'player1_win','2025-11-17 14:46:08','2025-11-17 14:46:12',0),(24,4,7,'completed',3,3,3,NULL,'draw','2025-11-17 14:50:35','2025-11-17 14:54:19',3),(25,4,7,'completed',3,3,3,NULL,'draw','2025-11-17 14:56:24','2025-11-17 15:00:15',3),(26,4,6,'completed',3,3,3,NULL,'draw','2025-11-17 15:02:27','2025-11-17 15:06:17',3),(27,7,4,'completed',3,3,0,7,'player1_win','2025-11-17 15:02:38','2025-11-17 15:02:40',0),(28,2,1,'completed',3,3,0,2,'player1_win','2025-11-17 15:10:03','2025-11-17 15:10:45',0),(29,2,1,'completed',3,3,3,NULL,'draw','2025-11-17 15:11:45','2025-11-17 15:15:35',3),(30,2,1,'completed',3,3,3,NULL,'draw','2025-11-17 15:23:44','2025-11-17 15:27:34',3),(31,2,1,'playing',3,0,0,NULL,NULL,'2025-11-17 15:30:53',NULL,NULL),(32,2,1,'completed',3,2,3,1,'player2_win','2025-11-17 15:33:00','2025-11-17 15:36:46',2),(33,2,1,'completed',3,2,3,1,'player2_win','2025-11-17 15:40:18','2025-11-17 15:44:08',2),(34,1,3,'completed',3,3,0,1,'player1_win','2025-11-17 15:47:42','2025-11-17 15:47:53',0),(35,3,1,'completed',3,3,3,NULL,'draw','2025-11-17 15:48:05','2025-11-17 15:51:55',3),(36,2,1,'completed',3,3,3,NULL,'draw','2025-11-17 15:55:11','2025-11-17 15:56:01',3),(37,2,1,'completed',3,3,3,NULL,'draw','2025-11-17 16:11:30','2025-11-17 16:12:20',3),(38,1,2,'completed',3,3,3,NULL,'draw','2025-11-17 16:12:48','2025-11-17 16:13:39',3),(39,2,1,'completed',3,3,3,NULL,'draw','2025-11-17 16:16:49','2025-11-17 16:17:39',3),(40,2,1,'completed',3,2,3,1,'player2_win','2025-11-17 16:18:08','2025-11-17 16:18:58',2),(41,2,1,'completed',3,2,3,1,'player2_win','2025-11-17 16:28:10','2025-11-17 16:28:55',2),(42,2,1,'completed',3,2,3,1,'player2_win','2025-11-17 16:30:37','2025-11-17 16:31:27',2),(43,2,1,'completed',3,2,3,1,'player2_win','2025-11-17 16:36:31','2025-11-17 16:37:22',2),(44,1,3,'completed',3,2,3,3,'player2_win','2025-11-17 16:41:23','2025-11-17 16:42:13',2),(45,1,2,'completed',3,1,3,2,'player2_win','2025-11-17 16:55:42','2025-11-17 16:56:32',1),(46,2,1,'completed',3,0,2,1,'player2_win','2025-11-17 16:59:28','2025-11-17 16:59:59',0),(47,2,1,'completed',3,3,3,NULL,'draw','2025-11-17 17:09:28','2025-11-17 17:10:18',3),(48,2,1,'completed',3,3,3,NULL,'draw','2025-11-17 17:11:33','2025-11-17 17:12:23',3),(49,2,1,'completed',3,2,3,1,'player2_win','2025-11-17 17:13:36','2025-11-17 17:14:26',2),(50,2,1,'completed',3,2,3,1,'player2_win','2025-11-17 17:17:42','2025-11-17 17:18:32',2),(51,2,1,'completed',3,2,3,1,'player2_win','2025-11-17 17:20:26','2025-11-17 17:21:16',2),(52,2,1,'completed',3,3,3,NULL,'draw','2025-11-17 17:23:12','2025-11-17 17:24:03',3),(53,2,1,'completed',3,3,3,NULL,'draw','2025-11-17 17:28:39','2025-11-17 17:29:29',3),(54,2,1,'completed',3,3,3,NULL,'draw','2025-11-17 17:43:37','2025-11-17 17:44:19',3),(55,2,1,'completed',3,3,3,NULL,'draw','2025-11-17 17:48:13','2025-11-17 17:49:03',3),(56,2,1,'completed',3,3,3,NULL,'draw','2025-11-17 17:49:12','2025-11-17 17:49:54',3),(57,2,1,'completed',3,3,3,NULL,'draw','2025-11-17 17:52:52','2025-11-17 17:53:35',3),(58,2,1,'completed',3,3,3,NULL,'draw','2025-11-17 17:57:43','2025-11-17 17:58:25',3),(59,2,1,'completed',3,3,3,NULL,'draw','2025-11-17 18:02:20','2025-11-17 18:02:58',3),(60,1,2,'completed',3,3,0,1,'player1_win','2025-11-17 18:03:12','2025-11-17 18:03:14',0),(61,2,1,'completed',3,3,0,2,'player1_win','2025-11-17 18:32:39','2025-11-17 18:33:02',0),(62,2,1,'completed',3,3,3,NULL,'draw','2025-11-17 18:36:54','2025-11-17 18:37:44',3),(63,1,2,'completed',3,3,3,NULL,'draw','2025-11-17 18:38:13','2025-11-17 18:39:03',3),(64,2,1,'completed',3,3,3,NULL,'draw','2025-11-17 18:48:42','2025-11-17 18:49:32',3),(65,2,1,'completed',3,3,3,NULL,'draw','2025-11-17 18:53:53','2025-11-17 18:54:43',3),(66,2,1,'completed',3,3,3,NULL,'draw','2025-11-17 18:56:42','2025-11-17 18:57:32',3),(67,2,1,'completed',3,3,3,NULL,'draw','2025-11-17 18:57:38','2025-11-17 18:58:28',3),(68,2,1,'completed',3,3,3,NULL,'draw','2025-11-17 19:00:00','2025-11-17 19:00:50',3),(69,2,1,'completed',3,3,0,2,'player1_win','2025-11-17 19:03:41','2025-11-17 19:04:13',0),(70,2,1,'completed',3,3,0,2,'player1_win','2025-11-17 19:04:31','2025-11-17 19:04:38',0),(71,2,1,'completed',3,3,0,2,'player1_win','2025-11-17 19:07:03','2025-11-17 19:07:20',0),(72,2,1,'completed',3,3,0,2,'player1_win','2025-11-17 19:07:27','2025-11-17 19:07:33',0),(73,2,1,'completed',3,3,2,2,'player1_win','2025-11-17 19:17:21','2025-11-17 19:18:04',2),(74,2,1,'completed',3,3,0,2,'player1_win','2025-11-17 22:36:12','2025-11-17 22:36:46',0),(75,2,1,'completed',3,3,1,2,'player1_win','2025-11-17 22:42:00','2025-11-17 22:45:50',1),(76,2,1,'completed',3,3,0,2,'player1_win','2025-11-17 22:54:00','2025-11-17 22:55:37',0),(77,2,1,'completed',3,0,3,1,'player2_win','2025-11-17 22:58:14','2025-11-17 22:58:17',0),(78,2,1,'completed',3,0,3,1,'player2_win','2025-11-17 23:01:21','2025-11-17 23:01:28',0),(79,1,2,'completed',3,3,0,1,'player1_win','2025-11-17 23:02:40','2025-11-17 23:02:43',0),(80,1,2,'completed',3,3,0,1,'player1_win','2025-11-17 23:03:38','2025-11-17 23:03:49',0),(81,2,1,'completed',3,3,0,2,'player1_win','2025-11-17 23:11:44','2025-11-17 23:11:58',0),(82,1,2,'completed',3,3,0,1,'player1_win','2025-11-17 23:15:09','2025-11-17 23:15:27',0),(83,1,2,'completed',3,3,2,1,'player1_win','2025-11-17 23:36:43','2025-11-17 23:40:31',2),(84,2,1,'completed',3,2,3,1,'player2_win','2025-11-17 23:41:45','2025-11-17 23:45:36',2),(85,1,2,'completed',3,0,3,2,'player2_win','2025-11-17 23:47:32','2025-11-17 23:49:46',0),(86,1,2,'completed',3,0,3,2,'player2_win','2025-11-18 00:03:51','2025-11-18 00:04:02',0),(87,1,2,'completed',3,0,3,2,'player2_win','2025-11-18 00:11:46','2025-11-18 00:11:54',0),(88,2,1,'completed',3,2,2,NULL,'draw','2025-11-18 00:32:30','2025-11-18 00:36:19',1),(89,2,1,'completed',3,0,3,1,'player2_win','2025-11-18 00:36:42','2025-11-18 00:36:43',0),(90,2,1,'completed',3,0,3,1,'player2_win','2025-11-18 00:36:54','2025-11-18 00:36:55',0),(91,2,1,'completed',3,3,0,2,'player1_win','2025-11-18 01:10:00','2025-11-18 01:13:13',0),(92,1,2,'completed',3,3,2,1,'player1_win','2025-11-18 23:00:00','2025-11-18 23:03:42',2),(93,1,2,'completed',3,3,0,1,'player1_win','2025-11-18 23:09:23','2025-11-18 23:09:26',0),(94,1,2,'completed',3,1,0,1,'player1_win','2025-11-19 01:23:50','2025-11-19 01:27:33',2),(95,1,2,'completed',3,1,0,1,'player1_win','2025-11-19 01:39:52','2025-11-19 01:43:35',2),(96,1,2,'completed',3,1,0,1,'player1_win','2025-11-19 02:02:52','2025-11-19 02:06:42',2),(97,1,2,'completed',3,0,1,2,'player2_win','2025-11-19 02:11:21','2025-11-19 02:15:11',2),(98,2,1,'completed',3,0,3,1,'player2_win','2025-11-19 02:17:57','2025-11-19 02:18:46',0),(99,4,6,'completed',3,0,3,6,'player2_win','2025-11-19 02:25:28','2025-11-19 02:26:08',0),(100,4,6,'completed',3,0,0,NULL,'draw','2025-11-19 02:26:24','2025-11-19 02:30:09',3),(101,1,2,'completed',3,0,0,NULL,'draw','2025-11-19 02:33:05','2025-11-19 02:36:51',3);
/*!40000 ALTER TABLE `matches` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `online_players`
--

DROP TABLE IF EXISTS `online_players`;
/*!50001 DROP VIEW IF EXISTS `online_players`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `online_players` AS SELECT 
 1 AS `user_id`,
 1 AS `username`,
 1 AS `full_name`,
 1 AS `status`,
 1 AS `total_points`,
 1 AS `total_wins`,
 1 AS `total_draws`,
 1 AS `total_losses`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Nên hash bằng BCrypt',
  `full_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('online','offline','playing') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'offline',
  `total_points` int DEFAULT '0' COMMENT 'Thắng: +2, Hòa: +1, Thua: +0',
  `total_wins` int DEFAULT '0',
  `total_draws` int DEFAULT '0',
  `total_losses` int DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`),
  KEY `idx_username` (`username`),
  KEY `idx_total_points` (`total_points` DESC),
  KEY `idx_total_wins` (`total_wins` DESC)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Quản lý tài khoản và thống kê người chơi';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'player1','123456','Nguyễn Văn A','offline',136,35,28,23,'2025-11-05 14:01:16','2025-11-19 02:36:59'),(2,'player2','123456','Trần Thị B','offline',94,20,28,32,'2025-11-05 14:01:16','2025-11-19 02:36:58'),(3,'player3','123456','Lê Văn C','offline',63,7,2,3,'2025-11-05 14:01:16','2025-11-18 00:53:15'),(4,'tranminhtu','123456','tran minh t','offline',1006,15,8,11,'2025-11-07 22:58:05','2025-11-19 02:37:21'),(5,'trinhvanquyet','123456','trinh van quyet','offline',200,2,100,1,'2025-11-07 23:00:51','2025-11-07 23:00:51'),(6,'truongmilan','123456','truong mi lan','offline',319,19,205,56,'2025-11-07 23:04:53','2025-11-19 02:37:22'),(7,'donaltrump','123456','donaltrump','offline',44,2,14,20,'2025-11-07 23:05:38','2025-11-17 15:08:28');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'gamevtv'
--

--
-- Dumping routines for database 'gamevtv'
--
/*!50003 DROP PROCEDURE IF EXISTS `CheckWord` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `CheckWord`(
    IN p_word VARCHAR(50),
    OUT p_is_valid BOOLEAN
)
BEGIN
    DECLARE v_count INT;
    
    SELECT COUNT(*) INTO v_count
    FROM dictionary
    WHERE word = UPPER(TRIM(p_word));
    
    SET p_is_valid = (v_count > 0);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `CreateMatch` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `CreateMatch`(
    IN p_player1_id INT,
    IN p_player2_id INT,
    OUT p_match_id INT
)
BEGIN
    -- Tạo trận đấu
    INSERT INTO matches (player1_id, player2_id, match_status, started_at)
    VALUES (p_player1_id, p_player2_id, 'playing', NOW());
    
    SET p_match_id = LAST_INSERT_ID();
    
    -- Đổi trạng thái 2 người sang 'playing'
    UPDATE users SET status = 'playing' 
    WHERE user_id IN (p_player1_id, p_player2_id);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `CreateRound` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `CreateRound`(
    IN p_match_id INT,
    IN p_round_number INT,
    IN p_letters VARCHAR(100),
    IN p_word_length INT,
    OUT p_detail_id INT
)
BEGIN
    INSERT INTO match_details (
        match_id, 
        round_number, 
        letters, 
        word_length,
        round_status,
        started_at
    )
    VALUES (
        p_match_id, 
        p_round_number, 
        p_letters, 
        p_word_length,
        'playing',
        NOW()
    );
    
    SET p_detail_id = LAST_INSERT_ID();
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `FinishMatch` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `FinishMatch`(
    IN p_match_id INT
)
BEGIN
    DECLARE v_player1_id INT;
    DECLARE v_player2_id INT;
    DECLARE v_player1_rounds INT;
    DECLARE v_player2_rounds INT;
    DECLARE v_winner_id INT;
    DECLARE v_result VARCHAR(20);
    
    -- Lấy thông tin
    SELECT player1_id, player2_id, player1_rounds_won, player2_rounds_won
    INTO v_player1_id, v_player2_id, v_player1_rounds, v_player2_rounds
    FROM matches WHERE match_id = p_match_id;
    
    -- Xác định người thắng
    IF v_player1_rounds > v_player2_rounds THEN
        SET v_winner_id = v_player1_id;
        SET v_result = 'player1_win';
    ELSEIF v_player2_rounds > v_player1_rounds THEN
        SET v_winner_id = v_player2_id;
        SET v_result = 'player2_win';
    ELSE
        SET v_winner_id = NULL;
        SET v_result = 'draw';
    END IF;
    
    -- Cập nhật trận đấu
    UPDATE matches
    SET winner_id = v_winner_id,
        result = v_result,
        match_status = 'completed',
        ended_at = NOW()
    WHERE match_id = p_match_id;
    
    -- Cập nhật thống kê user
    IF v_result = 'player1_win' THEN
        UPDATE users SET total_wins = total_wins + 1, total_points = total_points + 2
        WHERE user_id = v_player1_id;
        UPDATE users SET total_losses = total_losses + 1
        WHERE user_id = v_player2_id;
    ELSEIF v_result = 'player2_win' THEN
        UPDATE users SET total_wins = total_wins + 1, total_points = total_points + 2
        WHERE user_id = v_player2_id;
        UPDATE users SET total_losses = total_losses + 1
        WHERE user_id = v_player1_id;
    ELSE
        UPDATE users SET total_draws = total_draws + 1, total_points = total_points + 1
        WHERE user_id IN (v_player1_id, v_player2_id);
    END IF;
    
    -- Đổi trạng thái về online
    UPDATE users SET status = 'online'
    WHERE user_id IN (v_player1_id, v_player2_id);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `FinishRound` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `FinishRound`(
    IN p_detail_id INT
)
BEGIN
    DECLARE v_match_id INT;
    DECLARE v_round_number INT;
    DECLARE v_player1_words INT;
    DECLARE v_player2_words INT;
    DECLARE v_winner_id INT;
    
    -- Đếm số từ đúng của mỗi người
    SELECT match_id, COUNT(DISTINCT CASE WHEN is_valid = TRUE THEN word END)
    INTO v_match_id, v_player1_words
    FROM match_words
    WHERE detail_id = p_detail_id
    AND user_id = (SELECT player1_id FROM matches WHERE match_id = v_match_id)
    GROUP BY match_id;
    
    SELECT COUNT(DISTINCT CASE WHEN is_valid = TRUE THEN word END)
    INTO v_player2_words
    FROM match_words
    WHERE detail_id = p_detail_id
    AND user_id = (SELECT player2_id FROM matches WHERE match_id = v_match_id);
    
    -- Xác định người thắng vòng
    IF v_player1_words > v_player2_words THEN
        SELECT player1_id INTO v_winner_id FROM matches WHERE match_id = v_match_id;
    ELSEIF v_player2_words > v_player1_words THEN
        SELECT player2_id INTO v_winner_id FROM matches WHERE match_id = v_match_id;
    ELSE
        SET v_winner_id = NULL; -- Hòa
    END IF;
    
    -- Cập nhật kết quả vòng
    UPDATE match_details
    SET player1_words_count = v_player1_words,
        player2_words_count = v_player2_words,
        winner_id = v_winner_id,
        round_status = 'completed',
        ended_at = NOW()
    WHERE detail_id = p_detail_id;
    
    -- Cập nhật số vòng thắng trong matches
    SELECT round_number INTO v_round_number
    FROM match_details WHERE detail_id = p_detail_id;
    
    UPDATE matches
    SET total_rounds = v_round_number
    WHERE match_id = v_match_id;
    
    IF v_winner_id = (SELECT player1_id FROM matches WHERE match_id = v_match_id) THEN
        UPDATE matches SET player1_rounds_won = player1_rounds_won + 1
        WHERE match_id = v_match_id;
    ELSEIF v_winner_id = (SELECT player2_id FROM matches WHERE match_id = v_match_id) THEN
        UPDATE matches SET player2_rounds_won = player2_rounds_won + 1
        WHERE match_id = v_match_id;
    END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Final view structure for view `leaderboard_by_points`
--

/*!50001 DROP VIEW IF EXISTS `leaderboard_by_points`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `leaderboard_by_points` AS select `users`.`user_id` AS `user_id`,`users`.`username` AS `username`,`users`.`full_name` AS `full_name`,`users`.`total_points` AS `total_points`,`users`.`total_wins` AS `total_wins`,`users`.`total_draws` AS `total_draws`,`users`.`total_losses` AS `total_losses`,((`users`.`total_wins` + `users`.`total_draws`) + `users`.`total_losses`) AS `total_matches` from `users` order by `users`.`total_points` desc,`users`.`total_wins` desc limit 100 */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `leaderboard_by_wins`
--

/*!50001 DROP VIEW IF EXISTS `leaderboard_by_wins`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `leaderboard_by_wins` AS select `users`.`user_id` AS `user_id`,`users`.`username` AS `username`,`users`.`full_name` AS `full_name`,`users`.`total_wins` AS `total_wins`,`users`.`total_points` AS `total_points`,`users`.`total_draws` AS `total_draws`,`users`.`total_losses` AS `total_losses`,((`users`.`total_wins` + `users`.`total_draws`) + `users`.`total_losses`) AS `total_matches` from `users` order by `users`.`total_wins` desc,`users`.`total_points` desc limit 100 */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `match_history`
--

/*!50001 DROP VIEW IF EXISTS `match_history`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `match_history` AS select `m`.`match_id` AS `match_id`,`m`.`player1_id` AS `player1_id`,`u1`.`username` AS `player1_name`,`m`.`player2_id` AS `player2_id`,`u2`.`username` AS `player2_name`,`m`.`total_rounds` AS `total_rounds`,`m`.`player1_rounds_won` AS `player1_rounds_won`,`m`.`player2_rounds_won` AS `player2_rounds_won`,`m`.`winner_id` AS `winner_id`,(case when (`m`.`winner_id` = `m`.`player1_id`) then `u1`.`username` when (`m`.`winner_id` = `m`.`player2_id`) then `u2`.`username` else 'Hòa' end) AS `winner_name`,`m`.`result` AS `result`,`m`.`started_at` AS `started_at`,`m`.`ended_at` AS `ended_at`,timestampdiff(MINUTE,`m`.`started_at`,`m`.`ended_at`) AS `duration_minutes` from ((`matches` `m` join `users` `u1` on((`m`.`player1_id` = `u1`.`user_id`))) join `users` `u2` on((`m`.`player2_id` = `u2`.`user_id`))) where (`m`.`match_status` = 'completed') order by `m`.`ended_at` desc */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `online_players`
--

/*!50001 DROP VIEW IF EXISTS `online_players`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `online_players` AS select `users`.`user_id` AS `user_id`,`users`.`username` AS `username`,`users`.`full_name` AS `full_name`,`users`.`status` AS `status`,`users`.`total_points` AS `total_points`,`users`.`total_wins` AS `total_wins`,`users`.`total_draws` AS `total_draws`,`users`.`total_losses` AS `total_losses` from `users` where (`users`.`status` in ('online','playing')) order by `users`.`total_points` desc */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-19  2:37:50
