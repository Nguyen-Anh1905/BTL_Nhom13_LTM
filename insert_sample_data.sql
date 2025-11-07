-- ========================================
-- SCRIPT THÊM DỮ LIỆU MẪU
-- DATABASE: gamevtv
-- ========================================

USE gamevtv;

-- ========================================
-- 1. THÊM USERS (20 người chơi)
-- ========================================

INSERT INTO users (username, password, full_name, status, total_points, total_wins, total_draws, total_losses) VALUES
-- Admin và test users
('admin', '123456', 'Quản Trị Viên', 'offline', 100, 10, 5, 5),
('test1', '123456', 'Người Test 1', 'online', 85, 8, 4, 3),
('test2', '123456', 'Người Test 2', 'online', 75, 7, 3, 5),

-- Người chơi giỏi (top players)
('pro_player1', '123456', 'Nguyễn Văn Thắng', 'online', 150, 15, 5, 2),
('pro_player2', '123456', 'Trần Thị Huyền', 'online', 140, 14, 6, 3),
('pro_player3', '123456', 'Lê Minh Tuấn', 'offline', 130, 13, 4, 4),

-- Người chơi trung bình
('player_a', '123456', 'Phạm Văn An', 'online', 95, 9, 5, 6),
('player_b', '123456', 'Hoàng Thị Bích', 'offline', 88, 8, 6, 6),
('player_c', '123456', 'Vũ Văn Cường', 'online', 82, 7, 7, 6),
('player_d', '123456', 'Đỗ Thị Diệu', 'offline', 78, 6, 8, 7),
('player_e', '123456', 'Bùi Văn Em', 'online', 72, 6, 6, 8),

-- Người chơi mới
('newbie1', '123456', 'Ngô Văn Phúc', 'offline', 45, 4, 3, 8),
('newbie2', '123456', 'Trương Thị Giang', 'online', 40, 3, 4, 9),
('newbie3', '123456', 'Lý Văn Hải', 'offline', 35, 3, 2, 10),
('newbie4', '123456', 'Mai Thị Lan', 'online', 30, 2, 4, 9),

-- Người chơi ít hoạt động
('casual1', '123456', 'Võ Văn Khánh', 'offline', 25, 2, 3, 5),
('casual2', '123456', 'Phan Thị Linh', 'offline', 20, 2, 0, 8),
('casual3', '123456', 'Đinh Văn Minh', 'offline', 15, 1, 3, 6),
('casual4', '123456', 'Cao Thị Nga', 'offline', 12, 1, 2, 7),
('casual5', '123456', 'Dương Văn Ơn', 'offline', 10, 1, 1, 8);

-- ========================================
-- 2. THÊM TỪ ĐIỂN (200+ từ tiếng Việt)
-- ========================================

-- TỪ 2 CHỮ CÁI
INSERT INTO dictionary (word, word_length) VALUES
-- Động vật
('BÒ', 2), ('CÁ', 2), ('GÀ', 2), ('HỔ', 2), ('KỲ', 2),
-- Đồ vật
('ÁO', 2), ('BÀ', 2), ('CỜ', 2), ('ĐÁ', 2), ('HỐ', 2),
-- Động từ/Tính từ
('ĂN', 2), ('Ở', 2), ('ỦY', 2);

-- TỪ 3 CHỮ CÁI
INSERT INTO dictionary (word, word_length) VALUES
-- Động vật
('CUA', 3), ('ĐỈA', 3), ('HEO', 3), ('KHỈ', 3), ('LỢN', 3), ('MÈO', 3),
('NGỰ', 3), ('ONG', 3), ('RẮN', 3), ('SÂU', 3), ('TÔM', 3), ('VỊT', 3),
-- Thực phẩm
('BÁN', 3), ('BÚN', 3), ('CƠM', 3), ('GẠO', 3), ('MÌ', 3), ('PHỞ', 3), ('XÔI', 3),
-- Đồ vật
('BÀN', 3), ('BÚT', 3), ('CỬA', 3), ('ĐÈN', 3), ('GHẾ', 3), ('HỘP', 3), ('NỒI', 3), ('XÔ', 3),
-- Thiên nhiên
('CAO', 3), ('GIÓ', 3), ('HOA', 3), ('MÂY', 3), ('NÚI', 3), ('RẬM', 3), ('SÔI', 3),
('TRỜ', 3), ('ĐẤT', 3), ('LÁ', 3), ('CÂY', 3),
-- Động từ/Tính từ
('CÀY', 3), ('ĐÀO', 3), ('GÁC', 3), ('HÁT', 3), ('KÊU', 3), ('LÀM', 3),
('MUA', 3), ('NGỦ', 3), ('ỞI', 3), ('ĐẸP', 3), ('TỐT', 3), ('XẤU', 3), ('TO', 3);

