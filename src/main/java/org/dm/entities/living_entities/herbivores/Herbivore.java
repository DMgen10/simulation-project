package org.dm.entities.living_entities.herbivores;

import org.dm.core.GameMap;
import org.dm.core.Position;
import org.dm.entities.Entity;
import org.dm.entities.living_entities.Creature;
import org.dm.entities.static_entities.edible_objects.Edible;
import org.dm.entities.static_entities.edible_objects.Grass;
import org.dm.entities.static_entities.edible_objects.Mushroom;
import org.dm.path_finder.PathFinder;

public abstract class Herbivore extends Creature {

    public Herbivore(int speedPoint, int healthPoint) {
        super(speedPoint, healthPoint);
    }

    /**
     * Стремятся найти ресурс (траву), может потратить свой ход на движение в сторону травы, либо на её поглощение
     */

    @Override
    public void makeMove(GameMap map, PathFinder finder) {
        Position currentPosition = map.getPosition(this);
        if (currentPosition == null){
            return;
        }
        if (sufferHunger(map)){
            return;
        }
        Position targetPosition = findTarget(map, entity -> entity instanceof Grass || entity instanceof Mushroom);
        if (targetPosition == null){
            moveRandomly(map);
            return;
        }

        int distance = Math.abs(currentPosition.x() - targetPosition.x()) + Math.abs(currentPosition.y() - targetPosition.y());

        if (distance == 1){
            resetHunger();
            Entity food = map.getEntity(targetPosition);
            map.remove(targetPosition);
            if (food instanceof Edible){
                setHealthPoint(getHealthPoint() + ((Edible) food).getNutritional());
            }
            return;
        }

        Position nextStep = finder.findNextStep(map,currentPosition,targetPosition);
        if (nextStep != null){
            map.move(currentPosition, nextStep);
        }
    }
}