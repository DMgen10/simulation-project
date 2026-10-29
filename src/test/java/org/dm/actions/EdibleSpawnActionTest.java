package org.dm.actions;

import org.dm.actions.turn_actions.EdibleSpawnAction;
import org.dm.core.GameMap;
import org.dm.core.GameSettings;
import org.dm.core.Position;
import org.dm.entities.Entity;
import org.dm.entities.static_entities.edible_objects.Edible;
import org.dm.entities.static_entities.edible_objects.Grass;
import org.dm.entities.static_entities.edible_objects.Mushroom;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EdibleSpawnActionTest {

    private final GameSettings settings = new GameSettings();
    private final EdibleSpawnAction action = new EdibleSpawnAction();

    @Test
    public void addsFoodWhenThereIsTooLittleOfIt() {
        GameMap map = new GameMap(settings.getLength(), settings.getHeight());

        action.execute(map, settings);

        assertEquals(settings.getEdibleRespawnCount(), map.count(Edible.class));
    }

    @Test
    public void addsFoodWhenAmountIsJustBelowMinimum() {
        GameMap map = new GameMap(settings.getLength(), settings.getHeight());
        for (int x = 0; x < settings.getMinEdibleCount() - 1; x++) {
            map.add(new Position(x, 0), new Grass(1));
        }

        action.execute(map, settings);

        assertEquals(settings.getMinEdibleCount() - 1 + settings.getEdibleRespawnCount(),
                map.count(Edible.class));
    }

    @Test
    public void doesNothingWhenThereIsEnoughFood() {
        GameMap map = new GameMap(settings.getLength(), settings.getHeight());
        for (int x = 0; x < settings.getMinEdibleCount(); x++) {
            map.add(new Position(x, 0), new Mushroom(1));
        }

        action.execute(map, settings);

        assertEquals(settings.getMinEdibleCount(), map.count(Edible.class));
    }

    @Test
    public void newFoodHasConfiguredNutritionalValue() {
        GameMap map = new GameMap(settings.getLength(), settings.getHeight());

        action.execute(map, settings);

        for (Edible edible : map.getEntitiesOfType(Edible.class)) {
            assertEquals(settings.getNutritionalValue(), edible.getNutritional());
        }
    }

    @Test
    public void stopsWhenMapIsFull() {
        GameMap map = new GameMap(2, 2);

        action.execute(map, settings);

        assertTrue(map.isAllPositionsAreFilled());
        assertEquals(4, map.count(Entity.class));
    }
}
