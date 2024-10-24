package backend.academy.maze.factory;

import backend.academy.maze.terminal.encoding.TerminalEncodingIdentifier;
import backend.academy.maze.terminal.encoding.impl.LinuxTerminalEncodingIdentifier;
import backend.academy.maze.terminal.encoding.impl.WindowsTerminalEncodingIdentifier;
import lombok.experimental.UtilityClass;

/**
 * Фабрика стратегий определения кодировки терминала
 */
@UtilityClass
public class TerminalEncodingIdentifierFactory {

    /**
     * Метод для создания стратегии для определения кодировки терминала по ОС
     *
     * @return экземпляр {@link WindowsTerminalEncodingIdentifier}, {@link LinuxTerminalEncodingIdentifier}
     */
    public TerminalEncodingIdentifier createTerminalEncodingIdentifier() { // Думаю, смысла тестировать нет
        String os = System.getProperty("os.name").toLowerCase();

        return os.contains("win")
            ? new WindowsTerminalEncodingIdentifier()
            : new LinuxTerminalEncodingIdentifier();
    }
}
