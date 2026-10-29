package org.dm.actions.turn_actions;

import org.dm.actions.Action;
import org.dm.core.GameMap;
import org.dm.core.GameSettings;
import org.dm.core.Position;
import org.dm.entities.living_entities.Creature;
import org.dm.entities.living_entities.herbivores.Herbivore;
import org.dm.entities.living_entities.predators.Predator;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReproductionAction implements Action {

    private final Random random = new Random();

    @Override
    public void execute(GameMap map, GameSettings settings) {
        for (Creature parent : map.getEntitiesOfType(Creature.class)) {
            if (isPopulationFull(map, parent, settings) || !parent.canReproduce()) {
                continue;
            }
            Position birthPlace = pickFreeNeighbour(map, map.getPosition(parent));
            if (birthPlace != null) {
                map.add(birthPlace, parent.reproduce());
            }
        }
    }

    private boolean isPopulationFull(GameMap map, Creature parent, GameSettings settings) {
        if (parent instanceof Herbivore) {
            return map.count(Herbivore.class) >= settings.getMaxHerbivores();
        }
        if (parent instanceof Predator) {
            return map.count(Predator.class) >= settings.getMaxPredators();
        }
        return true;
    }

    private Position pickFreeNeighbour(GameMap map, Position position) {
        List<Position> free = new ArrayList<>();
        for (Position neighbour : map.getNeighbours(position)) {
            if (!map.isPositionBusy(neighbour)) {
                free.add(neighbour);
            }
        }
        return free.isEmpty() ? null : free.get(random.nextInt(free.size()));
    }
}
