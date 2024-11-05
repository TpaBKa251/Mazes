package backend.academy.maze.maze;

import backend.academy.maze.utils.RandomUtil;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Класс лабиринта
 */
@RequiredArgsConstructor
public class Maze {

    @Getter
    private final int height;
    @Getter
    private final int width;
    @Getter
    private final Cell[][] grid;

    private List<Cell> notWallCells = null;

    /**
     * Метод для получения случайного прохода/поверхности в лабиринте
     *
     * @param random рандомайзер
     *
     * @return клетку-проход/поверхность лабиринта
     */
    public Coordinate getRandomCoordinate() {
        initializeNotWallCells();

        Cell randomCell = notWallCells.get(RandomUtil.getRandomInt(notWallCells.size()));

        return new Coordinate(randomCell.row(), randomCell.col());
    }

    public List<Cell> notWallCells() {
        initializeNotWallCells();

        return notWallCells;
    }

    /**
     * Метод для инициализации списка клеток-не-стен
     */
    private void initializeNotWallCells() {
        if (notWallCells == null || notWallCells.isEmpty()) {
            notWallCells = Arrays.stream(grid)
                    .flatMap(Arrays::stream)
                    .filter(cell -> cell.type() != Cell.CellType.WALL)
                    .toList();
        }
    }
}
