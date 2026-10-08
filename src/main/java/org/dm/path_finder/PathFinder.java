package org.dm.path_finder;

import org.dm.core.GameMap;
import org.dm.core.Position;
import java.util.*;

public class PathFinder {

    public Position findNextStep(GameMap map, Position from, Position to){

        Queue<Position> queue = new LinkedList<>();
        Set<Position> visited = new HashSet<>();
        Map<Position, Position> parentMap = new HashMap<>();

        queue.add(from);
        visited.add(from);

        boolean found = false;

        while (!queue.isEmpty()){
            Position currentPosition = queue.poll();

            if (currentPosition.equals(to)){
                found = true;
                break;
            }
            List<Position> directions = map.getNeighbours(currentPosition);


            for (Position direction: directions){
                if (!visited.contains(direction)){
                    if (!map.isPositionBusy(direction)|| direction.equals(to)){
                        visited.add(direction);
                        parentMap.put(direction, currentPosition);
                        queue.add(direction);
                    }
                }
            }
        }
        if (!found){
            return null;
        }
        Position step = to;
        while (parentMap.get(step) != null && !parentMap.get(step).equals(from)){
            step = parentMap.get(step);
        }
        return step;
    }
}