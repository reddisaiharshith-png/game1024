import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    public static final int SIZE = 4;
    public static final int TARGET = 1024;

    private final int[][] grid = new int[SIZE][SIZE];
    private final Random random = new Random();
    private int score = 0;
    private boolean won = false;

    public Board() {
        reset();
    }

    public void reset() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                grid[r][c] = 0;
            }
        }
        score = 0;
        won = false;
        spawnTile();
        spawnTile();
    }

    public int[][] getGrid() {
        return grid;
    }

    public int getScore() {
        return score;
    }

    public boolean isWon() {
        return won;
    }

    public void spawnTile() {
        List<int[]> emptyCells = new ArrayList<>();
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (grid[r][c] == 0) {
                    emptyCells.add(new int[]{r, c});
                }
            }
        }

        if (!emptyCells.isEmpty()) {
            int[] cell = emptyCells.get(random.nextInt(emptyCells.size()));
            grid[cell[0]][cell[1]] = (random.nextDouble() < 0.9) ? 1 : 2;
        }
    }

   // Slide and merge a single 1D array to the left (Endless Mode)
    private boolean slideAndMergeRow(int[] row) {
        boolean changed = false;

        // 1. Shift non-zeros left
        int[] temp = new int[SIZE];
        int writeIdx = 0;
        for (int val : row) {
            if (val != 0) temp[writeIdx++] = val;
        }

        // 2. Merge identical adjacent values
        for (int i = 0; i < SIZE - 1; i++) {
            if (temp[i] != 0 && temp[i] == temp[i + 1]) {
                temp[i] *= 2;
                score += temp[i];
                // In endless mode, we don't halt when reaching 1024
                temp[i + 1] = 0;
                i++; // Skip next element
            }
        }

        // 3. Re-compress left after merges
        int[] finalRow = new int[SIZE];
        writeIdx = 0;
        for (int val : temp) {
            if (val != 0) finalRow[writeIdx++] = val;
        }

        for (int i = 0; i < SIZE; i++) {
            if (row[i] != finalRow[i]) changed = true;
            row[i] = finalRow[i];
        }

        return changed;
    }

    public boolean moveLeft() {
        boolean moved = false;
        for (int r = 0; r < SIZE; r++) {
            if (slideAndMergeRow(grid[r])) moved = true;
        }
        if (moved) spawnTile();
        return moved;
    }

    public boolean moveRight() {
        boolean moved = false;
        for (int r = 0; r < SIZE; r++) {
            reverse(grid[r]);
            if (slideAndMergeRow(grid[r])) moved = true;
            reverse(grid[r]);
        }
        if (moved) spawnTile();
        return moved;
    }

    public boolean moveUp() {
        boolean moved = false;
        for (int c = 0; c < SIZE; c++) {
            int[] col = new int[SIZE];
            for (int r = 0; r < SIZE; r++) col[r] = grid[r][c];
            if (slideAndMergeRow(col)) moved = true;
            for (int r = 0; r < SIZE; r++) grid[r][c] = col[r];
        }
        if (moved) spawnTile();
        return moved;
    }

    public boolean moveDown() {
        boolean moved = false;
        for (int c = 0; c < SIZE; c++) {
            int[] col = new int[SIZE];
            for (int r = 0; r < SIZE; r++) col[r] = grid[r][c];
            reverse(col);
            if (slideAndMergeRow(col)) moved = true;
            reverse(col);
            for (int r = 0; r < SIZE; r++) grid[r][c] = col[r];
        }
        if (moved) spawnTile();
        return moved;
    }

    private void reverse(int[] arr) {
        for (int i = 0; i < arr.length / 2; i++) {
            int tmp = arr[i];
            arr[i] = arr[arr.length - 1 - i];
            arr[arr.length - 1 - i] = tmp;
        }
    }

    public boolean isGameOver() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (grid[r][c] == 0) return false;
                if (c + 1 < SIZE && grid[r][c] == grid[r][c + 1]) return false;
                if (r + 1 < SIZE && grid[r][c] == grid[r + 1][c]) return false;
            }
        }
        return true;
    }
}