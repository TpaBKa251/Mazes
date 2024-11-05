package backend.academy.maze.generator.impl;

import backend.academy.maze.generator.GeneratorMaze;
import backend.academy.maze.maze.Cell;
import backend.academy.maze.maze.Coordinate;
import backend.academy.maze.maze.Maze;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Класс генератора лабиринта, основанный на алгоритме Краскала
 */
public class GeneratorKruskal extends GeneratorMaze {

    public GeneratorKruskal(int maxHeight, int maxWidth) {
        super(maxHeight, maxWidth);
    }

    @Override
    public Maze generateMaze(int height, int width) {
        validateGenerationMaze(height, width);

        // Размеры лабиринта с учетом рамки вокруг него
        int fullHeight = height + SIZE_DELTA;
        int fullWidth = width + SIZE_DELTA;

        Cell[][] grid = initializeGrid(fullHeight, fullWidth);

        DisjointSetUnion dsu = new DisjointSetUnion(fullHeight, fullWidth);
        List<Edge> walls = generateWalls(grid, fullHeight, fullWidth);

        replaceWallsWithPassages(walls, grid, dsu);
        // Если введенная высота или ширина четные,
        // то последняя строка или столбец соответственно будут иметь "двойные стены"
        removeRandomWalls(
            grid,
            fullHeight,
            fullWidth,
            height,
            width,
            true
        );

        randomizeWalls(grid, fullHeight, fullWidth);

        return new Maze(fullHeight, fullWidth, grid);
    }

    /**
     * Метод для создания списка стен лабиринта в виде ребер (стены между двумя клетками)
     *
     * @param grid сетка лабиринта
     * @param fullHeight полная высота лабиринта
     * @param fullWidth полная ширина лабиринта
     *
     * @return список стен в виде ребер
     */
    private List<Edge> generateWalls(Cell[][] grid, int fullHeight, int fullWidth) {
        List<Edge> walls = new ArrayList<>();

        for (int row = 1; row < fullHeight - 1; row += 2) {
            for (int col = 1; col < fullWidth - 1; col += 2) {
                grid[row][col] = new Cell(row, col, getRandomCellType());

                if (row + 2 < fullHeight - 1) {
                    walls.add(new Edge(new Coordinate(row, col), new Coordinate(row + 2, col)));
                }
                if (col + 2 < fullWidth - 1) {
                    walls.add(new Edge(new Coordinate(row, col), new Coordinate(row, col + 2)));
                }
            }
        }

        Collections.shuffle(walls);

        return walls;
    }

    /**
     * Метод для замены стен на проходы (в том числе поверхности), не создавая циклы
     *
     * @param walls список стен в виде ребер
     * @param grid сетка лабиринта
     * @param dsu система непересекающихся множеств
     */
    private void replaceWallsWithPassages(List<Edge> walls, Cell[][] grid, DisjointSetUnion dsu) {
        for (Edge wall : walls) {
            Coordinate cell1 = wall.firstCell();
            Coordinate cell2 = wall.secondCell();

            if (dsu.find(cell1.row(), cell1.col()) != dsu.find(cell2.row(), cell2.col())) {
                dsu.union(
                    cell1.row(),
                    cell1.col(),
                    cell2.row(),
                    cell2.col()
                );

                int passageRow = (int) (((long) cell1.row() + (long) cell2.row()) / 2);
                int passageCol = (int) (((long) cell1.col() + (long) cell2.col()) / 2);

                grid[passageRow][passageCol] = new Cell(passageRow, passageCol, getRandomCellType());
            }
        }
    }

    /**
     * Вложенный рекорд ребра лабиринта
     *
     * @param firstCell первая точка ребра
     * @param secondCell вторая точка ребра
     */
    private record Edge(Coordinate firstCell, Coordinate secondCell) {
    }

    /**
     * Система непересекающихся множеств. Реализует тип данных для объединения и поиска множеств
     */
    private static final class DisjointSetUnion {
        /**
         * Массив, где элемент указывает на родителя или самого себя, если это корень
         */
        private final int[] parent;
        /**
         * Массив с высотой (глубиной, рангом) деревьев
         */
        private final int[] rank;
        /**
         * Количество столбцов
         */
        private final int cols;

        /**
         * Конструктор для системы неперсекающихся множеств
         *
         * @param height высота лабиринта
         * @param width ширина лабиринта
         */
        private DisjointSetUnion(int height, int width) {
            int size = height * width;
            this.cols = width;
            parent = new int[size];
            rank = new int[size];

            for (int i = 0; i < size; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        /**
         * Метод для получения корня дерева. Использует сжатие пути для оптимизации поиска
         *
         * @param row строка клети в дереве
         * @param col столбец клетки в дереве
         *
         * @return корень дерева
         */
        private int find(int row, int col) {
            int index = getIndex(row, col);

            // Когда индекс массива равен элементу с этим индексом - корень найден
            if (parent[index] != index) {
                parent[index] = find(parent[index] / cols, parent[index] % cols);
            }

            return parent[index];
        }

        /**
         * Метод для объединения деревьев по корням, используя ранги.
         * Дерево с меньшим рангом объединяется с деревом с большим рангом
         *
         * @param fistTreeRow строка клетки первого дерева
         * @param firstTreeCol столбец клетки первого дерева
         * @param secondTreeRow строка клетки второго дерева
         * @param secondTreeCol столбец клетки второго дерева
         */
        private void union(
            int fistTreeRow,
            int firstTreeCol,
            int secondTreeRow,
            int secondTreeCol
        ) {
            int root1 = find(fistTreeRow, firstTreeCol);
            int root2 = find(secondTreeRow, secondTreeCol);

            if (root1 != root2) {
                if (rank[root1] > rank[root2]) {
                    parent[root2] = root1;
                } else if (rank[root1] < rank[root2]) {
                    parent[root1] = root2;
                } else {
                    parent[root2] = root1;
                    rank[root1]++;
                }
            }
        }

        /**
         * Метод для получения индекса клетки на основе ее строки и столбца
         *
         * @param row строка клетки
         * @param col столбец клетки
         *
         * @return индекс клетки
         */
        private int getIndex(int row, int col) {
            return row * cols + col;
        }
    }
}

