# Business Requirements Document (BRD)
## Game Othello — Player vs AI (PvE) Desktop Application

---

| Thông tin | Chi tiết |
|---|---|
| **Dự án** | Game Othello PvE |
| **Phiên bản tài liệu** | 2.0 |
| **Ngày tạo** | Tháng 4, 2026 |
| **Môn học** | Nhập môn Công nghệ Phần mềm |
| **Trường** | Đại học Nông Lâm TP.HCM |
| **Tác giả** | Trần Lê Minh Mẫn — MSSV: 23130186 |
| **GVHD** | Nguyễn Đức Công Song |

---

## Table of Contents

1. [Project Overview](#1-project-overview)
2. [Business Objectives](#2-business-objectives)
3. [Value Proposition](#3-value-proposition)
4. [Success Metrics & KPI](#4-success-metrics--kpi)
5. [Project Scope](#5-project-scope)
6. [Stakeholders & Actors](#6-stakeholders--actors)
7. [Business Requirements](#7-business-requirements)
8. [Business Rules](#8-business-rules)
9. [Functional Requirements](#9-functional-requirements)
10. [Non-Functional Requirements](#10-non-functional-requirements)
11. [High-Level Scenario](#11-high-level-scenario)
12. [Constraints & Assumptions](#12-constraints--assumptions)
13. [Risks](#13-risks)

---

## 1. Project Overview

### 1.1 Mô tả

Dự án xây dựng một ứng dụng desktop cho phép người dùng chơi trò chơi cờ lật **Othello** (còn gọi là Reversi) theo chế độ **một người chơi đấu với máy tính (Player vs AI — PvE)**.

Người chơi không cần tạo tài khoản hay đăng nhập. Trước mỗi ván đấu, người chơi chỉ cần nhập tên hiển thị. Điểm cao được lưu trữ cục bộ trên thiết bị để tạo động lực cải thiện kỹ năng theo thời gian.

### 1.2 Bối cảnh & Vấn đề cần giải quyết

Sinh viên và người yêu thích trò chơi trí tuệ thường không có bạn chơi cùng, nhưng các giải pháp hiện tại (web game Othello, ứng dụng mobile) đều yêu cầu đăng ký tài khoản hoặc kết nối Internet — tạo ra rào cản không cần thiết cho người muốn chơi nhanh, đơn giản.

> *"Tôi muốn có một ứng dụng Othello mà tôi có thể mở ra và chơi ngay với máy tính mà không cần phải đăng ký hay đăng nhập."*
> — Người dùng đại diện (sinh viên đại học)

---

## 2. Business Objectives

| ID | Mục tiêu kinh doanh | Lý do tồn tại |
|---|---|---|
| **BO-01** | Loại bỏ hoàn toàn rào cản đăng ký/đăng nhập | Người dùng bỏ ứng dụng ngay ở bước xác thực — giảm friction = tăng tỷ lệ sử dụng |
| **BO-02** | Cung cấp đối thủ AI đủ thách thức để thay thế bạn chơi | Người dùng không có bạn chơi cùng nhưng vẫn muốn trải nghiệm cạnh tranh có chiều sâu |
| **BO-03** | Tạo động lực chơi lại thông qua bảng điểm cá nhân | Không có vòng lặp động lực → người dùng chơi một lần rồi bỏ |
| **BO-04** | Ứng dụng hoạt động hoàn toàn offline, không phụ thuộc server | Đảm bảo tính khả dụng 100% bất kể kết nối mạng |

---

## 3. Value Proposition

| Đối tượng | Vấn đề hiện tại | Giá trị mà ứng dụng mang lại |
|---|---|---|
| Sinh viên yêu thích game trí tuệ | Không có bạn chơi, các web game yêu cầu đăng ký | Chơi ngay trong < 30 giây, không cần tài khoản |
| Người mới học Othello | Khó nhớ luật — không biết đặt quân ở đâu | Highlight nước đi hợp lệ ngay trên bàn cờ |
| Người muốn tự cải thiện | Không có cách theo dõi tiến bộ khi không có tài khoản | Bảng điểm cục bộ lưu kỷ lục cá nhân giữa các phiên |

---

## 4. Success Metrics & KPI

| ID | Chỉ số | Mục tiêu | Cách đo |
|---|---|---|---|
| **KPI-01** | Thời gian từ mở ứng dụng đến nước đi đầu tiên | ≤ 30 giây | Đo thủ công khi demo |
| **KPI-02** | Thời gian phản hồi của AI | ≤ 2 giây / nước đi | Đo bằng stopwatch hoặc log thời gian |
| **KPI-03** | Độ chính xác logic game | 0 lỗi trong 20 test case chuẩn theo luật Othello | Kiểm thử bằng bộ test case được thiết kế sẵn |
| **KPI-04** | Độ bền dữ liệu điểm cao | Điểm cao không bị mất sau 10 lần tắt/mở ứng dụng | Kiểm thử thủ công |
| **KPI-05** | Tỷ lệ ván đấu hoàn thành (không crash) | 100% trong 20 ván test liên tiếp | Chạy 20 ván, đếm số ván kết thúc bình thường |

---

## 5. Project Scope

### 5.1 In Scope

- Giao diện nhập tên và chọn màu quân trước mỗi ván đấu.
- Bàn cờ Othello 8×8 với logic game đầy đủ theo luật chuẩn quốc tế.
- Highlight nước đi hợp lệ cho người chơi trong mỗi lượt.
- Hiển thị trực quan quân bị lật sau mỗi nước đi.
- AI đối thủ sử dụng thuật toán tìm kiếm nước đi tự động.
- Xử lý tự động trường hợp bỏ lượt khi không có nước đi hợp lệ.
- Màn hình kết quả sau mỗi ván đấu (thắng / thua / hòa).
- Lưu trữ điểm cao vào file cục bộ, bền vững qua các phiên.
- Bảng xếp hạng Top 10 điểm cao.
- Tính năng chơi lại nhanh không cần nhập lại tên.

### 5.2 Out of Scope

- Hệ thống đăng ký / đăng nhập / xác thực tài khoản.
- Chế độ nhiều người chơi (PvP — Player vs Player).
- Tích hợp server hoặc cơ sở dữ liệu từ xa.
- Leaderboard trực tuyến hoặc đồng bộ giữa các thiết bị.
- Chức năng quản trị (Admin panel).
- Ứng dụng di động (mobile/tablet).
- Tính năng chỉnh độ khó AI (reserved for future version).

---

## 6. Stakeholders & Actors

### 6.1 Stakeholders

| Stakeholder | Vai trò | Kỳ vọng chính |
|---|---|---|
| Sinh viên phát triển | Thiết kế, lập trình, kiểm thử | Hoàn thành trong một học kỳ, đủ điểm môn |
| Giảng viên hướng dẫn | Định hướng, nghiệm thu | Tài liệu đúng chuẩn, sản phẩm demo được đầy đủ flow |
| Người dùng cuối | Sử dụng ứng dụng | Chơi được ngay, không rào cản, AI đủ thách thức |

### 6.2 Actors trong hệ thống

| Actor | Loại | Vai trò | Tương tác chính |
|---|---|---|---|
| **Player** | Con người | Người chơi duy nhất, thực hiện các thao tác game | Nhập tên, chọn màu, đặt quân, xem kết quả, xem bảng điểm |
| **AI Opponent** | Hệ thống tự động | Đối thủ máy tính | Tính toán và thực hiện nước đi tự động sau mỗi lượt của Player |
| **System** | Phần mềm | Xử lý logic game, lưu điểm, điều phối luồng | Khởi tạo game, xử lý nước đi, phát hiện kết thúc, lưu high score |

> **Ghi chú:** Không có actor Admin vì hệ thống không quản lý tài khoản người dùng. Mọi ván đấu đều bắt buộc có AI Opponent.

---

## 7. Business Requirements

Đây là các yêu cầu ở cấp độ nghiệp vụ — mô tả *điều gì cần xảy ra* từ góc nhìn người dùng và mục tiêu kinh doanh, không phụ thuộc vào giải pháp kỹ thuật cụ thể.

| ID | Business Requirement | Liên quan đến BO |
|---|---|---|
| **BR-01** | Người dùng phải có thể bắt đầu một ván đấu hoàn chỉnh mà không cần thực hiện bất kỳ bước đăng ký hay xác thực nào. | BO-01 |
| **BR-02** | Hệ thống phải cung cấp một đối thủ AI có khả năng đưa ra nước đi hợp lệ và có tính toán chiến thuật trong mọi ván đấu. | BO-02 |
| **BR-03** | Mọi kết quả ván đấu phải được lưu lại và có thể tra cứu lại trong các phiên sử dụng tiếp theo mà không cần tài khoản. | BO-03 |
| **BR-04** | Ứng dụng phải hoạt động đầy đủ chức năng mà không cần kết nối Internet tại bất kỳ thời điểm nào. | BO-04 |
| **BR-05** | Người dùng phải được thông báo rõ ràng về mọi sự kiện quan trọng trong ván đấu (lượt bị bỏ, kết thúc trận, kỷ lục mới). | BO-02, BO-03 |
| **BR-06** | Hệ thống phải cho phép người chơi bắt đầu ván mới ngay lập tức sau khi kết thúc ván trước mà không phải nhập lại thông tin. | BO-01 |

---

## 8. Business Rules

Các quy tắc bất biến xuất phát từ luật chơi Othello chuẩn quốc tế và các ràng buộc nghiệp vụ của hệ thống.

| ID | Business Rule |
|---|---|
| **BRule-01** | Quân đen luôn đi trước trong mọi ván đấu, không có ngoại lệ. |
| **BRule-02** | Một nước đi chỉ hợp lệ nếu quân được đặt xuống kẹp được ít nhất một quân đối thủ theo hàng ngang, hàng dọc, hoặc đường chéo. |
| **BRule-03** | Tất cả quân đối thủ bị kẹp bởi một nước đi đều phải bị lật cùng một lúc, không có ngoại lệ. |
| **BRule-04** | Nếu một bên không có nước đi hợp lệ, lượt đó tự động bị bỏ qua và quyền đi chuyển sang bên còn lại. |
| **BRule-05** | Ván đấu kết thúc khi cả hai bên đều không còn nước đi hợp lệ hoặc bàn cờ đã đầy. |
| **BRule-06** | Bên có nhiều quân trên bàn cờ khi ván kết thúc là bên thắng. Nếu bằng nhau, kết quả là hòa. |
| **BRule-07** | Điểm số của người chơi là số quân của họ trên bàn cờ khi ván kết thúc (tối đa 64). |
| **BRule-08** | Tên người chơi được nhập trước mỗi ván đấu và phải chứa ít nhất 1 ký tự. Tên này không được xác thực hay kiểm tra tính duy nhất. |
| **BRule-09** | Bảng xếp hạng chỉ lưu điểm cao nhất của mỗi tên người chơi — nếu tên trùng thì cập nhật điểm cao hơn, không tạo bản ghi mới. |

---

## 9. Functional Requirements

Mỗi yêu cầu chức năng mô tả *điều gì* hệ thống cần thực hiện. Chi tiết kỹ thuật và acceptance criteria chi tiết thuộc phạm vi SRS.

### 9.1 Onboarding

| ID | Mô tả | Liên quan đến BR |
|---|---|---|
| **FR-01** | Hệ thống cho phép người chơi nhập tên hiển thị trước khi bắt đầu ván đấu và lưu tên đó cho phiên hiện tại. | BR-01 |
| **FR-02** | Hệ thống cho phép người chơi chọn màu quân (đen hoặc trắng) trước khi ván đấu bắt đầu. | BR-01 |

### 9.2 Gameplay

| ID | Mô tả | Liên quan đến BR |
|---|---|---|
| **FR-03** | Hệ thống tự động khởi tạo bàn cờ 8×8 với thế quân ban đầu chuẩn khi ván đấu bắt đầu. | BR-02 |
| **FR-04** | Hệ thống tính toán và hiển thị tất cả nước đi hợp lệ cho người chơi tại đầu mỗi lượt. | BR-02, BR-05 |
| **FR-05** | Hệ thống xử lý nước đi của người chơi, lật tất cả quân bị kẹp và cập nhật trạng thái bàn cờ ngay lập tức. | BR-02 |
| **FR-06** | AI tự động tính toán và thực hiện nước đi sau mỗi lượt của người chơi, hiển thị vị trí đặt quân rõ ràng. | BR-02 |
| **FR-07** | Hệ thống tự động phát hiện và xử lý trường hợp một bên không có nước đi hợp lệ, thông báo cho người chơi và chuyển lượt. | BR-05 |

### 9.3 Result & High Score

| ID | Mô tả | Liên quan đến BR |
|---|---|---|
| **FR-08** | Hệ thống phát hiện điều kiện kết thúc ván đấu, đếm quân và hiển thị kết quả thắng/thua/hòa. | BR-05 |
| **FR-09** | Hệ thống tự động lưu điểm số của người chơi vào bộ nhớ cục bộ sau mỗi ván hoàn thành. | BR-03 |
| **FR-10** | Hệ thống hiển thị bảng xếp hạng Top 10 điểm cao có thể truy cập từ màn hình kết quả hoặc màn hình chào. | BR-03 |
| **FR-11** | Hệ thống cho phép người chơi bắt đầu ván mới ngay lập tức từ màn hình kết quả mà không cần nhập lại tên. | BR-06 |

---

## 10. Non-Functional Requirements

| ID | Danh mục | Mô tả | Mức ưu tiên |
|---|---|---|---|
| **NFR-01** | Hiệu năng | AI phản hồi và đặt quân trong vòng ≤ 2 giây; bàn cờ cập nhật sau mỗi nước đi trong < 200ms. | High |
| **NFR-02** | Tính khả dụng | Ứng dụng hoạt động hoàn toàn offline, không yêu cầu kết nối Internet tại bất kỳ thời điểm nào. | High |
| **NFR-03** | Độ bền dữ liệu | Điểm cao được lưu vào bộ nhớ cục bộ và không bị mất khi tắt hoặc khởi động lại ứng dụng. | High |
| **NFR-04** | Độ chính xác | Logic game tuân thủ chính xác 100% luật Othello tiêu chuẩn quốc tế trong mọi tình huống. | High |
| **NFR-05** | Tính sử dụng | Giao diện đủ trực quan để người dùng mới hoàn thành ván đấu đầu tiên mà không cần hướng dẫn bên ngoài. | Medium |
| **NFR-06** | Khả năng mở rộng | Kiến trúc hệ thống cho phép bổ sung chế độ độ khó AI hoặc chế độ PvP trong phiên bản tương lai mà không cần thiết kế lại toàn bộ. | Low |

---

## 11. High-Level Scenario

Luồng hoạt động tổng thể từ khi người chơi mở ứng dụng đến khi kết thúc một phiên chơi:

| # | Actor | Bước | Mô tả |
|---|---|---|---|
| 1 | Player | Mở ứng dụng | Player truy cập ứng dụng, thấy màn hình chào. |
| 2 | Player | Nhập tên và chọn màu quân | Player nhập tên hiển thị và chọn màu quân (đen/trắng). |
| 3 | System | Khởi tạo trận đấu | System thiết lập bàn cờ 8×8, đặt 4 quân ban đầu, xác định lượt đi đầu tiên (đen đi trước). |
| 4 | System | Hiển thị nước đi hợp lệ | System tính toán và highlight các ô hợp lệ của Player. |
| 5 | Player | Thực hiện nước đi | Player chọn một ô hợp lệ để đặt quân. |
| 6 | System | Xử lý nước đi của Player | System lật quân bị kẹp, cập nhật bàn cờ và điểm số. |
| 7 | System | Kiểm tra lượt bị bỏ qua | System kiểm tra AI hoặc Player có nước đi không; nếu không thì bỏ qua và thông báo. |
| 8 | AI | Tính toán và thực hiện nước đi | AI tính nước tốt nhất và đặt quân. |
| 9 | System | Xử lý nước đi của AI | System lật quân, cập nhật bàn cờ, chuyển lượt về Player. |
| 10 | System | Phát hiện kết thúc trận | System phát hiện không còn nước đi hợp lệ cho cả hai bên. |
| 11 | System | Hiển thị kết quả | System đếm quân, công bố thắng/thua/hòa. |
| 12 | System | Lưu điểm cao | System kiểm tra và lưu điểm nếu Player đạt kỷ lục mới. |
| 13 | Player | Xem bảng điểm / Chơi lại | Player xem bảng xếp hạng hoặc bắt đầu ván mới. |

> **Luồng ngoại lệ:** Bước 7 có thể xảy ra sau bất kỳ bước 6 hoặc 9 nào. Nếu cả hai bên đều không có nước đi hợp lệ liên tiếp, hệ thống nhảy thẳng đến bước 10.

---

## 12. Constraints & Assumptions

### 12.1 Constraints

| Ràng buộc | Mô tả |
|---|---|
| Không có backend server | Toàn bộ dữ liệu được xử lý và lưu trữ cục bộ trên máy người dùng. |
| Không có xác thực người dùng | Danh tính người chơi chỉ là tên hiển thị nhập trước ván đấu, không có cơ chế xác minh. |
| Chỉ hỗ trợ chế độ PvE | Không có chức năng nhiều người chơi trong phạm vi dự án này. |
| Phạm vi học kỳ | Dự án phải hoàn thành trong một học kỳ với đội một người. |
| Không có tính năng undo | Người chơi không thể rút lại nước đi đã thực hiện. |

### 12.2 Assumptions

- Người dùng đã quen với cách sử dụng ứng dụng desktop cơ bản (click chuột, nhập văn bản).
- Người dùng chấp nhận bảng điểm cao chỉ lưu trên thiết bị cục bộ, không đồng bộ giữa các máy.
- Người dùng hiểu cơ bản khái niệm thắng/thua trong game theo số quân — không cần giải thích thêm trên màn hình kết quả.
- Máy tính của người dùng đủ tài nguyên để chạy ứng dụng desktop thông thường.
- Một ô trên bàn cờ chỉ có thể chứa một quân duy nhất tại một thời điểm.
- Logic game tuân thủ hoàn toàn luật Othello/Reversi tiêu chuẩn quốc tế (World Othello Federation rules).

---

## 13. Risks

| ID | Rủi ro | Khả năng xảy ra | Mức độ ảnh hưởng | Biện pháp giảm thiểu |
|---|---|---|---|---|
| **R-01** | Logic AI tính toán sai nước đi trong các edge case (bàn cờ gần đầy, AI không có nước đi) | Cao | Cao — ảnh hưởng trực tiếp đến tính đúng đắn của game | Viết test case cho toàn bộ edge case của luật Othello trước khi tích hợp AI |
| **R-02** | File lưu điểm cao bị hỏng hoặc mất do lỗi ghi đĩa bất ngờ (tắt máy đột ngột) | Thấp | Trung bình — mất dữ liệu điểm của người dùng | Ghi file nguyên tử (ghi ra file tạm, sau đó rename) để tránh file bị ghi dở |
| **R-03** | Thời gian hoàn thành vượt quá một học kỳ do scope bị mở rộng ngoài kế hoạch | Trung bình | Cao — ảnh hưởng đến toàn bộ tiến độ nộp bài | Giới hạn MVP ở FR-01 đến FR-08; FR-09 đến FR-11 là tính năng mở rộng tùy thời gian |
| **R-04** | AI phản hồi quá chậm (> 2 giây) trên máy tính cấu hình thấp | Trung bình | Trung bình — trải nghiệm người dùng kém, vi phạm NFR-01 | Giới hạn độ sâu tìm kiếm của AI; kiểm thử trên máy cấu hình tối thiểu |
| **R-05** | Tên người chơi trùng nhau dẫn đến nhầm lẫn khi cập nhật bảng điểm | Trung bình | Thấp — dữ liệu bảng điểm không chính xác | Ghi rõ trong BRule-09: cùng tên = cùng người chơi, chỉ giữ điểm cao nhất |

---


*Phiên bản 2.1 — Trần Lê Minh Mẫn — Tháng 4/2026 — Đại học Nông Lâm TP.HCM*
