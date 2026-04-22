public class Player {
    private final String name;
    private final Mark mark;
    private final PlayerStrategy strategy;

    public Player(String name, Mark mark, PlayerStrategy strategy) {
        this.name = name;
        this.mark = mark;
        this.strategy = strategy;
    }

    public String getName() {
        return name;
    }

    public Mark getMark() {
        return mark;
    }

    public int[] chooseMove(Board board) {
        return strategy.chooseMove(board, mark);
    }
}