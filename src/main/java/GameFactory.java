import java.util.List;


public class GameFactory {

    public Game createGame(Board board, List<Player> players) {

        List<GameObserver> observers = List.of(new ConsoleObserver(board));
        return new Game(board, players, observers);
    }
}