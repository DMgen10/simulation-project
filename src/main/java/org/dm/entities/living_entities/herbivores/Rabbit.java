package org.dm.entities.living_entities.herbivores;

import org.dm.view.EntityType;

public class Rabbit extends Herbivore {

    public Rabbit(int speedPoint, int healthPoint) {
        super(speedPoint, healthPoint);
    }

    @Override
    public EntityType getViewType() {
        return EntityType.RABBIT;
    }
}