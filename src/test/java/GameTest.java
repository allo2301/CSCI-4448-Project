import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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


    private Board board;
    private CapturingObserver observer;
    private List<GameObserver> observers;

    @BeforeEach
    void setUp() {
        board    = new Board(3);
        observer = new CapturingObserver();
        observers = List.of(observer);
    }



    @Test
    void testRowWinDetected() {

        board.place(0, 0, Mark.X);
        board.place(0, 1, Mark.X);
        board.place(0, 2, Mark.X);

        assertTrue(board.hasWinner(Mark.X), "X should win with a full top row");
        assertFalse(board.hasWinner(Mark.O), "O should not be a winner");
    }



    @Test
    void testDiagonalWinDetected() {

        board.place(0, 0, Mark.O);
        board.place(1, 1, Mark.O);
        board.place(2, 2, Mark.O);

        assertTrue(board.hasWinner(Mark.O), "O should win with the main diagonal");
    }



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


    private String captureOutput(Runnable action) {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        PrintStream old = System.out;
        System.setOut(new PrintStream(buf));
        try { action.run(); } finally { System.setOut(old); }
        return buf.toString();
    }

    @Test
    void testBoardIsEmpty() {
        assertTrue(board.isEmpty(1, 1), "Fresh cell should be empty");
        board.place(1, 1, Mark.X);
        assertFalse(board.isEmpty(1, 1), "Occupied cell should not be empty");
    }


    @Test
    void testBoardIsEmpty_outOfBounds() {
        assertFalse(board.isEmpty(-1, 0));
        assertFalse(board.isEmpty(0, 3));
    }


    @Test
    void testBoardRemove() {
        board.place(2, 2, Mark.O);
        board.remove(2, 2);
        assertEquals(Mark.EMPTY, board.get(2, 2));
        assertTrue(board.isEmpty(2, 2));
    }


    @Test
    void testBoardGet() {
        board.place(0, 1, Mark.X);
        assertEquals(Mark.X, board.get(0, 1));
        assertEquals(Mark.EMPTY, board.get(2, 2));
    }


    @Test
    void testBoardPlace_outOfBounds() {
        assertThrows(IllegalArgumentException.class, () -> board.place(5, 0, Mark.X));
        assertThrows(IllegalArgumentException.class, () -> board.place(0, -1, Mark.X));
    }


    @Test
    void testBoardPrint() {
        board.place(0, 0, Mark.X);
        String out = captureOutput(board::print);
        assertTrue(out.contains("X"));
        assertTrue(out.contains("|"));
    }


    @Test
    void testBoardHasWinner_column() {
        board.place(0, 1, Mark.O);
        board.place(1, 1, Mark.O);
        board.place(2, 1, Mark.O);
        assertTrue(board.hasWinner(Mark.O));
    }


    @Test
    void testBoardHasWinner_antiDiagonal() {
        board.place(0, 2, Mark.X);
        board.place(1, 1, Mark.X);
        board.place(2, 0, Mark.X);
        assertTrue(board.hasWinner(Mark.X));
    }


    @Test
    void testMarkDisplay() {
        assertEquals("X", Mark.X.display());
        assertEquals("O", Mark.O.display());
        assertEquals("·", Mark.EMPTY.display());
    }


    @Test
    void testMarkOpponent() {
        assertEquals(Mark.O, Mark.X.opponent());
        assertEquals(Mark.X, Mark.O.opponent());
        assertThrows(IllegalStateException.class, Mark.EMPTY::opponent);
    }


    @Test
    void testPlayerGetName() {
        Player p = new Player("Alice", Mark.X, new ScriptedStrategy());
        assertEquals("Alice", p.getName());
    }


    @Test
    void testGamePlay_xWins() {
        Board b = new Board(3);
        Player x = new Player("X", Mark.X,
                new ScriptedStrategy(new int[]{0,0}, new int[]{1,0}, new int[]{2,0}));
        Player o = new Player("O", Mark.O,
                new ScriptedStrategy(new int[]{0,1}, new int[]{1,1}));
        CapturingObserver obs = new CapturingObserver();
        Game game = new Game(b, List.of(x, o), List.of(obs));

        captureOutput(game::play);

        assertTrue(obs.gameOverCalled);
        assertSame(x, obs.gameOverWinner);
    }


    @Test
    void testGamePlay_draw() {
        Board b = new Board(3);
        Player x = new Player("X", Mark.X,
                new ScriptedStrategy(
                        new int[]{0,0}, new int[]{1,0}, new int[]{1,1},
                        new int[]{0,2}, new int[]{2,1}));
        Player o = new Player("O", Mark.O,
                new ScriptedStrategy(
                        new int[]{0,1}, new int[]{1,2}, new int[]{2,0}, new int[]{2,2}));
        CapturingObserver obs = new CapturingObserver();
        Game game = new Game(b, List.of(x, o), List.of(obs));

        captureOutput(game::play);

        assertTrue(obs.gameOverCalled);
        assertNull(obs.gameOverWinner);
    }


    @Test
    void testConsoleObserver_onMove_noRemoval() {
        board.place(0, 0, Mark.X);
        Player player = new Player("Bob", Mark.X, new ScriptedStrategy());
        ConsoleObserver co = new ConsoleObserver(board);

        String out = captureOutput(() -> co.onMove(player, 0, 0, null));

        assertTrue(out.contains("Bob"));
        assertFalse(out.contains("Removed"));
    }


    @Test
    void testConsoleObserver_onMove_withRemoval() {
        Player player = new Player("Carol", Mark.O, new ScriptedStrategy());
        ConsoleObserver co = new ConsoleObserver(board);

        String out = captureOutput(() -> co.onMove(player, 1, 1, new int[]{2, 2}));

        assertTrue(out.contains("Carol"));
        assertTrue(out.contains("emoved"));
    }


    @Test
    void testConsoleObserver_onGameOver_winner() {
        Player winner = new Player("Dave", Mark.X, new ScriptedStrategy());
        ConsoleObserver co = new ConsoleObserver(board);

        String out = captureOutput(() -> co.onGameOver(winner));

        assertTrue(out.contains("Dave"));
        assertTrue(out.contains("wins"));
    }

    @Test
    void testConsoleObserver_onGameOver_draw() {
        ConsoleObserver co = new ConsoleObserver(board);
        String out = captureOutput(() -> co.onGameOver(null));
        assertTrue(out.toLowerCase().contains("draw"));
    }


    @Test
    void testHumanStrategy_validInput() {
        Scanner scanner = new Scanner("1 2\n");
        HumanStrategy hs = new HumanStrategy(scanner);
        captureOutput(() -> assertArrayEquals(new int[]{1, 2}, hs.chooseMove(board, Mark.X)));
    }


    @Test
    void testHumanStrategy_occupiedCellRetry() {
        board.place(0, 0, Mark.X);
        Scanner scanner = new Scanner("0 0\n1 1\n");
        HumanStrategy hs = new HumanStrategy(scanner);

        String out = captureOutput(() ->
                assertArrayEquals(new int[]{1, 1}, hs.chooseMove(board, Mark.O)));

        assertTrue(out.contains("not empty") || out.contains("Try again"));
    }


    @Test
    void testHumanStrategy_invalidInputRetry() {
        Scanner scanner = new Scanner("abc\n2 2\n");
        HumanStrategy hs = new HumanStrategy(scanner);

        String out = captureOutput(() ->
                assertArrayEquals(new int[]{2, 2}, hs.chooseMove(board, Mark.X)));

        assertTrue(out.contains("Invalid") || out.contains("numbers"));
    }
    @Test
    void testMainMethod() {
        // X: (0,0),(1,0),(2,0)  O: (0,1),(1,1)
        String input = "0 0\n0 1\n1 0\n1 1\n2 0\n";
        InputStream oldIn = System.in;
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        try {
            captureOutput(() -> Main.main(new String[]{}));
        } finally {
            System.setIn(oldIn);
        }
    }

}