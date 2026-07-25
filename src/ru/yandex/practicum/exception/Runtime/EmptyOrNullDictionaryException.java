package ru.yandex.practicum.exception.Runtime;

public class EmptyOrNullDictionaryException extends RuntimeException {
    public EmptyOrNullDictionaryException(String message) {
        super(message);
    }
}
