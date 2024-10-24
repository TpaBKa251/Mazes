package backend.academy.maze.terminal;

import backend.academy.maze.factory.TerminalEncodingIdentifierFactory;
import java.io.IOException;
import java.nio.charset.Charset;
import lombok.Builder;
import lombok.experimental.UtilityClass;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

/**
 * Утилитарный класс для получения размера терминала
 */
@UtilityClass
public class TerminalParametersIdentifier {

    private static final int MAX_HEIGHT = 100;
    private static final int MAX_WIDTH = 100;
    private static final int SIZE_DELTA = 2;

    /**
     * Метод для получения параметров терминала (размер и кодировка)
     *
     * @return параметры терминала
     */
    public TerminalParameters getTerminalParameters() {
        Charset encoding = Charset.defaultCharset();

        TerminalParameters.TerminalParametersBuilder builder = getTerminalSize();

        // Если один из параметров <= 0, то второй тоже
        if (builder.height <= 0) {
            builder.height(MAX_HEIGHT).width(MAX_WIDTH);
        } else {
            encoding = getEncoding();
        }

        return builder.encoding(encoding).build();
    }

    /**
     * Метод для получения размера терминала
     *
     * @return билдер с заполненными параметрами размера
     */
    private TerminalParameters.TerminalParametersBuilder getTerminalSize() {
        TerminalParameters.TerminalParametersBuilder builder = TerminalParameters.builder();

        int height = 0;
        int width = 0;

        try (Terminal terminal = TerminalBuilder.terminal()) {
            Size terminalSize = terminal.getSize();

            height = terminalSize.getRows() - SIZE_DELTA;
            width = terminalSize.getColumns() - SIZE_DELTA;
        } catch (IOException ignore) {
        }

        return builder.height(height).width(width);
    }

    /**
     * Метод для получения кодировки терминала
     *
     * @return кодировку терминала
     */
    private Charset getEncoding() {
        return TerminalEncodingIdentifierFactory.createTerminalEncodingIdentifier().getTerminalCharset();
    }

    /**
     * Вложенный рекорд для параметров терминала
     *
     * @param height высота терминала
     * @param width ширина терминала
     * @param encoding кодировка терминала
     */
    @Builder
    public record TerminalParameters(int height, int width, Charset encoding) {
    }
}
