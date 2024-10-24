package backend.academy.maze.terminal;

import backend.academy.maze.factory.TerminalEncodingIdentifierFactory;
import backend.academy.maze.terminal.encoding.TerminalEncodingIdentifier;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@DisplayName("Тесты класса TerminalParametersIdentifier")
class TerminalParametersIdentifierTest {

    @DisplayName("Тест успешного получения параметров терминала")
    @Test
    void testGetTerminalParametersSuccessful() {
        Terminal mockTerminal = mock(Terminal.class);
        Size mockSize = mock(Size.class);

        when(mockSize.getRows()).thenReturn(30);
        when(mockSize.getColumns()).thenReturn(80);
        when(mockTerminal.getSize()).thenReturn(mockSize);

        try (MockedStatic<TerminalBuilder> terminalBuilderMock = mockStatic(TerminalBuilder.class)) {
            terminalBuilderMock.when(TerminalBuilder::terminal).thenReturn(mockTerminal);

            TerminalEncodingIdentifier mockEncodingIdentifier = mock(TerminalEncodingIdentifier.class);
            when(mockEncodingIdentifier.getTerminalCharset()).thenReturn(StandardCharsets.UTF_8);

            try (MockedStatic<TerminalEncodingIdentifierFactory> factoryMock = mockStatic(TerminalEncodingIdentifierFactory.class)) {
                factoryMock.when(TerminalEncodingIdentifierFactory::createTerminalEncodingIdentifier)
                    .thenReturn(mockEncodingIdentifier);

                TerminalParametersIdentifier.TerminalParameters result = TerminalParametersIdentifier.getTerminalParameters();

                assertThat(result.height()).isEqualTo(28);
                assertThat(result.width()).isEqualTo(78);
                assertThat(result.encoding()).isEqualTo(StandardCharsets.UTF_8);
            }
        }
    }

    @DisplayName("Тест провального получения параметров терминала (значения по умолчанию)")
    @Test
    void testGetTerminalParametersFailed() {
        Terminal mockTerminal = mock(Terminal.class);
        Size mockSize = mock(Size.class);

        when(mockSize.getRows()).thenReturn(0);
        when(mockSize.getColumns()).thenReturn(80);
        when(mockTerminal.getSize()).thenReturn(mockSize);

        try (MockedStatic<TerminalBuilder> terminalBuilderMock = mockStatic(TerminalBuilder.class)) {
            terminalBuilderMock.when(TerminalBuilder::terminal).thenReturn(mockTerminal);

            TerminalParametersIdentifier.TerminalParameters result = TerminalParametersIdentifier.getTerminalParameters();

            assertThat(result.height()).isEqualTo(100);
            assertThat(result.width()).isEqualTo(100);
            assertThat(result.encoding()).isEqualTo(Charset.defaultCharset());
        }
    }
}
