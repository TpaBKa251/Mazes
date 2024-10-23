package backend.academy.maze.factory;

import backend.academy.maze.enums.GeneratorMazeType;
import backend.academy.maze.generator.GeneratorMaze;
import backend.academy.maze.generator.impl.GeneratorDfs;
import backend.academy.maze.generator.impl.GeneratorKruskal;
import backend.academy.maze.generator.impl.GeneratorPrim;
import java.io.PrintWriter;
import java.security.SecureRandom;
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
        SecureRandom random,
        int maxHeight,
        int maxWidth
    ) {
        writer.println(getGeneratorChoiceMessage());

        String selectedGenerator = scanner.nextLine().trim();

        GeneratorMaze[] allGenerators = {new GeneratorDfs(random, maxHeight, maxWidth),
            new GeneratorKruskal(random, maxHeight, maxWidth),
            new GeneratorPrim(random, maxHeight, maxWidth)};

        writer.print("\nАлгоритм генерации: ");

        switch (selectedGenerator) {
            case "1" -> {
                writer.print(GeneratorMazeType.DFS.generatorName() + '\n');
                return new GeneratorDfs(random, maxHeight, maxWidth);
            }
            case "2" -> {
                writer.print(GeneratorMazeType.KRUSKAL.generatorName() + '\n');
                return new GeneratorKruskal(random, maxHeight, maxWidth);
            }
            case "3" -> {
                writer.print(GeneratorMazeType.PRIM.generatorName() + '\n');
                return new GeneratorPrim(random, maxHeight, maxWidth);
            }
            default -> {
                int generatorNumber = random.nextInt(allGenerators.length);
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
