package org.dm.view;

import org.dm.core.GameSettings;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConsoleSettingsReaderTest {

    private final ByteArrayOutputStream output = new ByteArrayOutputStream();

    @Test
    public void emptyAnswersKeepTheDefaults() {
        GameSettings settings = read("\n\n\n\n\n\n\n\n");

        assertEquals(new GameSettings().toValues(), settings.toValues());
    }

    @Test
    public void answersAreAppliedInTheOrderOfTheQuestions() {
        GameSettings settings = read("8\n9\n15\n20\n2\n3\n7\n5\n");

        assertEquals(8, settings.getLength());
        assertEquals(9, settings.getHeight());
        assertEquals(15, settings.getHealthHerbivores());
        assertEquals(20, settings.getHealthPredators());
        assertEquals(2, settings.getSpeedHerbivores());
        assertEquals(3, settings.getSpeedPredators());
        assertEquals(7, settings.getAttackPredators());
        assertEquals(5, settings.getNutritionalValue());
    }

    @Test
    public void onlyTheAnsweredFieldsChange() {
        GameSettings settings = read("7\n\n\n\n\n\n\n\n");

        assertEquals(7, settings.getLength());
        assertEquals(new GameSettings().getHeight(), settings.getHeight());
        assertEquals(new GameSettings().getAttackPredators(), settings.getAttackPredators());
    }

    @Test
    public void invalidAnswerIsAskedAgain() {
        GameSettings settings = read("abc\n99\n7\n\n\n\n\n\n\n\n");

        assertEquals(7, settings.getLength());
        String text = output();
        assertTrue(text.contains("Map length must be a whole number"));
        assertTrue(text.contains("Map length must be between 5 and 20"));
    }

    @Test
    public void promptShowsTheRangeAndTheDefaultValue() {
        read("\n\n\n\n\n\n\n\n");

        assertTrue(output().contains("Map length (5-20) [12]"));
        assertTrue(output().contains("Predator attack (1-50) [4]"));
    }

    @Test
    public void endOfInputFallsBackToDefaults() {
        GameSettings settings = read("");

        assertEquals(new GameSettings().toValues(), settings.toValues());
    }

    @Test
    public void inputEndingInTheMiddleKeepsTheAnswersGivenSoFar() {
        GameSettings settings = read("6\n10\n");

        assertEquals(6, settings.getLength());
        assertEquals(10, settings.getHeight());
        assertEquals(new GameSettings().getHealthHerbivores(), settings.getHealthHerbivores());
    }

    private GameSettings read(String input) {
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        PrintStream out = new PrintStream(output, true, StandardCharsets.UTF_8);
        return new ConsoleSettingsReader(scanner, out).read();
    }

    private String output() {
        return output.toString(StandardCharsets.UTF_8);
    }
}
