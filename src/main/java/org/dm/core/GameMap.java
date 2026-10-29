package org.dm.core;

import org.dm.entities.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameMap {

    private final Random random = new Random();
    private final Map<Position, Entity> entities;
    private final Map<Entity, Position> entityPositions = new HashMap<>();
    private final int lengthX;
    private final int heightY;

    public GameMap(int lengthX, int heightY) {
        if (lengthX <= 0 || heightY <= 0) {
            throw new IllegalArgumentException("Map size must be positive: " + lengthX + "x" + heightY);
        }
        this.lengthX = lengthX;
        this.heightY = heightY;
        entities = new HashMap<>(lengthX * heightY);
    }

    public boolean add(Position position, Entity entity) {
        if (isOutOfBounds(position) || isPositionBusy(position) || entityPositions.containsKey(entity)) {
            return false;
        }
        entities.put(position, entity);
        entityPositions.put(entity, position);
        return true;
    }

    public void remove(Position position) {
        Entity entity = entities.remove(position);
        if (entity != null) {
            entityPositions.remove(entity);
        }
    }

    public void move(Position from, Position to) {
        Entity entity = entities.get(from);
        if (entity == null) {
            throw new IllegalArgumentException("There is no entity at " + from);
        }
        if (isOutOfBounds(to) || isPositionBusy(to)) {
            throw new IllegalArgumentException("Cannot move to " + to);
        }
        entities.remove(from);
        entities.put(to, entity);
        entityPositions.put(entity, to);
    }

    public Entity getEntity(Position position) {
        return entities.get(position);
    }

    public Position getPosition(Entity entity) {
        return entityPositions.get(entity);
    }

    public <T extends Entity> List<T> getEntitiesOfType(Class<T> type) {
        List<T> result = new ArrayList<>();
        for (Entity entity : entities.values()) {
            if (type.isInstance(entity)) {
                result.add(type.cast(entity));
            }
        }
        return result;
    }

    public long count(Class<? extends Entity> type) {
        return entities.values().stream().filter(type::isInstance).count();
    }

    public Position getRandomPosition() {
        if (isAllPositionsAreFilled()) {
            throw new IllegalStateException("There are no free positions on the map");
        }
        while (true) {
            Position position = new Position(random.nextInt(lengthX), random.nextInt(heightY));
            if (!isPositionBusy(position)) {
                return position;
            }
        }
    }

    public List<Position> getNeighbours(Position position) {
        List<Position> neighbours = new ArrayList<>(4);
        if (position.x() + 1 < lengthX) {
            neighbours.add(new Position(position.x() + 1, position.y()));
        }
        if (position.x() - 1 >= 0) {
            neighbours.add(new Position(position.x() - 1, position.y()));
        }
        if (position.y() + 1 < heightY) {
            neighbours.add(new Position(position.x(), position.y() + 1));
        }
        if (position.y() - 1 >= 0) {
            neighbours.add(new Position(position.x(), position.y() - 1));
        }
        return neighbours;
    }

    public boolean isPositionBusy(Position position) {
        return entities.containsKey(position);
    }

    public boolean isOutOfBounds(Position position) {
        return position.x() >= lengthX || position.y() >= heightY;
    }

    public boolean isAllPositionsAreFilled() {
        return entities.size() >= lengthX * heightY;
    }

    public int getLengthX() {
        return lengthX;
    }

    public int getHeightY() {
        return heightY;
    }
}
