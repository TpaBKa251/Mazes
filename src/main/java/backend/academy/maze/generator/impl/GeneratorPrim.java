package backend.academy.maze.generator.impl;

import backend.academy.maze.generator.GeneratorMaze;
import backend.academy.maze.maze.Cell;
import backend.academy.maze.maze.Coordinate;
import backend.academy.maze.maze.Maze;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс генератора лабиринта, основанный на алгоритме Прима
 */
public class GeneratorPrim extends GeneratorMaze {

    public GeneratorPrim(SecureRandom random, int maxHeight, int maxWidth) {
        super(random, maxHeight, maxWidth);
    }

    @Override
    public Maze generateMaze(int height, int width) {
        validateGenerationMaze(height, width);

        // Размеры лабиринта с учетом рамки вокруг него
        int fullHeight = height + SIZE_DELTA;
        int fullWidth = width + SIZE_DELTA;

        Cell[][] grid = initializeGrid(fullHeight, fullWidth);
        List<Coordinate> walls = new ArrayList<>();

        int startRow = 1;
        int startCol = 1;

        grid[startRow][startCol] = new Cell(startRow, startCol, getRandomCellType());
        addWalls(
            grid,
            walls,
            startRow,
            startCol,
            fullHeight,
            fullWidth
        );

        while (!walls.isEmpty()) {
            Coordinate wall = walls.remove(random.nextInt(walls.size()));
            int row = wall.row();
            int col = wall.col();

            List<Coordinate> neighbors = getNeighbors(grid, row, col);

            // Условие, предотвращающее создание циклов
            if (neighbors.size() == 1) {
                grid[row][col] = new Cell(row, col, getRandomCellType());
                Coordinate neighbor = neighbors.getFirst();

                int newRow = (int) (((long) row + (long) neighbor.row()) / 2);
                int newCol = (int) (((long) col + (long) neighbor.col()) / 2);

                grid[newRow][newCol] = new Cell(newRow, newCol, getRandomCellType());

                addWalls(
                    grid,
                    walls,
                    newRow,
                    newCol,
                    fullHeight,
                    fullWidth
                );
            }
        }

        randomizeWalls(grid, fullHeight, fullWidth);

        return new Maze(fullHeight, fullWidth, grid);
    }

    /**
     * Метод для добавления соседних стен (на расстоянии 2 клеток)
     *
     * @param grid сетка лабиринта
     * @param walls список стен, которые можно использовать для добавления новых проходов
     * @param row строка клетки
     * @param col столбец клетки
     * @param fullHeight полная высота лабиринта
     * @param fullWidth полная ширина лабиринта
     */
    private void addWalls(
        Cell[][] grid,
        List<Coordinate> walls,
        int row,
        int col,
        int fullHeight,
        int fullWidth
    ) {
        if (row - 2 > 0 && grid[row - 2][col].type() == Cell.CellType.WALL) {
            walls.add(new Coordinate(row - 2, col));
        }
        if (row + 2 < fullHeight - 1 && grid[row + 2][col].type() == Cell.CellType.WALL) {
            walls.add(new Coordinate(row + 2, col));
        }
        if (col - 2 > 0 && grid[row][col - 2].type() == Cell.CellType.WALL) {
            walls.add(new Coordinate(row, col - 2));
        }
        if (col + 2 < fullWidth - 1 && grid[row][col + 2].type() == Cell.CellType.WALL) {
            walls.add(new Coordinate(row, col + 2));
        }
    }

    /**
     * Метод для получения соседних клеток (на расстоянии 2 клеток), которые могут быть проходами
     *
     * @param grid сетка лабиринта
     * @param row строка клетки
     * @param col столбец клетки
     *
     * @return список соседей
     */
    private List<Coordinate> getNeighbors(Cell[][] grid, int row, int col) {
        List<Coordinate> neighbors = new ArrayList<>();

        if (row - 2 > 0 && grid[row - 2][col].type() != Cell.CellType.WALL) {
            neighbors.add(new Coordinate(row - 2, col));
        }
        if (row + 2 < grid.length - 1 && grid[row + 2][col].type() != Cell.CellType.WALL) {
            neighbors.add(new Coordinate(row + 2, col));
        }
        if (col - 2 > 0 && grid[row][col - 2].type() != Cell.CellType.WALL) {
            neighbors.add(new Coordinate(row, col - 2));
        }
        if (col + 2 < grid[0].length - 1 && grid[row][col + 2].type() != Cell.CellType.WALL) {
            neighbors.add(new Coordinate(row, col + 2));
        }

        return neighbors;
    }
}