-- TỪ 4 CHỮ CÁI
INSERT INTO dictionary (word, word_length) VALUES
-- Động vật
('BƯƠM', 4), ('CHIM', 4), ('DIỀU', 4), ('GIÁN', 4), ('HẾN', 4), ('KIẾN', 4), ('MUỖI', 4),
('NHỆN', 4), ('OANH', 4), ('RUỒI', 4), ('SÓC', 4), ('THIÊU', 4), ('VẸT', 4),
-- Thực phẩm
('BÁNH', 4), ('BƯỞI', 4), ('CHÁO', 4), ('ĐẬU', 4), ('GIĂM', 4), ('HÀNH', 4), ('KHOAI', 4),
('MĂNG', 4), ('NƯỚC', 4), ('ỚT', 4), ('RƯỢU', 4), ('SỮA', 4), ('THỊT', 4),
-- Đồ vật
('BÀNG', 4), ('CÁNH', 4), ('ĐŨA', 4), ('GƯƠNG', 4), ('KIẾNG', 4), ('LƯỠ', 4),
('MUỖNG', 4), ('ỐNG', 4), ('QUẠT', 4), ('THAU', 4), ('VỎ', 4),
-- Thiên nhiên
('BIỂN', 4), ('CỎNG', 4), ('ĐỒNG', 4), ('GIÔNG', 4), ('HỒNG', 4), ('KHUYA', 4), ('LÚNG', 4),
('MƯA', 4), ('NẮNG', 4), ('ỐC', 4), ('RẠN', 4), ('SÔNG', 4), ('TRỜI', 4),
-- Động từ/Tính từ
('ĐÁNH', 4), ('GÁNH', 4), ('NÓNG', 4), ('LẠNH', 4), ('SẠCH', 4), ('BẨN', 4),
('BÓNG', 4), ('SÁNG', 4), ('TỐI', 4), ('NGANG', 4), ('DỌC', 4), ('THẤP', 4);

-- TỪ 5 CHỮ CÁI
INSERT INTO dictionary (word, word_length) VALUES
-- Động vật
('BƯỚM', 5), ('CHỒN', 5), ('ĐƯỜI', 5), ('GẤU', 5), ('HẢI', 5), ('KÌNH', 5), ('LINH', 5),
('MÈNG', 5), ('NHÔNG', 5), ('OTTER', 5),
-- Thực phẩm
('CHANH', 5), ('DỪA', 5), ('GỪNG', 5), ('HẠNH', 5), 
('NHÃN', 5), ('ỚTHI', 5),
-- Đồ vật
('BÌNH', 5), ('CHÉN', 5), ('ĐĨAG', 5), ('GIANG', 5), ('KHĂN', 5), ('LƯỠI', 5), ('MŨ', 5),
('NHẪN', 5), ('THƯỚC', 5),
-- Thiên nhiên
('CHÔNG', 5), ('KHUNG', 5), ('LŨNG', 5),
('MƯANG', 5),
-- Tính từ/Động từ
('CHỒNG', 5), ('ĐẢNH', 5), ('NHÀNG', 5), ('THÁNG', 5);

-- TỪ 6 CHỮ CÁI
INSERT INTO dictionary (word, word_length) VALUES
('ĐƯỜNG', 6), ('HƯƠNG', 6), ('KHÔNG', 6),
('LƯƠNG', 6), ('MƯỜNG', 6), ('NƯỚNG', 6), ('PHƯƠNG', 6), ('THƯƠNG', 6), ('TRƯỜNG', 6);

