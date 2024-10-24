package backend.academy.maze.parser;

import backend.academy.maze.maze.Cell;
import backend.academy.maze.maze.Coordinate;
import backend.academy.maze.maze.Maze;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.util.Scanner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Тесты парсера ParserMazeParameters")
@ExtendWith(MockitoExtension.class)
public class ParserMazeParametersTest {

    String red = "\033[31m";
    String reset = "\033[0m";

    @Spy
    Maze maze = createMaze();

    @Mock
    Scanner scanner;

    @Mock
    PrintWriter writer;

    @Mock
    SecureRandom random;

    @InjectMocks
    ParserMazeParameters parserMazeParameters;

    @DisplayName("Тест парсинга параметров размера лабиринта при валидном вводе")
    @Test
    void testParseMazeSizeParameterWhenInputValid() {
        int expectedHeight = 10;
        int expectedWidth = 15;

        when(scanner.nextLine()).thenReturn("10", "15");

        int actualHeight = parserMazeParameters.parseMazeSizeParameter(10, 15, false);
        int actualWidth = parserMazeParameters.parseMazeSizeParameter(10, 15, true);

        assertThat(actualHeight).isEqualTo(expectedHeight);
        assertThat(actualWidth).isEqualTo(expectedWidth);
    }

    @DisplayName("Тест случайного выбора параметров размера")
    @Test
    void testParseMazeSizeParameterWithRandomChoice() {
        int expectedHeight = 10;
        int expectedWidth = 15;

        when(scanner.nextLine()).thenReturn("", "");
        when(random.nextInt(3, 11)).thenReturn(10);
        when(random.nextInt(3, 16)).thenReturn(15);

        int actualHeight = parserMazeParameters.parseMazeSizeParameter(10, 15, false);
        int actualWidth = parserMazeParameters.parseMazeSizeParameter(10, 15, true);

        assertThat(actualHeight).isEqualTo(expectedHeight);
        assertThat(actualWidth).isEqualTo(expectedWidth);
    }

    @DisplayName("Тест парсинга стартовой и конечной точек при валидном вводе")
    @Test
    void testParsePathCoordinatesWhenInputValid() {
        Coordinate expectedStartCoordinate = new Coordinate(1, 1);
        Coordinate expectedFinishCoordinate = new Coordinate(8, 8);

        when(scanner.nextLine()).thenReturn("1 1", "8 8");

        Coordinate actualStartCoordinate = parserMazeParameters.parseStartCoordinate(maze);
        Coordinate actualFinishCoordinate = parserMazeParameters.parseFinishCoordinate(maze, actualStartCoordinate);

        assertThat(actualStartCoordinate).isEqualTo(expectedStartCoordinate);
        assertThat(actualFinishCoordinate).isEqualTo(expectedFinishCoordinate);
    }

    @DisplayName("Тест случайного выбора стартовой и конечной точек")
    @Test
    void testParsePathCoordinatesWithRandomChoice() {
        Coordinate expectedStartCoordinate = new Coordinate(1, 1);
        Coordinate expectedFinishCoordinate = new Coordinate(8, 8);

        when(scanner.nextLine()).thenReturn("", "");
        when(maze.getRandomCoordinate(random))
            .thenReturn(new Coordinate(1, 1), new Coordinate(8, 8));

        Coordinate actualStartCoordinate = parserMazeParameters.parseStartCoordinate(maze);
        Coordinate actualFinishCoordinate = parserMazeParameters.parseFinishCoordinate(maze, actualStartCoordinate);

        assertThat(actualStartCoordinate).isEqualTo(expectedStartCoordinate);
        assertThat(actualFinishCoordinate).isEqualTo(expectedFinishCoordinate);
    }

    @DisplayName("Тест парсинга параметров лабиринта при невалидном вводе")
    @Test
    void testParseMazeSizeParameterWhenInputInvalid() {
        // Должен засчитаться именно валидный ввод
        int expectedHeight = 10;
        int expectedWidth = 15;

        // 3 невалидных ввода и 1 валидный для выхода из цикла
        when(scanner.nextLine())
            .thenReturn("11", "2", "десять", "10")
            .thenReturn("16", "2", "пятнадцать", "15");

        int actualHeight = parserMazeParameters.parseMazeSizeParameter(10, 15, false);
        int actualWidth = parserMazeParameters.parseMazeSizeParameter(10, 15, true);

        verify(writer, times(2))
            .println(red + "Размер лабиринта должен быть от 3 до 10." + reset);
        verify(writer, times(2))
            .println(red + "Размер лабиринта должен быть от 3 до 15." + reset);
        verify(writer, times(2))
            .println(red + "Размеры лабиринта задаются числами." + reset);

        assertThat(actualHeight).isEqualTo(expectedHeight);
        assertThat(actualWidth).isEqualTo(expectedWidth);
    }

    @DisplayName("Тест парсинга стартовой и конечной точек при невалидном вводе")
    @Test
    void testParsePathCoordinatesWhenInputInvalid() {
        // Должен засчитаться именно валидный ввод
        Coordinate expectedStartCoordinate = new Coordinate(1, 1);
        Coordinate expectedFinishCoordinate = new Coordinate(8, 8);

        // Вначале невалидные вводы, в конце валидный для выхода из цикла
        when(scanner.nextLine())
            .thenReturn("1","0 0", "0 1", "1 0", "1 3", "asd", "one one", "1 1") // 1 3 - стена
            // все невалидные варианты проверены выше, здесь только равенство старта и конца
            .thenReturn("1 1", "8 8");

        Coordinate actualStartCoordinate = parserMazeParameters.parseStartCoordinate(maze);
        Coordinate actualFinishCoordinate = parserMazeParameters.parseFinishCoordinate(maze, actualStartCoordinate);

        verify(writer, times(2))
            .println(red + "Координата задается двумя значениями через пробел - номер строки и номер столбца." + reset);
        verify(writer, times(4))
            .println(red + "Координата недоступна." + reset);
        verify(writer, times(1))
            .println(red + "Стартовая и конченая координаты одинаковы." + reset);
        verify(writer, times(1))
            .println(red + "Введите координату в виде двух чисел через пробел."
                + " Координаты должны быть в пределах лабиринта." + reset);

        assertThat(actualStartCoordinate).isEqualTo(expectedStartCoordinate);
        assertThat(actualFinishCoordinate).isEqualTo(expectedFinishCoordinate);
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
