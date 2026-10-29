package org.dm.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class SettingsFieldTest {

    @Test
    public void parsesWholeNumbers() {
        assertEquals(15, SettingsField.LENGTH.parse("15"));
    }

    @Test
    public void ignoresSurroundingWhitespace() {
        assertEquals(8, SettingsField.HEIGHT.parse("  8 \t"));
    }

    @Test
    public void boundariesAreIncluded() {
        for (SettingsField field : SettingsField.values()) {
            assertEquals(field.getMin(), field.parse(String.valueOf(field.getMin())));
            assertEquals(field.getMax(), field.parse(String.valueOf(field.getMax())));
        }
    }

    @Test
    public void valuesOutsideTheRangeAreRejected() {
        for (SettingsField field : SettingsField.values()) {
            assertThrows(IllegalArgumentException.class, () -> field.parse(String.valueOf(field.getMin() - 1)));
            assertThrows(IllegalArgumentException.class, () -> field.parse(String.valueOf(field.getMax() + 1)));
        }
    }

    @Test
    public void rangeErrorMessageNamesTheFieldAndTheRange() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> SettingsField.LENGTH.parse("100"));

        assertTrue(error.getMessage().contains("Map length"));
        assertTrue(error.getMessage().contains("5"));
        assertTrue(error.getMessage().contains("20"));
    }

    @Test
    public void textThatIsNotANumberIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> SettingsField.LENGTH.parse("abc"));
        assertThrows(IllegalArgumentException.class, () -> SettingsField.LENGTH.parse("1.5"));
        assertThrows(IllegalArgumentException.class, () -> SettingsField.LENGTH.parse("12 13"));
    }

    @Test
    public void emptyAndMissingTextAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> SettingsField.LENGTH.parse(""));
        assertThrows(IllegalArgumentException.class, () -> SettingsField.LENGTH.parse("   "));
        assertThrows(IllegalArgumentException.class, () -> SettingsField.LENGTH.parse(null));
    }

    @Test
    public void notANumberErrorNamesTheField() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> SettingsField.PREDATOR_ATTACK.parse("x"));

        assertTrue(error.getMessage().contains("Predator attack"));
    }

    @Test
    public void validateReturnsTheValueWhenItIsFine() {
        assertEquals(3, SettingsField.PREDATOR_SPEED.validate(3));
    }

    @Test
    public void everyDefaultValueIsInsideItsOwnRange() {
        for (SettingsField field : SettingsField.values()) {
            assertEquals(field.getDefaultValue(), field.validate(field.getDefaultValue()));
        }
    }

    @Test
    public void everyFieldHasALabelAndAnOrderedRange() {
        for (SettingsField field : SettingsField.values()) {
            assertFalse(field.getLabel().isBlank());
            assertTrue(field.getMin() <= field.getMax());
        }
    }
}
