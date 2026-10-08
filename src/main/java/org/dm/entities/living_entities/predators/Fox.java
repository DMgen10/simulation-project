package org.dm.entities.living_entities.predators;

import org.dm.view.EntityType;

public class Fox extends Predator {

    public Fox(int speedPoint, int healthPoint, int powerAttack) {
        super(speedPoint, healthPoint, powerAttack);
    }

    @Override
    public EntityType getViewType() {
        return EntityType.FOX;
    }
}
