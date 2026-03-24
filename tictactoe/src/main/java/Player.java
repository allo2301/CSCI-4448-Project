import java.util.ArrayDeque;
import java.util.Deque;

public class Player {
    public final String name;
    public final Mark mark;
    private final PlayerStrategy strategy;
    private final Deque<int[]> history;
    private final int maxMarks;

    public Player(String name, Mark mark, PlayerStrategy strategy, int maxMarks) {
        // store all fields, initialize history
    }

    public int[] chooseMove(Board board) {
        // delegate to strategy
    }

    public int[] recordMove(int r, int c) {
        // add [r,c] to history, if history exceeds maxMarks remove and return oldest, else return null
    }
}