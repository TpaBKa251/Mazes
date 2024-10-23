package backend.academy.maze.maze;

/**
 * Рекорд для координаты лабиринта (повторяет {@link Cell}, но не имеет тип)
 *
 * @param row номер строки
 * @param col номер столбца
 */
public record Coordinate(int row, int col) {
    public String getCoordinate() {
        return "(" + row + "; " + col + ")";
    }
}
