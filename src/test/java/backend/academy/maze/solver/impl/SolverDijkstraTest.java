package backend.academy.maze.solver.impl;

import backend.academy.maze.maze.Cell;
import backend.academy.maze.maze.Coordinate;
import backend.academy.maze.maze.Maze;
import backend.academy.maze.solver.SolverMaze;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тесты для класса SolverDijkstra")
public class SolverDijkstraTest {

    SolverMaze solver = new SolverDijkstra();
    Maze maze = createMaze();

    @DisplayName("Тест поиска пути, когда путь существует")
    @Test
    void testSolveMazeWhenPathExists() {
        Coordinate start = new Coordinate(1, 1);
        Coordinate finish = new Coordinate(8, 8);
        List<Coordinate> expectedPath = List.of(
                start,
                new Coordinate(1, 2),
                new Coordinate(2, 2),
                new Coordinate(3, 2),
                new Coordinate(4, 2),
                new Coordinate(4, 1),
                new Coordinate(5, 1),
                new Coordinate(6, 1),
                new Coordinate(6, 2),
                new Coordinate(6, 3),
                new Coordinate(7, 3),
                new Coordinate(7, 4),
                new Coordinate(7, 5),
                new Coordinate(7, 6),
                new Coordinate(6, 6),
                new Coordinate(6, 7),
                new Coordinate(6, 8),
                new Coordinate(7, 8),
                finish
        );

        List<Coordinate> actualPath = solver.solveMaze(maze, start, finish);

        assertThat(actualPath).isEqualTo(expectedPath);
    }

    @DisplayName("Тест поиска пути, когда путь НЕ существует")
    @Test
    void testSolveMazeWhenPathDoesNotExist() {
        Coordinate start = new Coordinate(1, 1);
        Coordinate finish = new Coordinate(1, 8);
        List<Coordinate> expectedPath = List.of();

        List<Coordinate> actualPath = solver.solveMaze(maze, start, finish);

        assertThat(actualPath).isEqualTo(expectedPath);
    }

