public interface GameObserver {
    void onMove(Player player, int r, int c, int[] removed);
    void onGameOver(Player winner); // winner is null for a draw
}