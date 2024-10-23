package backend.academy.maze.renderer.impl;

import backend.academy.maze.maze.Cell;
import backend.academy.maze.maze.Coordinate;
import backend.academy.maze.maze.Maze;
import backend.academy.maze.renderer.RendererMaze;
import java.util.List;

/**
 * Класс-маппер для преобразования лабиринта и решения лабиринта в строку для вывода
 */
public class RendererMazeImpl implements RendererMaze {

    private final static String RESET = "\u001B[0m";
    private final static String GREEN = "\u001B[32m";
    private final static String BOLD = "\033[1m";

    private final static int START_AND_FINISH_BUFFER = 8;
    private final static int PATH_FACTOR = 9; // я честно не знаю почему, методом подбора

    @Override
    public String renderMaze(Maze maze) {
        int buffer = maze.width() * maze.height() + maze.height();
        StringBuilder mazeString = new StringBuilder(buffer);

        for (int row = 0; row < maze.height(); row++) {
            for (int col = 0; col < maze.width(); col++) {
                mazeString.append(maze.grid()[row][col].type().symbol());
            }

            mazeString.append('\n');
        }

        return mazeString.toString();
    }

    @Override
    public String renderMazeWithPath(Maze maze, List<Coordinate> path) {
        if (path.isEmpty()) {
            return "Путь не найден";
        }
        int buffer = maze.width() * maze.height() + maze.height() + START_AND_FINISH_BUFFER + path.size() * PATH_FACTOR;

        StringBuilder mazeWithPathString = new StringBuilder(buffer);

        for (int row = 0; row < maze.height(); row++) {
            for (int col = 0; col < maze.width(); col++) {
                Coordinate coordinate = new Coordinate(row, col);

                if (path.contains(coordinate)) {
                    if (path.getFirst().equals(coordinate)) {
                        mazeWithPathString.append(GREEN + BOLD + "A" + RESET); // Стартовая координата пути
                        continue;
                    } else if (path.getLast().equals(coordinate)) {
                        mazeWithPathString.append(GREEN + BOLD + "B" + RESET); // Конечная координата пути
                        continue;
                    }

                    mazeWithPathString.append(Cell.CellType.PATH.symbol());
                    continue;
                }

                mazeWithPathString.append(maze.grid()[row][col].type().symbol());
            }

            mazeWithPathString.append('\n');
        }

        return mazeWithPathString.toString();
    }
}
