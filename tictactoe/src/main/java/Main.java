import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Board board     = new Board(3);

        System.out.print("Player X — enter your name: ");
        String nameX = scanner.next();

        System.out.print("Player O — enter your name: ");
        String nameO = scanner.next();

        List<Player> players = List.of(
                new Player(nameX, Mark.X, new HumanStrategy(scanner)),
                new Player(nameO, Mark.O, new HumanStrategy(scanner))
        );

        List<GameObserver> observers = List.of(
                new ConsoleObserver(board)
        );

        int turn = 0;
        while (true) {
            Player current = players.get(turn % 2);
            System.out.println("────────────────────────────────");
            System.out.println("  " + current.name + " (" + current.mark + ") to move");
            System.out.println("────────────────────────────────");

            int[] move = current.chooseMove(board);
            MoveCommand command = new PlaceMoveCommand(board, current, move[0], move[1], observers);
            command.execute();

            if (board.hasWinner(current.mark)) {
                for (GameObserver o : observers) o.onGameOver(current);
                break;
            }
            if (board.isFull()) {
                for (GameObserver o : observers) o.onGameOver(null);
                break;
            }
            turn++;
        }
        scanner.close();
    }
}