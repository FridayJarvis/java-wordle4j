package ru.yandex.practicum;

import ru.yandex.practicum.exception.exception.HintDictionaryIsEmptyException;
import ru.yandex.practicum.exception.exception.UserInputException;
import ru.yandex.practicum.exception.exception.WordNotFoundInDictionaryException;
import ru.yandex.practicum.exception.runtime.DictionaryLoadException;
import ru.yandex.practicum.game.WordleDictionary;
import ru.yandex.practicum.game.WordleGame;
import ru.yandex.practicum.io.Logger;
import ru.yandex.practicum.io.WordleDictionaryLoader;
import ru.yandex.practicum.util.ExceptionHandlerUtils;
import ru.yandex.practicum.util.WordFormatUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


public class Wordle {
    private static final int STEPS = 6;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        Logger.log("scanner успешно создался");
        WordleDictionary dictionary;

        try {
            dictionary = WordleDictionaryLoader.load("words_ru.txt");
            Logger.log("dictionary успешно загружен");
        } catch (IOException e) {
            ExceptionHandlerUtils.printExceptionToLog(e);
            throw new DictionaryLoadException("Ошибка загрузки словаря из word_ru.txt");
        }

        WordleGame game = new WordleGame(STEPS, dictionary);

        printIntroduction(game);
        while (!game.gameOver()) {
            System.out.println("Для выхода из игры введите \"exit\"");
            System.out.println("Попыток: " + game.getSteps());
            System.out.println("Введите ответ (для автоматической подсказки нажмите клавишу Enter):");
            String userInput = scanner.nextLine();

            if (WordFormatUtils.normalize(userInput).equals("exit")) {
                System.out.println("Вы вышли из игры");
                return;
            }

            String wordleMask;

            try {
                if (game.shouldBotMove(userInput)) {
                    final String botGuess = game.getBotGuess();
                    wordleMask = game.botMove(botGuess);

                    System.out.println(botGuess);
                } else {
                    wordleMask = game.playerMove(userInput);
                }
                System.out.println(wordleMask + '\n');

                if (game.wasWin(wordleMask)) {
                    System.out.println("\nВы отгадали слово и победили в игре!");
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
    }

    private static void printIntroduction(WordleGame game) {
        System.out.printf("""
                                    Добро пожаловать в игру!
                Было загадано 5-ти буквенное слово на русском языке, нужно его отгадать.
                При вводе ответа, компьютер будет давать ответ в виде +-^^+, где:
                \t+ значит, что буква есть в слове и она на своем месте
                \t^ значит, что буква есть, но не на своем месте
                \t- значит, что такой буквы в слове нет
                У вас %d попыток\n\n""", game.getSteps());
    }
}
