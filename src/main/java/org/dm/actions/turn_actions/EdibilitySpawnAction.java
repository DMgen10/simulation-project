package org.dm.actions.turn_actions;

import org.dm.core.GameMap;
import org.dm.core.GameSettings;
import org.dm.core.Position;
import org.dm.actions.Action;
import org.dm.entities.Entity;
import org.dm.entities.static_entities.edible_objects.Edible;
import org.dm.entities.static_entities.edible_objects.Grass;
import org.dm.entities.static_entities.edible_objects.Mushroom;
import java.util.Random;

public class EdibilitySpawnAction implements Action {

    private Random random = new Random();

    @Override
    public void execute(GameMap map, GameSettings settings) {
        if (isShortageOfEdibleInanimate(map) && !map.isAllPositionsAreFilled()) {
            int count = settings.getTotalEdibilityEntity();
            while (count > 0){
                spawnEdible(map, settings);
                count--;
                if (map.isAllPositionsAreFilled()){
                    break;
                }
            }
        }
    }

    private void spawnEdible(GameMap map, GameSettings settings) {

        int choice = random.nextInt(2);
        while (true) {
            Position position = map.getRandomPosition();
            if (!map.isPositionBusy(position)) {
                if (choice == 1){
                    map.add(position, new Mushroom(settings.getEdibility()));
                } else {
                    map.add(position, new Grass(settings.getEdibility()));
                }
                break;
            }
        }
    }

    private boolean isShortageOfEdibleInanimate(GameMap map) {

        int countEdible = 0;

        for (int x = 0; x < map.getLengthX(); x++) {
            for (int y = 0; y < map.getHeightY(); y++) {
                Position position = new Position(x, y);
                Entity entity = map.getEntity(position);
                if (entity instanceof Edible) {
                    countEdible++;
                }
            }
        }
        return countEdible < 5;
    }
}