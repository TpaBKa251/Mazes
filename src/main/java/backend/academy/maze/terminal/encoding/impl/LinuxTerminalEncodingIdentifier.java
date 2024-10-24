package backend.academy.maze.terminal.encoding.impl;

import backend.academy.maze.terminal.encoding.TerminalEncodingIdentifier;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;

/**
 * Класс для определения кодировки терминала на Linux/MacOS
 */
@Slf4j
public class LinuxTerminalEncodingIdentifier extends TerminalEncodingIdentifier {

    private ProcessBuilder processBuilder;

    public LinuxTerminalEncodingIdentifier(ProcessBuilder processBuilder) {
        this.processBuilder = processBuilder;
    }

    public LinuxTerminalEncodingIdentifier() {}

    @Override
    public Charset getTerminalCharset() {
        String charsetName;

        try {
            Process process;
            if (processBuilder == null) {
                processBuilder = new ProcessBuilder("locale", "charmap");

            }
            process = processBuilder.start();


            try (BufferedReader reader
                     = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String charsetLine = reader.readLine();

                if (charsetLine != null) {
                    charsetName = charsetLine.trim();

                    return Charset.forName(charsetName);
                }

                throw new IOException();
            }
        } catch (IOException e) {
            log.error("Не удалось определить кодировку терминала. Будет использоваться кодировка по умолчанию");
        }

        return defaultCharset;
    }
}
