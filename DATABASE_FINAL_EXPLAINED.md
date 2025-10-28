# 🎮 DATABASE FINAL - THIẾT KẾ TỐI ƯU

## 🌟 Ý TƯỞNG CỦA BẠN RẤT HAY!

Tách `matches` (tổng quan) và `match_details` (chi tiết vòng) là **CHUẨN DATABASE DESIGN**!

---

## 📊 CẤU TRÚC 5 BẢNG

```
1. users           → Tài khoản, thống kê
2. dictionary      → Từ điển
3. matches         → TỔNG QUAN trận đấu
4. match_details   → CHI TIẾT từng vòng
5. match_words     → Từ đã ghép
```

---

## 🔍 SO SÁNH THIẾT KẾ

### ❌ THIẾT KẾ CŨ (1 bảng):

```sql
matches {
    match_id: 1
    player1_id: 1
    player2_id: 2
    round1_letters: "Á,B,Đ,G,N"
    round1_word_length: 4
    round1_player1_words: 5
    round1_player2_words: 3
    round1_winner_id: 1
    round2_letters: "C,H,Ó"
    round2_word_length: 3
    round2_player1_words: 4
    round2_player2_words: 4
    round2_winner_id: NULL
    round3_letters: ...
    round3_...
}
```

**Vấn đề:**
- ❌ Lặp lại cột (round1_, round2_, round3_)
- ❌ Mỗi trận có 15-20 cột!
- ❌ Query phức tạp: `SELECT round1_letters, round2_letters, round3_letters...`
- ❌ Không mở rộng được (muốn 5 vòng phải thêm 10 cột nữa!)

---

### ✅ THIẾT KẾ MỚI (2 bảng):

#### Bảng MATCHES (Tổng quan):
```sql
matches {
    match_id: 1
    player1_id: 1
    player2_id: 2
    total_rounds: 3
    player1_rounds_won: 2
    player2_rounds_won: 1
    winner_id: 1
    result: 'player1_win'
}
```

#### Bảng MATCH_DETAILS (Chi tiết):
```sql
-- Vòng 1
match_details {
    detail_id: 1
    match_id: 1
    round_number: 1
    letters: "Á,B,Đ,G,N"
    word_length: 4
    player1_words_count: 5
    player2_words_count: 3
    winner_id: 1
}

-- Vòng 2
match_details {
    detail_id: 2
    match_id: 1
    round_number: 2
    letters: "C,H,Ó,Ồ"
    word_length: 4
    player1_words_count: 4
    player2_words_count: 4
    winner_id: NULL
}

-- Vòng 3
match_details {
    detail_id: 3
    match_id: 1
    round_number: 3
    ...
}
```

**Ưu điểm:**
- ✅ Chuẩn database normalization
- ✅ Mỗi bảng tập trung 1 mục đích rõ ràng
- ✅ Query đơn giản: `SELECT * FROM match_details WHERE match_id = 1`
- ✅ Dễ mở rộng: muốn 5 vòng chỉ cần INSERT thêm record!
- ✅ Code DAO sạch hơn

---

## 🎯 GIẢI THÍCH TỪNG BẢNG

### 3️⃣ BẢNG **MATCHES** - Tổng quan trận đấu

**Trách nhiệm:**
- Lưu thông tin **TỔNG QUAN**: Ai đấu với ai?
- Lưu kết quả **CHUNG**: Ai thắng? Kết quả gì?
- **KHÔNG** lưu chi tiết từng vòng

**Các cột:**
```sql
match_id              → ID trận đấu
player1_id            → Người chơi 1
player2_id            → Người chơi 2
match_status          → 'playing', 'completed', 'cancelled'

-- KẾT QUẢ TỔNG
total_rounds          → Đã chơi mấy vòng (1, 2, hoặc 3)
player1_rounds_won    → Player 1 thắng mấy vòng
player2_rounds_won    → Player 2 thắng mấy vòng

winner_id             → Người thắng trận
result                → 'player1_win', 'player2_win', 'draw'

started_at, ended_at  → Thời gian
```

**Ví dụ:**
```sql
-- Trận 1: Player 1 thắng 2-1
{
    match_id: 1,
    player1_id: 1,
    player2_id: 2,
    total_rounds: 3,
    player1_rounds_won: 2,   ← Thắng 2 vòng
    player2_rounds_won: 1,   ← Thắng 1 vòng
    winner_id: 1,            ← Player 1 thắng
    result: 'player1_win'
}
```