    private Maze createMaze() {
        int height = 10;
        int width = 10;
        Cell[][] grid = new Cell[][] {
                {
                        new Cell(0, 0, Cell.CellType.WALL),
                        new Cell(0, 1, Cell.CellType.WALL),
                        new Cell(0, 2, Cell.CellType.WALL),
                        new Cell(0, 3, Cell.CellType.WALL),
                        new Cell(0, 4, Cell.CellType.WALL),
                        new Cell(0, 5, Cell.CellType.WALL),
                        new Cell(0, 6, Cell.CellType.WALL),
                        new Cell(0, 7, Cell.CellType.WALL),
                        new Cell(0, 8, Cell.CellType.WALL),
                        new Cell(0, 9, Cell.CellType.WALL)
                },
                {
                        new Cell(1, 0, Cell.CellType.WALL),
                        new Cell(1, 1, Cell.CellType.PASSAGE),
                        new Cell(1, 2, Cell.CellType.COIN),
                        new Cell(1, 3, Cell.CellType.WALL),
                        new Cell(1, 4, Cell.CellType.PASSAGE),
                        new Cell(1, 5, Cell.CellType.WATER),
                        new Cell(1, 6, Cell.CellType.ESCALATOR),
                        new Cell(1, 7, Cell.CellType.WALL),
                        new Cell(1, 8, Cell.CellType.PASSAGE),
                        new Cell(1, 9, Cell.CellType.WALL)
                },
                {
                        new Cell(2, 0, Cell.CellType.WALL),
                        new Cell(2, 1, Cell.CellType.WALL),
                        new Cell(2, 2, Cell.CellType.SAND),
                        new Cell(2, 3, Cell.CellType.PASSAGE),
                        new Cell(2, 4, Cell.CellType.SWAMP),
                        new Cell(2, 5, Cell.CellType.WALL),
                        new Cell(2, 6, Cell.CellType.PASSAGE),
                        new Cell(2, 7, Cell.CellType.WALL),
                        new Cell(2, 8, Cell.CellType.SMOOTH_ROAD),
                        new Cell(2, 9, Cell.CellType.WALL)
                },
                {
                        new Cell(3, 0, Cell.CellType.WALL),
                        new Cell(3, 1, Cell.CellType.PASSAGE),
                        new Cell(3, 2, Cell.CellType.COIN),
                        new Cell(3, 3, Cell.CellType.COIN),
                        new Cell(3, 4, Cell.CellType.PASSAGE),
                        new Cell(3, 5, Cell.CellType.ESCALATOR),
                        new Cell(3, 6, Cell.CellType.WALL),
                        new Cell(3, 7, Cell.CellType.PASSAGE),
                        new Cell(3, 8, Cell.CellType.WALL),
                        new Cell(3, 9, Cell.CellType.WALL)
                },
                {
                        new Cell(4, 0, Cell.CellType.WALL),
                        new Cell(4, 1, Cell.CellType.ESCALATOR),
                        new Cell(4, 2, Cell.CellType.PASSAGE),
                        new Cell(4, 3, Cell.CellType.WALL),
                        new Cell(4, 4, Cell.CellType.WALL),
                        new Cell(4, 5, Cell.CellType.SWAMP),
                        new Cell(4, 6, Cell.CellType.WALL),
                        new Cell(4, 7, Cell.CellType.ESCALATOR),
                        new Cell(4, 8, Cell.CellType.WALL),
                        new Cell(4, 9, Cell.CellType.WALL)
                },
                {
                        new Cell(5, 0, Cell.CellType.WALL),
                        new Cell(5, 1, Cell.CellType.SMOOTH_ROAD),
                        new Cell(5, 2, Cell.CellType.WALL),
                        new Cell(5, 3, Cell.CellType.PASSAGE),
                        new Cell(5, 4, Cell.CellType.COIN),
                        new Cell(5, 5, Cell.CellType.WATER),
                        new Cell(5, 6, Cell.CellType.WALL),
                        new Cell(5, 7, Cell.CellType.WALL),
                        new Cell(5, 8, Cell.CellType.SAND),
                        new Cell(5, 9, Cell.CellType.WALL)
                },
                {
                        new Cell(6, 0, Cell.CellType.WALL),
                        new Cell(6, 1, Cell.CellType.ESCALATOR),
                        new Cell(6, 2, Cell.CellType.PASSAGE),
                        new Cell(6, 3, Cell.CellType.COIN),
                        new Cell(6, 4, Cell.CellType.WALL),
                        new Cell(6, 5, Cell.CellType.SAND),
                        new Cell(6, 6, Cell.CellType.SWAMP),
                        new Cell(6, 7, Cell.CellType.PASSAGE),
                        new Cell(6, 8, Cell.CellType.SWAMP),
                        new Cell(6, 9, Cell.CellType.WALL)
                },
                {
                        new Cell(7, 0, Cell.CellType.WALL),
                        new Cell(7, 1, Cell.CellType.WALL),
                        new Cell(7, 2, Cell.CellType.WALL),
                        new Cell(7, 3, Cell.CellType.SMOOTH_ROAD),
                        new Cell(7, 4, Cell.CellType.PASSAGE),
                        new Cell(7, 5, Cell.CellType.ESCALATOR),
                        new Cell(7, 6, Cell.CellType.PASSAGE),
                        new Cell(7, 7, Cell.CellType.WALL),
                        new Cell(7, 8, Cell.CellType.SMOOTH_ROAD),
                        new Cell(7, 9, Cell.CellType.WALL)
                },
                {
                        new Cell(8, 0, Cell.CellType.WALL),
                        new Cell(8, 1, Cell.CellType.WALL),
                        new Cell(8, 2, Cell.CellType.PASSAGE),
                        new Cell(8, 3, Cell.CellType.COIN),
                        new Cell(8, 4, Cell.CellType.WALL),
                        new Cell(8, 5, Cell.CellType.PASSAGE),
                        new Cell(8, 6, Cell.CellType.WALL),
                        new Cell(8, 7, Cell.CellType.WALL),
                        new Cell(8, 8, Cell.CellType.SAND),
                        new Cell(8, 9, Cell.CellType.WALL)
                },
                {
                        new Cell(9, 0, Cell.CellType.WALL),
                        new Cell(9, 1, Cell.CellType.WALL),
                        new Cell(9, 2, Cell.CellType.WALL),
                        new Cell(9, 3, Cell.CellType.WALL),
                        new Cell(9, 4, Cell.CellType.WALL),
                        new Cell(9, 5, Cell.CellType.WALL),
                        new Cell(9, 6, Cell.CellType.WALL),
                        new Cell(9, 7, Cell.CellType.WALL),
                        new Cell(9, 8, Cell.CellType.WALL),
                        new Cell(9, 9, Cell.CellType.WALL)
                }
        };

        return new Maze(height, width, grid);
    }
}
