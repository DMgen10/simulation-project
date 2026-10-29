package org.dm.actions;

import org.dm.actions.turn_actions.MoveCreaturesAction;
import org.dm.core.GameMap;
import org.dm.core.GameSettings;
import org.dm.core.Position;
import org.dm.entities.Entity;
import org.dm.entities.living_entities.herbivores.Rabbit;
import org.dm.entities.living_entities.predators.Fox;
import org.dm.entities.static_entities.edible_objects.Grass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

public class MoveCreaturesActionTest {

    private final GameSettings settings = new GameSettings();
    private final MoveCreaturesAction action = new MoveCreaturesAction();

    @Test
    public void everyCreatureMakesItsMove() {
        GameMap map = new GameMap(9, 9);
        Rabbit first = new Rabbit(1, 10);
        Rabbit second = new Rabbit(1, 10);
        Position firstStart = new Position(1, 1);
        Position secondStart = new Position(7, 7);
        map.add(firstStart, first);
        map.add(secondStart, second);

        action.execute(map, settings);

        assertNotEquals(firstStart, map.getPosition(first));
        assertNotEquals(secondStart, map.getPosition(second));
    }

    @Test
    public void creatureEatenDuringTheRoundDoesNotBreakIt() {
        GameMap map = new GameMap(2, 1);
        Fox fox = new Fox(1, 12, 100);
        Rabbit rabbit = new Rabbit(1, 10);
        map.add(new Position(0, 0), fox);
        map.add(new Position(1, 0), rabbit);

        action.execute(map, settings);

        assertEquals(0, map.count(Rabbit.class));
        assertNull(map.getPosition(rabbit));
        assertEquals(new Position(0, 0), map.getPosition(fox));
    }

    @Test
    public void herbivoreEatsFoodDuringTheRound() {
        GameMap map = new GameMap(2, 1);
        map.add(new Position(0, 0), new Rabbit(1, 10));
        map.add(new Position(1, 0), new Grass(1));

        action.execute(map, settings);

        assertEquals(0, map.count(Grass.class));
    }

    @Test
    public void worksOnEmptyMap() {
        GameMap map = new GameMap(3, 3);

        action.execute(map, settings);

        assertEquals(0, map.count(Entity.class));
    }
}
