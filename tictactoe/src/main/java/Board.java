public class Board {
    private final Mark[][] cells;
    public final int size;

    public Board(int size) {
        // initialize cells to EMPTY
    }

    public boolean isEmpty(int r, int c) {
        // return true if cell is EMPTY
    }

    public void place(int r, int c, Mark mark) {
        // set cells[r][c] to mark
    }

    public void remove(int r, int c) {
        // set cells[r][c] to EMPTY
    }

    public Mark get(int r, int c) {
        // return cells[r][c]
    }

    public boolean hasWinner(Mark mark) {
        // check all rows, cols, and diagonals for 3 in a row
    }

    public boolean isFull() {
        // return true if no EMPTY cells remain
    }

    public void print() {
        // print the board to the terminal
    }
}