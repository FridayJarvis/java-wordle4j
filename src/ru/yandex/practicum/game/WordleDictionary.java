package ru.yandex.practicum.game;

import ru.yandex.practicum.exception.runtime.EmptyOrNullDictionaryException;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class WordleDictionary {
    private final Set<String> words;

    private final Random random = new Random();

    public WordleDictionary(final Set<String> words) {
        this.words = words;
    }

    public boolean contains(final String word) {
        return words.contains(word);
    }

    public boolean isEmpty() {
        return words.isEmpty();
    }

    public String getRandomWord() {
        if (words == null || words.isEmpty())
            throw new EmptyOrNullDictionaryException("Нельзя выбрать слово. Словарь пустой или его не существует");

        int randomIndex = random.nextInt(words.size());

        Iterator<String> iterator = words.iterator();
        String randomWord = "";
        for (int i = 0; i <= randomIndex; i++) {
            randomWord = iterator.next();
        }
        return randomWord;
    }

    public Set<String> getWords() {
        return words;
    }
}
