# CSCI-4448-Project

# Proposal
Name of Project: Tic-Tac-Toe

Team members: Alexander Long, Renesh Panchal

Development language: Java 25.0.1

Description:
Tic-tac-toe is a simple two player game played on a 3×3 grid where players take turns placing Xs and Os. The goal is to get three of your symbols in a row, either horizontally, vertically, or diagonally. The game ends when one player wins or when all spaces are filled, resulting in a draw.


## Design Patterns

### 1. Strategy Pattern
Found in: `PlayerStrategy.java`, `HumanStrategy.java`, `Player.java`

Player.chooseMove() delegates to a PlayerStrategy interface instead of
deciding itself how to pick a move. This means Player never has any
if/else logic checking who the player is, it just calls
strategy.chooseMove() and gets back a move. 

### 2. Observer Pattern
Found in: `GameObserver.java`, `ConsoleObserver.java`,
`PlaceMoveCommand.java`

PlaceMoveCommand holds a List<GameObserver> and calls onMove() on each
one after a mark is placed, and Main calls onGameOver() on each one when
the game ends. ConsoleObserver handles all terminal output printing the
move and redrawing the board. FileObserver handles persistence saving
every move and the final result to gamelog.txt.

### 3. Command Pattern
Found in: `MoveCommand.java`, `PlaceMoveCommand.java`

Every move is wrapped in a PlaceMoveCommand object that encapsulates the
board, the player, the coordinates, and the list of observers to notify.
Main just calls command.execute(), it has no knowledge of how a mark
gets placed, how observers are notified, or any other move logic.

### 4. Factory Method Pattern
Found in: `PlayerFactory.java`, `GameFactory.java`, `Main.java`

PlayerFactory.createHumanPlayer() and GameFactory.createGame() encapsulate
the construction logic that would otherwise be sprawled across Main. Main
describes what it wants — a human player with a given name and mark, a
game with a given board and player list — without knowing how those objects
are assembled. Adding an AI player later only requires a new method on
PlayerFactory; Main does not change.

### 5. Dependency Injection
Found in: `PlayerFactory.java`, `GameFactory.java`, `Main.java`

Every object receives its dependencies from the outside rather than
creating them itself. Scanner is constructed once in Main and injected
into PlayerFactory, which injects it further into HumanStrategy. Board
is injected into GameFactory.createGame(), which injects it into
ConsoleObserver internally. Nothing reaches out to instantiate its own
dependencies, which makes each class independently testable by passing
in a substitute at construction time.




