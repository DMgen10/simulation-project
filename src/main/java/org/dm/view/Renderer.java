package org.dm.view;

import org.dm.core.GameMap;
import org.dm.core.Position;
import org.dm.entities.Entity;

public class Renderer {

    private final GameMap map;
    private final ViewRepository repository;

    public Renderer(GameMap map) {
        this.map = map;
        this.repository = new ViewRepository();
    }

    public void showMap(){
        for (int x = 0; x < map.getLengthX(); x++) {
            for (int y = 0; y < map.getHeightY(); y++) {
                Position position = new Position(x,y);
                Entity entity = map.getEntity(position);
                if (map.isPositionBusy(position)){
                    System.out.print(repository.getImageForEntity(entity));
                } else {
                    System.out.print(repository.getEmptySprite());
                }
            }
            System.out.println();
        }
    }
}