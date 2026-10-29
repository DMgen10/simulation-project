package org.dm.entities;

import org.dm.core.GameMap;
import org.dm.core.Position;
import org.dm.entities.living_entities.herbivores.Rabbit;
import org.dm.entities.living_entities.predators.Fox;
import org.dm.entities.living_entities.predators.Wolf;
import org.dm.entities.static_entities.edible_objects.Grass;
import org.dm.path_finder.PathFinder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class PredatorTest {

    private final PathFinder finder = new PathFinder();

    @Test
    public void attackReducesPreyHealthByAttackPower() {
        GameMap map = new GameMap(3, 1);
        Fox fox = new Fox(1, 12, 4);
        Rabbit rabbit = new Rabbit(1, 10);
        map.add(new Position(0, 0), fox);
        map.add(new Position(1, 0), rabbit);

        fox.makeMove(map, finder);

        assertEquals(6, rabbit.getHealthPoint());
        assertSame(rabbit, map.getEntity(new Position(1, 0)));
    }

    @Test
    public void preyDisappearsWhenHealthReachesZero() {
        GameMap map = new GameMap(3, 1);
        Fox fox = new Fox(1, 12, 10);
        Rabbit rabbit = new Rabbit(1, 10);
        map.add(new Position(0, 0), fox);
        map.add(new Position(1, 0), rabbit);

        fox.makeMove(map, finder);

        assertNull(map.getEntity(new Position(1, 0)));
        assertNull(map.getPosition(rabbit));
    }

    @Test
    public void preyDisappearsWhenAttackExceedsItsHealth() {
        GameMap map = new GameMap(3, 1);
        Wolf wolf = new Wolf(1, 12, 50);
        Rabbit rabbit = new Rabbit(1, 10);
        map.add(new Position(0, 0), wolf);
        map.add(new Position(1, 0), rabbit);

        wolf.makeMove(map, finder);

        assertEquals(0, map.count(Rabbit.class));
    }

    @Test
    public void severalAttacksAreNeededToKillStrongPrey() {
        GameMap map = new GameMap(3, 1);
        Fox fox = new Fox(1, 12, 4);
        Rabbit rabbit = new Rabbit(1, 10);
        map.add(new Position(0, 0), fox);
        map.add(new Position(1, 0), rabbit);

        fox.makeMove(map, finder);
        fox.makeMove(map, finder);
        assertNotNull(map.getEntity(new Position(1, 0)));

        fox.makeMove(map, finder);
        assertNull(map.getEntity(new Position(1, 0)));
    }

    @Test
    public void movesOneCellTowardsPreyWhenSpeedIsOne() {
        GameMap map = new GameMap(5, 1);
        Fox fox = new Fox(1, 12, 4);
        map.add(new Position(0, 0), fox);
        map.add(new Position(4, 0), new Rabbit(1, 10));

        fox.makeMove(map, finder);

        assertEquals(new Position(1, 0), map.getPosition(fox));
    }

    @Test
    public void speedDefinesHowManyCellsAreCoveredPerTurn() {
        GameMap map = new GameMap(6, 1);
        Fox fox = new Fox(2, 12, 4);
        map.add(new Position(0, 0), fox);
        map.add(new Position(5, 0), new Rabbit(1, 10));

        fox.makeMove(map, finder);

        assertEquals(new Position(2, 0), map.getPosition(fox));
    }

    @Test
    public void ignoresGrass() {
        GameMap map = new GameMap(3, 1);
        Fox fox = new Fox(1, 12, 4);
        Grass grass = new Grass(1);
        map.add(new Position(0, 0), fox);
        map.add(new Position(1, 0), grass);

        fox.makeMove(map, finder);

        assertSame(grass, map.getEntity(new Position(1, 0)));
    }

    @Test
    public void doesNotAttackOtherPredators() {
        GameMap map = new GameMap(2, 1);
        Fox fox = new Fox(1, 12, 4);
        Wolf wolf = new Wolf(1, 12, 4);
        map.add(new Position(0, 0), fox);
        map.add(new Position(1, 0), wolf);

        fox.makeMove(map, finder);

        assertEquals(12, wolf.getHealthPoint());
    }

    @Test
    public void killingPreyRelievesHunger() {
        GameMap map = new GameMap(2, 1);
        Fox fox = new Fox(1, 3, 10);
        map.add(new Position(0, 0), fox);

        for (int turn = 0; turn < 30; turn++) {
            map.add(new Position(1, 0), new Rabbit(1, 1));
            fox.makeMove(map, finder);
        }

        assertEquals(3, fox.getHealthPoint());
        assertEquals(new Position(0, 0), map.getPosition(fox));
    }

    @Test
    public void attackWithoutKillDoesNotRelieveHunger() {
        GameMap map = new GameMap(2, 1);
        Fox fox = new Fox(1, 3, 1);
        Rabbit rabbit = new Rabbit(1, 1000);
        map.add(new Position(0, 0), fox);
        map.add(new Position(1, 0), rabbit);

        int moves = 0;
        while (map.getPosition(fox) != null && moves < 100) {
            fox.makeMove(map, finder);
            moves++;
        }

        assertNull(map.getPosition(fox));
        assertTrue(moves < 100);
    }

    @Test
    public void starvesWhenThereIsNoPrey() {
        GameMap map = new GameMap(3, 3);
        Fox fox = new Fox(1, 3, 4);
        map.add(new Position(1, 1), fox);

        int moves = 0;
        while (map.getPosition(fox) != null && moves < 100) {
            fox.makeMove(map, finder);
            moves++;
        }

        assertNull(map.getPosition(fox));
        assertTrue("Hunger should not kill immediately", moves > 5);
    }
}
