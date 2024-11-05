package backend.academy.maze.generator;

import backend.academy.maze.generator.impl.GeneratorDfs;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Тесты класса GeneratorMaze")
public class GeneratorMazeTest {

    @DisplayName("Тест валидации при создании лабиринта")
    @ParameterizedTest(name = "высота = {0}, ширина = {1}, валидные значения: 3 <= высота <= {2}; 3 <= ширина <= {3}")
    @MethodSource("provideDataTestValidation")
    void testValidation(int height, int width, int maxHeight, int maxWidth) {
        GeneratorMaze generatorMaze = new GeneratorDfs(maxHeight, maxWidth);

        assertThatThrownBy(() -> generatorMaze.generateMaze(height, width))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(String.format("Высота и ширина лабиринта должны быть больше 3, ширина меньше или равно %d, "
                + "а высота меньше или равно %d.", maxWidth, maxHeight));
    }


    static Stream<Arguments> provideDataTestValidation() {
        return Stream.of(
            Arguments.of(1, 5, 100, 100),
            Arguments.of(5, 1, 100, 100),
            Arguments.of(200, 100, 100, 100),
            Arguments.of(5, 200, 100, 150)
        );
    }
}
