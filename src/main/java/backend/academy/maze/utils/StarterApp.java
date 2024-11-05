package backend.academy.maze.utils;

import backend.academy.maze.MazeHandler;
import backend.academy.maze.enums.GeneratorMazeType;
import backend.academy.maze.enums.SolverMazeType;
import backend.academy.maze.factory.GeneratorMazeFactory;
import backend.academy.maze.generator.GeneratorMaze;
import backend.academy.maze.maze.Cell;
import backend.academy.maze.parser.ParserMazeParameters;
import backend.academy.maze.renderer.RendererMaze;
import backend.academy.maze.renderer.impl.RendererMazeImpl;
import backend.academy.maze.terminal.TerminalParametersIdentifier;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StarterApp {
    private static final int MIN_SIZE = 3;
    private static final String NEW_LINE_TAB = "\n\t";
    private static final int STANDARD_BUFFER_SIZE = 876;
    private static final int SPECIAL_CHARACTERS_COUNT = 3;
    private static final int PARAMS_OF_CELLS = 17;

    public static void start() {
        TerminalParametersIdentifier.TerminalParameters terminalParameters =
            TerminalParametersIdentifier.getTerminalParameters();

        int heightOfTerminal = terminalParameters.height();
        int widthOfTerminal = terminalParameters.width();
        Charset encoding = terminalParameters.encoding();

        try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out, encoding), true)
        ) {
            writer.println(getWelcomeMessage(heightOfTerminal, widthOfTerminal));

            GeneratorMaze generatorMaze = GeneratorMazeFactory.createGeneratorMazeFromConsole(
                scanner,
                writer,
                heightOfTerminal,
                widthOfTerminal
            );
            RendererMaze rendererMaze = new RendererMazeImpl();

            ParserMazeParameters parserMazeParameters = new ParserMazeParameters(scanner, writer);
            MazeHandler mazeHandler = new MazeHandler(parserMazeParameters, scanner, writer);

            mazeHandler.handleMaze(generatorMaze, rendererMaze);
        }
    }

    private static String getWelcomeMessage(int maxHeight, int maxWidth) {
        GeneratorMazeType[] generatorMazeTypes = GeneratorMazeType.values();
        SolverMazeType[] solverMazeTypes = SolverMazeType.values();
        Cell.CellType[] cellTypes = Cell.CellType.values();

        int buffer = getBuffer(maxHeight, maxWidth, generatorMazeTypes, solverMazeTypes, cellTypes);

        StringBuilder welcomeMessage = new StringBuilder(buffer);
        String message = "Данное приложение генерирует лабиринт по заданной высоте и ширине"
            + " одним из выбранных алгоритмов:";
        welcomeMessage.append(message);

        for (int i = 0; i < generatorMazeTypes.length; i++) {
            welcomeMessage
                .append(NEW_LINE_TAB)
                .append(generatorMazeTypes[i].generatorName())
                .append(i == generatorMazeTypes.length - 1 ? '.' : ";");
        }

        message = "\nЗатем строит путь по введенным координатам - стартовой и конечной."
            + " Для нахождения пути используются следующие алгоритмы:";
        welcomeMessage.append(message);

        for (int i = 0; i < solverMazeTypes.length; i++) {
            welcomeMessage
                .append(NEW_LINE_TAB)
                .append(solverMazeTypes[i].solverName())
                .append(i == solverMazeTypes.length - 1 ? '.' : ";");
        }

        welcomeMessage.append("\n\nВиды клеток лабиринта:");

        for (int i = 0; i < cellTypes.length; i++) {
            Cell.CellType cellType = cellTypes[i];

            welcomeMessage
                .append(NEW_LINE_TAB)
                .append(cellType.cellTypeName())
                .append(", вес: ")
                .append(cellType.weight())
                .append(", символ: ")
                .append(cellType.symbol())
                .append(i == cellTypes.length - 1 ? '.' : ";");
        }

        message = String.format("""
            %n%n\
            Для старта приложения необходимо:%n\
                Выбрать алгоритм генерации;%n\
                Ввести значения высоты в пределах %d <= высота <= %d;%n\
                Ввести значение ширины в пределах %d <= ширина <= %d;%n\
                Ввести отличные друг от друга координаты старта и конца пути.%n%n\
            Координаты при этом должны находиться в рамках лабиринта и не находиться на стенах.%n\
            Координаты находятся в следующих пределах:%n\
                %d <= номер строки <= %d;%n\
                %d <= номер столбца <= %d.%n\
            Пример ввода координаты:%n\
                1 1 (строка столбец)%n%n\
            При вводе отрицательных чисел они будут автоматически взяты по модулю.%n%n\
            И само собой все может быть выбрано случайно - просто нажмите Enter :)%n\
            """, MIN_SIZE, maxHeight, MIN_SIZE, maxWidth, 1, maxHeight, 1, maxWidth);
        welcomeMessage.append(message);

        return welcomeMessage.toString();
    }

    private static int getBuffer(
        int maxHeight,
        int maxWidth,
        GeneratorMazeType[] generatorMazeTypes,
        SolverMazeType[] solverMazeTypes,
        Cell.CellType[] cellTypes
    ) {
        int totalLengthGeneratorTypes = Arrays.stream(generatorMazeTypes)
            .mapToInt(generatorMazeType -> generatorMazeType.generatorName().length())
            .sum() + generatorMazeTypes.length * SPECIAL_CHARACTERS_COUNT;

        int totalLengthSolverTypes = Arrays.stream(solverMazeTypes)
            .mapToInt(solverMazeType -> solverMazeType.solverName().length())
            .sum() + solverMazeTypes.length * SPECIAL_CHARACTERS_COUNT;

        int totalLengthCellType = Arrays.stream(cellTypes)
            .mapToInt(cellType -> cellType.cellTypeName().length()
                + Integer.toString(cellType.weight()).length()
                + cellType.symbol().length())
            .sum() + cellTypes.length * (SPECIAL_CHARACTERS_COUNT + PARAMS_OF_CELLS);


        return STANDARD_BUFFER_SIZE
            + totalLengthGeneratorTypes
            + totalLengthSolverTypes
            + totalLengthCellType
            + Integer.toString(MIN_SIZE).length() * 2
            + Integer.toString(maxHeight).length() * 2
            + Integer.toString(maxWidth).length() * 2
            + 2;
    }
}
