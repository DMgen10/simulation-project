package org.dm.actions.turn_actions;

import org.dm.actions.Action;
import org.dm.core.GameMap;
import org.dm.core.GameSettings;
import org.dm.entities.living_entities.Creature;
import org.dm.path_finder.PathFinder;

public class MoveCreaturesAction implements Action {

    private final PathFinder finder = new PathFinder();

    @Override
    public void execute(GameMap map, GameSettings settings) {
        for (Creature creature : map.getEntitiesOfType(Creature.class)) {
            creature.makeMove(map, finder);
        }
    }
}
