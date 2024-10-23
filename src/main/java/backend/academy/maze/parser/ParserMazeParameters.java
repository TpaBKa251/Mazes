package backend.academy.maze.parser;

import backend.academy.maze.maze.Cell;
import backend.academy.maze.maze.Coordinate;
import backend.academy.maze.maze.Maze;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.util.Scanner;
import lombok.RequiredArgsConstructor;

/**
 * Класс-парсер параметров для построения лабиринта и пути в лабиринте из командной строки
 */
@RequiredArgsConstructor
public class ParserMazeParameters {

    private static final String RED = "\033[31m";
    private static final String RESET = "\033[0m";
    private static final int MIN_SIZE = 3;
    private static final String ENTER = "Введите ";

    private final Scanner scanner;
    private final PrintWriter writer;
    private final SecureRandom random;

    /**
     * Метод для получения параметров размера лабиринта (высоты и ширины)
     *
     * @param maxHeight максимальная высота лабиринта
     * @param maxWidth максимальная ширина лабиринта
     * @param isWidth флаг о том, какой это параметр (высота или нет)
     *
     * @return параметр размера лабиринта
     */
    public int parseMazeSizeParameter(int maxHeight, int maxWidth, boolean isWidth) {
        int sizeParameter;
        int maxSize = isWidth ? maxWidth : maxHeight;

        String messageToEnterSize = getMessageToEnterSize(isWidth);

        do {
            writer.println(messageToEnterSize);
            String sizeParameterLine = scanner.nextLine().trim();

            if (sizeParameterLine.isEmpty()) {
                // Случайный выбор, параметр 100% валиден
                sizeParameter = random.nextInt(MIN_SIZE, maxSize + 1);
                break;
            }

            // Пользовательский ввод
            Integer parsedSize = parseUserSizeParameterInput(sizeParameterLine);

            if (parsedSize != null) {
                if (isValidSize(parsedSize, maxSize)) {
                    sizeParameter = parsedSize;
                    break;
                } else {
                    writer.println(String.format(RED
                            + "Размер лабиринта должен быть от %d до %d."
                            + RESET, MIN_SIZE, maxSize));
                }
            }
        } while (true);


        return sizeParameter;
    }

    private Integer parseUserSizeParameterInput(String input) {
        try {
            return (Math.abs(Integer.parseInt(input)));
        } catch (NumberFormatException e) {
            writer.println(RED + "Размеры лабиринта задаются числами." + RESET);
            return null;
        }
    }

    private boolean isValidSize(int size, int maxSize) {
        return size >= MIN_SIZE && size <= maxSize;
    }

    /**
     * Метод для получения сообщения для ввода размеров лабиринта
     *
     * @param isWidth флаг о том, какой параметр вводится
     *
     * @return сообщение для ввода размеров
     */
    private String getMessageToEnterSize(boolean isWidth) {
        return ENTER + (isWidth ? "ширину" : "высоту") + " лабиринта. Для случайного выбора нажмите Enter:";
    }

    /**
     * Метод для получения стартовой координаты пути
     *
     * @param maze лабиринт
     *
     * @return стартовую координату
     */
    public Coordinate parseStartCoordinate(Maze maze) {
        return parseCoordinate(maze, null, false);
    }

    /**
     * Метод для получения конечной координаты пути
     *
     * @param maze лабиринт
     * @param startCoordinate стартовая координата
     *
     * @return конечную координату
     */
    public Coordinate parseFinishCoordinate(Maze maze, Coordinate startCoordinate) {
        return parseCoordinate(maze, startCoordinate, true);
    }

    /**
     * Метод для получения координаты старта или начала пути
     *
     * @param maze лабиринт
     * @param startCoordinate стартовая координата (если есть, иначе null)
     * @param isFinish флаг о том, какую координату нужно получить
     *
     * @return координату старта или конца пути
     */
    private Coordinate parseCoordinate(Maze maze, Coordinate startCoordinate, boolean isFinish) {
        Coordinate coordinate = null;

        String messageToEnterCoordinate = getMessageToEnterCoordinate(isFinish);

        do {
            writer.println(messageToEnterCoordinate);
            String[] coordinateLine = scanner.nextLine().trim().split("\\s+");

            if (isRandomChoiceCoordinate(coordinateLine)) {
                // Координата 100% валидна
                coordinate = getRandomCoordinate(maze, startCoordinate, isFinish);
            } else if (isValidInputCoordinateFormat(coordinateLine)) {
                coordinate = parseAndValidateCoordinate(maze, startCoordinate, coordinateLine, isFinish);
            } else {
                writer.println(RED
                    + "Координата задается двумя значениями через пробел - номер строки и номер столбца."
                    + RESET);
            }
        } while (coordinate == null);

        return coordinate;
    }

    /**
     * Метод получения сообщения для ввода координаты старта или конца пути
     *
     * @param isFinish флаг о том, какая координата вводится
     *
     * @return сообщение о вводе координаты
     */
    private String getMessageToEnterCoordinate(boolean isFinish) {
        return ENTER + (isFinish ? "конечную" : "стартовую")
            + " координату пути. Для случайного выбора нажмите Enter:";
    }

