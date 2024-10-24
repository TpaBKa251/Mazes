package backend.academy.maze;

import backend.academy.maze.enums.SolverMazeType;
import backend.academy.maze.factory.SolverMazeFactory;
import backend.academy.maze.generator.GeneratorMaze;
import backend.academy.maze.maze.Coordinate;
import backend.academy.maze.maze.Maze;
import backend.academy.maze.parser.ParserMazeParameters;
import backend.academy.maze.renderer.RendererMaze;
import backend.academy.maze.solver.SolverMaze;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Тесты класса MazeHandler")
@ExtendWith(MockitoExtension.class)
public class MazeHandlerTest {

    @Mock
    ParserMazeParameters parserMazeParameters;

    @Mock
    Scanner scanner;

    @Mock
    PrintWriter printWriter;

    @Mock
    Maze maze;

    @Mock
    GeneratorMaze generatorMaze;

    @Mock
    RendererMaze rendererMaze;

    @InjectMocks
    MazeHandler mazeHandler;

    @DisplayName("Тест запуска процесса генерации лабиринта и пути")
    @Test
    void testHandleMaze() {
        int height = 10;
        int width = 10;
        Coordinate startCoordinate = new Coordinate(1, 1);
        Coordinate finishCoordinate = new Coordinate(8, 8);
        String continueMessage = "\nДля продолжения введите любой символ\n";

        when(generatorMaze.maxHeight()).thenReturn(height);
        when(generatorMaze.maxWidth()).thenReturn(width);
        when(generatorMaze.generateMaze(height, width)).thenReturn(maze);

        when(scanner.nextLine()).thenReturn("");

        when(parserMazeParameters.parseMazeSizeParameter(height, width, false)).thenReturn(height);
        when(parserMazeParameters.parseMazeSizeParameter(height, width, true)).thenReturn(width);
        when(parserMazeParameters.parseStartCoordinate(maze)).thenReturn(startCoordinate);
        when(parserMazeParameters.parseFinishCoordinate(maze, startCoordinate)).thenReturn(finishCoordinate);

        when(rendererMaze.renderMaze(maze)).thenReturn("Предположим лабиринт");
        when(rendererMaze.renderMazeWithPath(maze, List.of())).thenReturn("Предположим путь");

        SolverMaze solverMaze = mock(SolverMaze.class);
        when(solverMaze.solveMaze(maze, startCoordinate, finishCoordinate))
            .thenReturn(List.of());
        when(solverMaze.getSolverMazeType()).thenReturn(SolverMazeType.BFS);

        try (MockedStatic<SolverMazeFactory> mockedStatic = mockStatic(SolverMazeFactory.class)) {
            mockedStatic.when(SolverMazeFactory::createAllSolverMazes).thenReturn(List.of(solverMaze));

            mazeHandler.handleMaze(generatorMaze, rendererMaze);

            verify(printWriter).println("\nВысота лабиринта: 10\nШирина лабиринта: 10" + continueMessage);
            verify(printWriter).println("\nЛабиринт:\nПредположим лабиринт");
            verify(printWriter).println("\nСтартовая координата: (1; 1)\nКонечная координата: (8; 8)"
                + continueMessage);
            verify(printWriter)
                .println("Предположим путь");
        }
    }
}
