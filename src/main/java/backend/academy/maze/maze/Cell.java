package backend.academy.maze.maze;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Рекорд клетки лабиринта. Имеет вложенное перечисление {@link CellType} с типами клеток
 *
 * @param row номер строки
 * @param col номер столбца
 * @param type тип клетки
 */
public record Cell(int row, int col, CellType type) {

    /**
     * Перечисление типов клеток. Каждая клетка имеет название, вес и символ для отображения
     */
    @Getter
    @RequiredArgsConstructor
    public enum CellType {
        WALL("Стена", 0, "█"),
        PATH("Путь", 0, "\u001B[32m" + "x" + "\u001B[0m"),
        ESCALATOR("Эскалатор", 10, "/"),
        COIN("Монета", 20, "@"),
        SMOOTH_ROAD("Ровная дорога", 30, "═"),
        PASSAGE("Проход", 40, " "),
        SAND("Песок", 50, "░"),
        WATER("Вода", 75, "~"),
        SWAMP("Болото", 100, "#");


        private final String cellTypeName;
        private final int weight;
        private final String symbol;
    }
}
