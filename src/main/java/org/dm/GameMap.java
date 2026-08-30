package org.dm;

import org.dm.entities.Entity;
import java.util.Map;

public class GameMap {

    private Map<Position, Entity> entities;
    private int lengthX;
    private int heightY;

    public GameMap(int lengthX, int heightY) {

        if (lengthX <= 0 || heightY <= 0){
            throw new IllegalArgumentException("the value of the height or length cannot be less than or equal to zero");
        }

        this.lengthX = lengthX;
        this.heightY = heightY;
    }

    public void add(Position position, Entity entity){
        entities.put(position, entity);
    }

    public int getLengthX() {
        return lengthX;
    }

    public int getHeightY() {
        return heightY;
    }

    public void remove(Position position){
        entities.remove(position);
    }

    public boolean isPositionIsBusy(Position position){
        return entities.containsKey(position);
    }
}
