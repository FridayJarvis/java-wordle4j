package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {

    public static WordleDictionary load(String path) throws IOException {
        List<String> validWords = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Path.of(path), StandardCharsets.UTF_8)) {
            String word;

            while ((word = reader.readLine()) != null) {
                word = WordFormatUtils.normalize(word);

                if (WordFormatUtils.isValidDictionaryWord(word)) {
                    validWords.add(word);
                }
            }
        }

        return new WordleDictionary(validWords);
    }
}
