package ru.yandex.practicum.exception.runtime;

public class EmptyOrNullDictionaryException extends RuntimeException {
    public EmptyOrNullDictionaryException(String message) {
        super(message);
    }
}