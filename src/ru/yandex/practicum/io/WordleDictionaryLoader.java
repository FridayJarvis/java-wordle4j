package ru.yandex.practicum.io;

import ru.yandex.practicum.exception.runtime.EmptyOrNullDictionaryException;
import ru.yandex.practicum.game.WordleDictionary;
import ru.yandex.practicum.util.WordFormatUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class WordleDictionaryLoader {
    private WordleDictionaryLoader() {
    }

    public static WordleDictionary load(final String path) throws IOException {
        final Set<String> validWords = new HashSet<>();

        try (final BufferedReader reader = Files.newBufferedReader(Path.of(path), StandardCharsets.UTF_8)) {
            String word;
            while ((word = reader.readLine()) != null) {
                word = WordFormatUtils.normalize(word);

                if (WordFormatUtils.isValidDictionaryWord(word)) {
                    validWords.add(word);
                }
            }
        }

        if (validWords.isEmpty()) {
            throw new EmptyOrNullDictionaryException("Словарь пуст по итогу его загрузки");
        }

        return new WordleDictionary(validWords);
    }
}
