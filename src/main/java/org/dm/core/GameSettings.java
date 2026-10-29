package org.dm.core;

import java.util.EnumMap;
import java.util.Map;

public class GameSettings {

    private static final double PREDATOR_SHARE = 0.055;
    private static final double HERBIVORE_SHARE = 0.167;
    private static final double EDIBLE_SHARE = 0.125;
    private static final double INANIMATE_SHARE = 0.125;
    private static final double MIN_EDIBLE_SHARE = 0.035;
    private static final double MAX_HERBIVORE_SHARE = 0.30;
    private static final double MAX_PREDATOR_SHARE = 0.10;
    private static final long DEFAULT_TURN_DELAY_MILLIS = 700;

    private final int length;
    private final int height;
    private final int healthHerbivores;
    private final int healthPredators;
    private final int speedHerbivores;
    private final int speedPredators;
    private final int attackPredators;
    private final int nutritionalValue;

    private final int totalFox;
    private final int totalWolf;
    private final int totalHogs;
    private final int totalRabbit;
    private final int totalGrass;
    private final int totalMushrooms;
    private final int totalRocks;
    private final int totalTrees;

    private final int maxHerbivores;
    private final int maxPredators;
    private final int minEdibleCount;
    private final long turnDelayMillis;

    public GameSettings() {
        this(new Builder());
    }

