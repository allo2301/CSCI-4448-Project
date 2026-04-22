import java.util.Scanner;

public class HumanStrategy implements PlayerStrategy {
    private final Scanner scanner;

    public HumanStrategy(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public int[] chooseMove(Board board, Mark mark) {
        while (true) {
            System.out.print("Enter row col: ");
            String line = scanner.nextLine();

            try {
                String[] parts = line.trim().split("\\s+");
                int r = Integer.parseInt(parts[0]);
                int c = Integer.parseInt(parts[1]);

                if (board.isEmpty(r, c)) {
                    return new int[]{r, c};
                } else {
                    System.out.println("Cell not empty. Try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Enter two numbers.");
            }
        }
    }
}