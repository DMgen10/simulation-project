package org.dm.entities.living_entities.herbivores;

import org.dm.entities.living_entities.Creature;

public class Rabbit extends Herbivore {

    public Rabbit(int speedPoint, int healthPoint) {
        super(speedPoint, healthPoint);
    }

    @Override
    protected Creature createOffspring() {
        return new Rabbit(getSpeedPoint(), getMaxHealthPoint());
    }
}