    private GameSettings(Builder builder) {
        length = SettingsField.LENGTH.validate(builder.length);
        height = SettingsField.HEIGHT.validate(builder.height);
        healthHerbivores = SettingsField.HERBIVORE_HEALTH.validate(builder.healthHerbivores);
        healthPredators = SettingsField.PREDATOR_HEALTH.validate(builder.healthPredators);
        speedHerbivores = SettingsField.HERBIVORE_SPEED.validate(builder.speedHerbivores);
        speedPredators = SettingsField.PREDATOR_SPEED.validate(builder.speedPredators);
        attackPredators = SettingsField.PREDATOR_ATTACK.validate(builder.attackPredators);
        nutritionalValue = SettingsField.NUTRITIONAL_VALUE.validate(builder.nutritionalValue);

        int area = length * height;
        int predators = share(area, PREDATOR_SHARE, 2);
        int herbivores = share(area, HERBIVORE_SHARE, 2);
        int edible = share(area, EDIBLE_SHARE, 2);
        int inanimate = share(area, INANIMATE_SHARE, 2);

        totalFox = count(builder.foxes, (predators + 1) / 2);
        totalWolf = count(builder.wolves, predators / 2);
        totalHogs = count(builder.hogs, (herbivores + 1) / 2);
        totalRabbit = count(builder.rabbits, herbivores / 2);
        totalGrass = count(builder.grass, (edible + 1) / 2);
        totalMushrooms = count(builder.mushrooms, edible / 2);
        totalRocks = count(builder.rocks, (inanimate + 1) / 2);
        totalTrees = count(builder.trees, inanimate / 2);

        maxHerbivores = Math.max(totalHogs + totalRabbit, share(area, MAX_HERBIVORE_SHARE, 0));
        maxPredators = Math.max(totalFox + totalWolf, share(area, MAX_PREDATOR_SHARE, 0));
        minEdibleCount = builder.minEdibleCount != null
                ? count(builder.minEdibleCount, 0)
                : share(area, MIN_EDIBLE_SHARE, 2);

        if (builder.turnDelayMillis <= 0) {
            throw new IllegalArgumentException("Turn delay must be positive");
        }
        turnDelayMillis = builder.turnDelayMillis;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static GameSettings from(Map<SettingsField, Integer> values) {
        return builder()
                .length(valueOf(values, SettingsField.LENGTH))
                .height(valueOf(values, SettingsField.HEIGHT))
                .healthHerbivores(valueOf(values, SettingsField.HERBIVORE_HEALTH))
                .healthPredators(valueOf(values, SettingsField.PREDATOR_HEALTH))
                .speedHerbivores(valueOf(values, SettingsField.HERBIVORE_SPEED))
                .speedPredators(valueOf(values, SettingsField.PREDATOR_SPEED))
                .attackPredators(valueOf(values, SettingsField.PREDATOR_ATTACK))
                .nutritionalValue(valueOf(values, SettingsField.NUTRITIONAL_VALUE))
                .build();
    }

    public Map<SettingsField, Integer> toValues() {
        Map<SettingsField, Integer> values = new EnumMap<>(SettingsField.class);
        values.put(SettingsField.LENGTH, length);
        values.put(SettingsField.HEIGHT, height);
        values.put(SettingsField.HERBIVORE_HEALTH, healthHerbivores);
        values.put(SettingsField.PREDATOR_HEALTH, healthPredators);
        values.put(SettingsField.HERBIVORE_SPEED, speedHerbivores);
        values.put(SettingsField.PREDATOR_SPEED, speedPredators);
        values.put(SettingsField.PREDATOR_ATTACK, attackPredators);
        values.put(SettingsField.NUTRITIONAL_VALUE, nutritionalValue);
        return values;
    }

    private static int valueOf(Map<SettingsField, Integer> values, SettingsField field) {
        return values.getOrDefault(field, field.getDefaultValue());
    }

    private static int share(int area, double share, int minimum) {
        return Math.max(minimum, (int) Math.round(area * share));
    }

    private static int count(Integer explicit, int derived) {
        int result = explicit != null ? explicit : derived;
        if (result < 0) {
            throw new IllegalArgumentException("Amount cannot be negative: " + result);
        }
        return result;
    }

    public int getLength() {
        return length;
    }

    public int getHeight() {
        return height;
    }

    public int getTotalFox() {
        return totalFox;
    }

    public int getTotalWolf() {
        return totalWolf;
    }

    public int getTotalHogs() {
        return totalHogs;
    }

    public int getTotalRabbit() {
        return totalRabbit;
    }

    public int getTotalGrass() {
        return totalGrass;
    }

    public int getTotalMushrooms() {
        return totalMushrooms;
    }

    public int getTotalRocks() {
        return totalRocks;
    }

    public int getTotalTrees() {
        return totalTrees;
    }

    public int getEdibleRespawnCount() {
        return totalGrass + totalMushrooms;
    }

    public int getMaxHerbivores() {
        return maxHerbivores;
    }

    public int getMaxPredators() {
        return maxPredators;
    }

    public int getHealthHerbivores() {
        return healthHerbivores;
    }

    public int getHealthPredators() {
        return healthPredators;
    }

    public int getSpeedHerbivores() {
        return speedHerbivores;
    }

    public int getSpeedPredators() {
        return speedPredators;
    }

    public int getAttackPredators() {
        return attackPredators;
    }

    public int getNutritionalValue() {
        return nutritionalValue;
    }

    public int getMinEdibleCount() {
        return minEdibleCount;
    }

    public long getTurnDelayMillis() {
        return turnDelayMillis;
    }

    public static class Builder {

        private int length = SettingsField.LENGTH.getDefaultValue();
        private int height = SettingsField.HEIGHT.getDefaultValue();
        private int healthHerbivores = SettingsField.HERBIVORE_HEALTH.getDefaultValue();
        private int healthPredators = SettingsField.PREDATOR_HEALTH.getDefaultValue();
        private int speedHerbivores = SettingsField.HERBIVORE_SPEED.getDefaultValue();
        private int speedPredators = SettingsField.PREDATOR_SPEED.getDefaultValue();
        private int attackPredators = SettingsField.PREDATOR_ATTACK.getDefaultValue();
        private int nutritionalValue = SettingsField.NUTRITIONAL_VALUE.getDefaultValue();

        private Integer foxes;
        private Integer wolves;
        private Integer hogs;
        private Integer rabbits;
        private Integer grass;
        private Integer mushrooms;
        private Integer rocks;
        private Integer trees;
        private Integer minEdibleCount;
        private long turnDelayMillis = DEFAULT_TURN_DELAY_MILLIS;

        public Builder length(int length) {
            this.length = length;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder healthHerbivores(int healthHerbivores) {
            this.healthHerbivores = healthHerbivores;
            return this;
        }

        public Builder healthPredators(int healthPredators) {
            this.healthPredators = healthPredators;
            return this;
        }

        public Builder speedHerbivores(int speedHerbivores) {
            this.speedHerbivores = speedHerbivores;
            return this;
        }

        public Builder speedPredators(int speedPredators) {
            this.speedPredators = speedPredators;
            return this;
        }

        public Builder attackPredators(int attackPredators) {
            this.attackPredators = attackPredators;
            return this;
        }

        public Builder nutritionalValue(int nutritionalValue) {
            this.nutritionalValue = nutritionalValue;
            return this;
        }

        public Builder foxes(int foxes) {
            this.foxes = foxes;
            return this;
        }

        public Builder wolves(int wolves) {
            this.wolves = wolves;
            return this;
        }

        public Builder hogs(int hogs) {
            this.hogs = hogs;
            return this;
        }

        public Builder rabbits(int rabbits) {
            this.rabbits = rabbits;
            return this;
        }

        public Builder grass(int grass) {
            this.grass = grass;
            return this;
        }

        public Builder mushrooms(int mushrooms) {
            this.mushrooms = mushrooms;
            return this;
        }

        public Builder rocks(int rocks) {
            this.rocks = rocks;
            return this;
        }

        public Builder trees(int trees) {
            this.trees = trees;
            return this;
        }

        public Builder minEdibleCount(int minEdibleCount) {
            this.minEdibleCount = minEdibleCount;
            return this;
        }

        public Builder turnDelayMillis(long turnDelayMillis) {
            this.turnDelayMillis = turnDelayMillis;
            return this;
        }

        public GameSettings build() {
            return new GameSettings(this);
        }
    }
}
