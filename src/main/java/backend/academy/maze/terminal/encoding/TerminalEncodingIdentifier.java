package backend.academy.maze.terminal.encoding;

import java.nio.charset.Charset;

/**
 * Абстрактный класс стратегии определения кодировки терминала
 */
public abstract class TerminalEncodingIdentifier {

    /**
     * Стандартная кодировка терминала в системе
     */
    protected final Charset defaultCharset = Charset.defaultCharset();

    /**
     * Метод для получения кодировки терминала
     *
     * @return кодировку терминала
     */
    public abstract Charset getTerminalCharset();
}
