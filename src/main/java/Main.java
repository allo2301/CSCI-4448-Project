import java.io.IOException;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Board board = new Board(3);

        PlayerFactory playerFactory = new PlayerFactory(scanner);
        Player player1 = playerFactory.createHumanPlayer("Player 1", Mark.X);
        Player player2 = playerFactory.createHumanPlayer("Player 2", Mark.O);

        try {
            FileObserver fileObserver = new FileObserver("gamelog.txt");
            List<GameObserver> observers = List.of(new ConsoleObserver(board), fileObserver);
            Game game = new Game(board, List.of(player1, player2), observers);
            game.play();
        } catch (IOException e) {
            System.err.println("Could not open gamelog.txt: " + e.getMessage());
        }
    }
}