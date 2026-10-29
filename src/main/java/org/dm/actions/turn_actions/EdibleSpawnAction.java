package org.dm.actions.turn_actions;

import org.dm.actions.Action;
import org.dm.core.GameMap;
import org.dm.core.GameSettings;
import org.dm.entities.Entity;
import org.dm.entities.static_entities.edible_objects.Edible;
import org.dm.entities.static_entities.edible_objects.Grass;
import org.dm.entities.static_entities.edible_objects.Mushroom;

import java.util.Random;

public class EdibleSpawnAction implements Action {

    private final Random random = new Random();

    @Override
    public void execute(GameMap map, GameSettings settings) {
        if (map.count(Edible.class) >= settings.getMinEdibleCount()) {
            return;
        }
        for (int i = 0; i < settings.getEdibleRespawnCount() && !map.isAllPositionsAreFilled(); i++) {
            spawnEdible(map, settings);
        }
    }

    private void spawnEdible(GameMap map, GameSettings settings) {
        Entity edible = random.nextBoolean() ? new Mushroom(settings.getNutritionalValue()) : new Grass(settings.getNutritionalValue());
        map.add(map.getRandomPosition(), edible);
    }
}
