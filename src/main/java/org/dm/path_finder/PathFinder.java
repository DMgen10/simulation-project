package org.dm.path_finder;

import org.dm.core.GameMap;
import org.dm.core.Position;
import org.dm.entities.Entity;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.Predicate;

public class PathFinder {

      public List<Position> findPath(GameMap map, Position from, Predicate<Entity> isTarget) {
        Queue<Position> queue = new ArrayDeque<>();
        Set<Position> visited = new HashSet<>();
        Map<Position, Position> parents = new HashMap<>();

        queue.add(from);
        visited.add(from);

        while (!queue.isEmpty()) {
            Position current = queue.poll();

            for (Position neighbour : map.getNeighbours(current)) {
                if (!visited.add(neighbour)) {
                    continue;
                }
                parents.put(neighbour, current);

                Entity entity = map.getEntity(neighbour);
                if (entity == null) {
                    queue.add(neighbour);
                } else if (isTarget.test(entity)) {
                    return buildPath(parents, from, neighbour);
                }
            }
        }
        return List.of();
    }

    private List<Position> buildPath(Map<Position, Position> parents, Position from, Position target) {
        List<Position> path = new ArrayList<>();
        for (Position step = target; !step.equals(from); step = parents.get(step)) {
            path.add(step);
        }
        Collections.reverse(path);
        return path;
    }
}
