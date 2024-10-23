package backend.academy.maze.factory;

import backend.academy.maze.solver.SolverMaze;
import backend.academy.maze.solver.impl.SolverAStar;
import backend.academy.maze.solver.impl.SolverBfs;
import backend.academy.maze.solver.impl.SolverDijkstra;
import java.util.List;
import lombok.experimental.UtilityClass;

/**
 * Фабрика решателей лабиринта
 */
@UtilityClass
public class SolverMazeFactory {

    // Не вижу смысла это тестировать
    /**
     * Метод для получения писка всех решателей лабиринта
     *
     * @return список из экземпляров {@link SolverBfs}, {@link SolverDijkstra}, {@link SolverAStar}
     */
    public List<SolverMaze> createAllSolverMazes() {
        return List.of(new SolverBfs(), new SolverDijkstra(), new SolverAStar());
    }
}
