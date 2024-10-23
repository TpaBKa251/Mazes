package backend.academy.maze.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Перечисление алгоритмов поиска пути в лабиринте
 */
@Getter
@RequiredArgsConstructor
public enum SolverMazeType {
    BFS("Обход в ширину"),
    DIJKSTRA("Алгоритм Дейкстры"),
    A_STAR("Алгоритм A* (A-Star)");

    private final String solverName;
}
