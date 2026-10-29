package org.dm.entities;

import org.dm.core.GameMap;
import org.dm.core.Position;
import org.dm.entities.living_entities.herbivores.Rabbit;
import org.dm.entities.static_entities.edible_objects.Grass;
import org.dm.entities.static_entities.inedible_objects.Rock;
import org.dm.path_finder.PathFinder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class HerbivoreTest {

    private final PathFinder finder = new PathFinder();

    @Test
    public void eatsAdjacentGrassAndStaysInPlace() {
        GameMap map = new GameMap(3, 1);
        Rabbit rabbit = new Rabbit(1, 10);
        map.add(new Position(0, 0), rabbit);
        map.add(new Position(1, 0), new Grass(1));

        rabbit.makeMove(map, finder);

        assertNull(map.getEntity(new Position(1, 0)));
        assertEquals(new Position(0, 0), map.getPosition(rabbit));
    }

    @Test
    public void eatingRestoresHealthByNutritionalValue() {
        GameMap map = new GameMap(3, 1);
        Rabbit rabbit = new Rabbit(1, 10);
        rabbit.takeDamage(5);
        map.add(new Position(0, 0), rabbit);
        map.add(new Position(1, 0), new Grass(3));

        rabbit.makeMove(map, finder);

        assertEquals(8, rabbit.getHealthPoint());
    }

    @Test
    public void healthNeverExceedsMaximum() {
        GameMap map = new GameMap(3, 1);
        Rabbit rabbit = new Rabbit(1, 10);
        map.add(new Position(0, 0), rabbit);
        map.add(new Position(1, 0), new Grass(5));

        rabbit.makeMove(map, finder);

        assertEquals(10, rabbit.getHealthPoint());
    }

    @Test
    public void movesOneCellTowardsFoodWhenSpeedIsOne() {
        GameMap map = new GameMap(5, 1);
        Rabbit rabbit = new Rabbit(1, 10);
        map.add(new Position(0, 0), rabbit);
        map.add(new Position(4, 0), new Grass(1));

        rabbit.makeMove(map, finder);

        assertEquals(new Position(1, 0), map.getPosition(rabbit));
        assertNotNull(map.getEntity(new Position(4, 0)));
    }

    @Test
    public void speedDefinesHowManyCellsAreCoveredPerTurn() {
        GameMap map = new GameMap(6, 1);
        Rabbit rabbit = new Rabbit(3, 10);
        map.add(new Position(0, 0), rabbit);
        map.add(new Position(5, 0), new Grass(1));

        rabbit.makeMove(map, finder);

        assertEquals(new Position(3, 0), map.getPosition(rabbit));
    }

    @Test
    public void fastCreatureStopsNextToFoodInsteadOfWalkingOntoIt() {
        GameMap map = new GameMap(3, 1);
        Rabbit rabbit = new Rabbit(10, 10);
        map.add(new Position(0, 0), rabbit);
        Grass grass = new Grass(1);
        map.add(new Position(2, 0), grass);

        rabbit.makeMove(map, finder);

        assertEquals(new Position(1, 0), map.getPosition(rabbit));
        assertSame(grass, map.getEntity(new Position(2, 0)));
    }

    @Test
    public void turnIsSpentEitherOnMovingOrOnEating() {
        GameMap map = new GameMap(3, 1);
        Rabbit rabbit = new Rabbit(10, 10);
        map.add(new Position(0, 0), rabbit);
        map.add(new Position(2, 0), new Grass(1));

        rabbit.makeMove(map, finder);
        assertNotNull(map.getEntity(new Position(2, 0)));

        rabbit.makeMove(map, finder);
        assertNull(map.getEntity(new Position(2, 0)));
    }

    @Test
    public void doesNotEatRocks() {
        GameMap map = new GameMap(3, 1);
        Rabbit rabbit = new Rabbit(1, 10);
        map.add(new Position(0, 0), rabbit);
        Rock rock = new Rock();
        map.add(new Position(1, 0), rock);

        rabbit.makeMove(map, finder);

        assertSame(rock, map.getEntity(new Position(1, 0)));
    }

    @Test
    public void walksAroundObstaclesToReachFood() {
        GameMap map = new GameMap(3, 3);
        Rabbit rabbit = new Rabbit(1, 10);
        map.add(new Position(0, 1), rabbit);
        map.add(new Position(1, 0), new Rock());
        map.add(new Position(1, 1), new Rock());
        map.add(new Position(2, 1), new Grass(1));

        rabbit.makeMove(map, finder);

        assertEquals(new Position(0, 2), map.getPosition(rabbit));
    }

    @Test
    public void makesRandomStepWhenThereIsNoFood() {
        GameMap map = new GameMap(3, 3);
        Rabbit rabbit = new Rabbit(1, 10);
        Position start = new Position(1, 1);
        map.add(start, rabbit);

        rabbit.makeMove(map, finder);

        assertEquals(1, start.distanceTo(map.getPosition(rabbit)));
    }

    @Test
    public void staysInPlaceWhenBoxedInAndThereIsNoFood() {
        GameMap map = new GameMap(2, 2);
        Rabbit rabbit = new Rabbit(1, 10);
        map.add(new Position(0, 0), rabbit);
        map.add(new Position(1, 0), new Rock());
        map.add(new Position(0, 1), new Rock());

        rabbit.makeMove(map, finder);

        assertEquals(new Position(0, 0), map.getPosition(rabbit));
    }

    @Test
    public void starvesWhenNothingToEat() {
        GameMap map = new GameMap(3, 3);
        Rabbit rabbit = new Rabbit(1, 3);
        map.add(new Position(1, 1), rabbit);

        int moves = 0;
        while (map.getPosition(rabbit) != null && moves < 100) {
            rabbit.makeMove(map, finder);
            moves++;
        }

        assertNull(map.getPosition(rabbit));
        assertTrue(rabbit.isDead());
        assertTrue("Hunger should not kill immediately", moves > 5);
    }

    @Test
    public void regularEatingPreventsHunger() {
        GameMap map = new GameMap(2, 1);
        Rabbit rabbit = new Rabbit(1, 3);
        map.add(new Position(0, 0), rabbit);

        for (int turn = 0; turn < 30; turn++) {
            map.add(new Position(1, 0), new Grass(0));
            rabbit.makeMove(map, finder);
        }

        assertEquals(3, rabbit.getHealthPoint());
        assertEquals(new Position(0, 0), map.getPosition(rabbit));
    }

    @Test
    public void creatureRemovedFromMapSkipsItsTurn() {
        GameMap map = new GameMap(3, 1);
        Rabbit rabbit = new Rabbit(1, 10);
        map.add(new Position(0, 0), rabbit);
        map.add(new Position(1, 0), new Grass(1));
        map.remove(new Position(0, 0));

        rabbit.makeMove(map, finder);

        assertNotNull(map.getEntity(new Position(1, 0)));
        assertNull(map.getPosition(rabbit));
    }
}