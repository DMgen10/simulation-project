package org.dm.actions;

import org.dm.actions.init_actions.EntitySpawnAction;
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
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EntitySpawnActionTest {

    private final GameSettings settings = new GameSettings();
    private final EntitySpawnAction action = new EntitySpawnAction();

    @Test
    public void placesEveryKindOfEntityInConfiguredAmount() {
        GameMap map = new GameMap(settings.getLength(), settings.getHeight());

        action.execute(map, settings);

        assertEquals(settings.getTotalFox(), map.count(Fox.class));
        assertEquals(settings.getTotalWolf(), map.count(Wolf.class));
        assertEquals(settings.getTotalHogs(), map.count(Hog.class));
        assertEquals(settings.getTotalRabbit(), map.count(Rabbit.class));
        assertEquals(settings.getTotalGrass(), map.count(Grass.class));
        assertEquals(settings.getTotalMushrooms(), map.count(Mushroom.class));
        assertEquals(settings.getTotalRocks(), map.count(Rock.class));
        assertEquals(settings.getTotalTrees(), map.count(Tree.class));
    }

    @Test
    public void everyEntityOccupiesItsOwnCell() {
        GameMap map = new GameMap(settings.getLength(), settings.getHeight());
        int expected = settings.getTotalFox() + settings.getTotalWolf()
                + settings.getTotalHogs() + settings.getTotalRabbit()
                + settings.getTotalGrass() + settings.getTotalMushrooms()
                + settings.getTotalRocks() + settings.getTotalTrees();

        action.execute(map, settings);

        assertEquals(expected, map.count(Entity.class));
    }

    @Test
    public void createdCreaturesUseValuesFromSettings() {
        GameMap map = new GameMap(settings.getLength(), settings.getHeight());

        action.execute(map, settings);

        Fox fox = map.getEntitiesOfType(Fox.class).get(0);
        Rabbit rabbit = map.getEntitiesOfType(Rabbit.class).get(0);
        assertEquals(settings.getHealthPredators(), fox.getHealthPoint());
        assertEquals(settings.getSpeedPredators(), fox.getSpeedPoint());
        assertEquals(settings.getAttackPredators(), fox.getPowerAttack());
        assertEquals(settings.getHealthHerbivores(), rabbit.getHealthPoint());
        assertEquals(settings.getSpeedHerbivores(), rabbit.getSpeedPoint());
    }

    @Test
    public void doesNotFailAndFillsMapWhenItIsTooSmall() {
        GameMap map = new GameMap(2, 2);

        action.execute(map, settings);

        assertTrue(map.isAllPositionsAreFilled());
        assertEquals(4, map.count(Entity.class));
    }
}
