package ru.yandex.practicum.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exception.exception.HintDictionaryIsEmptyException;
import ru.yandex.practicum.exception.exception.UserInputException;
import ru.yandex.practicum.exception.exception.WordNotFoundInDictionaryException;
import java.io.PrintWriter;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class WordleGameTest {

    private WordleGame game;
    private WordleDictionary testDictionary;
    private PrintWriter testLogger;

    @BeforeEach
    void setUp() {
        testLogger = new PrintWriter(System.out, true);

        testDictionary = new WordleDictionary(Set.of("кошка", "кашак", "шапка"));
        game = new WordleGame(6, testDictionary);
    }

    @Test
    void shouldReturnWinMaskAndDecreaseSteps() throws UserInputException {
        testLogger.println("Запуск теста победного хода...");

        String mask = game.playerMove("кошка");

        assertEquals("+++++", mask);
        assertEquals(5, game.getSteps());
        assertTrue(game.wasWin(mask));
    }

    @Test
    void shouldCalculateMaskCorrectly() throws UserInputException {
        String mask = game.playerMove("шапка");

        assertEquals("^--++", mask);
        assertFalse(game.wasWin(mask));
    }

    @Test
    void shouldBeGameOverWhenStepsAreZero() throws HintDictionaryIsEmptyException {
        WordleGame game = new WordleGame(1, testDictionary);
        assertFalse(game.gameOver());

        game.botMove("кашак");

        assertTrue(game.gameOver());
    }
}