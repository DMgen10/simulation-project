package org.dm.entities.living_entities.predators;

import org.dm.core.GameMap;
import org.dm.core.Position;
import org.dm.entities.Entity;
import org.dm.entities.living_entities.Creature;
import org.dm.entities.living_entities.herbivores.Herbivore;

public abstract class Predator extends Creature {

    private static final int REPRODUCTION_COOLDOWN = 15;

    private final int powerAttack;

    protected Predator(int speedPoint, int healthPoint, int powerAttack) {
        super(speedPoint, healthPoint);
        this.powerAttack = powerAttack;
    }

    @Override
    protected boolean isTarget(Entity entity) {
        return entity instanceof Herbivore;
    }

    @Override
    protected void interact(GameMap map, Position targetPosition) {
        if (map.getEntity(targetPosition) instanceof Herbivore prey) {
            prey.takeDamage(powerAttack);
            if (prey.isDead()) {
                map.remove(targetPosition);
                resetHunger();
            }
        }
    }

    @Override
    protected int getReproductionCooldown() {
        return REPRODUCTION_COOLDOWN;
    }

    public int getPowerAttack() {
        return powerAttack;
    }
}
