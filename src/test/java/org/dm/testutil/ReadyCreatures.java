package org.dm.testutil;

import org.dm.core.GameMap;
import org.dm.core.Position;
import org.dm.entities.Entity;
import org.dm.entities.living_entities.Creature;
import org.dm.entities.living_entities.herbivores.Rabbit;
import org.dm.entities.living_entities.predators.Fox;
import org.dm.entities.static_entities.edible_objects.Grass;
import org.dm.path_finder.PathFinder;
import java.util.function.Supplier;

public final class ReadyCreatures {

    private static final int GIVE_UP_AFTER_TURNS = 200;

    private ReadyCreatures() {
    }

    public static Rabbit readyRabbit(int health) {
        Rabbit rabbit = new Rabbit(1, health);
        feedUntilReady(rabbit, () -> new Grass(0));
        return rabbit;
    }

    public static Fox readyFox(int health) {
        Fox fox = new Fox(1, health, 50);
        feedUntilReady(fox, () -> new Rabbit(1, 1));
        return fox;
    }

    public static int turnsUntilReady(Creature creature, Supplier<Entity> food) {
        return feedUntilReady(creature, food);
    }

    private static int feedUntilReady(Creature creature, Supplier<Entity> food) {
        GameMap map = new GameMap(2, 1);
        PathFinder finder = new PathFinder();
        map.add(new Position(0, 0), creature);
        int turns = 0;
        while (!creature.canReproduce()) {
            if (turns >= GIVE_UP_AFTER_TURNS) {
                throw new AssertionError("Creature did not become ready to reproduce in " + turns + " turns");
            }
            map.add(new Position(1, 0), food.get());
            creature.makeMove(map, finder);
            turns++;
        }
        map.remove(new Position(0, 0));
        return turns;
    }
}
