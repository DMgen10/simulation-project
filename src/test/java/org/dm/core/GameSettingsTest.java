package org.dm.core;

import org.junit.Test;
import java.util.EnumMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class GameSettingsTest {

    private final GameSettings settings = new GameSettings();

    @Test
    public void defaultsMatchTheDefaultsOfEveryField() {
        Map<SettingsField, Integer> values = settings.toValues();

        for (SettingsField field : SettingsField.values()) {
            assertEquals(field.getDefaultValue(), (int) values.get(field));
        }
    }

    @Test
    public void defaultWorldKeepsTheTunedBalance() {
        assertEquals(4, settings.getTotalFox());
        assertEquals(4, settings.getTotalWolf());
        assertEquals(12, settings.getTotalHogs());
        assertEquals(12, settings.getTotalRabbit());
        assertEquals(9, settings.getTotalGrass());
        assertEquals(9, settings.getTotalMushrooms());
        assertEquals(9, settings.getTotalRocks());
        assertEquals(9, settings.getTotalTrees());
        assertEquals(5, settings.getMinEdibleCount());
    }

    @Test
    public void builderAppliesEveryValue() {
        GameSettings custom = GameSettings.builder()
                .length(9).height(7)
                .healthHerbivores(15).healthPredators(20)
                .speedHerbivores(2).speedPredators(3)
                .attackPredators(6).nutritionalValue(4)
                .build();

        assertEquals(9, custom.getLength());
        assertEquals(7, custom.getHeight());
        assertEquals(15, custom.getHealthHerbivores());
        assertEquals(20, custom.getHealthPredators());
        assertEquals(2, custom.getSpeedHerbivores());
        assertEquals(3, custom.getSpeedPredators());
        assertEquals(6, custom.getAttackPredators());
        assertEquals(4, custom.getNutritionalValue());
    }

    @Test
    public void fromValuesAppliesEveryValue() {
        Map<SettingsField, Integer> values = new EnumMap<>(SettingsField.class);
        values.put(SettingsField.LENGTH, 8);
        values.put(SettingsField.HEIGHT, 9);
        values.put(SettingsField.HERBIVORE_HEALTH, 11);
        values.put(SettingsField.PREDATOR_HEALTH, 22);
        values.put(SettingsField.HERBIVORE_SPEED, 2);
        values.put(SettingsField.PREDATOR_SPEED, 4);
        values.put(SettingsField.PREDATOR_ATTACK, 9);
        values.put(SettingsField.NUTRITIONAL_VALUE, 3);

        GameSettings custom = GameSettings.from(values);

        assertEquals(8, custom.getLength());
        assertEquals(9, custom.getHeight());
        assertEquals(11, custom.getHealthHerbivores());
        assertEquals(22, custom.getHealthPredators());
        assertEquals(2, custom.getSpeedHerbivores());
        assertEquals(4, custom.getSpeedPredators());
        assertEquals(9, custom.getAttackPredators());
        assertEquals(3, custom.getNutritionalValue());
    }

    @Test
    public void missingValuesFallBackToDefaults() {
        GameSettings custom = GameSettings.from(new EnumMap<>(SettingsField.class));

        assertEquals(settings.toValues(), custom.toValues());
    }

    @Test
    public void valuesSurviveARoundTrip() {
        GameSettings custom = GameSettings.builder().length(15).height(6).attackPredators(10).build();

        assertEquals(custom.toValues(), GameSettings.from(custom.toValues()).toValues());
    }

    @Test
    public void everyOutOfRangeValueIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> GameSettings.builder().length(4).build());
        assertThrows(IllegalArgumentException.class, () -> GameSettings.builder().length(21).build());
        assertThrows(IllegalArgumentException.class, () -> GameSettings.builder().height(4).build());
        assertThrows(IllegalArgumentException.class, () -> GameSettings.builder().height(21).build());
        assertThrows(IllegalArgumentException.class, () -> GameSettings.builder().healthHerbivores(0).build());
        assertThrows(IllegalArgumentException.class, () -> GameSettings.builder().healthPredators(101).build());
        assertThrows(IllegalArgumentException.class, () -> GameSettings.builder().speedHerbivores(0).build());
        assertThrows(IllegalArgumentException.class, () -> GameSettings.builder().speedPredators(6).build());
        assertThrows(IllegalArgumentException.class, () -> GameSettings.builder().attackPredators(0).build());
        assertThrows(IllegalArgumentException.class, () -> GameSettings.builder().attackPredators(51).build());
        assertThrows(IllegalArgumentException.class, () -> GameSettings.builder().nutritionalValue(0).build());
        assertThrows(IllegalArgumentException.class, () -> GameSettings.builder().nutritionalValue(21).build());
    }

    @Test
    public void fromValuesRejectsOutOfRangeValues() {
        Map<SettingsField, Integer> values = new EnumMap<>(SettingsField.class);
        values.put(SettingsField.LENGTH, 100);

        assertThrows(IllegalArgumentException.class, () -> GameSettings.from(values));
    }

    @Test
    public void biggerMapHoldsMoreOfEverything() {
        GameSettings small = GameSettings.builder().length(5).height(5).build();
        GameSettings large = GameSettings.builder().length(20).height(20).build();

        assertTrue(large.getTotalHogs() + large.getTotalRabbit() > small.getTotalHogs() + small.getTotalRabbit());
        assertTrue(large.getTotalFox() + large.getTotalWolf() > small.getTotalFox() + small.getTotalWolf());
        assertTrue(large.getEdibleRespawnCount() > small.getEdibleRespawnCount());
        assertTrue(large.getTotalRocks() + large.getTotalTrees() > small.getTotalRocks() + small.getTotalTrees());
        assertTrue(large.getMaxHerbivores() > small.getMaxHerbivores());
        assertTrue(large.getMaxPredators() > small.getMaxPredators());
    }

    @Test
    public void everyKindOfCreatureIsPresentOnEveryAllowedMapSize() {
        for (int length = 5; length <= 20; length++) {
            for (int height = 5; height <= 20; height++) {
                GameSettings sized = GameSettings.builder().length(length).height(height).build();

                assertTrue(sized.getTotalFox() >= 1);
                assertTrue(sized.getTotalWolf() >= 1);
                assertTrue(sized.getTotalHogs() >= 1);
                assertTrue(sized.getTotalRabbit() >= 1);
            }
        }
    }

    @Test
    public void startingEntitiesLeaveRoomToMoveOnEveryAllowedMapSize() {
        for (int length = 5; length <= 20; length++) {
            for (int height = 5; height <= 20; height++) {
                GameSettings sized = GameSettings.builder().length(length).height(height).build();
                int entities = sized.getTotalFox() + sized.getTotalWolf()
                        + sized.getTotalHogs() + sized.getTotalRabbit()
                        + sized.getTotalGrass() + sized.getTotalMushrooms()
                        + sized.getTotalRocks() + sized.getTotalTrees();

                assertTrue("Too crowded: " + length + "x" + height, entities * 100 <= length * height * 60);
            }
        }
    }

    @Test
    public void populationLimitsAreNeverBelowTheStartingPopulation() {
        for (int size = 5; size <= 20; size++) {
            GameSettings sized = GameSettings.builder().length(size).height(size).build();

            assertTrue(sized.getMaxHerbivores() >= sized.getTotalHogs() + sized.getTotalRabbit());
            assertTrue(sized.getMaxPredators() >= sized.getTotalFox() + sized.getTotalWolf());
        }
    }

    @Test
    public void explicitAmountsOverrideTheDerivedOnes() {
        GameSettings custom = GameSettings.builder()
                .foxes(1).wolves(0).hogs(2).rabbits(3)
                .grass(4).mushrooms(5).rocks(6).trees(7)
                .build();

        assertEquals(1, custom.getTotalFox());
        assertEquals(0, custom.getTotalWolf());
        assertEquals(2, custom.getTotalHogs());
        assertEquals(3, custom.getTotalRabbit());
        assertEquals(4, custom.getTotalGrass());
        assertEquals(5, custom.getTotalMushrooms());
        assertEquals(6, custom.getTotalRocks());
        assertEquals(7, custom.getTotalTrees());
        assertEquals(9, custom.getEdibleRespawnCount());
    }

    @Test
    public void negativeAmountsAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> GameSettings.builder().foxes(-1).build());
        assertThrows(IllegalArgumentException.class, () -> GameSettings.builder().minEdibleCount(-1).build());
    }

    @Test
    public void explicitMinimumOfFoodIsApplied() {
        assertEquals(3, GameSettings.builder().minEdibleCount(3).build().getMinEdibleCount());
    }

    @Test
    public void turnDelayMustBePositive() {
        assertThrows(IllegalArgumentException.class, () -> GameSettings.builder().turnDelayMillis(0).build());
        assertEquals(50, GameSettings.builder().turnDelayMillis(50).build().getTurnDelayMillis());
    }

    @Test
    public void defaultTurnDelayIsPositive() {
        assertTrue(settings.getTurnDelayMillis() > 0);
    }
}
