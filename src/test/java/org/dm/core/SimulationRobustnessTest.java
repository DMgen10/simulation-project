package org.dm.core;

import org.dm.entities.Entity;
import org.dm.entities.living_entities.Creature;
import org.dm.entities.living_entities.herbivores.Herbivore;
import org.dm.entities.living_entities.predators.Predator;
import org.dm.view.Renderer;
import org.junit.Test;
import java.util.Random;
import static org.junit.Assert.assertTrue;

public class SimulationRobustnessTest {

    @Test
    public void randomValidSettingsNeverBreakTheSimulation() {
        Random random = new Random(20260101);
        for (int game = 0; game < 25; game++) {
            GameSettings settings = GameSettings.builder()
                    .length(between(random, 5, 12)).height(between(random, 5, 12))
                    .healthHerbivores(between(random, 1, 100)).healthPredators(between(random, 1, 100))
                    .speedHerbivores(between(random, 1, 5)).speedPredators(between(random, 1, 5))
                    .attackPredators(between(random, 1, 50)).nutritionalValue(between(random, 1, 20))
                    .build();

            play(settings, 120);
        }
    }

    @Test
    public void smallestPossibleWorldWithWeakestCreaturesWorks() {
        GameSettings settings = GameSettings.builder()
                .length(5).height(5)
                .healthHerbivores(1).healthPredators(1)
                .speedHerbivores(1).speedPredators(1)
                .attackPredators(1).nutritionalValue(1)
                .build();

        play(settings, 100);
    }

    @Test
    public void biggestPossibleWorldWithStrongestCreaturesWorks() {
        GameSettings settings = GameSettings.builder()
                .length(20).height(20)
                .healthHerbivores(100).healthPredators(100)
                .speedHerbivores(5).speedPredators(5)
                .attackPredators(50).nutritionalValue(20)
                .build();

        play(settings, 25);
    }

    @Test
    public void longGameOnTheDefaultWorldKeepsAllInvariants() {
        play(new GameSettings(), 300);
    }

    private void play(GameSettings settings, int maxTurns) {
        InvariantChecker checker = new InvariantChecker(settings);
        Simulation simulation = new Simulation(checker, settings);
        simulation.preparation();
        for (int turn = 0; turn < maxTurns && !simulation.isOver(); turn++) {
            simulation.nextTurn();
        }
    }

    private int between(Random random, int min, int max) {
        return min + random.nextInt(max - min + 1);
    }

    private static class InvariantChecker implements Renderer {

        private final GameSettings settings;

        InvariantChecker(GameSettings settings) {
            this.settings = settings;
        }

        @Override
        public void render(GameMap map, int turn) {
            long total = map.count(Entity.class);
            assertTrue("More entities than cells", total <= (long) map.getLengthX() * map.getHeightY());
            assertTrue("Too many herbivores", map.count(Herbivore.class) <= settings.getMaxHerbivores());
            assertTrue("Too many predators", map.count(Predator.class) <= settings.getMaxPredators());
            for (Creature creature : map.getEntitiesOfType(Creature.class)) {
                assertTrue("Dead creature left on the map", !creature.isDead());
                assertTrue("Health above maximum", creature.getHealthPoint() <= creature.getMaxHealthPoint());
                assertTrue("Creature lost its position", map.getPosition(creature) != null);
            }
        }

        @Override
        public void showMessage(String message) {
        }
    }
}
