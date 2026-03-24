import java.util.List;

public class PlaceMoveCommand implements MoveCommand {
    private final Board board;
    private final Player player;
    private final int r, c;
    private final List<GameObserver> observers;

    public PlaceMoveCommand(Board board, Player player, int r, int c,
                            List<GameObserver> observers) {
        this.board     = board;
        this.player    = player;
        this.r         = r;
        this.c         = c;
        this.observers = observers;
    }

    @Override
    public void execute() {
        board.place(r, c, player.mark);
        for (GameObserver observer : observers) {
            observer.onMove(player, r, c);
        }
    }
}