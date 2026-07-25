package ru.yandex.practicum.exception.runtime;

public class DictionaryLoadException extends RuntimeException {
    public DictionaryLoadException(String message) {
        super(message);
    }
}
