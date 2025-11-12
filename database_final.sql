-- ========================================
-- DATABASE SCHEMA TỐI ƯU - GAME ĐẤU TỪ
-- 5 BẢNG: users, dictionary, matches, match_details, match_words
-- ========================================

CREATE DATABASE IF NOT EXISTS gamevtv 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE gamevtv;

-- ========================================
-- 1. BẢNG USERS - Quản lý người chơi
-- ========================================
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL COMMENT 'Nên hash bằng BCrypt',
    full_name VARCHAR(100) NOT NULL,
    
    -- Trạng thái hiện tại
    status ENUM('online', 'offline', 'playing') DEFAULT 'offline',
    
    -- Thống kê cho bảng xếp hạng
    total_points INT DEFAULT 0 COMMENT 'Thắng: +2, Hòa: +1, Thua: +0',
    total_wins INT DEFAULT 0,
    total_draws INT DEFAULT 0,
    total_losses INT DEFAULT 0,
    
    -- Thời gian
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_username (username),
    INDEX idx_total_points (total_points DESC),
    INDEX idx_total_wins (total_wins DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Quản lý tài khoản và thống kê người chơi';

-- ========================================
-- 2. BẢNG DICTIONARY - Từ điển
-- ========================================
CREATE TABLE dictionary (
    word_id INT PRIMARY KEY AUTO_INCREMENT,
    word VARCHAR(50) NOT NULL COMMENT 'Từ tiếng Việt có dấu, viết HOA',
    word_length INT NOT NULL COMMENT 'Số chữ cái của từ',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    UNIQUE KEY unique_word (word),
    INDEX idx_word_length (word_length)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Từ điển tiếng Việt để kiểm tra từ hợp lệ';

-- ========================================
-- 3. BẢNG MATCHES - Quản lý trận đấu (TỔNG QUAN)
-- ========================================
/*
MỤC ĐÍCH:
- Lưu thông tin TỔNG QUAN của trận đấu
- Ai đấu với ai, ai thắng, kết quả gì
- KHÔNG lưu chi tiết từng vòng (chi tiết ở bảng match_details)

TẠI SAO THIẾT KẾ NÀY TỐT:
- ✅ Tách biệt rõ ràng: TỔNG QUAN vs CHI TIẾT
- ✅ Dễ query "lấy tất cả trận của user X"
- ✅ Dễ mở rộng (muốn thêm info chung không ảnh hưởng chi tiết)
*/
CREATE TABLE matches (
    match_id INT PRIMARY KEY AUTO_INCREMENT,
    
    -- Người chơi
    player1_id INT NOT NULL COMMENT 'Người chơi 1',
    player2_id INT NOT NULL COMMENT 'Người chơi 2',
    
    -- Trạng thái
    match_status ENUM('waiting', 'playing', 'completed', 'cancelled') DEFAULT 'playing',
    
    -- Kết quả CHUNG
    total_rounds INT DEFAULT 0 COMMENT 'Số vòng đã chơi (1, 2, hoặc 3)',
    player1_rounds_won INT DEFAULT 0 COMMENT 'Số vòng player 1 thắng',
    player2_rounds_won INT DEFAULT 0 COMMENT 'Số vòng player 2 thắng',
    
    winner_id INT COMMENT 'Người thắng trận (NULL nếu hòa)',
    result ENUM('player1_win', 'player2_win', 'draw', 'cancelled') NULL,
    
    -- Thời gian
    started_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    ended_at DATETIME,
    
    FOREIGN KEY (player1_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (player2_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (winner_id) REFERENCES users(user_id) ON DELETE SET NULL,
    
    INDEX idx_players (player1_id, player2_id),
    INDEX idx_status (match_status),
    INDEX idx_started (started_at DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Quản lý trận đấu - thông tin tổng quan';

-- ========================================
-- 4. BẢNG MATCH_DETAILS - Chi tiết từng vòng đấu
-- ========================================
/*
MỤC ĐÍCH:
- Lưu CHI TIẾT từng vòng đấu
- Mỗi trận có 1-3 records trong bảng này (tùy số vòng chơi)

ƯU ĐIỂM:
- ✅ Chuẩn database normalization
- ✅ Dễ query "lấy vòng 1 của trận X"
- ✅ Dễ mở rộng (muốn 5 vòng chỉ cần INSERT thêm)
- ✅ Không lặp lại cột như round1_, round2_, round3_

VÍ DỤ:
Trận match_id=1 có 3 vòng → 3 records:
- detail_id=1: match_id=1, round_number=1, letters="Á,B,Đ,G,N"
- detail_id=2: match_id=1, round_number=2, letters="C,H,Ó,Ồ,M"
- detail_id=3: match_id=1, round_number=3, letters="X,Y,Z,K,L"
*/
CREATE TABLE match_details (
    detail_id INT PRIMARY KEY AUTO_INCREMENT,
    match_id INT NOT NULL,
    round_number INT NOT NULL COMMENT 'Vòng 1, 2, hoặc 3',
    
    -- Cài đặt vòng đấu
    letters VARCHAR(100) NOT NULL COMMENT 'Chữ cái cho vòng này, VD: Á,B,Đ,G,N,Ó',
    word_length INT NOT NULL COMMENT 'Số chữ cái yêu cầu',
    duration_seconds INT DEFAULT 60 COMMENT 'Thời gian (giây)',
    
    -- Kết quả vòng đấu
    player1_words_count INT DEFAULT 0 COMMENT 'Số từ đúng của player 1',
    player2_words_count INT DEFAULT 0 COMMENT 'Số từ đúng của player 2',
    winner_id INT COMMENT 'Người thắng vòng này (NULL nếu hòa)',
    
    -- Trạng thái
    round_status ENUM('waiting', 'playing', 'completed') DEFAULT 'waiting',
    
    -- Thời gian
    started_at DATETIME,
    ended_at DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (match_id) REFERENCES matches(match_id) ON DELETE CASCADE,
    FOREIGN KEY (winner_id) REFERENCES users(user_id) ON DELETE SET NULL,
    
    -- Đảm bảo không trùng vòng trong 1 trận
    UNIQUE KEY unique_match_round (match_id, round_number),
    
    INDEX idx_match_id (match_id),
    INDEX idx_round_number (round_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Chi tiết từng vòng đấu trong trận';

-- ========================================
-- 5. BẢNG MATCH_WORDS - Từ đã ghép
-- ========================================
/*
MỤC ĐÍCH:
- Lưu từng từ người chơi ghép
- Hiển thị kết quả "những từ đã ghép đúng của đối thủ"
*/
CREATE TABLE match_words (
    word_id INT PRIMARY KEY AUTO_INCREMENT,
    match_id INT NOT NULL,
    detail_id INT NOT NULL COMMENT 'Chi tiết vòng đấu nào',
    user_id INT NOT NULL,
    word VARCHAR(50) NOT NULL COMMENT 'Từ người chơi ghép',
    is_valid BOOLEAN NOT NULL COMMENT 'Từ có trong từ điển không',
    submitted_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (match_id) REFERENCES matches(match_id) ON DELETE CASCADE,
    FOREIGN KEY (detail_id) REFERENCES match_details(detail_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    
    INDEX idx_match_detail (match_id, detail_id),
    INDEX idx_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Từ đã ghép trong từng vòng đấu';

-- ========================================
-- DỮ LIỆU MẪU
-- ========================================

-- User mẫu
INSERT INTO users (username, password, full_name, total_points, total_wins, total_draws, total_losses) VALUES
('player1', '123456', 'Nguyễn Văn A', 50, 5, 2, 3),
('player2', '123456', 'Trần Thị B', 35, 3, 3, 4),
('player3', '123456', 'Lê Văn C', 60, 6, 1, 2);

-- Từ điển mẫu
INSERT INTO dictionary (word, word_length) VALUES
-- 2 chữ
('BÀ', 2), ('CÁ', 2), ('ĐÁ', 2), ('GÀ', 2), ('HÓ', 2),
-- 3 chữ
('BÀN', 3), ('HOA', 3), ('CÂY', 3), ('NHÀ', 3), ('MÁY', 3),
('NÓN', 3), ('ĐÁM', 3), ('GẠO', 3),
-- 4 chữ
('BÁNH', 4), ('CÁNH', 4), ('ĐÁNH', 4), ('GÁNH', 4), ('NÓNG', 4),
('BÓNG', 4), ('ĐỒNG', 4), ('HỒNG', 4), ('NGANG', 4),
-- 5 chữ
('NHÀNG', 5), ('CHỒNG', 5);

-- ========================================
-- VIEWS
-- ========================================

-- Bảng xếp hạng theo điểm
CREATE OR REPLACE VIEW leaderboard_by_points AS
SELECT 
    user_id,
    username,
    full_name,
    total_points,
    total_wins,
    total_draws,
    total_losses,
    (total_wins + total_draws + total_losses) as total_matches
FROM users
ORDER BY total_points DESC, total_wins DESC
LIMIT 100;

-- Bảng xếp hạng theo số trận thắng
CREATE OR REPLACE VIEW leaderboard_by_wins AS
SELECT 
    user_id,
    username,
    full_name,
    total_wins,
    total_points,
    total_draws,
    total_losses,
    (total_wins + total_draws + total_losses) as total_matches
FROM users
ORDER BY total_wins DESC, total_points DESC
LIMIT 100;

-- Danh sách người chơi online
CREATE OR REPLACE VIEW online_players AS
SELECT 
    user_id,
    username,
    full_name,
    status,
    total_points,
    total_wins,
    total_draws,
    total_losses
FROM users
WHERE status IN ('online', 'playing')
ORDER BY total_points DESC;

-- Lịch sử trận đấu với thông tin chi tiết
CREATE OR REPLACE VIEW match_history AS
SELECT 
    m.match_id,
    m.player1_id,
    u1.username as player1_name,
    m.player2_id,
    u2.username as player2_name,
    m.total_rounds,
    m.player1_rounds_won,
    m.player2_rounds_won,
    m.winner_id,
    CASE 
        WHEN m.winner_id = m.player1_id THEN u1.username
        WHEN m.winner_id = m.player2_id THEN u2.username
        ELSE 'Hòa'
    END as winner_name,
    m.result,
    m.started_at,
    m.ended_at,
    TIMESTAMPDIFF(MINUTE, m.started_at, m.ended_at) as duration_minutes
FROM matches m
JOIN users u1 ON m.player1_id = u1.user_id
JOIN users u2 ON m.player2_id = u2.user_id
WHERE m.match_status = 'completed'
ORDER BY m.ended_at DESC;

-- ========================================
-- STORED PROCEDURES
-- ========================================

-- 1. Tạo trận đấu mới
DELIMITER //
CREATE PROCEDURE CreateMatch(
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
END //
DELIMITER ;

-- 2. Tạo vòng đấu mới
DELIMITER //
CREATE PROCEDURE CreateRound(
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
END //
DELIMITER ;

-- 3. Kiểm tra từ hợp lệ
DELIMITER //
CREATE PROCEDURE CheckWord(
    IN p_word VARCHAR(50),
    OUT p_is_valid BOOLEAN
)
BEGIN
    DECLARE v_count INT;
    
    SELECT COUNT(*) INTO v_count
    FROM dictionary
    WHERE word = UPPER(TRIM(p_word));
    
    SET p_is_valid = (v_count > 0);
END //
DELIMITER ;

-- 4. Kết thúc vòng đấu
DELIMITER //
CREATE PROCEDURE FinishRound(
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
END //
DELIMITER ;

-- 5. Kết thúc trận đấu
DELIMITER //
CREATE PROCEDURE FinishMatch(
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
END //
DELIMITER ;

-- ========================================
-- HƯỚNG DẪN SỬ DỤNG
-- ========================================
/*
=== LUỒNG CHƠI GAME ===

1. TẠO TRẬN ĐẤU:
CALL CreateMatch(1, 2, @match_id);
→ match_id = 1

2. TẠO VÒNG 1:
CALL CreateRound(1, 1, 'Á,B,Đ,G,N,Ó', 4, @detail_id);
→ detail_id = 1

3. NGƯỜI CHƠI GỬI TỪ:
-- Kiểm tra từ
CALL CheckWord('BÁNH', @is_valid);

-- Lưu từ
INSERT INTO match_words (match_id, detail_id, user_id, word, is_valid)
VALUES (1, 1, 1, 'BÁNH', @is_valid);

4. KẾT THÚC VÒNG 1:
CALL FinishRound(1);
→ Tự động đếm từ, xác định người thắng vòng 1

5. TẠO VÒNG 2, 3 (nếu cần):
CALL CreateRound(1, 2, 'C,H,Ó,Ồ,M,P', 5, @detail_id2);
... (lặp lại bước 3-4)

6. KẾT THÚC TRẬN:
CALL FinishMatch(1);
→ Tự động xác định người thắng, cộng điểm

7. XEM KẾT QUẢ:
-- Lịch sử trận đấu
SELECT * FROM match_history WHERE match_id = 1;

-- Chi tiết từng vòng
SELECT * FROM match_details WHERE match_id = 1;

-- Từ đã ghép
SELECT u.username, mw.word, mw.is_valid
FROM match_words mw
JOIN users u ON mw.user_id = u.user_id
WHERE mw.match_id = 1 AND mw.detail_id = 1
ORDER BY mw.submitted_at;

8. BẢNG XẾP HẠNG:
SELECT * FROM leaderboard_by_points;
SELECT * FROM leaderboard_by_wins;
*/
