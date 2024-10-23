package backend.academy.maze.generator.impl;

import backend.academy.maze.generator.GeneratorMaze;
import backend.academy.maze.maze.Cell;
import backend.academy.maze.maze.Maze;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тесты класса GeneratorKruskal")
public class GeneratorKruskalTest {

    @DisplayName("Тест создания непустого лабиринта с корректными размерами")
    @Test
    void testGenerateMaze() {
        GeneratorMaze generatorMaze = new GeneratorKruskal(new SecureRandom(), 100, 100);

        int expectedHeight = 100;
        int expectedWidth = 100;

        Maze maze = generatorMaze.generateMaze(expectedHeight, expectedWidth);

        int actualHeight = maze.height() - 2;
        int actualWidth = maze.width() - 2;

        Set<Cell.CellType> expectedTypes = EnumSet.complementOf(EnumSet.of(Cell.CellType.PATH));
        Set<Cell.CellType> actualTypes = Arrays.stream(maze.grid())
            .flatMap(Arrays::stream)
            .map(Cell::type)
            .collect(Collectors.toSet());

        assertThat(actualHeight).isEqualTo(expectedHeight);
        assertThat(actualWidth).isEqualTo(expectedWidth);
        assertThat(maze.grid()).isNotNull();
        assertThat(Arrays.stream(maze.grid())
            .flatMap(Arrays::stream)
            .allMatch(cell -> cell.type() == Cell.CellType.WALL))
            .isFalse();
        assertThat(actualTypes).containsAll(expectedTypes);
    }
}
