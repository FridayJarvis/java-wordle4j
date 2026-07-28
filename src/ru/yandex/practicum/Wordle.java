package ru.yandex.practicum;

import ru.yandex.practicum.exception.exception.HintDictionaryIsEmptyException;
import ru.yandex.practicum.exception.exception.UserInputException;
import ru.yandex.practicum.exception.exception.WordNotFoundInDictionaryException;
import ru.yandex.practicum.exception.runtime.DictionaryLoadException;
import ru.yandex.practicum.exception.runtime.EmptyOrNullDictionaryException;
import ru.yandex.practicum.game.WordleDictionary;
import ru.yandex.practicum.game.WordleGame;
import ru.yandex.practicum.io.WordleDictionaryLoader;
import ru.yandex.practicum.util.ExceptionHandlerUtils;
import ru.yandex.practicum.util.WordFormatUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

import static ru.yandex.practicum.io.Logger.log;
import static ru.yandex.practicum.io.Logger.setWriter;

public class Wordle {
    private static final int STEPS = 6;

    public static void main(String[] args) {
        try (PrintWriter fileWriter = new PrintWriter(Files.newBufferedWriter(
                Path.of("log.txt"),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND), true)) {
            setWriter(fileWriter);

            final Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
            log("scanner успешно создался");
            final WordleDictionary dictionary = initDictionary("word_ru.txt");
            final WordleGame game = initGame(STEPS, dictionary);

            printIntroduction(game.getSteps());
            while (!game.gameOver()) {
                System.out.println("Для выхода из игры введите \"exit\"");
                System.out.println("Попыток: " + game.getSteps());
                System.out.println("Введите ответ (для автоматической подсказки нажмите клавишу Enter):");
                String userInput = scanner.nextLine();
                log(String.format("Пользователь ввел с клавиатуры \"%s\"", userInput));

                if (WordFormatUtils.normalize(userInput).equals("exit")) {
                    System.out.println("Вы вышли из игры");
                    log("Пользователь сам вышел из игры");
                    return;
                }

                final String wordleMask;
                try {
                    if (game.shouldBotMove(userInput)) {
                        final String botGuess = game.getBotGuess();
                        wordleMask = game.botMove(botGuess);

                        log(String.format("Бот дал подсказку: \"%s\"\n" +
                                        "\t- wordle-маска: %s\n" +
                                        "\t- количество ходов осталось после хода: %d",
                                botGuess, wordleMask, game.getSteps()));

                        System.out.println(botGuess);
                    } else {
                        wordleMask = game.playerMove(userInput);

                        log(String.format("Игрок ввел ответ: \"%s\"\n" +
                                        "\t- wordle-маска: %s\n" +
                                        "\t- количество оставшихся ходов, оставшихся после хода: %d",
                                userInput, wordleMask, game.getSteps()));
                    }
                    System.out.println(wordleMask + '\n');

                    if (game.wasWin(wordleMask)) {
                        System.out.println("\nВы отгадали слово и победили в игре!");
                        log(String.format("Игрок отгадал слово и победил\n" +
                                        "\t- wordle-маска: %s\n" +
                                        "\t- количество ходов: %s",
                                wordleMask, game.getSteps()));
                        return;
                    } else if (game.gameOver()) {
                        System.out.println("\nВы проиграли!");
                        return;
                    }
                } catch (WordNotFoundInDictionaryException e) {
                    System.err.printf("\nСлова \"%s\" в словаре нет. Введите ответ еще раз. Попыток: %d\n", userInput, game.getSteps());
                } catch (UserInputException e) {
                    System.err.println("\nНевалидный ввод, слово должно быть 5-ти буквенным и состоять из русских символов.\n" +
                            "Введите слово еще раз. Попыток осталось: " + game.getSteps());
                } catch (HintDictionaryIsEmptyException e) {
                    System.err.println("\nСловарь подсказок пуст. Введите ответ сами. Попыток осталось: " + game.getSteps());
                }
            }
        } catch (IOException e) {
            System.err.println("Невозможно создать файл лога. Логи будут выводиться в консоль");
        } catch (DictionaryLoadException e) {
            ExceptionHandlerUtils.printExceptionToLog(e);
            System.err.println("Критическая ошибка: Не удалось загрузить файл словаря. Игра прервана.");
        } catch (EmptyOrNullDictionaryException e) {
            ExceptionHandlerUtils.printExceptionToLog(e);
            System.err.println("Критическая ошибка: Словарь пуст или не содержит подходящих слов. Игра прервана.");
        } catch (RuntimeException e) {
            ExceptionHandlerUtils.printExceptionToLog(e);
            System.err.println("Произошла непредвиденная системная ошибка. Подробности сохранены в лог.");
        }
    }

    private static WordleGame initGame(final int steps, WordleDictionary dictionary) {
        final WordleGame game = new WordleGame(steps, dictionary);
        log(String.format("Игра успешно создана.\n" +
                        "\t- слово загадано: %s\n" +
                        "\t- оставшихся попыток: %s",
                game.getAnswer(), game.getSteps()));

        return game;
    }

    private static WordleDictionary initDictionary(final String path) {
        try {
            WordleDictionary dictionary = WordleDictionaryLoader.load(path);
            log("dictionary успешно загружен");
            return dictionary;
        } catch (IOException e) {
            throw new DictionaryLoadException("Ошибка загрузки словаря из " + path);
        }
    }

    private static void printIntroduction(final int gameSteps) {
        System.out.printf("""
                                    Добро пожаловать в игру!
                Было загадано 5-ти буквенное слово на русском языке, нужно его отгадать.
                При вводе ответа, компьютер будет давать ответ в виде +-^^+, где:
                \t+ значит, что буква есть в слове и она на своем месте
                \t^ значит, что буква есть, но не на своем месте
                \t- значит, что такой буквы в слове нет
                У вас %d попыток\n\n""", gameSteps);
    }
}