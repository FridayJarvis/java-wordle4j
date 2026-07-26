package ru.yandex.practicum.util;

public class WordFormatUtils {
    private static final String ALPHABET = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";

    private WordFormatUtils() {
    }

    public static String normalize(String word) {
        return word.trim().toLowerCase().replace('ё', 'е');
    }

    public static boolean isValidDictionaryWord(final String word) {
        return !word.isBlank() && hasValidLength(word) && inRussianLanguage(word);
    }

    private static boolean inRussianLanguage(String word) {
        word = normalize(word);

        for (final char c : word.toCharArray()) {
            if (ALPHABET.indexOf(c) == -1) {
                return false;
            }
        }

        return true;
    }

    private static boolean hasValidLength(final String word) {
        return word.trim().length() == 5;
    }
}
