package org.dm.entities.living_entities;

import org.dm.core.GameMap;
import org.dm.core.Position;
import org.dm.entities.Entity;
import org.dm.path_finder.PathFinder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;

public abstract class Creature extends Entity {

    private static final int hungerLimit = 5;
    private static final int hungerDamage = 1;
    private int turnsWithoutFood = 0;
    private int speedPoint;
    private int healthPoint;
    private static final Random random = new Random();
    public Creature(int speedPoint, int healthPoint) {
        this.speedPoint = speedPoint;
        this.healthPoint = healthPoint;
    }

    public abstract void makeMove(GameMap map, PathFinder finder);

    public Position findTarget(GameMap map, Predicate<Entity> condition){
        Position currentPosition = map.getPosition(this);

        if (currentPosition == null){
            return null;
        }
        int minDistance = Integer.MAX_VALUE;
        Position nearest = null;
        for (Map.Entry<Position, Entity> entry : map.getEntities().entrySet()){
            Entity entityTarget = entry.getValue();
            Position positionTarget = entry.getKey();

            if (entityTarget == this){
                continue;
            }

            if (condition.test(entityTarget)){

                int distance = Math.abs(currentPosition.x() - positionTarget.x()) + Math.abs(currentPosition.y() - positionTarget.y());
                if (distance < minDistance){
                    minDistance = distance;
                    nearest = positionTarget;
                }
            }
        }
        return nearest;
    }

    public void moveRandomly(GameMap map){
        Position currentPosition = map.getPosition(this);
        if (currentPosition == null){
            return;
        }

        List<Position> freePositions = new ArrayList<>();
        for (Position neighbour : map.getNeighbours(currentPosition)){
            if (!map.isPositionBusy(neighbour)){
                freePositions.add(neighbour);
            }
        }
        if (freePositions.isEmpty()){
            return;
        }
        Position targetPosition = freePositions.get(random.nextInt(freePositions.size()));
        map.move(currentPosition, targetPosition);
    }

    protected void resetHunger(){
        turnsWithoutFood = 0;
    }

    protected boolean sufferHunger(GameMap map){
        turnsWithoutFood++;
        if (turnsWithoutFood > hungerLimit){
            healthPoint -= hungerDamage;
            if (healthPoint <= 0){
                Position position = map.getPosition(this);
                if (position != null){
                    map.remove(position);
                }

                return true;
            }
        }
        return false;
    }

    public int getHealthPoint() {
        return healthPoint;
    }

    public void setHealthPoint(int healthPoint) {
        this.healthPoint = healthPoint;
    }
}