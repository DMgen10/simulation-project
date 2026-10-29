package org.dm.actions.init_actions;

import org.dm.actions.Action;
import org.dm.core.GameMap;
import org.dm.core.GameSettings;
import org.dm.entities.Entity;
import org.dm.entities.living_entities.herbivores.Hog;
import org.dm.entities.living_entities.herbivores.Rabbit;
import org.dm.entities.living_entities.predators.Fox;
import org.dm.entities.living_entities.predators.Wolf;
import org.dm.entities.static_entities.edible_objects.Grass;
import org.dm.entities.static_entities.edible_objects.Mushroom;
import org.dm.entities.static_entities.inedible_objects.Rock;
import org.dm.entities.static_entities.inedible_objects.Tree;
import java.util.function.Supplier;

public class EntitySpawnAction implements Action {

    @Override
    public void execute(GameMap map, GameSettings settings) {
        spawn(map, settings.getTotalFox(), () -> new Fox(settings.getSpeedPredators(), settings.getHealthPredators(), settings.getAttackPredators()));
        spawn(map, settings.getTotalWolf(), () -> new Wolf(settings.getSpeedPredators(), settings.getHealthPredators(), settings.getAttackPredators()));
        spawn(map, settings.getTotalHogs(), () -> new Hog(settings.getSpeedHerbivores(), settings.getHealthHerbivores()));
        spawn(map, settings.getTotalRabbit(), () -> new Rabbit(settings.getSpeedHerbivores(), settings.getHealthHerbivores()));
        spawn(map, settings.getTotalMushrooms(), () -> new Mushroom(settings.getNutritionalValue()));
        spawn(map, settings.getTotalGrass(), () -> new Grass(settings.getNutritionalValue()));
        spawn(map, settings.getTotalRocks(), Rock::new);
        spawn(map, settings.getTotalTrees(), Tree::new);
    }

    private void spawn(GameMap map, int count, Supplier<Entity> factory) {
        for (int i = 0; i < count && !map.isAllPositionsAreFilled(); i++) {
            map.add(map.getRandomPosition(), factory.get());
        }
    }
}
