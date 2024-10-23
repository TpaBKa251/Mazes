package backend.academy.maze.maze;

import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Тесты класса Maze")
public class MazeTest {

    @DisplayName("Тест того, что в списке notWallCells действительно находятся только клетки-не-стены")
    @Test
    void shouldContainsOnlyNotWallCells() {
        Maze maze = Instancio.of(Maze.class)
                .ignore(field(Maze.class, "notWallCells"))
                .create();

        List<Cell> notWallCells = maze.notWallCells();

        assertTrue(notWallCells.stream().noneMatch(cell -> cell.type() == Cell.CellType.WALL));

        long nonWallCellsCount = Arrays.stream(maze.grid())
                .flatMap(Arrays::stream)
                .filter(cell -> cell.type() != Cell.CellType.WALL)
                .count();

        assertEquals(nonWallCellsCount, notWallCells.size());
    }
}
