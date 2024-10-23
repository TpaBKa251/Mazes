package backend.academy.maze.generator.impl;

import backend.academy.maze.generator.GeneratorMaze;
import backend.academy.maze.maze.Cell;
import backend.academy.maze.maze.Maze;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Класс генератора лабиринта, основанный на алгоритме обхода в глубину
 */
public class GeneratorDfs extends GeneratorMaze {

    private static final int NEGATIVE_DIRECTION = -2;
    private static final int POSITIVE_DIRECTION = 2;

    public GeneratorDfs(SecureRandom random, int maxHeight, int maxWidth) {
        super(random, maxHeight, maxWidth);
    }

    @Override
    public Maze generateMaze(int height, int width) {
        validateGenerationMaze(height, width);

        // Размеры лабиринта с учетом рамки вокруг него
        int fullHeight = height + SIZE_DELTA;
        int fullWidth = width + SIZE_DELTA;

        Cell[][] grid = initializeGrid(fullHeight, fullWidth);

        int startRow = (random.nextInt(height / 2) * 2) + 1;
        int startCol = (random.nextInt(width / 2) * 2) + 1;

        dfs(
            grid,
            startRow,
            startCol,
            fullHeight,
            fullWidth
        );

        // Если введенная высота или ширина четные,
        // то нижний или правый край лабиринта соответственно будут иметь "двойные стены"
        removeRandomWalls(
            grid,
            fullHeight,
            fullWidth,
            height,
            width,
            false
        );

        return new Maze(fullHeight, fullWidth, grid);
    }

    /**
     * Обход в глубину
     *
     * @param grid сетка лабиринта
     * @param row строка
     * @param col столбец
     * @param height высота
     * @param width ширина
     */
    private static void dfs(
        Cell[][] grid,
        int row,
        int col,
        int height,
        int width
    ) {
        grid[row][col] = new Cell(row, col, Cell.CellType.PASSAGE);

        List<int[]> directions = new ArrayList<>();

        directions.add(new int[]{NEGATIVE_DIRECTION, 0}); // вверх
        directions.add(new int[]{POSITIVE_DIRECTION, 0}); // вниз
        directions.add(new int[]{0, NEGATIVE_DIRECTION}); // влево
        directions.add(new int[]{0, POSITIVE_DIRECTION}); // вправо

        Collections.shuffle(directions);

        for (int[] direction : directions) {
            int directionRow = 0;
            int directionCol = 1;
            int newRow = row + direction[directionRow];
            int newCol = col + direction[directionCol];

            if (newRow > 0
                && newRow < height - 1
                && newCol > 0
                && newCol < width - 1
            ) {
                if (grid[newRow][newCol].type() == Cell.CellType.WALL) {
                    int midRow = (int) (((long) row + (long) newRow) / 2);
                    int midCol = (int) (((long) col + (long) newCol) / 2);

                    grid[midRow][midCol] = new Cell(
                        midRow,
                        midCol,
                        Cell.CellType.PASSAGE
                    );

                    dfs(
                        grid,
                        newRow,
                        newCol,
                        height,
                        width
                    );
                }
            }
        }
    }
}
