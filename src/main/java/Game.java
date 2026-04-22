import java.util.List;

public class Game {
    private final Board board;
    private final List<Player> players;
    private final List<GameObserver> observers;
    private int currentPlayerIndex;

    public Game(Board board, List<Player> players, List<GameObserver> observers) {
        this.board = board;
        this.players = players;
        this.observers = observers;
        this.currentPlayerIndex = 0;
    }

    public void play() {
        board.print();

        while (true) {
            Player currentPlayer = players.get(currentPlayerIndex);

            System.out.println(currentPlayer.getName() + "'s turn (" + currentPlayer.getMark() + ")");

            int[] move = currentPlayer.chooseMove(board);
            int row = move[0];
            int col = move[1];

            MoveCommand command = new PlaceMoveCommand(board, currentPlayer, row, col, observers);
            command.execute();

            if (board.hasWinner(currentPlayer.getMark())) {
                notifyGameOver(currentPlayer);
                break;
            }

            if (board.isFull()) {
                notifyGameOver(null);
                break;
            }

            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }
    }

    private void notifyGameOver(Player winner) {
        for (GameObserver observer : observers) {
            observer.onGameOver(winner);
        }
    }
}