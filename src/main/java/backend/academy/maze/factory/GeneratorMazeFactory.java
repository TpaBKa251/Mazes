package backend.academy.maze.factory;

import backend.academy.maze.enums.GeneratorMazeType;
import backend.academy.maze.generator.GeneratorMaze;
import backend.academy.maze.generator.impl.GeneratorDfs;
import backend.academy.maze.generator.impl.GeneratorKruskal;
import backend.academy.maze.generator.impl.GeneratorPrim;
import backend.academy.maze.utils.RandomUtil;
import java.io.PrintWriter;
import java.util.Scanner;
import lombok.experimental.UtilityClass;

/**
 * Фабрика генераторов лабиринта
 */
@UtilityClass
public class GeneratorMazeFactory {

    /**
     * Длина заранее известных строк
     */
    private static final int STANDARD_BUFFER_SIZE = 76;
    /**
     * Длина строк {@code "\n", ". ", "\t"}
     */
    private static final int CONTROL_SYMBOLS_BUFFER_SIZE = 4;

    /**
     * Метод для создания генератора из пользовательского ввода в консоли
     *
     * @param scanner сканер для чтения с консоли
     * @param writer райтер
     * @param random рандомайзер
     * @param maxHeight максимальная высота лабиринта
     * @param maxWidth максимальная ширина лабиринта
     *
     * @return экземпляр классов {@link GeneratorDfs}, {@link GeneratorKruskal}, {@link GeneratorPrim}
     */
    public GeneratorMaze createGeneratorMazeFromConsole(
        Scanner scanner,
        PrintWriter writer,
        int maxHeight,
        int maxWidth
    ) {
        writer.println(getGeneratorChoiceMessage());

        String selectedGenerator = scanner.nextLine().trim();

        GeneratorMaze[] allGenerators = {new GeneratorDfs(maxHeight, maxWidth),
            new GeneratorKruskal(maxHeight, maxWidth),
            new GeneratorPrim(maxHeight, maxWidth)};

        writer.print("\nАлгоритм генерации: ");

        switch (selectedGenerator) {
            case "1" -> {
                writer.print(GeneratorMazeType.DFS.generatorName() + '\n');
                return new GeneratorDfs(maxHeight, maxWidth);
            }
            case "2" -> {
                writer.print(GeneratorMazeType.KRUSKAL.generatorName() + '\n');
                return new GeneratorKruskal(maxHeight, maxWidth);
            }
            case "3" -> {
                writer.print(GeneratorMazeType.PRIM.generatorName() + '\n');
                return new GeneratorPrim(maxHeight, maxWidth);
            }
            default -> {
                int generatorNumber = RandomUtil.getRandomInt(allGenerators.length);
                writer.print(GeneratorMazeType.values()[generatorNumber].generatorName() + '\n');
                return allGenerators[generatorNumber];
            }
        }
    }

    /**
     * Метод для генерации сообщения о выборе генератора лабиринта
     *
     * @return сообщение о выборе генератора
     */
    private String getGeneratorChoiceMessage() {
        int bufferChoiceMessage = 0;
        GeneratorMazeType[] generatorMazeTypes = GeneratorMazeType.values();
        for (GeneratorMazeType generatorMazeType : generatorMazeTypes) {
            bufferChoiceMessage += generatorMazeType.generatorName().length()
                + CONTROL_SYMBOLS_BUFFER_SIZE                                 // символы: '\t', ". ", '\n'
                + String.valueOf(generatorMazeType.ordinal() + 1).length(); // длина числа i + 1
        }

        int buffer = STANDARD_BUFFER_SIZE + bufferChoiceMessage;
        StringBuilder choiceMessage = new StringBuilder(buffer);
        choiceMessage.append("Выберите алгоритм генерации лабиринта. Для случайного выбора нажмите Enter:\n");

        for (int i = 0; i < generatorMazeTypes.length; i++) {
            choiceMessage
                .append('\t')
                .append(i + 1)
                .append(". ")
                .append(GeneratorMazeType.values()[i].generatorName())
                .append('\n');
        }

        return choiceMessage.toString();
    }
}
