public class Player {
    public final String name;
    public final Mark mark;
    private final PlayerStrategy strategy;

    public Player(String name, Mark mark, PlayerStrategy strategy) {
        this.name     = name;
        this.mark     = mark;
        this.strategy = strategy;
    }

    public int[] chooseMove(Board board) {
        return strategy.chooseMove(board, mark);
    }
}