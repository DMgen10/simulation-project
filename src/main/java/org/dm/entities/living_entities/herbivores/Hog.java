package org.dm.entities.living_entities.herbivores;

import org.dm.entities.living_entities.Creature;

public class Hog extends Herbivore {

    public Hog(int speedPoint, int healthPoint) {
        super(speedPoint, healthPoint);
    }

    @Override
    protected Creature createOffspring() {
        return new Hog(getSpeedPoint(), getMaxHealthPoint());
    }
}
