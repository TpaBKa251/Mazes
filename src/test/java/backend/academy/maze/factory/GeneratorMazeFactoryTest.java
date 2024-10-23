package backend.academy.maze.factory;

import backend.academy.maze.enums.GeneratorMazeType;
import backend.academy.maze.generator.GeneratorMaze;
import backend.academy.maze.generator.impl.GeneratorDfs;
import backend.academy.maze.generator.impl.GeneratorKruskal;
import backend.academy.maze.generator.impl.GeneratorPrim;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintWriter;
import java.security.SecureRandom;
import java.util.Scanner;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Тесты фабрики генераторов лабиринта GeneratorMazeFactoryTest")
@ExtendWith(MockitoExtension.class)
public class GeneratorMazeFactoryTest {

    @Mock
    Scanner scanner;

    @Mock
    PrintWriter writer;

    @Mock
    SecureRandom random;

    @DisplayName("Тест создания генераторов из пользовательского ввода")
    @ParameterizedTest(name = "ввод: {0}, ожидаемый экземпляр: {1}")
    @MethodSource("provideDataTestCreateGeneratorMazeFromConsole")
    void testCreateGeneratorMazeFromConsole(String input, Class<?> expectedClass) {
        int height = 100;
        int width = 100;

        when(scanner.nextLine()).thenReturn(input);

        // Рандом и так всегда будет 0 выкидывать, но для наглядности
        if (input.equals("не знаю") || input.isEmpty()) {
            when(random.nextInt(GeneratorMazeType.values().length)).thenReturn(0);
        }

        GeneratorMaze generatorMaze = GeneratorMazeFactory.createGeneratorMazeFromConsole(
                scanner,
                writer,
                random,
                height,
                width
        );

        assertThat(generatorMaze).isInstanceOf(expectedClass);
    }

    static Stream<Arguments> provideDataTestCreateGeneratorMazeFromConsole() {
        return Stream.of(
                Arguments.of("1", GeneratorDfs.class),
                Arguments.of("2", GeneratorKruskal.class),
                Arguments.of("3", GeneratorPrim.class),
                Arguments.of("не знаю", GeneratorDfs.class),
                Arguments.of("", GeneratorDfs.class)
        );
    }
}
