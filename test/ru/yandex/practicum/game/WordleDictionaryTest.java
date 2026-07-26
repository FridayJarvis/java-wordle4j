package ru.yandex.practicum.game;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exception.runtime.EmptyOrNullDictionaryException;
import java.util.Collections;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryTest {

    @Test
    void shouldFindWordInDictionary() {
        Set<String> words = Set.of("кошка", "собак", "птица");
        WordleDictionary dictionary = new WordleDictionary(words);

        assertTrue(dictionary.contains("кошка"));
        assertFalse(dictionary.contains("мышка"));
    }

    @Test
    void shouldThrowExceptionWhenDictionaryIsEmpty() {
        WordleDictionary emptyDict = new WordleDictionary(Collections.emptySet());

        assertThrows(EmptyOrNullDictionaryException.class, emptyDict::getRandomWord);
    }
}