-- ========================================
-- 3. THÊM LỊCH SỬ TRẬN ĐẤU MẪU
-- ========================================

-- Trận 1: player1 vs player2 (player1 thắng 2-1)
INSERT INTO matches (player1_id, player2_id, match_status, total_rounds, player1_rounds_won, player2_rounds_won, winner_id, result, started_at, ended_at)
VALUES (1, 2, 'completed', 3, 2, 1, 1, 'player1_win', '2025-11-01 10:00:00', '2025-11-01 10:15:00');

-- Chi tiết vòng đấu trận 1
INSERT INTO match_details (match_id, round_number, letters, word_length, player1_words_count, player2_words_count, winner_id, round_status, started_at, ended_at)
VALUES 
(1, 1, 'B,Á,N,H,C,Ó', 4, 5, 3, 1, 'completed', '2025-11-01 10:00:00', '2025-11-01 10:05:00'),
(1, 2, 'G,I,Ó,M,Ư,A', 5, 3, 4, 2, 'completed', '2025-11-01 10:05:00', '2025-11-01 10:10:00'),
(1, 3, 'C,H,Ồ,N,G,Đ', 5, 6, 4, 1, 'completed', '2025-11-01 10:10:00', '2025-11-01 10:15:00');

-- Từ đã ghép trong trận 1
INSERT INTO match_words (match_id, detail_id, user_id, word, is_valid) VALUES
-- Vòng 1 - Player 1
(1, 1, 1, 'BÁNH', TRUE),
(1, 1, 1, 'CHÁO', TRUE),
(1, 1, 1, 'NÓNG', TRUE),
(1, 1, 1, 'BÓNG', TRUE),
(1, 1, 1, 'CÁNH', TRUE),
-- Vòng 1 - Player 2
(1, 1, 2, 'BÁNH', TRUE),
(1, 1, 2, 'NÓNG', TRUE),
(1, 1, 2, 'BÔNG', TRUE),
-- Vòng 2 - Player 1
(1, 2, 1, 'GIANG', TRUE),
(1, 2, 1, 'MƯA', TRUE),
(1, 2, 1, 'GIÔNG', TRUE),
-- Vòng 2 - Player 2
(1, 2, 2, 'GIANG', TRUE),
(1, 2, 2, 'MƯA', TRUE),
(1, 2, 2, 'GIÔNG', TRUE),
(1, 2, 2, 'ĐÀO', TRUE),
-- Vòng 3 - Player 1
(1, 3, 1, 'CHỒNG', TRUE),
(1, 3, 1, 'ĐỒNG', TRUE),
(1, 3, 1, 'NÓNG', TRUE),
(1, 3, 1, 'GIANG', TRUE),
(1, 3, 1, 'NHÀNG', TRUE),
(1, 3, 1, 'ĐÁNH', TRUE),
-- Vòng 3 - Player 2
(1, 3, 2, 'CHỒNG', TRUE),
(1, 3, 2, 'ĐỒNG', TRUE),
(1, 3, 2, 'NÓNG', TRUE),
(1, 3, 2, 'GIANG', TRUE);

-- Trận 2: player1 vs player3 (Hòa 1-1, chỉ chơi 2 vòng)
INSERT INTO matches (player1_id, player2_id, match_status, total_rounds, player1_rounds_won, player2_rounds_won, winner_id, result, started_at, ended_at)
VALUES (1, 3, 'completed', 2, 1, 1, NULL, 'draw', '2025-11-02 14:00:00', '2025-11-02 14:12:00');

INSERT INTO match_details (match_id, round_number, letters, word_length, player1_words_count, player2_words_count, winner_id, round_status, started_at, ended_at)
VALUES 
(2, 1, 'C,Á,H,O,A,T', 3, 6, 5, 1, 'completed', '2025-11-02 14:00:00', '2025-11-02 14:06:00'),
(2, 2, 'B,I,Ể,N,S,Ô', 4, 4, 5, 3, 'completed', '2025-11-02 14:06:00', '2025-11-02 14:12:00');

