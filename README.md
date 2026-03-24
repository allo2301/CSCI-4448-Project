# CSCI-4448-Project

# Proposal
Name of Project: Tic-Tac-Toe
Team members: Alexander Long, Renesh Panchal
Development language: Java
Description of what it will do:
Tic-tac-toe is a simple two player game played on a 3×3 grid where players take turns placing Xs and Os. The goal is to get three of your symbols in a row, either horizontally, vertically, or diagonally. The game ends when one player wins or when all spaces are filled, resulting in a draw.


## Design Patterns

### 1. Strategy Pattern
Found in: `PlayerStrategy.java`, `HumanStrategy.java`, `Player.java`

Player.chooseMove() delegates to a PlayerStrategy interface instead of
deciding itself how to pick a move. This means Player never has any
if/else logic checking who the player is, it just calls
strategy.chooseMove() and gets back a move. 

### 2. Observer Pattern
Found in: `GameObserver.java`, `ConsoleObserver.java`, `FileObserver.java`,
`PlaceMoveCommand.java`

PlaceMoveCommand holds a List<GameObserver> and calls onMove() on each
one after a mark is placed, and Main calls onGameOver() on each one when
the game ends. ConsoleObserver handles all terminal output printing the
move and redrawing the board. FileObserver handles persistence sdaving
every move and the final result to gamelog.txt.

### 3. Command Pattern
Found in: `MoveCommand.java`, `PlaceMoveCommand.java`

Every move is wrapped in a PlaceMoveCommand object that encapsulates the
board, the player, the coordinates, and the list of observers to notify.
Main just calls command.execute(), it has no knowledge of how a mark
gets placed, how observers are notified, or any other move logic.
