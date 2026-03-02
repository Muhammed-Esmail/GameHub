# Esmail's Game Hub

A Java Swing desktop application featuring two classic mini-games: **Minesweeper** and **Snake**.  
Built as part of **Task 1 – CS251: Intro to Software Engineering (Winter 2026)**, Faculty of Computers and Artificial Intelligence (FCAI), Cairo University.

---

## Project Structure

```
├── Main.java          # Entry point, launches the Game Hub menu
├── Screen.java        # Base class for all game windows (JFrame wrapper)
├── Minesweeper.java   # Minesweeper game logic and UI
├── Snake.java         # Snake game logic and UI
└── assets/
    └── icon.png
```

---

## Games

### Minesweeper
- 8×8 grid with 10 hidden mines
- **Left-click** to reveal a tile
- **Right-click** to place/remove a flag
- First click is always safe — bombs are placed after it
- Flood-fill reveals empty regions automatically
- Win by clearing all non-mine tiles

### Snake
- 16×16 grid
- Move with **Arrow Keys** or **WASD**
- Press **Space** to pause
- Snake grows when it eats food (red tile)
- Game ends on wall or self collision

---

## How to Run

### Steps

```bash
# 1. Compile all files
javac *.java

# 2. Run the app
java Main
```

The **Game Hub** window will open — click a game button to launch it.

---

## 🧠 Language & Tools

| Item       | Details              |
|------------|----------------------|
| Language   | Java                 |
| UI Library | Java Swing (javax.swing) |
| IDE        | VS Code |

---

## Author
Developed individually for **Task 1** of Homework 1 — CS251 Winter 2026.

## Under supervision of:
Dr. M. El-Ramly — Faculty of Computers and Artificial Intelligence, Cairo University.
