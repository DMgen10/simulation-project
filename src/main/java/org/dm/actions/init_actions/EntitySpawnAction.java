package org.dm.actions.init_actions;

import org.dm.core.GameMap;
import org.dm.core.GameSettings;
import org.dm.core.Position;
import org.dm.actions.Action;
import org.dm.entities.Entity;
import org.dm.entities.living_entities.herbivores.Hog;
import org.dm.entities.living_entities.herbivores.Rabbit;
import org.dm.entities.living_entities.predators.Fox;
import org.dm.entities.living_entities.predators.Wolf;
import org.dm.entities.static_entities.edible_objects.Grass;
import org.dm.entities.static_entities.edible_objects.Mushroom;
import org.dm.entities.static_entities.inedible_objects.Rock;
import org.dm.entities.static_entities.inedible_objects.Tree;
import org.dm.view.EntityType;

public class EntitySpawnAction implements Action {

    @Override
    public void execute(GameMap map, GameSettings settings) {
        spawnAllEntities(map, EntityType.FOX, settings.getTotalFox(), settings);
        spawnAllEntities(map, EntityType.WOLF, settings.getTotalWolf(), settings);
        spawnAllEntities(map, EntityType.HOG, settings.getTotalHogs(), settings);
        spawnAllEntities(map, EntityType.RABBIT, settings.getTotalRabbit(), settings);
        spawnAllEntities(map, EntityType.MUSHROOM, settings.getTotalMushrooms(), settings);
        spawnAllEntities(map, EntityType.GRASS, settings.getTotalGrass(), settings);
        spawnAllEntities(map, EntityType.ROCK, settings.getTotalRocks(), settings);
        spawnAllEntities(map, EntityType.TREE, settings.getTotalTrees(), settings);
        }

    private Entity getEntity(EntityType type, GameSettings settings){
        Entity entity = null;
        switch (type){
            case FOX -> entity = new Fox(settings.getSpeedPredators(), settings.getHealthPredators(), settings.getAttackPredators());
            case WOLF -> entity = new Wolf(settings.getSpeedPredators(), settings.getHealthPredators(), settings.getAttackPredators());
            case HOG -> entity = new Hog(settings.getSpeedHerbivores(), settings.getHealthHerbivores());
            case RABBIT -> entity = new Rabbit(settings.getSpeedHerbivores(), settings.getHealthHerbivores());
            case MUSHROOM -> entity = new Mushroom(settings.getEdibility());
            case GRASS -> entity = new Grass(settings.getEdibility());
            case ROCK -> entity = new Rock();
            case TREE -> entity = new Tree();
        }
        return entity;
    }

    private void spawnAllEntities(GameMap map, EntityType type, int countEntities, GameSettings settings){
        int attempts = map.getHeightY() * map.getLengthX();

        while (countEntities > 0 && attempts > 0){
            spawnEntity(map, type, settings);
            countEntities--;
            attempts--;
        }
    }

    private void spawnEntity(GameMap map, EntityType type, GameSettings settings){
        while (true){
            Position position = map.getRandomPosition();
            if (!map.isPositionBusy(position)){
                map.add(position, getEntity(type, settings));
                break;
                }
            }
    }
}