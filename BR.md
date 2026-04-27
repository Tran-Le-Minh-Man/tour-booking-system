# Business Requirements Document (BRD)
## Game Othello — Player vs AI (PvE) Desktop Application

---

| Thông tin | Chi tiết |
|---|---|
| **Dự án** | Game Othello PvE |
| **Phiên bản tài liệu** | 1.0 |
| **Ngày tạo** | Tháng 4, 2026 |
| **Môn học** | Nhập môn Công nghệ Phần mềm |
| **Trường** | Đại học Nông Lâm TP.HCM |
| **Tác giả** | Trần Lê Minh Mẫn — MSSV: 23130186 |
| **GVHD** | Nguyễn Đức Công Song |

---

## Mục lục

1. [Tổng quan dự án](#1-tổng-quan-dự-án)
2. [Mục tiêu kinh doanh](#2-mục-tiêu-kinh-doanh)
3. [Phạm vi dự án](#3-phạm-vi-dự-án)
4. [Các bên liên quan (Stakeholders & Actors)](#4-các-bên-liên-quan-stakeholders--actors)
5. [Yêu cầu chức năng (Functional Requirements)](#5-yêu-cầu-chức-năng-functional-requirements)
   - 5.1 [Nhóm Khởi Đầu — Onboarding](#51-nhóm-khởi-đầu--onboarding)
   - 5.2 [Nhóm Gameplay](#52-nhóm-gameplay)
   - 5.3 [Nhóm Kết Quả & Điểm Cao](#53-nhóm-kết-quả--điểm-cao)
6. [Yêu cầu phi chức năng (Non-Functional Requirements)](#6-yêu-cầu-phi-chức-năng-non-functional-requirements)
7. [Luồng hoạt động tổng thể (High-Level Scenario)](#7-luồng-hoạt-động-tổng-thể-high-level-scenario)
8. [User Stories](#8-user-stories)
9. [Ràng buộc & Giả định](#9-ràng-buộc--giả-định)
10. [Đề xuất công nghệ](#10-đề-xuất-công-nghệ)
11. [Kế hoạch triển khai](#11-kế-hoạch-triển-khai)
12. [Tiêu chí chấp nhận MVP](#12-tiêu-chí-chấp-nhận-mvp)

---

## 1. Tổng quan dự án

### 1.1 Mô tả

Dự án xây dựng một ứng dụng desktop cho phép người dùng chơi trò chơi cờ lật **Othello** (còn gọi là Reversi) theo chế độ **một người chơi đấu với máy tính (Player vs AI — PvE)**.

Người chơi không cần tạo tài khoản hay đăng nhập. Trước mỗi ván đấu, người chơi chỉ cần nhập tên hiển thị. Điểm cao được lưu trữ cục bộ trên thiết bị để tạo động lực cải thiện kỹ năng theo thời gian.

### 1.2 Bối cảnh

Người dùng mục tiêu là sinh viên và người yêu thích trò chơi trí tuệ, thường không có bạn chơi cùng và muốn một ứng dụng đơn giản, không cần đăng ký, có thể mở ra và chơi ngay.

> *"Tôi thích chơi các trò chơi trí tuệ nhưng thường không có bạn chơi cùng. Tôi muốn có một ứng dụng Othello mà tôi có thể mở ra và chơi ngay với máy tính mà không cần phải đăng ký hay đăng nhập."*
> — Người dùng đại diện (sinh viên đại học)

---

## 2. Mục tiêu kinh doanh

| Mục tiêu | Mô tả |
|---|---|
| **BG-01** | Cung cấp một ứng dụng Othello PvE hoàn chỉnh, không yêu cầu tài khoản người dùng. |
| **BG-02** | Mang lại trải nghiệm chơi game mượt mà, trực quan, phù hợp với người mới và người có kinh nghiệm. |
| **BG-03** | Tạo động lực chơi lại thông qua bảng xếp hạng điểm cao lưu trữ cục bộ. |
| **BG-04** | Xây dựng một đối thủ AI đủ thách thức nhưng có thể điều chỉnh độ khó trong tương lai. |
| **BG-05** | Hoàn thiện sản phẩm trong phạm vi một học kỳ với đội nhỏ và không phụ thuộc server/database phức tạp. |

---

## 3. Phạm vi dự án

### 3.1 Trong phạm vi (In Scope)

- Giao diện nhập tên và chọn màu quân trước mỗi ván đấu.
- Bàn cờ Othello 8×8 với logic game đầy đủ theo luật chuẩn.
- Highlight nước đi hợp lệ cho người chơi.
- Hiển thị trực quan quân bị lật sau mỗi nước đi.
- AI đối thủ sử dụng thuật toán Minimax với Alpha-Beta Pruning.
- Xử lý tự động trường hợp bỏ lượt (no valid moves).
- Màn hình kết quả sau mỗi ván đấu (thắng / thua / hòa).
- Lưu trữ điểm cao vào file cục bộ.
- Bảng xếp hạng Top 10 điểm cao.
- Tính năng chơi lại nhanh (Play Again) không cần nhập lại tên.

### 3.2 Ngoài phạm vi (Out of Scope)

- Hệ thống đăng ký / đăng nhập / xác thực tài khoản.
- Chế độ nhiều người chơi (PvP — Player vs Player).
- Tích hợp server hoặc cơ sở dữ liệu từ xa.
- Leaderboard trực tuyến.
- Chức năng quản trị (Admin).
- Ứng dụng di động (mobile).

---

## 4. Các bên liên quan (Stakeholders & Actors)

### 4.1 Stakeholders

| Stakeholder | Vai trò |
|---|---|
| Sinh viên phát triển | Thiết kế, lập trình và kiểm thử toàn bộ hệ thống |
| Giảng viên hướng dẫn | Định hướng kỹ thuật và nghiệm thu sản phẩm |
| Người dùng cuối (sinh viên, người yêu thích game trí tuệ) | Đối tượng sử dụng ứng dụng |

### 4.2 Actors trong hệ thống

| Actor | Loại | Vai trò | Tương tác chính |
|---|---|---|---|
| **Player** | Con người | Người chơi duy nhất, thực hiện các thao tác game | Nhập tên, chọn màu, đặt quân, xem kết quả, xem bảng điểm |
| **AI Opponent** | Hệ thống tự động | Đối thủ máy tính (Minimax) | Tính toán và thực hiện nước đi tự động sau mỗi lượt của Player |
| **System** | Phần mềm | Xử lý logic game, lưu điểm, điều phối luồng | Khởi tạo game, xử lý nước đi, phát hiện kết thúc, lưu high score |

> **Ghi chú:** Không có actor Admin vì hệ thống không quản lý tài khoản người dùng. Mọi ván đấu đều bắt buộc có AI Opponent (không có chế độ PvP).

---

## 5. Yêu cầu chức năng (Functional Requirements)

### 5.1 Nhóm Khởi Đầu — Onboarding

#### FR-01: Nhập tên người chơi

- **Mô tả:** Người chơi phải nhập tên hiển thị trước khi bắt đầu ván đấu để hệ thống có thể nhận diện và lưu điểm dưới tên đó.
- **Điều kiện kích hoạt:** Người chơi mở ứng dụng lần đầu hoặc bắt đầu ván đấu mới từ màn hình chào.
- **Tiêu chí chấp nhận:**
  - Màn hình nhập tên hiển thị khi mở ứng dụng.
  - Người chơi phải nhập ít nhất 1 ký tự mới được tiếp tục.
  - Hệ thống lưu tên cho phiên hiện tại và liên kết với điểm số.

#### FR-02: Chọn màu quân

- **Mô tả:** Người chơi được chọn màu quân (đen hoặc trắng) trước khi ván đấu bắt đầu.
- **Điều kiện kích hoạt:** Sau khi nhập tên, trước khi bắt đầu ván đấu.
- **Tiêu chí chấp nhận:**
  - Hiển thị hai lựa chọn: **Black** và **White**.
  - Quân đen luôn đi trước theo luật Othello.
  - AI tự động được phân công màu còn lại.

---

### 5.2 Nhóm Gameplay

#### FR-03: Khởi tạo ván đấu

- **Mô tả:** Hệ thống tự động thiết lập bàn cờ khi ván đấu bắt đầu.
- **Tiêu chí chấp nhận:**
  - Bàn cờ 8×8 được khởi tạo với 4 quân ở trung tâm (2 đen, 2 trắng theo thế tiêu chuẩn).
  - Hiển thị số quân hiện tại của mỗi bên.
  - Hiển thị thông tin lượt đi (Player/AI).
  - Quân đen đi trước.

#### FR-04: Hiển thị nước đi hợp lệ

- **Mô tả:** Hệ thống tính toán và đánh dấu (highlight) tất cả các ô hợp lệ người chơi có thể đặt quân trong lượt của mình.
- **Tiêu chí chấp nhận:**
  - Tất cả ô hợp lệ được highlight rõ ràng khi bắt đầu lượt của Player.
  - Highlight biến mất sau khi Player đặt quân.
  - Nếu không có nước đi hợp lệ, lượt tự động bị bỏ qua (xem FR-07).

#### FR-05: Đặt quân và hiển thị quân bị lật

- **Mô tả:** Khi Player click vào ô hợp lệ, hệ thống đặt quân và lật tất cả quân đối thủ bị kẹp, cập nhật bàn cờ và điểm số ngay lập tức.
- **Tiêu chí chấp nhận:**
  - Player click ô được highlight → hệ thống đặt quân và lật tất cả quân bị kẹp theo hàng/cột/đường chéo.
  - Bàn cờ và điểm số cập nhật ngay lập tức.
  - Có animation hoặc chỉ báo trực quan cho thấy quân nào vừa bị lật.

#### FR-06: AI thực hiện nước đi

- **Mô tả:** Sau lượt của Player, AI tính toán và thực hiện nước đi của mình, hiển thị rõ vị trí đặt quân.
- **Tiêu chí chấp nhận:**
  - Sau lượt của Player, AI tính toán và đặt quân trong vòng **≤ 2 giây**.
  - Nước đi của AI được highlight rõ ràng trên bàn cờ.
  - Bàn cờ và điểm số cập nhật sau nước đi của AI.
  - Lượt trả về Player sau khi AI đi xong.

#### FR-07: Xử lý tự động khi không có nước đi hợp lệ

- **Mô tả:** Khi một bên không có nước đi hợp lệ, hệ thống tự động bỏ lượt và thông báo cho người chơi.
- **Tiêu chí chấp nhận:**
  - Hệ thống phát hiện không có nước đi hợp lệ cho bên đang đến lượt.
  - Hiển thị thông báo rõ ràng (ví dụ: *"No valid moves — turn skipped"*).
  - Lượt tự động chuyển sang bên còn lại.

---

### 5.3 Nhóm Kết Quả & Điểm Cao

#### FR-08: Hiển thị kết quả ván đấu

- **Mô tả:** Khi ván đấu kết thúc, hệ thống hiển thị tổng số quân mỗi bên và thông báo kết quả thắng/thua/hòa.
- **Điều kiện kết thúc:** Cả hai bên đều không còn nước đi hợp lệ.
- **Tiêu chí chấp nhận:**
  - Hệ thống phát hiện điều kiện kết thúc trận đấu.
  - Hiển thị tổng số quân của mỗi bên.
  - Thông báo: người thắng, người thua, hoặc hòa.
  - Cung cấp tùy chọn **Play Again** và **View High Score**.

#### FR-09: Lưu điểm cao

- **Mô tả:** Điểm số của người chơi (số quân đạt được) được tự động lưu sau mỗi ván đấu và cập nhật nếu vượt kỷ lục cũ.
- **Tiêu chí chấp nhận:**
  - Hệ thống lưu tên người chơi và số quân sau mỗi ván hoàn thành.
  - Nếu điểm mới cao hơn điểm cũ của cùng tên người chơi → cập nhật.
  - Dữ liệu được lưu vào file cục bộ (txt/JSON), tồn tại qua các lần tắt/mở ứng dụng.

#### FR-10: Xem bảng xếp hạng điểm cao

- **Mô tả:** Người chơi có thể xem bảng Top 10 điểm cao từ màn hình kết quả hoặc màn hình chào.
- **Tiêu chí chấp nhận:**
  - Có thể truy cập bảng xếp hạng từ màn hình kết quả hoặc màn hình home.
  - Hiển thị Top 10 điểm với: tên người chơi, điểm số, ngày chơi.
  - Kỷ lục cá nhân của người chơi hiện tại được làm nổi bật.

#### FR-11: Chơi lại không cần nhập lại tên

- **Mô tả:** Sau khi ván đấu kết thúc, người chơi có thể bắt đầu ván mới ngay lập tức mà không cần nhập lại tên.
- **Tiêu chí chấp nhận:**
  - Màn hình kết quả có nút **Play Again**.
  - Click Play Again → reset bàn cờ, bắt đầu ván mới, giữ nguyên tên người chơi.
  - Người chơi có thể chọn màu quân mới trước khi bắt đầu lại (tùy chọn).

---

## 6. Yêu cầu phi chức năng (Non-Functional Requirements)

| ID | Danh mục | Mô tả | Mức ưu tiên |
|---|---|---|---|
| **NFR-01** | Hiệu năng | AI phản hồi và đặt quân trong vòng **≤ 2 giây**; bàn cờ cập nhật tức thì sau mỗi nước đi (**< 200ms**). | High |
| **NFR-02** | Tính khả dụng | Ứng dụng chạy trên máy tính cài Java (JRE), **không cần kết nối Internet**. | High |
| **NFR-03** | Lưu trữ cục bộ | Điểm cao được lưu vào file cục bộ (txt/JSON), **không mất dữ liệu** khi tắt ứng dụng. | High |
| **NFR-04** | Độ chính xác logic | Logic game phải **chính xác 100%** theo luật Othello tiêu chuẩn (lật quân, kiểm tra nước đi hợp lệ, điều kiện kết thúc). | High |
| **NFR-05** | Tính sử dụng | Giao diện rõ ràng, trực quan; nước đi hợp lệ được highlight dễ nhận biết; thông báo bỏ lượt hiển thị rõ ràng. | Medium |
| **NFR-06** | Khả năng mở rộng | Kiến trúc code cho phép tích hợp thêm chế độ độ khó AI hoặc chế độ PvP trong tương lai mà không cần refactor lớn. | Low |

---

## 7. Luồng hoạt động tổng thể (High-Level Scenario)

Dưới đây là toàn bộ luồng hoạt động từ khi người chơi mở ứng dụng đến khi kết thúc phiên chơi:

| # | Actor | Bước | Mô tả |
|---|---|---|---|
| 1 | Player | Mở ứng dụng | Player truy cập ứng dụng, thấy màn hình chào. |
| 2 | Player | Nhập tên và chọn màu quân | Player nhập tên hiển thị và chọn màu quân (đen/trắng). |
| 3 | System | Khởi tạo trận đấu | System thiết lập bàn cờ 8×8, đặt 4 quân ban đầu, xác định lượt đi đầu tiên (đen đi trước). |
| 4 | System | Hiển thị nước đi hợp lệ | System tính toán và highlight các ô hợp lệ của Player. |
| 5 | Player | Thực hiện nước đi | Player chọn một ô hợp lệ để đặt quân. |
| 6 | System | Xử lý nước đi của Player | System lật quân bị kẹp, cập nhật bàn cờ và điểm số. |
| 7 | System | Kiểm tra lượt bị bỏ qua | System kiểm tra AI hoặc Player có nước đi không; nếu không thì bỏ qua và thông báo. |
| 8 | AI | Tính toán và thực hiện nước đi | AI dùng Minimax + Alpha-Beta Pruning tính nước tốt nhất và đặt quân. |
| 9 | System | Xử lý nước đi của AI | System lật quân, cập nhật bàn cờ, chuyển lượt về Player. |
| 10 | System | Phát hiện kết thúc trận | System phát hiện không còn nước đi hợp lệ cho cả hai bên. |
| 11 | System | Hiển thị kết quả | System đếm quân, công bố thắng/thua/hòa. |
| 12 | System | Lưu điểm cao | System kiểm tra và lưu điểm nếu Player đạt kỷ lục mới. |
| 13 | Player | Xem bảng điểm / Chơi lại | Player xem bảng xếp hạng hoặc bắt đầu ván mới. |

---

## 8. User Stories

### US-01 — Enter Name Before Playing

| Trường | Nội dung |
|---|---|
| **ID** | US-01 |
| **User Story** | As a player, I want to enter my display name before starting a game, so that the system can identify me and save my score under my name. |
| **Actor** | Player, System |
| **Priority** | High |
| **FR liên quan** | FR-01 |
| **Acceptance Criteria** | Player sees a name input screen when opening the app. Player must enter at least 1 character to proceed. System stores the name for the current session and links it to the score. |

### US-02 — Choose Piece Color

| Trường | Nội dung |
|---|---|
| **ID** | US-02 |
| **User Story** | As a player, I want to choose whether I play as black or white before the game starts, so that I can play with my preferred color. |
| **Actor** | Player, System |
| **Priority** | Medium |
| **FR liên quan** | FR-02 |
| **Acceptance Criteria** | Player sees a color selection option (Black / White) on the setup screen. Black always moves first per Othello rules. AI is automatically assigned the opposite color. |

### US-03 — Start a New Game

| Trường | Nội dung |
|---|---|
| **ID** | US-03 |
| **User Story** | As a player, I want the game board to be automatically set up when a match starts, so that I can begin playing immediately without manual configuration. |
| **Actor** | Player, System |
| **Priority** | High |
| **FR liên quan** | FR-03 |
| **Acceptance Criteria** | System initializes 8×8 board with 4 pieces at center (2 black, 2 white). System displays the board clearly with piece counts for both sides. System shows whose turn it is. Black moves first. |

### US-04 — View Valid Moves

| Trường | Nội dung |
|---|---|
| **ID** | US-04 |
| **User Story** | As a player, I want to see all valid cells I can place my piece on during my turn, so that I can make decisions without having to memorize all the rules. |
| **Actor** | Player, System |
| **Priority** | High |
| **FR liên quan** | FR-04 |
| **Acceptance Criteria** | System highlights all valid cells at the start of the player's turn. Highlights disappear once the player places a piece. If there are no valid moves, the turn is skipped automatically. |

### US-05 — Place a Piece and See Flipped Pieces

| Trường | Nội dung |
|---|---|
| **ID** | US-05 |
| **User Story** | As a player, I want to place my piece on a valid cell and immediately see which opponent pieces get flipped, so that I can understand the result of my move in real time. |
| **Actor** | Player, System |
| **Priority** | High |
| **FR liên quan** | FR-05 |
| **Acceptance Criteria** | Player clicks a highlighted cell → System places the piece and flips all sandwiched opponent pieces. Board and score update immediately. An animation or visual indicator shows which pieces were flipped. |

### US-06 — Watch AI Make Its Move

| Trường | Nội dung |
|---|---|
| **ID** | US-06 |
| **User Story** | As a player, I want to see where the AI places its piece after my turn, so that I can follow the game and understand the AI's strategy. |
| **Actor** | AI Opponent, System |
| **Priority** | High |
| **FR liên quan** | FR-06 |
| **Acceptance Criteria** | After player's turn, AI calculates and places a piece within 2 seconds. The AI's move is visually highlighted on the board. Board and score update after the AI move. Turn returns to the player. |

### US-07 — Handle Skipped Turns Automatically

| Trường | Nội dung |
|---|---|
| **ID** | US-07 |
| **User Story** | As a player, I want the system to automatically skip a turn when no valid moves exist for either side, so that the game continues without getting stuck. |
| **Actor** | System |
| **Priority** | Medium |
| **FR liên quan** | FR-07 |
| **Acceptance Criteria** | System detects no valid moves for the current side. System displays a clear notification (e.g., "No valid moves — turn skipped"). Turn passes to the other side automatically. |

### US-08 — View Game Result

| Trường | Nội dung |
|---|---|
| **ID** | US-08 |
| **User Story** | As a player, I want to see the final piece count and winner announcement when the game ends, so that I know clearly whether I won, lost, or drew. |
| **Actor** | Player, System |
| **Priority** | High |
| **FR liên quan** | FR-08 |
| **Acceptance Criteria** | System detects end-of-game (no valid moves for both sides). System displays total piece count for each side. System announces winner, loser, or draw. Options to play again or view high score are shown. |

### US-09 — Save High Score

| Trường | Nội dung |
|---|---|
| **ID** | US-09 |
| **User Story** | As a player, I want my score to be saved automatically after each game, so that I can track my personal best without needing an account. |
| **Actor** | Player, System |
| **Priority** | High |
| **FR liên quan** | FR-09 |
| **Acceptance Criteria** | System saves player name and piece count after each completed game. If the new score is higher than the player's previous best, it is updated. Score is persisted in local storage so it remains after app restart. |

### US-10 — View High Score Leaderboard

| Trường | Nội dung |
|---|---|
| **ID** | US-10 |
| **User Story** | As a player, I want to view a leaderboard of top scores, so that I am motivated to improve and beat my own or others' records. |
| **Actor** | Player, System |
| **Priority** | Medium |
| **FR liên quan** | FR-10 |
| **Acceptance Criteria** | Player can navigate to a high score screen from the result screen or home screen. System displays top 10 scores with player name, score, and date. Current player's personal best is highlighted. |

### US-11 — Start a New Game Without Re-entering Name

| Trường | Nội dung |
|---|---|
| **ID** | US-11 |
| **User Story** | As a player, I want to start a new game immediately after a match ends without re-entering my name, so that I can keep playing without interruption. |
| **Actor** | Player, System |
| **Priority** | Medium |
| **FR liên quan** | FR-11 |
| **Acceptance Criteria** | Result screen shows a "Play Again" button. Clicking it resets the board and starts a new game keeping the same player name. Player may optionally choose a new color before restarting. |

---

## 9. Ràng buộc & Giả định

### 9.1 Ràng buộc

| Ràng buộc | Mô tả |
|---|---|
| Không có backend server | Toàn bộ dữ liệu được xử lý và lưu trữ cục bộ trên máy người dùng. |
| Không có xác thực người dùng | Danh tính người chơi chỉ là tên hiển thị nhập trước ván đấu, không có cơ chế xác minh. |
| Chỉ hỗ trợ chế độ PvE | Không có chức năng nhiều người chơi (PvP). |
| Môi trường Java | Ứng dụng được xây dựng bằng Java, yêu cầu máy cài JRE để chạy. |
| Phạm vi học kỳ | Dự án phải hoàn thành trong một học kỳ với đội một người. |

### 9.2 Giả định

- Người dùng hiểu cơ bản cách sử dụng ứng dụng desktop.
- Máy tính của người dùng đã cài Java Runtime Environment (JRE).
- Người dùng chấp nhận bảng điểm cao chỉ lưu trên thiết bị cục bộ, không đồng bộ giữa các máy.
- Một ô trên bàn cờ chỉ có thể chứa một quân duy nhất.
- Logic game tuân thủ hoàn toàn theo luật Othello/Reversi tiêu chuẩn quốc tế.

---

## 10. Đề xuất công nghệ

| Tầng | Công nghệ | Lý do |
|---|---|---|
| **Frontend / UI** | Java Swing | Xây dựng giao diện desktop, xử lý sự kiện click, phù hợp cho game bàn cờ. |
| **Game Logic** | Java (OOP) | Tổ chức code theo lớp (Board, Game, Piece) rõ ràng, quản lý trạng thái dễ kiểm thử. |
| **AI Engine** | Minimax + Alpha-Beta Pruning (Java) | Thuật toán đủ mạnh để tạo thử thách, có thể điều chỉnh độ sâu tìm kiếm. |
| **Lưu trữ điểm cao** | File cục bộ (txt / JSON) | Đơn giản, không cần database, dễ triển khai và đọc/ghi. |
| **Version Control** | GitHub | Quản lý phiên bản code. |
| **Deployment** | File `.jar` (Java Archive) | Đóng gói ứng dụng, chạy độc lập trên mọi máy có Java. |

---

## 11. Kế hoạch triển khai

Dựa trên 11 User Stories, dự án được chia thành 3 giai đoạn:

### Giai đoạn 1 — Xây dựng nền tảng game

> *Mục tiêu: Có bàn cờ chạy được với logic đặt quân cơ bản.*

- Giao diện nhập tên và chọn màu quân (**US-01, US-02**)
- Xây dựng bàn cờ 8×8 bằng Swing (JFrame, JPanel, GridLayout)
- Logic khởi tạo ván đấu (**US-03**)
- Logic kiểm tra nước đi hợp lệ và highlight (**US-04**)
- Logic đặt quân và lật quân (**US-05**)

### Giai đoạn 2 — Tích hợp AI & hoàn thiện gameplay

> *Mục tiêu: Ván đấu hoàn chỉnh từ đầu đến cuối.*

- Tích hợp AI Minimax + Alpha-Beta (**US-06**)
- Xử lý trường hợp bỏ lượt tự động (**US-07**)
- Xác định và hiển thị kết quả ván đấu (**US-08**)
- Lưu điểm cao vào file cục bộ (**US-09**)

### Giai đoạn 3 — Hoàn thiện & tối ưu

> *Mục tiêu: Trải nghiệm người dùng hoàn chỉnh.*

- Hiển thị bảng xếp hạng từ file (**US-10**)
- Tính năng chơi lại nhanh (**US-11**)
- Cải thiện UI: animation lật quân, thông báo lượt chơi
- Kiểm thử các trường hợp đặc biệt (edge cases):
  - Cả hai bên không còn nước đi
  - Bàn cờ đầy hoàn toàn
  - AI không có nước đi hợp lệ
  - Player nhập tên trùng với kỷ lục cũ
- Viết tài liệu hướng dẫn cài đặt và sử dụng

---

## 12. Tiêu chí chấp nhận MVP

MVP (Minimum Viable Product) của hệ thống bao gồm các user stories từ **US-01 đến US-08**, đảm bảo:

| Tiêu chí | US liên quan | Trạng thái |
|---|---|---|
| Người chơi có thể nhập tên và bắt đầu ván đấu | US-01, US-02 | MVP |
| Bàn cờ khởi tạo đúng theo luật Othello | US-03 | MVP |
| Nước đi hợp lệ được highlight rõ ràng | US-04 | MVP |
| Quân lật hiển thị trực quan sau mỗi nước đi | US-05 | MVP |
| AI thực hiện nước đi trong ≤ 2 giây | US-06 | MVP |
| Hệ thống xử lý bỏ lượt tự động và thông báo | US-07 | MVP |
| Kết quả ván đấu hiển thị rõ ràng | US-08 | MVP |
| Lưu điểm cao cục bộ | US-09 | Extended |
| Bảng xếp hạng Top 10 | US-10 | Extended |
| Chơi lại không cần nhập tên | US-11 | Extended |

> **MVP** = bắt buộc có để sản phẩm hoàn chỉnh và demo được.
> **Extended** = tính năng mở rộng, nâng cao trải nghiệm người dùng.

---

*Tài liệu này được soạn thảo dựa trên đề xuất đề tài môn Nhập môn Công nghệ Phần mềm — Trường Đại học Nông Lâm TP.HCM, tháng 4/2026.*
