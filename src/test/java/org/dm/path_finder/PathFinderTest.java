package org.dm.path_finder;

import org.dm.core.GameMap;
import org.dm.core.Position;
import org.dm.entities.Entity;
import org.dm.entities.static_entities.edible_objects.Grass;
import org.dm.entities.static_entities.inedible_objects.Rock;
import org.junit.Test;
import java.util.List;
import java.util.function.Predicate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PathFinderTest {

    private static final Predicate<Entity> IS_GRASS = entity -> entity instanceof Grass;

    private final PathFinder finder = new PathFinder();

    @Test
    public void adjacentTargetGivesPathOfOneStep() {
        GameMap map = new GameMap(5, 5);
        map.add(new Position(1, 0), new Grass(1));

        List<Position> path = finder.findPath(map, new Position(0, 0), IS_GRASS);

        assertEquals(List.of(new Position(1, 0)), path);
    }

    @Test
    public void pathGoesStepByStepToTarget() {
        GameMap map = new GameMap(5, 1);
        map.add(new Position(3, 0), new Grass(1));

        List<Position> path = finder.findPath(map, new Position(0, 0), IS_GRASS);

        assertEquals(List.of(new Position(1, 0), new Position(2, 0), new Position(3, 0)), path);
    }

    @Test
    public void pathDoesNotContainStartPosition() {
        GameMap map = new GameMap(5, 5);
        Position start = new Position(2, 2);
        map.add(new Position(4, 4), new Grass(1));

        List<Position> path = finder.findPath(map, start, IS_GRASS);

        assertFalse(path.contains(start));
    }

    @Test
    public void pathIsTheShortestOne() {
        GameMap map = new GameMap(5, 5);
        map.add(new Position(4, 4), new Grass(1));

        List<Position> path = finder.findPath(map, new Position(0, 0), IS_GRASS);

        assertEquals(8, path.size());
        assertEquals(new Position(4, 4), path.get(path.size() - 1));
    }

    @Test
    public void consecutiveStepsAreAlwaysNeighbouringCells() {
        GameMap map = new GameMap(6, 6);
        map.add(new Position(5, 5), new Grass(1));
        map.add(new Position(2, 2), new Rock());
        map.add(new Position(3, 2), new Rock());
        Position previous = new Position(0, 0);

        for (Position step : finder.findPath(map, previous, IS_GRASS)) {
            assertEquals(1, previous.distanceTo(step));
            previous = step;
        }
    }

    @Test
    public void pathGoesAroundObstacles() {
        GameMap map = new GameMap(3, 3);
        map.add(new Position(1, 0), new Rock());
        map.add(new Position(1, 1), new Rock());
        map.add(new Position(2, 1), new Grass(1));

        List<Position> path = finder.findPath(map, new Position(0, 1), IS_GRASS);

        assertEquals(4, path.size());
        assertFalse(path.contains(new Position(1, 0)));
        assertFalse(path.contains(new Position(1, 1)));
        assertEquals(new Position(2, 1), path.get(path.size() - 1));
    }

    @Test
    public void emptyPathWhenTargetIsWalledOff() {
        GameMap map = new GameMap(3, 3);
        map.add(new Position(2, 2), new Grass(1));
        map.add(new Position(1, 2), new Rock());
        map.add(new Position(2, 1), new Rock());

        assertTrue(finder.findPath(map, new Position(0, 0), IS_GRASS).isEmpty());
    }

    @Test
    public void emptyPathWhenThereAreNoTargets() {
        GameMap map = new GameMap(4, 4);
        map.add(new Position(1, 1), new Rock());

        assertTrue(finder.findPath(map, new Position(0, 0), IS_GRASS).isEmpty());
    }

    @Test
    public void nearestTargetIsChosen() {
        GameMap map = new GameMap(6, 1);
        map.add(new Position(0, 0), new Grass(1));
        map.add(new Position(5, 0), new Grass(1));

        List<Position> path = finder.findPath(map, new Position(2, 0), IS_GRASS);

        assertEquals(List.of(new Position(1, 0), new Position(0, 0)), path);
    }

    @Test
    public void nearestReachableTargetIsChosenWhenNearerOneIsWalledOff() {
        GameMap map = new GameMap(7, 3);
        map.add(new Position(2, 0), new Grass(1));
        map.add(new Position(1, 0), new Rock());
        map.add(new Position(3, 0), new Rock());
        map.add(new Position(2, 1), new Rock());
        map.add(new Position(6, 2), new Grass(1));

        List<Position> path = finder.findPath(map, new Position(0, 2), IS_GRASS);

        assertEquals(6, path.size());
        assertEquals(new Position(6, 2), path.get(path.size() - 1));
    }

    @Test
    public void entitiesThatAreNotTargetsBlockTheWay() {
        GameMap map = new GameMap(3, 1);
        map.add(new Position(1, 0), new Rock());
        map.add(new Position(2, 0), new Grass(1));

        assertTrue(finder.findPath(map, new Position(0, 0), IS_GRASS).isEmpty());
    }

    @Test
    public void targetConditionDecidesWhatIsTarget() {
        GameMap map = new GameMap(4, 1);
        map.add(new Position(3, 0), new Rock());

        List<Position> path = finder.findPath(map, new Position(0, 0), entity -> entity instanceof Rock);

        assertEquals(3, path.size());
    }
}
