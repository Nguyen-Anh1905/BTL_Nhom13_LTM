-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: localhost    Database: gamevtv
-- ------------------------------------------------------
-- Server version	8.0.39

CREATE DATABASE IF NOT EXISTS gamevtv CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE gamevtv;
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Từ điển tiếng Việt để kiểm tra từ hợp lệ';
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `winner_id` int DEFAULT NULL COMMENT 'Người thắng vòng này (NULL nếu hòa)',
  `round_status` enum('waiting','playing','completed') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'waiting',
  `started_at` datetime DEFAULT NULL,
  `ended_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`detail_id`),
  UNIQUE KEY `unique_match_round` (`match_id`,`round_number`),
  KEY `winner_id` (`winner_id`),
  KEY `idx_match_id` (`match_id`),
  KEY `idx_round_number` (`round_number`),
  KEY `fk_match_letter` (`letter_id`),
  CONSTRAINT `fk_match_letter` FOREIGN KEY (`letter_id`) REFERENCES `letters` (`letter_id`),
  CONSTRAINT `match_details_ibfk_1` FOREIGN KEY (`match_id`) REFERENCES `matches` (`match_id`) ON DELETE CASCADE,
  CONSTRAINT `match_details_ibfk_2` FOREIGN KEY (`winner_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Chi tiết từng vòng đấu trong trận';
/*!40101 SET character_set_client = @saved_cs_client */;

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
  PRIMARY KEY (`match_id`),
  KEY `player2_id` (`player2_id`),
  KEY `winner_id` (`winner_id`),
  KEY `idx_players` (`player1_id`,`player2_id`),
  KEY `idx_status` (`match_status`),
  KEY `idx_started` (`started_at` DESC),
  CONSTRAINT `matches_ibfk_1` FOREIGN KEY (`player1_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `matches_ibfk_2` FOREIGN KEY (`player2_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `matches_ibfk_3` FOREIGN KEY (`winner_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Quản lý trận đấu - thông tin tổng quan';
/*!40101 SET character_set_client = @saved_cs_client */;

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
-- Table structure for table `tmp`
--

DROP TABLE IF EXISTS `tmp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tmp` (
  `letter_id` int NOT NULL,
  `match_id` int NOT NULL,
  `detail_id` int NOT NULL COMMENT 'Chi tiết vòng đấu nào',
  `user_id` int NOT NULL,
  `word` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Từ người chơi ghép',
  `is_valid` tinyint(1) NOT NULL COMMENT 'Từ có trong từ điển không',
  `submitted_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`letter_id`),
  KEY `detail_id` (`detail_id`),
  KEY `idx_match_detail` (`match_id`,`detail_id`),
  KEY `idx_user` (`user_id`),
  CONSTRAINT `tmp_ibfk_1` FOREIGN KEY (`match_id`) REFERENCES `matches` (`match_id`) ON DELETE CASCADE,
  CONSTRAINT `tmp_ibfk_2` FOREIGN KEY (`detail_id`) REFERENCES `match_details` (`detail_id`) ON DELETE CASCADE,
  CONSTRAINT `tmp_ibfk_3` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Từ đã ghép trong từng vòng đấu';
/*!40101 SET character_set_client = @saved_cs_client */;

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
    DECLARE v_player1_id INT;
    DECLARE v_player2_id INT;
    DECLARE v_player1_words INT;
    DECLARE v_player2_words INT;
    DECLARE v_winner_id INT;
    
    -- Lấy match_id và player IDs trước
    SELECT match_id INTO v_match_id
    FROM match_details
    WHERE detail_id = p_detail_id;
    
    SELECT player1_id, player2_id
    INTO v_player1_id, v_player2_id
    FROM matches
    WHERE match_id = v_match_id;
    
    -- Đếm số từ đúng của player 1
    SELECT COUNT(DISTINCT CASE WHEN is_valid = TRUE THEN word END)
    INTO v_player1_words
    FROM match_words
    WHERE detail_id = p_detail_id
    AND user_id = v_player1_id;
    
    -- Đếm số từ đúng của player 2
    SELECT COUNT(DISTINCT CASE WHEN is_valid = TRUE THEN word END)
    INTO v_player2_words
    FROM match_words
    WHERE detail_id = p_detail_id
    AND user_id = v_player2_id;
    
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

-- Dump completed on 2025-11-11 21:58:19
