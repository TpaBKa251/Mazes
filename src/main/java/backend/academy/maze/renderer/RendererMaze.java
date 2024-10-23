package backend.academy.maze.renderer;

import backend.academy.maze.maze.Coordinate;
import backend.academy.maze.maze.Maze;
import java.util.List;

/**
 * Интерфейс отрисовщика (маппера) для преобразования лабиринта и решения лабиринта в формат для вывода
 */
public interface RendererMaze {

    /**
     * Метод для преобразования лабиринта в строку для вывода
     *
     * @param maze лабиринта
     *
     * @return лабиринт, преобразованный в строку
     */
    String renderMaze(Maze maze);

    /**
     * Метод для добавления пути в лабиринт и последующего преобразования лабиринта и пути в строку для вывода
     *
     * @param maze лабиринт
     * @param path путь в лабиринте
     *
     * @return объединенные лабиринт и путь, преобразованные в строку
     */
    String renderMazeWithPath(Maze maze, List<Coordinate> path);
}
