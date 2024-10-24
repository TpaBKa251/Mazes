package backend.academy.maze.terminal.encoding.impl;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

@DisplayName("Тесты класса LinuxTerminalEncodingIdentifierTest")
class LinuxTerminalEncodingIdentifierTest {

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
            byte[] buffer = invocation.getArgument(0);
            String response = "UTF-8\n";
            System.arraycopy(response.getBytes(StandardCharsets.UTF_8), 0, buffer, 0, response.length());
            return response.length();
        });

        LinuxTerminalEncodingIdentifier identifier = new LinuxTerminalEncodingIdentifier(mockProcessBuilder);

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

        LinuxTerminalEncodingIdentifier identifier = new LinuxTerminalEncodingIdentifier(mockProcessBuilder);

        Charset result = identifier.getTerminalCharset();
        assertThat(result).isEqualTo(StandardCharsets.UTF_8);
    }
}
