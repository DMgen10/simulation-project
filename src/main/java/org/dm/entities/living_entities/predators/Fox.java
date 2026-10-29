package org.dm.entities.living_entities.predators;

import org.dm.entities.living_entities.Creature;

public class Fox extends Predator {

    public Fox(int speedPoint, int healthPoint, int powerAttack) {
        super(speedPoint, healthPoint, powerAttack);
    }

    @Override
    protected Creature createOffspring() {
        return new Fox(getSpeedPoint(), getMaxHealthPoint(), getPowerAttack());
    }
}
