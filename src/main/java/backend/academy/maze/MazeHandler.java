package backend.academy.maze;

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
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MazeHandler {

    private static final String CONTINUE_MESSAGE = "\nДля продолжения введите любой символ\n";

    private final ParserMazeParameters parserMazeParameters;
    private final Scanner scanner;
    private final PrintWriter writer;

    /**
     * Метод для запуска процесс генерации лабиринта и путей
     *
     * @param generatorMaze генератор лабиринта
     * @param rendererMaze отрисовщик (маппер)
     */
    public void handleMaze(GeneratorMaze generatorMaze, RendererMaze rendererMaze) {
        int height = parserMazeParameters
            .parseMazeSizeParameter(generatorMaze.maxHeight(), generatorMaze.maxWidth(), false);
        int width = parserMazeParameters
            .parseMazeSizeParameter(generatorMaze.maxHeight(), generatorMaze.maxWidth(), true);

        writer.println(
            "\nВысота лабиринта: " + height
                + "\nШирина лабиринта: " + width
                + CONTINUE_MESSAGE);

        scanner.nextLine();

        // Параметры 100% валидны, исключение не выкинет
        Maze maze = generatorMaze.generateMaze(height, width);

        writer.println("\nЛабиринт:\n" + rendererMaze.renderMaze(maze));

        Coordinate startCoordinate = parserMazeParameters.parseStartCoordinate(maze);
        Coordinate finishCoordinate = parserMazeParameters.parseFinishCoordinate(maze, startCoordinate);

        writer.println(
            "\nСтартовая координата: " + startCoordinate.getCoordinate()
                + "\nКонечная координата: " + finishCoordinate.getCoordinate()
                + CONTINUE_MESSAGE);

        scanner.nextLine();

        solveMazeAndPrintAllPaths(
            maze,
            startCoordinate,
            finishCoordinate,
            rendererMaze
        );
    }

    /**
     * Метод для запуска решения лабиринта всеми алгоритмами и вывода путей
     *
     * @param maze лабиринт
     * @param startCoordinate стартовая координата пути
     * @param finishCoordinate конечная координата пути
     * @param renderer отрисовщик (маппер)
     */
    private void solveMazeAndPrintAllPaths(
        Maze maze,
        Coordinate startCoordinate,
        Coordinate finishCoordinate,
        RendererMaze renderer
    ) {
        List<SolverMaze> solverMazes = SolverMazeFactory.createAllSolverMazes();

        for (SolverMaze solverMaze : solverMazes) {
            writer.println(solverMaze.getSolverMazeType().solverName() + ":");
            writer.println(
                renderer.renderMazeWithPath(
                    maze, solverMaze.solveMaze(
                        maze, startCoordinate, finishCoordinate // Координаты 100% валидны
                    )
                )
            );

            writer.println(CONTINUE_MESSAGE);
            scanner.nextLine();
        }
    }
}

