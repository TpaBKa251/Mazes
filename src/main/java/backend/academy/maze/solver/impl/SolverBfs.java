package backend.academy.maze.solver.impl;

import backend.academy.maze.enums.SolverMazeType;
import backend.academy.maze.maze.Cell;
import backend.academy.maze.maze.Coordinate;
import backend.academy.maze.maze.Maze;
import backend.academy.maze.solver.SolverMaze;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Класс решателя лабиринта, основанный на алгоритме обхода в ширину
 */
public class SolverBfs extends SolverMaze {

    @Override
    public List<Coordinate> solveMaze(Maze maze, Coordinate start, Coordinate finish) {
        validateStartAndFinish(maze, start, finish);

        // Очередь координат, которые нужно проверить
        Queue<Coordinate> coordinateQueue = new LinkedList<>();
        // Карта родительских клеток
        Map<Coordinate, Coordinate> parentCoordinateMap = new HashMap<>();

        coordinateQueue.add(start);
        parentCoordinateMap.put(start, null);

        // вверх, вниз, влево, вправо
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        while (!coordinateQueue.isEmpty()) {
            Coordinate current = coordinateQueue.poll();

            if (current.equals(finish)) {
                break;
            }

            for (int[] direction : directions) {
                int rowDirection = 0;
                int colDirection = 1;
                int newRow = current.row() + direction[rowDirection];
                int newCol = current.col() + direction[colDirection];

                Coordinate newCoordinate = new Coordinate(newRow, newCol);

                if (!isCoordinateNotInBounds(newCoordinate, maze)
                    && maze.grid()[newRow][newCol].type() != Cell.CellType.WALL
                    && !parentCoordinateMap.containsKey(newCoordinate)
                ) {
                    coordinateQueue.add(newCoordinate);
                    parentCoordinateMap.put(newCoordinate, current);
                }
            }
        }

        List<Coordinate> path = new ArrayList<>();

        for (Coordinate at = finish; at != null; at = parentCoordinateMap.get(at)) {
            path.add(at);
        }

        if (path.isEmpty() || !path.getLast().equals(start)) {
            return List.of();
        }

        Collections.reverse(path);

        return path;
    }

    @Override
    public SolverMazeType getSolverMazeType() {
        return SolverMazeType.BFS;
    }
}
