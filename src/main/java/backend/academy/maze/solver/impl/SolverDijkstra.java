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
 * Класс решателя лабиринта, основанный на алгоритме Дейкстры
 */
public class SolverDijkstra extends SolverMaze {

    @Override
    public List<Coordinate> solveMaze(Maze maze, Coordinate start, Coordinate finish) {
        validateStartAndFinish(maze, start, finish);

        int height = maze.height();
        int width = maze.width();

        Cell[][] grid = maze.grid();

        // Карта с минимальным расстоянием от старта до каждой точки
        Map<Coordinate, Integer> distances = new HashMap<>();
        // Карта родителей для каждой точки
        Map<Coordinate, Coordinate> parent = new HashMap<>();
        // Выбирается клетка с наименьшим весом
        PriorityQueue<Coordinate> coordinateQueue = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Coordinate coordinate = new Coordinate(row, col);

                distances.put(coordinate, Integer.MAX_VALUE);
            }
        }

        distances.put(start, 0);
        coordinateQueue.add(start);

        while (!coordinateQueue.isEmpty()) {
            Coordinate current = coordinateQueue.poll();

            if (current.equals(finish)) {
                return reconstructPath(parent, finish);
            }

            for (Coordinate neighbor : getNeighborsOfCoordinate(current, height, width)) {
                if (grid[neighbor.row()][neighbor.col()].type() == Cell.CellType.WALL) {
                    continue;
                }

                int newDist = distances.get(current) + grid[neighbor.row()][neighbor.col()].type().weight();

                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    parent.put(neighbor, current);
                    coordinateQueue.add(neighbor);
                }
            }
        }

        return Collections.emptyList();
    }

    @Override
    public SolverMazeType getSolverMazeType() {
        return SolverMazeType.DIJKSTRA;
    }
}