    /**
     * Метод для определения хочет ли пользователь получить случайную величину параметра лабиринта или пути в лабиринте
     *
     * @param input пользовательский ввод
     *
     * @return {@code true}, если пользователь хочет получить случайную величину;
     * <p>{@code false}, если пользователь ввел свои данные
     */
    private boolean isRandomChoiceCoordinate(String[] input) {
        return input.length == 1 && input[0].isEmpty();
    }

    /**
     * Метод для получения случайной координаты старта или конца пути.
     * Если нужно получить конечную координату, то она рандомится до тех пор, пока не станет не равной стартовой
     *
     * @param maze лабиринт
     * @param startCoordinate стартовая координата (если есть, иначе null)
     * @param isFinish флаг о том, какую координату нужно получить
     *
     * @return случайную координату старта или конца пути
     */
    private Coordinate getRandomCoordinate(Maze maze, Coordinate startCoordinate, boolean isFinish) {
        Coordinate coordinate;

        do {
            coordinate = maze.getRandomCoordinate(random);
        } while (isFinish && coordinate.equals(startCoordinate));

        return coordinate;
    }

    /**
     * Проверка валидности пользовательского ввода при выборе координаты
     *
     * @param input пользовательский ввод
     *
     * @return {@code true}, если ввод валиден
     * <p>{@code false}, если ввод невалиден
     */
    private boolean isValidInputCoordinateFormat(String[] input) {
        return input.length == 2;
    }

    /**
     * Метод для получения и валидации координаты, введенной пользователем
     *
     * @param maze лабиринт
     * @param startCoordinate стартовая координата (если есть, иначе null)
     * @param coordinateLine пользовательский ввод
     * @param isFinish флаг о том, какую координату нужно получить и провалидировать
     *
     * @return координату пути, если она валидна, иначе {@code null}
     */
    private Coordinate parseAndValidateCoordinate(
        Maze maze,
        Coordinate startCoordinate,
        String[] coordinateLine,
        boolean isFinish
    ) {
        try {
            int row = 0;
            int col = 1;
            int coordinateRow = Math.abs(Integer.parseInt(coordinateLine[row]));
            int coordinateCol = Math.abs(Integer.parseInt(coordinateLine[col]));

            if (isFinish) {
                validateCoordinate(maze, coordinateRow, coordinateCol);
                validateFinishCoordinate(
                    maze,
                    startCoordinate.row(),
                    startCoordinate.col(),
                    coordinateRow,
                    coordinateCol
                );
            } else {
                validateCoordinate(maze, coordinateRow, coordinateCol);
            }

            return new Coordinate(coordinateRow, coordinateCol);
        } catch (NumberFormatException e) {
            writer.println(RED + "Введите координату в виде двух чисел через пробел."
                + " Координаты должны быть в пределах лабиринта." + RESET);
        } catch (IllegalArgumentException e) {
            writer.println(e.getMessage());
        }

        return null;
    }

    /**
     * Метод для валидации стартовой координаты пути. Должна находится в рамках лабиринта и не на стенах
     *
     * @param maze лабиринт
     * @param row номер строки
     * @param col номер столбца
     */
    private void validateCoordinate(Maze maze, int row, int col) {
        if (isCoordinateNotInBounds(maze, row, col)
            || maze.grid()[row][col].type() == Cell.CellType.WALL
        ) {
            throw new IllegalArgumentException(RED + "Координата недоступна." + RESET);
        }
    }

    /**
     * Метод для валидации конечной координаты пути.
     * Должна находится в рамках лабиринта, не на стенах и не быть равной стартовой
     *
     * @param maze лабиринт
     * @param startRow стартовая строка
     * @param startCol стартовый столбец
     * @param finishRow конечная строка
     * @param finishCol конечный столбец
     */
    private void validateFinishCoordinate(
        Maze maze,
        int startRow,
        int startCol,
        int finishRow,
        int finishCol
    ) {
        if (isCoordinateNotInBounds(maze, finishRow, finishCol)
            || maze.grid()[finishRow][finishCol].type() == Cell.CellType.WALL
            || startRow == finishRow && startCol == finishCol
        ) {
            throw new IllegalArgumentException(RED
                + "Стартовая и конченая координаты одинаковы."
                + RESET);
        }
    }

    /**
     * Вспомогательный метод для методов валидации {@link ParserMazeParameters#validateCoordinate(Maze, int, int)}
     * и {@link ParserMazeParameters#validateFinishCoordinate(Maze, int, int, int, int)}.
     * Определяет, находится ли координата в рамках лабиринта
     *
     * @param maze лабиринт
     * @param row номер строки
     * @param col номер столбца
     *
     * @return {@code true}, если координата ВНЕ рамках лабиринта
     * <p>{@code false}, если координата в рамках лабиринте
     */
    private boolean isCoordinateNotInBounds(Maze maze, int row, int col) {
        return row < 1 || row >= maze.height() - 1
            || col < 1 || col >= maze.width() - 1;
    }
}
