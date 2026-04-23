import java.util.Scanner;


public class PlayerFactory {


    private final Scanner scanner;

    public PlayerFactory(Scanner scanner) {
        this.scanner = scanner;
    }

    public Player createHumanPlayer(String name, Mark mark) {
        return new Player(name, mark, new HumanStrategy(scanner));
    }
}