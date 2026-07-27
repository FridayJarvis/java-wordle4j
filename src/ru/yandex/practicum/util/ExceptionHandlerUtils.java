package ru.yandex.practicum.util;

import ru.yandex.practicum.io.Logger;

public class ExceptionHandlerUtils {
    private ExceptionHandlerUtils() {
    }

    public static void printExceptionToLog(final Throwable e) {
        final StringBuilder logMessage = new StringBuilder();
        final StackTraceElement[] stackTrace = e.getStackTrace();

        if (stackTrace.length > 0) {
            StackTraceElement root = stackTrace[0];

            logMessage.append("Ошибка: ").append(e.getClass().getSimpleName()).append(" в классе: ").append(root.getClassName())
                    .append(", методе: ").append(root.getMethodName()).append('\n');
            logMessage.append("Стек-трейс:\n");

            for (final StackTraceElement stackTraceEl : stackTrace) {
                logMessage.append("\tметод: ").append(stackTraceEl.getClassName()).append(stackTraceEl.getMethodName())
                        .append('(').append(stackTraceEl.getFileName()).append(':').append(stackTraceEl.getLineNumber()).append(")\n");
            }

            Logger.log(logMessage.toString());
        }
    }
}