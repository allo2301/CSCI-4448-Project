import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class FileObserver implements GameObserver {

    private final PrintWriter writer;

    public FileObserver(String filename) throws IOException {
        this.writer = new PrintWriter(new FileWriter(filename, true));
    }

    @Override
    public void onMove(Player player, int r, int c, int[] removed) {
        writer.printf("%s placed at (%d, %d)%n", player.getName(), r, c);
        if (removed != null) {
            writer.printf("  Removed piece from (%d, %d)%n", removed[0], removed[1]);
        }
        writer.flush();
    }

    @Override
    public void onGameOver(Player winner) {
        if (winner == null) {
            writer.println("Result: Draw");
        } else {
            writer.println("Result: " + winner.getName() + " wins");
        }
        writer.println("----------");
        writer.flush();
        writer.close();
    }
}