**Khi nào dùng:**
- ✅ "Lấy tất cả trận đấu của user X"
- ✅ "Lấy lịch sử 10 trận gần nhất"
- ✅ "Ai thắng ai trong trận này?"

---

### 4️⃣ BẢNG **MATCH_DETAILS** - Chi tiết vòng đấu

**Trách nhiệm:**
- Lưu thông tin **CHI TIẾT** từng vòng
- 1 trận có 1-3 records trong bảng này

**Các cột:**
```sql
detail_id             → ID chi tiết
match_id              → Thuộc trận nào
round_number          → Vòng 1, 2, hay 3

-- CÀI ĐẶT VÒNG
letters               → Chữ cái cho vòng này
word_length           → Số chữ yêu cầu
duration_seconds      → Thời gian (60s)

-- KẾT QUẢ VÒNG
player1_words_count   → Số từ đúng của player 1
player2_words_count   → Số từ đúng của player 2
winner_id             → Người thắng vòng này

round_status          → 'waiting', 'playing', 'completed'
started_at, ended_at  → Thời gian
```

**Ví dụ:**
```sql
-- Vòng 1 của trận 1
{
    detail_id: 1,
    match_id: 1,
    round_number: 1,
    letters: "Á,B,Đ,G,N,Ó",
    word_length: 4,
    player1_words_count: 5,  ← Player 1 ghép đúng 5 từ
    player2_words_count: 3,  ← Player 2 ghép đúng 3 từ
    winner_id: 1             ← Player 1 thắng vòng 1
}

-- Vòng 2 của trận 1
{
    detail_id: 2,
    match_id: 1,
    round_number: 2,
    letters: "C,H,Ó,Ồ,M",
    word_length: 5,
    player1_words_count: 4,  ← Hòa
    player2_words_count: 4,  ← Hòa
    winner_id: NULL          ← Không ai thắng
}
```

**Khi nào dùng:**
- ✅ "Lấy chi tiết vòng 1 của trận X"
- ✅ "Chữ cái nào được dùng trong vòng 2?"
- ✅ "Ai thắng vòng 3?"

---

## 🔗 QUAN HỆ GIỮA 2 BẢNG

```
matches (1) ←→ (1-3) match_details

VÍ DỤ:
Match 1 → 3 records trong match_details (3 vòng)
Match 2 → 2 records trong match_details (2 vòng, thắng liên tiếp)
Match 3 → 1 record trong match_details (1 người thoát sớm)
```

---

## 💻 CODE EXAMPLES

### ✅ Query đơn giản với thiết kế mới:

#### 1. Lấy thông tin trận đấu:
```sql
-- Chỉ cần 1 query
SELECT * FROM matches WHERE match_id = 1;
```

#### 2. Lấy chi tiết từng vòng:
```sql
-- Lấy tất cả vòng
SELECT * FROM match_details 
WHERE match_id = 1 
ORDER BY round_number;

-- Lấy chỉ vòng 1
SELECT * FROM match_details 
WHERE match_id = 1 AND round_number = 1;
```

#### 3. Lấy FULL thông tin (JOIN):
```sql
SELECT 
    m.match_id,
    m.winner_id,
    md.round_number,
    md.letters,
    md.winner_id as round_winner
FROM matches m
LEFT JOIN match_details md ON m.match_id = md.match_id
WHERE m.match_id = 1
ORDER BY md.round_number;
```

---

### ❌ Query phức tạp với thiết kế cũ:

```sql
-- Phải SELECT nhiều cột lặp lại
SELECT 
    match_id,
    round1_letters, round1_word_length, round1_winner_id,
    round2_letters, round2_word_length, round2_winner_id,
    round3_letters, round3_word_length, round3_winner_id
FROM matches
WHERE match_id = 1;

-- Muốn lấy chỉ vòng 1? Phải SELECT hết rồi filter trong code!
-- Không thể WHERE round_number = 1
```

---

## 🎮 LUỒNG CHƠI GAME

### 1️⃣ Tạo trận đấu:
```sql
CALL CreateMatch(1, 2, @match_id);
-- Kết quả: match_id = 1
```

### 2️⃣ Tạo vòng 1:
```sql
CALL CreateRound(1, 1, 'Á,B,Đ,G,N,Ó', 4, @detail_id);
-- Kết quả: detail_id = 1
```

### 3️⃣ Người chơi ghép từ:
```sql
-- Player 1 gửi từ "BÁNH"
CALL CheckWord('BÁNH', @is_valid);
-- @is_valid = TRUE

INSERT INTO match_words (match_id, detail_id, user_id, word, is_valid)
VALUES (1, 1, 1, 'BÁNH', TRUE);
```

