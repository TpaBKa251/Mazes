package backend.academy.maze.generator.impl;

import backend.academy.maze.generator.GeneratorMaze;
import backend.academy.maze.maze.Cell;
import backend.academy.maze.maze.Maze;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тесты класса GeneratorDfs")
public class GeneratorDfsTest {

    @DisplayName("Тест создания непустого лабиринта с корректными размерами")
    @Test
    void testGenerateMaze() {
        GeneratorMaze generatorMaze = new GeneratorDfs(100, 100);

        int expectedHeight = 10;
        int expectedWidth = 15;

        Maze maze = generatorMaze.generateMaze(expectedHeight, expectedWidth);

        int actualHeight = maze.height() - 2;
        int actualWidth = maze.width() - 2;

        assertThat(actualHeight).isEqualTo(expectedHeight);
        assertThat(actualWidth).isEqualTo(expectedWidth);
        assertThat(maze.grid()).isNotNull();
        assertThat(Arrays.stream(maze.grid())
            .flatMap(Arrays::stream)
            .allMatch(cell -> cell.type() == Cell.CellType.WALL))
            .isFalse();
        assertThat(Arrays.stream(maze.grid())
            .flatMap(Arrays::stream)
            .allMatch(cell -> cell.type() == Cell.CellType.WALL || cell.type() == Cell.CellType.PASSAGE))
            .isTrue();
    }
}
