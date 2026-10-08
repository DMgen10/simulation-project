package org.dm.core;

import org.dm.entities.Entity;

import java.util.*;

public class GameMap {

    private final Random random = new Random();
    private final Map<Position, Entity> entities;
    private final int lengthX;
    private final int heightY;
    private final Map<Entity, Position> entityPositions = new HashMap<>();

    public GameMap(int lengthX, int heightY) {
        if (lengthX <= 0 || heightY <= 0){
            throw new IllegalArgumentException("The value of the height or length cannot be less than or equal to zero");
        }
        this.lengthX = lengthX;
        this.heightY = heightY;
        entities = new HashMap<>(lengthX * heightY);
    }

    public void add(Position position, Entity entity){
        if (!isPositionBusy(position)){
            entities.put(position, entity);
            entityPositions.put(entity, position);
        }
    }

    public Map<Position, Entity> getEntities() {
        return entities;
    }

    public int getLengthX() {
        return lengthX;
    }

    public int getHeightY() {
        return heightY;
    }

    public Entity getEntity(Position position){
        return entities.get(position);
    }

    public Position getRandomPosition(){

        while(true){
            int x = random.nextInt(getLengthX());
            int y = random.nextInt(getHeightY());
            Position position = new Position(x,y);
            if (!isPositionBusy(position) && !isOutOfBounds(position)){
                return position;
            }
        }
    }

    public void move(Position from, Position to){
        Entity entity = entities.remove(from);
        if (entity != null){
            entities.put(to, entity);
            entityPositions.put(entity, to);
        }
    }

    public void remove(Position position){
        Entity entity = entities.get(position);
        if (entity != null) {
            entities.remove(position);
            entityPositions.remove(entity);
        }
    }

    public List<Position> getNeighbours(Position position){
        List<Position> neighbours = new ArrayList<>(4);
        if (position.x() + 1 < lengthX){
            neighbours.add(new Position(position.x() + 1, position.y() ));
        }
        if (position.x() - 1 >= 0){
            neighbours.add(new Position(position.x() - 1,position.y()));
        }
        if (position.y() + 1 < heightY){
            neighbours.add(new Position(position.x(), position.y() + 1));
        }
        if (position.y() - 1 >= 0){
            neighbours.add(new Position(position.x(), position.y() - 1));
        }
        return neighbours;
    }

    public Position getPosition(Entity entity){
        return entityPositions.get(entity);
    }

    public boolean isPositionBusy(Position position){
        return entities.containsKey(position);
    }

    public boolean isOutOfBounds(Position position){
        return position.x() >= getLengthX() || position.y() >= getHeightY() || position.x() < 0 || position.y() < 0;
    }

    public boolean isAllPositionsAreFilled(){
        return entities.size() >= lengthX * heightY;
    }
}