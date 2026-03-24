import java.util.Arrays;

public class Board {
    private final Mark[][] cells;
    public final int size;

    public Board(int size) {
        this.size = size;
        cells = new Mark[size][size];
        for (Mark[] row : cells) Arrays.fill(row, Mark.EMPTY);
    }

    public boolean isEmpty(int r, int c) {
        return inBounds(r, c) && cells[r][c] == Mark.EMPTY;
    }

    public void place(int r, int c, Mark mark) {
        if (!inBounds(r, c)) throw new IllegalArgumentException("Out of bounds: " + r + "," + c);
        cells[r][c] = mark;
    }

    public void remove(int r, int c) {
        cells[r][c] = Mark.EMPTY;
    }

    public Mark get(int r, int c) {
        return cells[r][c];
    }

    public boolean hasWinner(Mark mark) {
        for (int i = 0; i < size; i++) {
            if (rowMatch(i, mark) || colMatch(i, mark)) return true;
        }
        return diagMatch(mark);
    }

    public boolean isFull() {
        for (Mark[] row : cells)
            for (Mark cell : row)
                if (cell == Mark.EMPTY) return false;
        return true;
    }

    public void print() {
        System.out.println();
        System.out.print("    ");
        for (int c = 0; c < size; c++) System.out.print(c + "   ");
        System.out.println();
        System.out.println("  ┌───" + "┬───".repeat(size - 1) + "┐");
        for (int r = 0; r < size; r++) {
            System.out.print(r + " │");
            for (int c = 0; c < size; c++) {
                System.out.print(" " + cells[r][c].display() + " │");
            }
            System.out.println();
            if (r < size - 1)
                System.out.println("  ├───" + "┼───".repeat(size - 1) + "┤");
        }
        System.out.println("  └───" + "┴───".repeat(size - 1) + "┘");
        System.out.println();
    }

    private boolean rowMatch(int r, Mark m) {
        for (int c = 0; c < size; c++) if (cells[r][c] != m) return false;
        return true;
    }

    private boolean colMatch(int c, Mark m) {
        for (int r = 0; r < size; r++) if (cells[r][c] != m) return false;
        return true;
    }

    private boolean diagMatch(Mark m) {
        boolean d1 = true, d2 = true;
        for (int i = 0; i < size; i++) {
            if (cells[i][i] != m)             d1 = false;
            if (cells[i][size - 1 - i] != m) d2 = false;
        }
        return d1 || d2;
    }

    private boolean inBounds(int r, int c) {
        return r >= 0 && r < size && c >= 0 && c < size;
    }
}