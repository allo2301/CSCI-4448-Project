public class ConsoleObserver implements GameObserver {
    private final Board board;

    public ConsoleObserver(Board board) {
        // store board
        this.board = board;
    }

    @Override
    public void onMove(Player player, int r, int c, int[] removed) {
        // print what move was made, print removed notice if not null, print board
        System.out.printf("%s placed at (%d, %d)%n", player.getName(), r, c);
 
        if (removed != null) {
            System.out.printf("  → Removed piece from (%d, %d)%n", removed[0], removed[1]);
        }
 
        System.out.println(board);
    }

    @Override
    public void onGameOver(Player winner) {
        // print winner name or "draw"
        if (winner == null) {
            System.out.println("Game over — it's a draw!");
        } else {
            System.out.printf("Game over — %s wins!%n", winner.getName());
        }
    }
}