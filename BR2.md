# BUSINESS REQUIREMENTS DOCUMENT (BRD)  
## Project: Othello Game (Player vs AI)

---

## 1. Business Objectives
- Provide a simple Othello game playable instantly without login
- Enable single-player gameplay against AI
- Encourage replay through leaderboard system
- Ensure quick demo capability (~2 minutes)

---

## 2. Stakeholders
- Player (Student)
- Instructor
- Development Team

---

## 3. Actors
- Player
- AI Opponent
- System

---

## 4. Scope

### In Scope
- Othello 8x8 gameplay
- Player vs AI
- Enter name
- Choose piece color
- Valid move highlighting
- Score tracking
- Leaderboard
- Replay

### Out of Scope
- Authentication
- Multiplayer
- Online server/database

---

## 5. Main Flow
1. Open app
2. Enter name & choose color
3. Initialize board
4. Player move
5. System updates
6. AI move
7. Repeat until end
8. Show result
9. Save score
10. Replay or leaderboard

---

## 6. Business Requirements

### BR-01: Quick Start
- No login required

### BR-02: Basic Personalization
- Enter name
- Choose color

### BR-03: Gameplay Rules
- Standard Othello rules
- Auto skip turn if no move

### BR-04: Player Support
- Highlight valid moves
- Show flipped pieces

### BR-05: AI Behavior
- Minimax algorithm
- Response ≤ 2 seconds

### BR-06: Transparency
- Show AI moves clearly

### BR-07: Game Result
- Display winner and score

### BR-08: Data Persistence
- Save score locally

### BR-09: Leaderboard
- Top 10 scores

### BR-10: Replay
- Play again without re-entering name

---

## 7. Business Rules
- Black moves first
- Must capture to place piece
- Skip if no valid move
- Game ends when no moves left
- Higher score wins

---

## 8. Non-Functional Requirements
- Performance: AI ≤ 2s
- Offline support
- Local storage
- Usability
- Accuracy

---

## 9. Success Metrics
- Start game within 10s
- No crashes
- AI responsive
- Score saved correctly

---

## 10. MVP
- Name input
- Gameplay
- AI
- Result display

---

## 11. Roadmap
Phase 1: Core gameplay  
Phase 2: AI + scoring  
Phase 3: UI + leaderboard  

---

## 12. Conclusion
Focus on simple, fast, and correct gameplay experience without unnecessary complexity.
