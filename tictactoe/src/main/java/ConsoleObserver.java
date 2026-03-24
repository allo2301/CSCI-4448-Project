public class ConsoleObserver implements GameObserver {
    private final Board board;

    public ConsoleObserver(Board board) {
        // store board
    }

    @Override
    public void onMove(Player player, int r, int c, int[] removed) {
        // print what move was made, print removed notice if not null, print board
    }

    @Override
    public void onGameOver(Player winner) {
        // print winner name or "draw"
    }
}