### 4️⃣ Kết thúc vòng 1:
```sql
CALL FinishRound(1);
-- Tự động:
-- - Đếm số từ đúng của mỗi người
-- - Cập nhật player1_words_count, player2_words_count
-- - Xác định winner_id của vòng
-- - Cập nhật player1_rounds_won hoặc player2_rounds_won trong matches
```

### 5️⃣ Kiểm tra điều kiện kết thúc:
```java
// Trong code Java
if (player1RoundsWon == 2 || player2RoundsWon == 2) {
    // Ai thắng 2 vòng → Kết thúc luôn
    finishMatch(matchId);
} else if (totalRounds == 3) {
    // Đã chơi đủ 3 vòng → Kết thúc
    finishMatch(matchId);
} else {
    // Chơi tiếp vòng mới
    createRound(matchId, nextRoundNumber);
}
```

### 6️⃣ Kết thúc trận:
```sql
CALL FinishMatch(1);
-- Tự động:
-- - So sánh player1_rounds_won vs player2_rounds_won
-- - Xác định winner_id
-- - Cập nhật total_wins, total_points trong users
-- - Đổi status về 'online'
```

---

## 📈 TRUY VẤN THƯỜNG DÙNG

### 1. Lấy lịch sử trận của 1 user:
```sql
SELECT * FROM match_history
WHERE player1_id = 1 OR player2_id = 1
ORDER BY ended_at DESC
LIMIT 10;
```

### 2. Lấy chi tiết trận vừa chơi:
```sql
-- Thông tin tổng
SELECT * FROM matches WHERE match_id = 1;

-- Chi tiết từng vòng
SELECT 
    round_number,
    letters,
    word_length,
    player1_words_count,
    player2_words_count,
    CASE 
        WHEN winner_id = (SELECT player1_id FROM matches WHERE match_id = 1)
        THEN 'Player 1 thắng'
        WHEN winner_id = (SELECT player2_id FROM matches WHERE match_id = 1)
        THEN 'Player 2 thắng'
        ELSE 'Hòa'
    END as result
FROM match_details
WHERE match_id = 1
ORDER BY round_number;
```

### 3. Xem từ đã ghép trong vòng 1:
```sql
SELECT 
    u.username,
    mw.word,
    CASE WHEN mw.is_valid THEN '✓ Đúng' ELSE '✗ Sai' END as status
FROM match_words mw
JOIN users u ON mw.user_id = u.user_id
WHERE mw.match_id = 1 AND mw.detail_id = 1
ORDER BY mw.submitted_at;
```

---

## 🏆 TẠI SAO THIẾT KẾ NÀY TỐT?

### 1. **Chuẩn Database Normalization**
- ✅ Mỗi bảng 1 trách nhiệm rõ ràng
- ✅ Không lặp lại dữ liệu
- ✅ Dễ maintain

### 2. **Dễ Query**
- ✅ Muốn thông tin tổng → Query `matches`
- ✅ Muốn chi tiết vòng → Query `match_details`
- ✅ Muốn cả 2 → JOIN

### 3. **Dễ Mở Rộng**
- ✅ Muốn 5 vòng? Chỉ cần INSERT thêm vào `match_details`
- ✅ Muốn thêm info vòng (VD: nhiệt độ phòng 😄)? Thêm cột vào `match_details`
- ✅ Không ảnh hưởng bảng `matches`

### 4. **Code DAO Sạch**
```java
// MatchDAO - Xử lý trận đấu tổng quan
class MatchDAO {
    Match getMatch(int matchId);
    void createMatch(int p1, int p2);
    void finishMatch(int matchId);
}

// MatchDetailDAO - Xử lý chi tiết vòng
class MatchDetailDAO {
    MatchDetail getRound(int matchId, int roundNumber);
    List<MatchDetail> getAllRounds(int matchId);
    void createRound(int matchId, int roundNumber, String letters);
    void finishRound(int detailId);
}
```

---

## ✅ KẾT LUẬN

**Ý tưởng của bạn HOÀN TOÀN ĐÚNG!** 🎉

```
matches        → Quản lý trận đấu (tổng quan)
match_details  → Chi tiết 3 vòng
```

Đây là **best practice** trong database design!

**Database cuối cùng: 5 bảng**
1. ✅ users
2. ✅ dictionary
3. ✅ matches (tổng quan)
4. ✅ match_details (chi tiết vòng)
5. ✅ match_words (từ đã ghép)

---

**File SQL đã tạo: `database_final.sql`** ✨
