package backend.academy.maze.solver;

import backend.academy.maze.enums.SolverMazeType;
import backend.academy.maze.maze.Cell;
import backend.academy.maze.maze.Coordinate;
import backend.academy.maze.maze.Maze;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Абстрактный класс для решателя лабиринта
 */
public abstract class SolverMaze {

    /**
     * Метод для поиска пути в лабиринте по одному из алгоритмов от стартовой до конечной точки
     *
     * @param maze лабиринт
     * @param start стартовая координата
     * @param finish конечная координата
     *
     * @return список координат, составляющих путь в лабиринте
     */
    public abstract List<Coordinate> solveMaze(Maze maze, Coordinate start, Coordinate finish);

    /**
     * Метод для получения типа алгоритма поиска пути в лабиринте
     *
     * @return тип алгоритма
     */
    public abstract SolverMazeType getSolverMazeType();

    /**
     * Метод валидации стартовой и конечной координат пути.
     * Они должны находится в лабиринте, не на стенах и не быть равными
     *
     * @param maze лабиринт
     * @param start стартовая координата
     * @param finish конечная координата
     */
    protected void validateStartAndFinish(Maze maze, Coordinate start, Coordinate finish) {
        if (isCoordinateNotInBounds(start, maze) || isCoordinateNotInBounds(finish, maze)
            || maze.grid()[start.row()][start.col()].type() == Cell.CellType.WALL
            || maze.grid()[finish.row()][finish.col()].type() == Cell.CellType.WALL
            || start.equals(finish)
        ) {
            throw new IllegalArgumentException("Стартовая или конечная координаты недоступны, или они одинаковы.");
        }
    }

    /**
     * Вспомогательный метод для метода валидации
     * {@link SolverMaze#validateStartAndFinish(Maze, Coordinate, Coordinate)}
     * для определения, что координата находится в рамках лабиринта.
     *
     * @param coordinate координата
     * @param maze лабиринт
     *
     * @return {@code true}, если координата ВНЕ рамках лабиринта
     * <p>{@code false}, если координата в рамках лабиринте
     */
    protected boolean isCoordinateNotInBounds(Coordinate coordinate, Maze maze) {
        return coordinate.row() < 1 || coordinate.row() >= maze.height() - 1
            || coordinate.col() < 1 || coordinate.col() >= maze.width() - 1;
    }

    /**
     * Метод для получения соседних клеток
     *
     * @param coordinate координата клетки, для которой нужно получить соседей
     * @param height высота лабиринта
     * @param width ширина лабиринта
     *
     * @return список клеток-соседей
     */
    protected List<Coordinate> getNeighborsOfCoordinate(Coordinate coordinate, int height, int width) {
        List<Coordinate> neighbors = new ArrayList<>();
        int row = coordinate.row();
        int col = coordinate.col();

        if (row > 0) {
            neighbors.add(new Coordinate(row - 1, col)); // вверх
        }
        if (row < height - 1) {
            neighbors.add(new Coordinate(row + 1, col)); // вниз
        }
        if (col > 0) {
            neighbors.add(new Coordinate(row, col - 1)); // влево
        }
        if (col < width - 1) {
            neighbors.add(new Coordinate(row, col + 1)); // вправо
        }

        return neighbors;
    }

    /**
     * Метод для реконструкции пути. Собирает путь по координатам и разворачивает
     *
     * @param previous карта для отслеживания из какой клетки пришли в текущую
     * @param finish конечная точка
     *
     * @return список координат пути
     */
    protected List<Coordinate> reconstructPath(Map<Coordinate, Coordinate> previous, Coordinate finish) {
        List<Coordinate> path = new LinkedList<>();

        for (Coordinate at = finish; at != null; at = previous.get(at)) {
            path.add(at);
        }

        Collections.reverse(path);

        return path;
    }
}
