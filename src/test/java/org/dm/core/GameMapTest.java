package org.dm.core;

import org.dm.entities.Entity;
import org.dm.entities.living_entities.herbivores.Herbivore;
import org.dm.entities.living_entities.herbivores.Hog;
import org.dm.entities.living_entities.herbivores.Rabbit;
import org.dm.entities.static_entities.edible_objects.Grass;
import org.dm.entities.static_entities.inedible_objects.Rock;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class GameMapTest {

    private GameMap map;

    @Before
    public void setUp() {
        map = new GameMap(4, 3);
    }

    @Test
    public void constructorRejectsNonPositiveSize() {
        assertThrows(IllegalArgumentException.class, () -> new GameMap(0, 5));
        assertThrows(IllegalArgumentException.class, () -> new GameMap(5, 0));
        assertThrows(IllegalArgumentException.class, () -> new GameMap(-1, 5));
    }

    @Test
    public void addPlacesEntityOnMap() {
        Entity rock = new Rock();
        Position position = new Position(1, 1);

        assertTrue(map.add(position, rock));

        assertSame(rock, map.getEntity(position));
        assertEquals(position, map.getPosition(rock));
        assertTrue(map.isPositionBusy(position));
    }

    @Test
    public void addToBusyPositionIsRejected() {
        Entity first = new Rock();
        Position position = new Position(1, 1);
        map.add(position, first);

        assertFalse(map.add(position, new Rock()));

        assertSame(first, map.getEntity(position));
    }

    @Test
    public void addOutsideOfMapIsRejected() {
        assertFalse(map.add(new Position(4, 0), new Rock()));
        assertFalse(map.add(new Position(0, 3), new Rock()));
        assertEquals(0, map.count(Entity.class));
    }

    @Test
    public void sameEntityCannotBeAddedTwice() {
        Entity rock = new Rock();
        map.add(new Position(0, 0), rock);

        assertFalse(map.add(new Position(1, 1), rock));

        assertEquals(new Position(0, 0), map.getPosition(rock));
        assertFalse(map.isPositionBusy(new Position(1, 1)));
    }

    @Test
    public void getEntityReturnsNullForEmptyPosition() {
        assertNull(map.getEntity(new Position(2, 2)));
    }

    @Test
    public void removeFreesPosition() {
        Entity rock = new Rock();
        Position position = new Position(2, 1);
        map.add(position, rock);

        map.remove(position);

        assertNull(map.getEntity(position));
        assertNull(map.getPosition(rock));
        assertFalse(map.isPositionBusy(position));
    }

    @Test
    public void removeFromEmptyPositionDoesNothing() {
        map.add(new Position(0, 0), new Rock());

        map.remove(new Position(3, 2));

        assertEquals(1, map.count(Entity.class));
    }

    @Test
    public void moveChangesEntityPosition() {
        Entity rock = new Rock();
        Position from = new Position(0, 0);
        Position to = new Position(3, 2);
        map.add(from, rock);

        map.move(from, to);

        assertFalse(map.isPositionBusy(from));
        assertSame(rock, map.getEntity(to));
        assertEquals(to, map.getPosition(rock));
    }

    @Test
    public void moveToBusyPositionThrowsAndChangesNothing() {
        Entity mover = new Rock();
        Entity blocker = new Rock();
        Position from = new Position(0, 0);
        Position to = new Position(1, 0);
        map.add(from, mover);
        map.add(to, blocker);

        assertThrows(IllegalArgumentException.class, () -> map.move(from, to));

        assertSame(mover, map.getEntity(from));
        assertSame(blocker, map.getEntity(to));
    }

    @Test
    public void moveFromEmptyPositionThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> map.move(new Position(0, 0), new Position(1, 1)));
    }

    @Test
    public void moveOutsideOfMapThrows() {
        Entity rock = new Rock();
        Position from = new Position(0, 0);
        map.add(from, rock);

        assertThrows(IllegalArgumentException.class, () -> map.move(from, new Position(4, 0)));

        assertSame(rock, map.getEntity(from));
    }

    @Test
    public void cornerPositionHasTwoNeighbours() {
        List<Position> neighbours = map.getNeighbours(new Position(0, 0));

        assertEquals(2, neighbours.size());
        assertTrue(neighbours.contains(new Position(1, 0)));
        assertTrue(neighbours.contains(new Position(0, 1)));
    }

    @Test
    public void oppositeCornerHasTwoNeighbours() {
        List<Position> neighbours = map.getNeighbours(new Position(3, 2));

        assertEquals(2, neighbours.size());
        assertTrue(neighbours.contains(new Position(2, 2)));
        assertTrue(neighbours.contains(new Position(3, 1)));
    }

    @Test
    public void innerPositionHasFourNeighbours() {
        List<Position> neighbours = map.getNeighbours(new Position(1, 1));

        assertEquals(4, neighbours.size());
        assertTrue(neighbours.contains(new Position(2, 1)));
        assertTrue(neighbours.contains(new Position(0, 1)));
        assertTrue(neighbours.contains(new Position(1, 2)));
        assertTrue(neighbours.contains(new Position(1, 0)));
    }

    @Test
    public void countIncludesSubclasses() {
        map.add(new Position(0, 0), new Rabbit(1, 10));
        map.add(new Position(1, 0), new Hog(1, 10));
        map.add(new Position(2, 0), new Grass(1));

        assertEquals(2, map.count(Herbivore.class));
        assertEquals(1, map.count(Rabbit.class));
        assertEquals(1, map.count(Grass.class));
        assertEquals(0, map.count(Rock.class));
        assertEquals(3, map.count(Entity.class));
    }

    @Test
    public void getEntitiesOfTypeReturnsOnlyMatchingEntities() {
        Rabbit rabbit = new Rabbit(1, 10);
        map.add(new Position(0, 0), rabbit);
        map.add(new Position(1, 0), new Grass(1));

        List<Rabbit> rabbits = map.getEntitiesOfType(Rabbit.class);

        assertEquals(1, rabbits.size());
        assertSame(rabbit, rabbits.get(0));
    }

    @Test
    public void getEntitiesOfTypeReturnsCopy() {
        map.add(new Position(0, 0), new Rabbit(1, 10));

        map.getEntitiesOfType(Rabbit.class).clear();

        assertEquals(1, map.count(Rabbit.class));
    }

    @Test
    public void randomPositionIsFree() {
        for (int i = 0; i < map.getLengthX() * map.getHeightY(); i++) {
            Position position = map.getRandomPosition();
            assertFalse(map.isPositionBusy(position));
            assertFalse(map.isOutOfBounds(position));
            map.add(position, new Rock());
        }
    }

    @Test
    public void randomPositionFindsTheLastFreeCell() {
        Position free = new Position(2, 1);
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 3; y++) {
                Position position = new Position(x, y);
                if (!position.equals(free)) {
                    map.add(position, new Rock());
                }
            }
        }

        assertEquals(free, map.getRandomPosition());
    }

    @Test
    public void randomPositionThrowsWhenMapIsFull() {
        fillMap();

        assertThrows(IllegalStateException.class, () -> map.getRandomPosition());
    }

    @Test
    public void isAllPositionsAreFilledOnlyWhenEveryCellIsBusy() {
        assertFalse(map.isAllPositionsAreFilled());

        fillMap();

        assertTrue(map.isAllPositionsAreFilled());
    }

    @Test
    public void isOutOfBoundsChecksBothAxes() {
        assertFalse(map.isOutOfBounds(new Position(3, 2)));
        assertTrue(map.isOutOfBounds(new Position(4, 2)));
        assertTrue(map.isOutOfBounds(new Position(3, 3)));
    }

    @Test
    public void sizeGettersReturnConstructorValues() {
        assertEquals(4, map.getLengthX());
        assertEquals(3, map.getHeightY());
    }

    private void fillMap() {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 3; y++) {
                map.add(new Position(x, y), new Rock());
            }
        }
    }
}
