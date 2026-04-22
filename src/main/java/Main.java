import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Board board = new Board(3);

        Player player1 = new Player("Player 1", Mark.X, new HumanStrategy(scanner));
        Player player2 = new Player("Player 2", Mark.O, new HumanStrategy(scanner));

        List<Player> players = List.of(player1, player2);
        List<GameObserver> observers = List.of(new ConsoleObserver(board));

        Game game = new Game(board, players, observers);
        game.play();
    }
}