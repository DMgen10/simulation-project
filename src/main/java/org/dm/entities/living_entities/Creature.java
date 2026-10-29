package org.dm.entities.living_entities;

import org.dm.core.GameMap;
import org.dm.core.Position;
import org.dm.entities.Entity;
import org.dm.path_finder.PathFinder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Creature extends Entity {

    private static final int HUNGER_LIMIT = 5;
    private static final int HUNGER_DAMAGE = 1;
    private static final int WELL_FED_LIMIT = 3;
    private static final int MIN_HEALTH_PERCENT_FOR_REPRODUCTION = 70;
    private static final int REPRODUCTION_COST_DIVISOR = 3;
    private static final Random RANDOM = new Random();

    private final int speedPoint;
    private final int maxHealthPoint;
    private int healthPoint;
    private int turnsWithoutFood = 0;
    private int turnsSinceReproduction = 0;

    protected Creature(int speedPoint, int healthPoint) {
        this.speedPoint = speedPoint;
        this.maxHealthPoint = healthPoint;
        this.healthPoint = healthPoint;
    }

    protected abstract boolean isTarget(Entity entity);

    protected abstract void interact(GameMap map, Position targetPosition);

    protected abstract Creature createOffspring();

    protected abstract int getReproductionCooldown();

    public final void makeMove(GameMap map, PathFinder finder) {
        Position start = map.getPosition(this);
        if (start == null) {
            return;
        }
        turnsSinceReproduction++;
        if (sufferHunger(map)) {
            return;
        }

        List<Position> path = finder.findPath(map, start, this::isTarget);
        if (path.isEmpty()) {
            moveRandomly(map, start);
        } else if (path.size() == 1) {
            interact(map, path.get(0));
        } else {
            walk(map, start, path);
        }
    }

    public boolean canReproduce() {
        return turnsSinceReproduction >= getReproductionCooldown()
                && turnsWithoutFood <= WELL_FED_LIMIT
                && healthPoint * 100 >= maxHealthPoint * MIN_HEALTH_PERCENT_FOR_REPRODUCTION
                && healthPoint > reproductionCost();
    }

    public Creature reproduce() {
        if (!canReproduce()) {
            throw new IllegalStateException("The creature cannot reproduce now");
        }
        takeDamage(reproductionCost());
        turnsSinceReproduction = 0;
        return createOffspring();
    }

    private int reproductionCost() {
        return Math.max(1, maxHealthPoint / REPRODUCTION_COST_DIVISOR);
    }

    private void walk(GameMap map, Position start, List<Position> path) {
        int steps = Math.min(speedPoint, path.size() - 1);
        Position current = start;
        for (int i = 0; i < steps; i++) {
            map.move(current, path.get(i));
            current = path.get(i);
        }
    }

    private void moveRandomly(GameMap map, Position start) {
        List<Position> freePositions = new ArrayList<>();
        for (Position neighbour : map.getNeighbours(start)) {
            if (!map.isPositionBusy(neighbour)) {
                freePositions.add(neighbour);
            }
        }
        if (!freePositions.isEmpty()) {
            map.move(start, freePositions.get(RANDOM.nextInt(freePositions.size())));
        }
    }

    private boolean sufferHunger(GameMap map) {
        turnsWithoutFood++;
        if (turnsWithoutFood > HUNGER_LIMIT) {
            takeDamage(HUNGER_DAMAGE);
            if (isDead()) {
                map.remove(map.getPosition(this));
                return true;
            }
        }
        return false;
    }

    protected void resetHunger() {
        turnsWithoutFood = 0;
    }

    public void takeDamage(int damage) {
        healthPoint -= damage;
    }

    public void heal(int amount) {
        healthPoint = Math.min(maxHealthPoint, healthPoint + amount);
    }

    public boolean isDead() {
        return healthPoint <= 0;
    }

    public int getSpeedPoint() {
        return speedPoint;
    }

    public int getHealthPoint() {
        return healthPoint;
    }

    public int getMaxHealthPoint() {
        return maxHealthPoint;
    }
}
