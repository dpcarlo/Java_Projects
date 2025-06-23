import java.util.Random;
import java.util.Scanner;

public class CampoMinato {
    private static final int ROWS = 8;
    private static final int COLS = 8;
    private static final int BOMBS = 10;

    private boolean[][] bombs = new boolean[ROWS][COLS];
    private boolean[][] revealed = new boolean[ROWS][COLS];
    private int[][] counts = new int[ROWS][COLS];

    private void initialize() {
        Random rand = new Random();
        int placed = 0;
        while (placed < BOMBS) {
            int r = rand.nextInt(ROWS);
            int c = rand.nextInt(COLS);
            if (!bombs[r][c]) {
                bombs[r][c] = true;
                placed++;
            }
        }
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                counts[r][c] = countNeighbors(r, c);
            }
        }
    }

    private int countNeighbors(int row, int col) {
        int count = 0;
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                int nr = row + dr;
                int nc = col + dc;
                if (nr >= 0 && nr < ROWS && nc >= 0 && nc < COLS && bombs[nr][nc]) {
                    count++;
                }
            }
        }
        return count;
    }

    private void reveal(int row, int col) {
        if (row < 0 || row >= ROWS || col < 0 || col >= COLS) return;
        if (revealed[row][col]) return;
        revealed[row][col] = true;
        if (counts[row][col] == 0 && !bombs[row][col]) {
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    if (dr == 0 && dc == 0) continue;
                    reveal(row + dr, col + dc);
                }
            }
        }
    }

    private void printBoard(boolean revealBombs) {
        System.out.print("  ");
        for (int c = 0; c < COLS; c++) {
            System.out.print(c + " ");
        }
        System.out.println();
        for (int r = 0; r < ROWS; r++) {
            System.out.print(r + " ");
            for (int c = 0; c < COLS; c++) {
                if (revealed[r][c]) {
                    if (bombs[r][c]) {
                        System.out.print("* ");
                    } else if (counts[r][c] > 0) {
                        System.out.print(counts[r][c] + " ");
                    } else {
                        System.out.print("  ");
                    }
                } else {
                    if (revealBombs && bombs[r][c]) {
                        System.out.print("* ");
                    } else {
                        System.out.print("- ");
                    }
                }
            }
            System.out.println();
        }
    }

    private boolean allSafeRevealed() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (!bombs[r][c] && !revealed[r][c]) return false;
            }
        }
        return true;
    }

    public void play() {
        initialize();
        Scanner scanner = new Scanner(System.in);
        boolean gameOver = false;
        while (!gameOver) {
            printBoard(false);
            System.out.print("Inserisci riga e colonna (es. 3 4): ");
            int r = scanner.nextInt();
            int c = scanner.nextInt();
            if (r < 0 || r >= ROWS || c < 0 || c >= COLS) {
                System.out.println("Coordinate fuori dal campo.");
                continue;
            }
            if (bombs[r][c]) {
                revealed[r][c] = true;
                gameOver = true;
                System.out.println("BOOM! Hai colpito una mina.");
            } else {
                reveal(r, c);
                if (allSafeRevealed()) {
                    System.out.println("Complimenti! Hai scoperto tutte le caselle sicure.");
                    gameOver = true;
                }
            }
        }
        printBoard(true);
        scanner.close();
    }

    public static void main(String[] args) {
        new CampoMinato().play();
    }
}
