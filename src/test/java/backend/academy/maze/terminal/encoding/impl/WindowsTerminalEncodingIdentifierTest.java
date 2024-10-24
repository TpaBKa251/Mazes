package backend.academy.maze.terminal.encoding.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@DisplayName("Тесты класса WindowsTerminalEncodingIdentifierTest")
public class WindowsTerminalEncodingIdentifierTest {

    @DisplayName("Тест успешного получения кодировки")
    @Test
    void testGetTerminalCharsetSuccessful() throws Exception {
        ProcessBuilder mockProcessBuilder = Mockito.mock(ProcessBuilder.class);
        Process mockProcess = Mockito.mock(Process.class);
        InputStream mockInputStream = Mockito.mock(InputStream.class);

        when(mockProcessBuilder.command(any(String[].class))).thenReturn(mockProcessBuilder);
        when(mockProcessBuilder.start()).thenReturn(mockProcess);
        when(mockProcess.getInputStream()).thenReturn(mockInputStream);

        when(mockInputStream.read(any(byte[].class), anyInt(), anyInt())).thenAnswer(invocation -> {
            String response = "BodyName: UTF-8\n";
            byte[] buffer = invocation.getArgument(0);
            System.arraycopy(response.getBytes(StandardCharsets.UTF_8), 0, buffer, 0, response.length());
            return response.length();
        });

        WindowsTerminalEncodingIdentifier identifier = new WindowsTerminalEncodingIdentifier(mockProcessBuilder);

        Charset result = identifier.getTerminalCharset();
        assertThat(result).isEqualTo(StandardCharsets.UTF_8);
    }

    @DisplayName("Тест провального получения кодировки (по умолчанию)")
    @Test
    void testGetTerminalCharsetFailed() throws Exception {
        ProcessBuilder mockProcessBuilder = Mockito.mock(ProcessBuilder.class);
        Process mockProcess = Mockito.mock(Process.class);
        InputStream mockInputStream = Mockito.mock(InputStream.class);

        when(mockProcessBuilder.command(any(String[].class))).thenReturn(mockProcessBuilder);
        when(mockProcessBuilder.start()).thenReturn(mockProcess);
        when(mockProcess.getInputStream()).thenReturn(mockInputStream);
        when(mockInputStream.read(any(byte[].class), anyInt(), anyInt())).thenReturn(-1);

        WindowsTerminalEncodingIdentifier identifier = new WindowsTerminalEncodingIdentifier(mockProcessBuilder);

        Charset result = identifier.getTerminalCharset();

        assertThat(result).isEqualTo(StandardCharsets.UTF_8);
    }
}
