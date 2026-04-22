public interface PlayerStrategy {
    int[] chooseMove(Board board, Mark mark); // returns [row, col]
}