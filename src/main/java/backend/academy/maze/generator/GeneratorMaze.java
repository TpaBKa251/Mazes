package backend.academy.maze.generator;

import backend.academy.maze.maze.Cell;
import backend.academy.maze.maze.Maze;
import backend.academy.maze.utils.RandomUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Абстрактный класс для генератора лабиринта
 */
@RequiredArgsConstructor
@Getter
public abstract class GeneratorMaze {

    private final static int MIN_SIZE = 3;
    protected static final int SIZE_DELTA = 2;
    private static final int RANDOM_BOUND = 100;
    private final static int ESCALATOR_CHANCE = 14;
    private final static int COINS_CHANCE = 28;
    private final static int SMOOTH_ROAD_CHANCE = 42;
    private final static int PASSAGE_CHANCE = 57;
    private final static int SAND_CHANCE = 71;
    private final static int WATER_CHANCE = 85;
    private final static int SWAMP_CHANCE = 99;
    private final static int REMOVE_WALL_CHANCE = 33;

    /**
     * Максимальная высота лабиринта. Определяется размером терминала
     */
    private final int maxHeight;
    /**
     * Максимальная ширина лабиринта. Определяется размером терминала
     */
    private final int maxWidth;

    /**
     * Метод для генерации лабиринта по одному из алгоритмов генерации
     *
     * @param maxHeight максимальная высота лабиринта
     * @param maxWidth максимальная ширина лабиринта
     *
     * @return лабиринт
     */
    public abstract Maze generateMaze(int maxHeight, int maxWidth);

    /**
     * Метод для валидации размеров лабиринта.
     * Должны находится в пределах минимального (3) и максимального (задается размером терминала) значений
     *
     * @param height введенная высота лабиринта
     * @param width введенная ширина лабиринта
     */
    protected void validateGenerationMaze(int height, int width) {
        if (width < MIN_SIZE
            || height < MIN_SIZE
            || width > maxWidth
            || height > maxHeight
        ) {
            throw new IllegalArgumentException(String.format(
                "Высота и ширина лабиринта должны быть больше %d, ширина меньше или равно %d, "
                    + "а высота меньше или равно %d.",
                MIN_SIZE, maxWidth, maxHeight));
        }
    }

    /**
     * Метод для инициализации сетки лабиринта. Изначально она заполняется стенами
     *
     * @param height высота лабиринта
     * @param width ширина лабиринта
     *
     * @return сетку лабиринта
     */
    protected Cell[][] initializeGrid(int height, int width) {
        Cell[][] grid = new Cell[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                grid[row][col] = new Cell(row, col, Cell.CellType.WALL);
            }
        }

        return grid;
    }

    /**
     * Метод для замены случайных стен на проходы (поверхности) в предпоследних строке и столбце
     *
     * @param grid сетка лабиринта
     * @param fullHeight полная высота лабиринта
     * @param fullWidth полная ширина лабиринта
     * @param height введенная высота лабиринта
     * @param width введенная ширина лабиринта
     * @param isWeighted флаг о том, что лабиринт взвешенный
     */
    protected void removeRandomWalls(
        Cell[][] grid,
        int fullHeight,
        int fullWidth,
        int height,
        int width,
        boolean isWeighted
    ) {
        // Если введенная высота четная,
        // то нижний край лабиринта будет иметь двойные стены (рамка + последняя строка лабиринта)
        if (height % 2 == 0) {
            for (int col = 1; col < fullWidth - 1; col++) {
                if (grid[fullHeight - 2][col].type() == Cell.CellType.WALL && RandomUtil.getRandomBoolean()) {
                    grid[fullHeight - 2][col] = new Cell(fullHeight - 2, col, isWeighted ? getRandomCellType()
                        : Cell.CellType.PASSAGE);
                }
            }
        }

        // Если введенная ширина четная,
        // то правый край лабиринта будет иметь двойные стены (рамка + последний столбец лабиринта)
        if (width % 2 == 0) {
            for (int row = 1; row < fullHeight - 1; row++) {
                if (grid[row][fullWidth - 2].type() == Cell.CellType.WALL && RandomUtil.getRandomBoolean()) {
                    grid[row][fullWidth - 2] = new Cell(row, fullWidth - 2, isWeighted ? getRandomCellType()
                        : Cell.CellType.PASSAGE);
                }
            }
        }
    }

    /**
     * Метод для рандомизации стен (замена случайных стен на проходы/поверхности во всем лабиринте).
     * Нужен для создания не идеального лабиринта
     *
     * @param grid сетка лабиринта
     * @param height высота лабиринта
     * @param width ширина лабиринта
     */
    protected void randomizeWalls(Cell[][] grid, int height, int width) {
        for (int row = 1; row < height - 1; row++) {
            for (int col = 1; col < width - 1; col++) {
                if (grid[row][col].type() == Cell.CellType.WALL
                    && RandomUtil.getRandomInt(RANDOM_BOUND) < REMOVE_WALL_CHANCE) {
                    grid[row][col] = new Cell(row, col, getRandomCellType());
                }
            }
        }
    }

    /**
     * Метод для получения случайной поверхности клетки (в том числе проход)
     *
     * @return тип клетки (поверхности)
     */
    protected Cell.CellType getRandomCellType() {
        int randomValue = RandomUtil.getRandomInt(RANDOM_BOUND);

        Cell.CellType cellType;

        if (randomValue < ESCALATOR_CHANCE) {
            cellType = Cell.CellType.ESCALATOR;
        } else if (randomValue < COINS_CHANCE) {
            cellType = Cell.CellType.COIN;
        } else if (randomValue < SMOOTH_ROAD_CHANCE) {
            cellType = Cell.CellType.SMOOTH_ROAD;
        } else if (randomValue < PASSAGE_CHANCE) {
            cellType = Cell.CellType.PASSAGE;
        } else if (randomValue < SAND_CHANCE) {
            cellType = Cell.CellType.SAND;
        } else if (randomValue < WATER_CHANCE) {
            cellType = Cell.CellType.WATER;
        } else if (randomValue < SWAMP_CHANCE) {
            cellType = Cell.CellType.SWAMP;
        } else {
            cellType = Cell.CellType.PASSAGE;
        }

        return cellType;
    }
}
