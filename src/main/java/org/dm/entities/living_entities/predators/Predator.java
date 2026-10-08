package org.dm.entities.living_entities.predators;

import org.dm.core.GameMap;
import org.dm.core.Position;
import org.dm.entities.living_entities.Creature;
import org.dm.entities.living_entities.herbivores.Herbivore;
import org.dm.path_finder.PathFinder;

public abstract class Predator extends Creature {

    private int powerAttack;

    public Predator(int speedPoint, int healthPoint, int powerAttack) {
        super(speedPoint, healthPoint);
        this.powerAttack = powerAttack;
    }

    public void makeMove(GameMap map, PathFinder finder) {
        Position currentPosition = map.getPosition(this);
        Position targetPosition = findTarget(map, entity -> entity instanceof Herbivore);
        if (currentPosition == null){
            return;
        }

        if (sufferHunger(map)){
            return;
        }

        if (targetPosition == null){
            moveRandomly(map);
            return;
        }

        int distance = Math.abs(currentPosition.x() - targetPosition.x()) + Math.abs(currentPosition.y() - targetPosition.y());

        if (distance == 1){
            resetHunger();
            Creature food = (Creature) map.getEntity(targetPosition);
            if (food != null){
                food.setHealthPoint(food.getHealthPoint() - getPowerAttack());
                if (food.getHealthPoint() <= 0){
                    map.remove(targetPosition);
                }
            } return;
        }

        Position nextStep = finder.findNextStep(map,currentPosition,targetPosition);
        if (nextStep != null){
            map.move(currentPosition, nextStep);
        }
    }

    public int getPowerAttack() {
        return powerAttack;
    }
}