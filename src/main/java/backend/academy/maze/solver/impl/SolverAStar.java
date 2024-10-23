package backend.academy.maze.solver.impl;

import backend.academy.maze.enums.SolverMazeType;
import backend.academy.maze.maze.Cell;
import backend.academy.maze.maze.Coordinate;
import backend.academy.maze.maze.Maze;
import backend.academy.maze.solver.SolverMaze;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Класс решателя лабиринта, основанный на алгоритме A* (A-Star)
 */
public class SolverAStar extends SolverMaze {

    @Override
    public List<Coordinate> solveMaze(Maze maze, Coordinate start, Coordinate finish) {
        validateStartAndFinish(maze, start, finish);

        int height = maze.height();
        int width = maze.width();

        Cell[][] grid = maze.grid();

        Map<Coordinate, Integer> gScores = new HashMap<>(); // стоимость пути от начала до каждой клетки
        // Стоимость пути от начальной до конечной клетки через каждую клетку
        Map<Coordinate, Integer> fScores = new HashMap<>();
        Map<Coordinate, Coordinate> cameFrom = new HashMap<>(); // отслеживание из какой клетки пришли

        // Сначала обрабатываются клетки с наименьшей стоимостью
        PriorityQueue<Coordinate> openQueue = new PriorityQueue<>(Comparator.comparingInt(fScores::get));

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Coordinate coordinate = new Coordinate(row, col);

                gScores.put(coordinate, Integer.MAX_VALUE);
                fScores.put(coordinate, Integer.MAX_VALUE);
            }
        }

        gScores.put(start, 0);
        fScores.put(start, heuristic(start, finish));
        openQueue.add(start);

        while (!openQueue.isEmpty()) {
            Coordinate current = openQueue.poll();

            if (current.equals(finish)) {
                return reconstructPath(cameFrom, finish);
            }

            for (Coordinate neighbor : getNeighborsOfCoordinate(current, height, width)) {
                if (grid[neighbor.row()][neighbor.col()].type() == Cell.CellType.WALL) {
                    continue;
                }

                // Стоимость пути до соседней клетки
                int tentativeGScore = gScores.get(current) + grid[neighbor.row()][neighbor.col()].type().weight();

                if (tentativeGScore < gScores.get(neighbor)) {
                    cameFrom.put(neighbor, current);
                    gScores.put(neighbor, tentativeGScore);
                    fScores.put(neighbor, tentativeGScore + heuristic(neighbor, finish));

                    if (!openQueue.contains(neighbor)) {
                        openQueue.add(neighbor);
                    }
                }
            }
        }

        return Collections.emptyList();
    }

    @Override
    public SolverMazeType getSolverMazeType() {
        return SolverMazeType.A_STAR;
    }

    /**
     * Метод для расчета эвристики - манхэттенского расстояния между точками для нахождения наиболее оптимального пути
     *
     * @param a первая точка
     * @param b вторая точка
     *
     * @return манхэттонское расстояние между точками
     */
    private int heuristic(Coordinate a, Coordinate b) {
        return Math.abs(a.row() - b.row()) + Math.abs(a.col() - b.col());
    }
}
