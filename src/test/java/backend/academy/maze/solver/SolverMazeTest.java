package backend.academy.maze.solver;

import backend.academy.maze.maze.Cell;
import backend.academy.maze.maze.Coordinate;
import backend.academy.maze.maze.Maze;
import backend.academy.maze.solver.impl.SolverBfs;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Тесты класса SolverMaze")
public class SolverMazeTest {

    @DisplayName("Тест валидации стартовой и конечной точек при решении лабиринта")
    @ParameterizedTest(name = "старт: {0}, конец: {1}")
    @MethodSource("provideDataTestValidation")
    void testValidation(Coordinate start, Coordinate finish) {
        Maze maze = createMaze();

        SolverMaze solverMaze = new SolverBfs();

        assertThatThrownBy(() -> solverMaze.solveMaze(maze, start, finish))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Стартовая или конечная координаты недоступны, или они одинаковы.");
    }

    static Stream<Arguments> provideDataTestValidation() {
        return Stream.of(
            Arguments.of(new Coordinate(0, 0), new Coordinate(1, 2)), // Вне рамок
            Arguments.of(new Coordinate(1, 2), new Coordinate(4, 2)), // Вне рамок
            Arguments.of(new Coordinate(1, 1), new Coordinate(1, 2)), // На стене
            Arguments.of(new Coordinate(1, 2), new Coordinate(3, 3)), // На стене
            Arguments.of(new Coordinate(1, 2), new Coordinate(1, 2)) // Равны
        );
    }

    private Maze createMaze() {
        Cell[][] grid = {
                {
                        new Cell(0, 0, Cell.CellType.WALL),
                        new Cell(0, 1, Cell.CellType.WALL),
                        new Cell(0, 2, Cell.CellType.WALL),
                        new Cell(0, 3, Cell.CellType.WALL),
                        new Cell(0, 4, Cell.CellType.WALL)
                },
                {
                        new Cell(1, 0, Cell.CellType.WALL),
                        new Cell(1, 1, Cell.CellType.WALL),
                        new Cell(1, 2, Cell.CellType.PASSAGE),
                        new Cell(1, 3, Cell.CellType.PASSAGE),
                        new Cell(1, 4, Cell.CellType.WALL)
                },
                {
                        new Cell(2, 0, Cell.CellType.WALL),
                        new Cell(2, 1, Cell.CellType.WALL),
                        new Cell(2, 2, Cell.CellType.WALL),
                        new Cell(2, 3, Cell.CellType.PASSAGE),
                        new Cell(2, 4, Cell.CellType.WALL),
                },
                {
                        new Cell(3, 0, Cell.CellType.WALL),
                        new Cell(3, 1, Cell.CellType.PASSAGE),
                        new Cell(3, 2, Cell.CellType.PASSAGE),
                        new Cell(3, 3, Cell.CellType.WALL),
                        new Cell(3, 4, Cell.CellType.WALL),
                },
                {
                        new Cell(4, 0, Cell.CellType.WALL),
                        new Cell(4, 1, Cell.CellType.PASSAGE),
                        new Cell(4, 2, Cell.CellType.PASSAGE),
                        new Cell(4, 3, Cell.CellType.WALL),
                        new Cell(4, 4, Cell.CellType.WALL)
                },
        };

        return new Maze(5, 5, grid);
    }
}
