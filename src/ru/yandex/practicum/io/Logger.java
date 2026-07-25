package ru.yandex.practicum.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Logger {
    private Logger() {}

    public static void log(final String  log) {
        try (final BufferedWriter writer = Files.newBufferedWriter(
                Path.of("log.txt"),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {

            writer.write(log);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Невозможно обработать ошибку создания лога");
            throw new RuntimeException(e);
        }
    }
}