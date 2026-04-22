import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    


    static class ScriptedStrategy implements PlayerStrategy {
        private final int[][] moves;
        private int index = 0;

        ScriptedStrategy(int[]... moves) { this.moves = moves; }

        @Override
        public int[] chooseMove(Board board, Mark mark) {
            return moves[index++];
        }
    }


    static class CapturingObserver implements GameObserver {
        record MoveEvent(Player player, int r, int c, int[] removed) {}

        final List<MoveEvent> moves = new ArrayList<>();
        Player gameOverWinner = null;       
        boolean gameOverCalled = false;

        @Override
        public void onMove(Player player, int r, int c, int[] removed) {
            moves.add(new MoveEvent(player, r, c, removed));
        }

        @Override
        public void onGameOver(Player winner) {
            gameOverCalled = true;
            gameOverWinner = winner;
        }
    }

    // ── Shared fixtures ───────────────────────────────────────────────────────

    private Board board;
    private CapturingObserver observer;
    private List<GameObserver> observers;

    @BeforeEach
    void setUp() {
        board    = new Board(3);
        observer = new CapturingObserver();
        observers = List.of(observer);
    }


    // Test 1 — Board correctly detects a row win

    @Test
    void testRowWinDetected() {
        
        board.place(0, 0, Mark.X);
        board.place(0, 1, Mark.X);
        board.place(0, 2, Mark.X);

        assertTrue(board.hasWinner(Mark.X), "X should win with a full top row");
        assertFalse(board.hasWinner(Mark.O), "O should not be a winner");
    }


    // Test 2 — Board correctly detects a diagonal win

    @Test
    void testDiagonalWinDetected() {
     
        board.place(0, 0, Mark.O);
        board.place(1, 1, Mark.O);
        board.place(2, 2, Mark.O);

        assertTrue(board.hasWinner(Mark.O), "O should win with the main diagonal");
    }


    // Test 3 — PlaceMoveCommand notifies observers with the correct data

    @Test
    void testPlaceMoveCommandNotifiesObserver() {
        Player player = new Player("Alice", Mark.X, new ScriptedStrategy());

        MoveCommand cmd = new PlaceMoveCommand(board, player, 1, 2, observers);
        cmd.execute();

        assertEquals(1, observer.moves.size());
        CapturingObserver.MoveEvent event = observer.moves.get(0);
        assertSame(player, event.player());
        assertEquals(1, event.r());
        assertEquals(2, event.c());
        assertNull(event.removed(), "No removal expected for a standard place");
    }


    // Test 4 — Full game: X wins via column, onGameOver called with correct winner

    @Test
    void testFullGameXWinsColumn() {
        Player x = new Player("X-Player", Mark.X,
                new ScriptedStrategy(new int[]{0,0}, new int[]{1,0}, new int[]{2,0}));
        Player o = new Player("O-Player", Mark.O,
                new ScriptedStrategy(new int[]{0,1}, new int[]{1,1}));

        Player[] players = {x, o};
        int turn = 0;

        outer:
        while (true) {
            Player current = players[turn % 2];
            int[] move = current.chooseMove(board);
            new PlaceMoveCommand(board, current, move[0], move[1], observers).execute();

            if (board.hasWinner(current.getMark())) {
                for (GameObserver obs : observers) obs.onGameOver(current);
                break;
            }
            if (board.isFull()) {
                for (GameObserver obs : observers) obs.onGameOver(null);
                break;
            }
            turn++;
        }

        assertTrue(observer.gameOverCalled);
        assertSame(x, observer.gameOverWinner, "X should be declared the winner");
        assertEquals(5, observer.moves.size(), "5 moves total before X wins column");
    }


    // Test 5 — Full game ends in a draw, onGameOver called with null winner
   
    @Test
    void testDrawGame() {
        // Board outcome (X = X, O = O):
        //   X O X
        //   X X O
        //   O X O  → no winner, board full
        Player x = new Player("X-Player", Mark.X,
                new ScriptedStrategy(
                        new int[]{0,0}, new int[]{1,0}, new int[]{1,1},
                        new int[]{0,2}, new int[]{2,1}));
        Player o = new Player("O-Player", Mark.O,
                new ScriptedStrategy(
                        new int[]{0,1}, new int[]{1,2}, new int[]{2,0}, new int[]{2,2}));

        Player[] players = {x, o};
        int turn = 0;

        while (true) {
            Player current = players[turn % 2];
            int[] move = current.chooseMove(board);
            new PlaceMoveCommand(board, current, move[0], move[1], observers).execute();

            if (board.hasWinner(current.getMark())) {
                for (GameObserver obs : observers) obs.onGameOver(current);
                break;
            }
            if (board.isFull()) {
                for (GameObserver obs : observers) obs.onGameOver(null);
                break;
            }
            turn++;
        }

        assertTrue(observer.gameOverCalled);
        assertNull(observer.gameOverWinner, "Winner should be null for a draw");
        assertEquals(9, observer.moves.size(), "All 9 cells should be filled");
    }
}
