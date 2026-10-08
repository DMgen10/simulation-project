package org.dm.core;

public class GameSettings {

    /*
    1. Размеры карты - длина и ширина
    2. Количестово сущностей (ВСЕ сущности) 50 % от объема карты
    3. Количество травоядных = Количество хищников = 50 % от всех существ
    4. Количество неживых непитательных сущностей (камни, деревья = 25 % от всех существ
    5. Количество неживых съедобных сущностей (грибы, трава = 25 % от всех сущностей
    6. Скорость травоядных
    7. Скорость хищников
    8. Питательность съедобных объектов
    9. Атака хищников
     */
    private final int length = 10;
    private final int height = 10;

    private final int totalNumberOfEntities = ((length * height) * 50) / 100;
    private final int totalHerbivores = (totalNumberOfEntities * 25) / 100;
    private final int totalPredators = totalHerbivores;
    private final int totalEdibilityEntity = (totalNumberOfEntities * 25) / 100;
    private final int totalInanimate = totalEdibilityEntity;

    private final int totalFox = totalPredators / 2;
    private final int totalWolf = totalFox;

    private final int totalHogs = totalHerbivores / 2;
    private final int totalRabbit = totalHogs;

    private final int totalGrass = totalEdibilityEntity / 2;
    private final int totalMushrooms = totalGrass;

    private final int totalRocks = totalInanimate / 2;
    private final int totalTrees = totalRocks;

    private final int healthPredators = 10;
    private final int healthHerbivores = 10;
    private final int speedHerbivores = 1;
    private final int speedPredators = 1;
    private final int edibility = 1;
    private final int attackPredators = 1;


    public int getLength() {
        return length;
    }

    public int getHeight() {
        return height;
    }

    public int getTotalNumberOfEntities() {
        return totalNumberOfEntities;
    }

    public int getTotalHerbivores() {
        return totalHerbivores;
    }

    public int getTotalPredators() {
        return totalPredators;
    }

    public int getTotalEdibilityEntity() {
        return totalEdibilityEntity;
    }

    public int getTotalInanimate() {
        return totalInanimate;
    }

    public int getSpeedHerbivores() {
        return speedHerbivores;
    }

    public int getSpeedPredators() {
        return speedPredators;
    }

    public int getEdibility() {
        return edibility;
    }

    public int getAttackPredators() {
        return attackPredators;
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

    public int getHealthPredators() {
        return healthPredators;
    }

    public int getHealthHerbivores() {
        return healthHerbivores;
    }
}
