package org.dm.core;

public enum SettingsField {

    LENGTH("Map length", 5, 20, 12),
    HEIGHT("Map height", 5, 20, 12),
    HERBIVORE_HEALTH("Herbivore health", 1, 100, 10),
    PREDATOR_HEALTH("Predator health", 1, 100, 12),
    HERBIVORE_SPEED("Herbivore speed (cells per turn)", 1, 5, 1),
    PREDATOR_SPEED("Predator speed (cells per turn)", 1, 5, 1),
    PREDATOR_ATTACK("Predator attack", 1, 50, 4),
    NUTRITIONAL_VALUE("Food nutritional value", 1, 20, 1);

    private final String label;
    private final int min;
    private final int max;
    private final int defaultValue;

    SettingsField(String label, int min, int max, int defaultValue) {
        this.label = label;
        this.min = min;
        this.max = max;
        this.defaultValue = defaultValue;
    }

    public int parse(String text) {
        try {
            return validate(Integer.parseInt(text == null ? "" : text.trim()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(label + " must be a whole number");
        }
    }

    public int validate(int value) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(label + " must be between " + min + " and " + max);
        }
        return value;
    }

    public String getLabel() {
        return label;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getDefaultValue() {
        return defaultValue;
    }
}
