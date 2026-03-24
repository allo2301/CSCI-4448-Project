import java.util.Scanner;

public class HumanStrategy implements PlayerStrategy {
    private final Scanner scanner;

    public HumanStrategy(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public int[] chooseMove(Board board, Mark mark) {
        while (true) {
            System.out.print("  Enter row col: ");
            try {
                int r = scanner.nextInt();
                int c = scanner.nextInt();
                if (board.isEmpty(r, c)) return new int[]{r, c};
                System.out.println("  Cell already taken, try again.");
            } catch (Exception e) {
                scanner.nextLine();
                System.out.println("  Invalid input. Enter two numbers e.g: 0 2");
            }
        }
    }
}