-- Trận 3: player2 vs player3 (player3 thắng 2-0)
INSERT INTO matches (player1_id, player2_id, match_status, total_rounds, player1_rounds_won, player2_rounds_won, winner_id, result, started_at, ended_at)
VALUES (2, 3, 'completed', 2, 0, 2, 3, 'player2_win', '2025-11-03 16:00:00', '2025-11-03 16:10:00');

INSERT INTO match_details (match_id, round_number, letters, word_length, player1_words_count, player2_words_count, winner_id, round_status, started_at, ended_at)
VALUES 
(3, 1, 'G,Ạ,O,M,Ì,P', 3, 3, 5, 3, 'completed', '2025-11-03 16:00:00', '2025-11-03 16:05:00'),
(3, 2, 'N,H,À,C,Â,Y', 3, 4, 6, 3, 'completed', '2025-11-03 16:05:00', '2025-11-03 16:10:00');

-- Trận 4: pro_player1 vs pro_player2 (pro_player1 thắng 2-1)
INSERT INTO matches (player1_id, player2_id, match_status, total_rounds, player1_rounds_won, player2_rounds_won, winner_id, result, started_at, ended_at)
VALUES (4, 5, 'completed', 3, 2, 1, 4, 'player1_win', '2025-11-04 09:00:00', '2025-11-04 09:18:00');

INSERT INTO match_details (match_id, round_number, letters, word_length, player1_words_count, player2_words_count, winner_id, round_status, started_at, ended_at)
VALUES 
(4, 1, 'T,R,Ờ,I,Đ,Ấ', 4, 7, 6, 4, 'completed', '2025-11-04 09:00:00', '2025-11-04 09:06:00'),
(4, 2, 'H,Ồ,N,G,B,Ư', 5, 5, 8, 5, 'completed', '2025-11-04 09:06:00', '2025-11-04 09:12:00'),
(4, 3, 'C,H,Ồ,N,G,M', 6, 9, 7, 4, 'completed', '2025-11-04 09:12:00', '2025-11-04 09:18:00');

-- Trận 5: newbie1 vs newbie2 (newbie2 thắng)
INSERT INTO matches (player1_id, player2_id, match_status, total_rounds, player1_rounds_won, player2_rounds_won, winner_id, result, started_at, ended_at)
VALUES (12, 13, 'completed', 2, 0, 2, 13, 'player2_win', '2025-11-05 20:00:00', '2025-11-05 20:08:00');

INSERT INTO match_details (match_id, round_number, letters, word_length, player1_words_count, player2_words_count, winner_id, round_status, started_at, ended_at)
VALUES 
(5, 1, 'B,À,N,C,Ơ,M', 3, 2, 4, 13, 'completed', '2025-11-05 20:00:00', '2025-11-05 20:04:00'),
(5, 2, 'G,À,C,Á,H,O', 3, 3, 5, 13, 'completed', '2025-11-05 20:04:00', '2025-11-05 20:08:00');

-- ========================================
-- 4. CẬP NHẬT THỐNG KÊ (đã tự động qua stored procedure FinishMatch)
-- Nhưng nếu cần điều chỉnh thủ công:
-- ========================================

-- Cập nhật lại total_points, total_wins, total_draws, total_losses
-- (Đã được cập nhật tự động khi INSERT users ở trên)

-- ========================================
-- 5. KIỂM TRA DỮ LIỆU
-- ========================================

-- Xem tất cả users
SELECT user_id, username, full_name, status, total_points, total_wins, total_draws, total_losses 
FROM users 
ORDER BY total_points DESC;

-- Xem số lượng từ trong từ điển
SELECT word_length, COUNT(*) as so_luong_tu
FROM dictionary
GROUP BY word_length
ORDER BY word_length;

-- Xem lịch sử trận đấu
SELECT * FROM match_history;

-- Xem bảng xếp hạng theo điểm
SELECT * FROM leaderboard_by_points LIMIT 10;

-- Xem bảng xếp hạng theo thắng
SELECT * FROM leaderboard_by_wins LIMIT 10;

-- Xem người chơi online
SELECT * FROM online_players;

-- ========================================
-- KẾT THÚC SCRIPT
-- ========================================
