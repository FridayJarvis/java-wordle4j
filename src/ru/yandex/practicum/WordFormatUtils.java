package ru.yandex.practicum;

public class WordFormatUtils {
    static final String ALPHABET = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";

    private WordFormatUtils() {}

    public static String normalize(String word) {
        return word.trim().toLowerCase().replace('ё', 'е');
    }

    public static boolean isValidDictionaryWord(String word) {
        return !word.isBlank() && hasValidLength(word) && inRussianLanguge(word);
    }

    private static boolean inRussianLanguge(String word) {
        word = normalize(word);

        for (char c : word.toCharArray()) {
            if (ALPHABET.indexOf(c) == -1) {
                return false;
            }
        }

        return true;
    }

    private static boolean hasValidLength(String word) {
        return word.trim().length() == 5;
    }
}
