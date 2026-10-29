package org.dm.entities.living_entities.predators;

import org.dm.entities.living_entities.Creature;

public class Wolf extends Predator {

    public Wolf(int speedPoint, int healthPoint, int powerAttack) {
        super(speedPoint, healthPoint, powerAttack);
    }

    @Override
    protected Creature createOffspring() {
        return new Wolf(getSpeedPoint(), getMaxHealthPoint(), getPowerAttack());
    }
}
