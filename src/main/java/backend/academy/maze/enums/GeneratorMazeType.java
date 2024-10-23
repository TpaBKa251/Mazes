package backend.academy.maze.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Перечисление алгоритмов генерации лабиринта
 */
@Getter
@RequiredArgsConstructor
public enum GeneratorMazeType {
    DFS("Обход в глубину"),
    KRUSKAL("Алгоритм Краскала"),
    PRIM("Алгоритм Прима");

    private final String generatorName;
}
