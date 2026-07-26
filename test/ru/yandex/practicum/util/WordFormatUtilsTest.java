package ru.yandex.practicum.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WordFormatUtilsTest {

    @Test
    void shouldNormalizeWordCorrectly() {
        String inputWithSpaces = "  СлОвО  ";
        String inputWithYo = "ЁЖИКИ";
        String alreadyNormal = "кошка";

        assertEquals("слово", WordFormatUtils.normalize(inputWithSpaces));
        assertEquals("ежики", WordFormatUtils.normalize(inputWithYo));
        assertEquals("кошка", WordFormatUtils.normalize(alreadyNormal));
    }

    @Test
    void shouldValidateCorrectRussianWord() {
        assertTrue(WordFormatUtils.isValidDictionaryWord("песня"));
        assertTrue(WordFormatUtils.isValidDictionaryWord("взлет"));
    }

    @Test
    void shouldRejectInvalidWords() {
        assertFalse(WordFormatUtils.isValidDictionaryWord("кот"));
        assertFalse(WordFormatUtils.isValidDictionaryWord("собака"));
        assertFalse(WordFormatUtils.isValidDictionaryWord("hello"));
        assertFalse(WordFormatUtils.isValidDictionaryWord("слон1"));
        assertFalse(WordFormatUtils.isValidDictionaryWord("    "));
    }
}