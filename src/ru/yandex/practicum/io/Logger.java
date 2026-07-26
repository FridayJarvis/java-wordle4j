package ru.yandex.practicum.io;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class Logger {
    private static PrintWriter writer = new PrintWriter(System.out, true, StandardCharsets.UTF_8);

    private Logger() {
    }

    public static void setWriter(PrintWriter newWriter) {
        writer = newWriter;
    }

    public static void log(String logMessage) {
        writer.println(logMessage);
    }
}