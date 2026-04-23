import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Board board = new Board(3);

        PlayerFactory playerFactory = new PlayerFactory(scanner);
        Player player1 = playerFactory.createHumanPlayer("Player 1", Mark.X);
        Player player2 = playerFactory.createHumanPlayer("Player 2", Mark.O);

        GameFactory gameFactory = new GameFactory();
        Game game = gameFactory.createGame(board, List.of(player1, player2));

        game.play();
    }
}