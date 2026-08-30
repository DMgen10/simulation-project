package org.dm;

import org.dm.entities.Entity;
import java.util.HashMap;
import java.util.Map;

public class GameMap {

    private final Map<Position, Entity> entities;
    private final int lengthX;
    private final int heightY;

    public GameMap(int lengthX, int heightY) {

        if (lengthX <= 0 || heightY <= 0){
            throw new IllegalArgumentException("the value of the height or length cannot be less than or equal to zero");
        }

        this.lengthX = lengthX;
        this.heightY = heightY;
        entities = new HashMap<>(lengthX, heightY);
        // возможно позже стот добавить проверку на сверх-большие значения высоты и длины - нужно подобрать оптимальыне.
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

    public boolean isOutOfBounds(Position position){
        return position.x() > getLengthX() || position.y() > getHeightY();
    }


}
