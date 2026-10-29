package org.dm.entities.living_entities.herbivores;

import org.dm.core.GameMap;
import org.dm.core.Position;
import org.dm.entities.Entity;
import org.dm.entities.living_entities.Creature;
import org.dm.entities.static_entities.edible_objects.Edible;

public abstract class Herbivore extends Creature {

    private static final int REPRODUCTION_COOLDOWN = 8;

    protected Herbivore(int speedPoint, int healthPoint) {
        super(speedPoint, healthPoint);
    }

    @Override
    protected boolean isTarget(Entity entity) {
        return entity instanceof Edible;
    }

    @Override
    protected void interact(GameMap map, Position targetPosition) {
        if (map.getEntity(targetPosition) instanceof Edible food) {
            map.remove(targetPosition);
            resetHunger();
            heal(food.getNutritional());
        }
    }

    @Override
    protected int getReproductionCooldown() {
        return REPRODUCTION_COOLDOWN;
    }
}
