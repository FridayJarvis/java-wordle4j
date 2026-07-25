package ru.yandex.practicum.game;

import ru.yandex.practicum.exception.Exception.HintDictionaryIsEmptyException;
import ru.yandex.practicum.util.WordFormatUtils;
import ru.yandex.practicum.exception.Exception.UserInputException;
import ru.yandex.practicum.exception.Exception.WordNotFoundInDictionaryException;

import java.util.*;

public class WordleGame {
    private static final String RIGHT_WORDLE_MASK = "+++++";

    private static final char DELETED_CHAR_MARKER = '!';

    private final String answer;

    private int steps;

    private final WordleDictionary dictionary;
    private final WordleDictionary dictionaryForHints;

    public WordleGame(int steps, WordleDictionary dictionary) {
        this.steps = steps;
        this.dictionary = dictionary;
        answer = dictionary.getRandomWord();
        dictionaryForHints = new WordleDictionary(new HashSet<>(dictionary.getWords()));
    }

    public boolean gameOver() {
        return steps <= 0;
    }

    public String playerMove(String guess) throws UserInputException {
        guess = WordFormatUtils.normalize(guess);

        if (!WordFormatUtils.isValidDictionaryWord(guess))
            throw new UserInputException();
        if (!dictionary.contains(guess))
            throw new WordNotFoundInDictionaryException();


        String wordleMask = calculateWordleMask(guess);
        updateHints(guess, wordleMask);

        --steps;
        return wordleMask;
    }

    public String botMove(final String botGuess) throws HintDictionaryIsEmptyException {
        String wordleMask = calculateWordleMask(botGuess);

        if (dictionaryForHints.isEmpty()) {
            throw new HintDictionaryIsEmptyException();
        }

        updateHints(botGuess, wordleMask);
        --steps;
        return wordleMask;
    }

    private void updateHints(final String guess, final String wordleMask) {
        Iterator<String> iterator = dictionaryForHints.getWords().iterator();

        while (iterator.hasNext()) {
            final String word = iterator.next();
            for (int i = 0; i < guess.length(); i++) {
                char guessChar = guess.charAt(i);
                char maskChar = wordleMask.charAt(i);

                if (maskChar == '+' && word.charAt(i) != guessChar) {
                    iterator.remove();
                    break;
                } else if (maskChar == '^' && (word.indexOf(guessChar) == -1 || word.charAt(i) == guessChar)) {
                    iterator.remove();
                    break;
                } else if (maskChar == '-' && !answer.contains(String.valueOf(guessChar))
                        && word.contains(String.valueOf(guessChar))) {
                    iterator.remove();
                    break;
                }
            }
        }
    }

    public String getBotGuess() {
        return dictionaryForHints.getRandomWord();
    }

    private String calculateWordleMask(final String guess) {
        final char[] mask = new char[5];
        final StringBuilder answerSb = new StringBuilder(answer);

        for (int i = 0; i < guess.length(); i++) {
            if (guess.charAt(i) == answer.charAt(i)) {
                mask[i] = '+';
                answerSb.setCharAt(i, DELETED_CHAR_MARKER);
            }
        }

        for (int i = 0; i < guess.length(); i++) {
            if (mask[i] == '+') {
                continue;
            }

            final int idxInAnswer = answerSb.indexOf(String.valueOf(guess.charAt(i)));
            if (idxInAnswer != -1) {
                mask[i] = '^';
                answerSb.setCharAt(i, DELETED_CHAR_MARKER);
            } else {
                mask[i] = '-';
            }
        }
        return new String(mask);
    }

    public int getSteps() {
        return steps;
    }

    public boolean wasWin(final String wordleMask) {
        return wordleMask.equals(RIGHT_WORDLE_MASK);
    }

    public boolean shouldBotMove(String userInput) {
        return userInput.isBlank();
    }
}