package org.dm.view;

import org.dm.core.GameMap;
import org.dm.core.Position;
import org.dm.entities.Entity;
import org.dm.entities.living_entities.herbivores.Herbivore;
import org.dm.entities.living_entities.predators.Predator;
import org.dm.entities.static_entities.edible_objects.Edible;

public class ConsoleRenderer implements Renderer {

    private final ViewRepository repository = new ViewRepository();

    @Override
    public void render(GameMap map, int turn) {
        showStatistics(map);
        showMap(map);
        System.out.println("Step: " + turn);
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    private void showStatistics(GameMap map) {
        System.out.println("Predators: " + map.count(Predator.class));
        System.out.println("Herbivores: " + map.count(Herbivore.class));
        System.out.println("Edible plants: " + map.count(Edible.class));
    }

    private void showMap(GameMap map) {
        for (int y = 0; y < map.getHeightY(); y++) {
            for (int x = 0; x < map.getLengthX(); x++) {
                Entity entity = map.getEntity(new Position(x, y));
                if (entity == null) {
                    System.out.print(repository.getEmptySprite());
                } else {
                    System.out.print(repository.getImageForEntity(entity));
                }
            }
            System.out.println();
        }
    }
}
