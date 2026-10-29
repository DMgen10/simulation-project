package org.dm.core;

import org.dm.entities.living_entities.herbivores.Rabbit;
import org.dm.entities.living_entities.predators.Fox;
import org.dm.entities.static_entities.edible_objects.Grass;
import org.dm.view.Renderer;
import org.junit.Test;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SimulationTest {

    private static final long WAIT_LIMIT_MILLIS = 5000;

    @Test
    public void preparationPlacesEntitiesAndRendersInitialState() {
        RecordingRenderer renderer = new RecordingRenderer();
        Simulation simulation = new Simulation(renderer, new GameSettings());

        simulation.preparation();

        assertEquals(List.of(0), renderer.turns);
        assertFalse(simulation.isOver());
        assertFalse(simulation.isRunning());
    }

    @Test
    public void nextTurnCountsTurnsAndRendersEachOfThem() {
        RecordingRenderer renderer = new RecordingRenderer();
        Simulation simulation = new Simulation(renderer, new GameSettings());
        simulation.preparation();

        simulation.nextTurn();
        simulation.nextTurn();

        assertEquals(List.of(0, 1, 2), renderer.turns);
    }

    @Test
    public void chosenSettingsAreAppliedToTheMapAndTheCreatures() {
        RecordingRenderer renderer = new RecordingRenderer();
        GameSettings settings = GameSettings.builder()
                .length(9).height(7)
                .healthHerbivores(15).healthPredators(20)
                .speedHerbivores(2).speedPredators(3)
                .attackPredators(6).nutritionalValue(4)
                .build();

        new Simulation(renderer, settings).preparation();

        GameMap map = renderer.lastMap;
        assertEquals(9, map.getLengthX());
        assertEquals(7, map.getHeightY());
        Rabbit rabbit = map.getEntitiesOfType(Rabbit.class).get(0);
        assertEquals(15, rabbit.getHealthPoint());
        assertEquals(2, rabbit.getSpeedPoint());
        Fox fox = map.getEntitiesOfType(Fox.class).get(0);
        assertEquals(20, fox.getHealthPoint());
        assertEquals(3, fox.getSpeedPoint());
        assertEquals(6, fox.getPowerAttack());
        assertEquals(4, map.getEntitiesOfType(Grass.class).get(0).getNutritional());
    }

    @Test
    public void simulationIsOverWhenThereAreNoPredators() {
        Simulation simulation = new Simulation(new RecordingRenderer(), smallWorld(0, 3));

        simulation.preparation();

        assertTrue(simulation.isOver());
    }

    @Test
    public void simulationIsOverWhenThereAreNoHerbivores() {
        Simulation simulation = new Simulation(new RecordingRenderer(), smallWorld(2, 0));

        simulation.preparation();

        assertTrue(simulation.isOver());
    }

    @Test
    public void nextTurnDoesNothingWhenSimulationIsOver() {
        RecordingRenderer renderer = new RecordingRenderer();
        Simulation simulation = new Simulation(renderer, smallWorld(0, 3));
        simulation.preparation();

        simulation.nextTurn();

        assertEquals(List.of(0), renderer.turns);
        assertTrue(renderer.messages.isEmpty());
    }

    @Test
    public void simulationFinishesAndReportsItOnce() {
        RecordingRenderer renderer = new RecordingRenderer();
        Simulation simulation = new Simulation(renderer, smallWorld(1, 1));
        simulation.preparation();

        for (int turn = 0; turn < 200 && !simulation.isOver(); turn++) {
            simulation.nextTurn();
        }
        simulation.nextTurn();

        assertTrue(simulation.isOver());
        assertEquals(1, renderer.messages.size());
    }

    @Test
    public void startRunsTurnsInBackgroundAndPauseStopsThem() throws InterruptedException {
        RecordingRenderer renderer = new RecordingRenderer();
        Simulation simulation = new Simulation(renderer, fastWorld());
        simulation.preparation();

        simulation.startSimulation();
        assertTrue(simulation.isRunning());
        waitForTurns(renderer, 4);
        simulation.pauseSimulation();

        assertFalse(simulation.isRunning());
        int turnsAfterPause = renderer.turns.size();
        Thread.sleep(150);
        assertEquals(turnsAfterPause, renderer.turns.size());
    }

    @Test
    public void simulationCanBeResumedAfterPause() throws InterruptedException {
        RecordingRenderer renderer = new RecordingRenderer();
        Simulation simulation = new Simulation(renderer, fastWorld());
        simulation.preparation();
        simulation.startSimulation();
        waitForTurns(renderer, 3);
        simulation.pauseSimulation();
        int turnsBeforeResume = renderer.turns.size();

        simulation.startSimulation();
        waitForTurns(renderer, turnsBeforeResume + 2);
        simulation.pauseSimulation();

        assertTrue(renderer.turns.size() > turnsBeforeResume);
    }

    @Test
    public void turnsAreNumberedWithoutGapsAcrossPauseAndResume() throws InterruptedException {
        RecordingRenderer renderer = new RecordingRenderer();
        Simulation simulation = new Simulation(renderer, fastWorld());
        simulation.preparation();
        simulation.startSimulation();
        waitForTurns(renderer, 3);
        simulation.pauseSimulation();
        simulation.startSimulation();
        waitForTurns(renderer, renderer.turns.size() + 2);
        simulation.pauseSimulation();

        for (int i = 0; i < renderer.turns.size(); i++) {
            assertEquals(Integer.valueOf(i), renderer.turns.get(i));
        }
    }

    @Test
    public void startingTwiceDoesNotBreakAnything() throws InterruptedException {
        RecordingRenderer renderer = new RecordingRenderer();
        Simulation simulation = new Simulation(renderer, fastWorld());
        simulation.preparation();

        simulation.startSimulation();
        simulation.startSimulation();
        waitForTurns(renderer, 3);
        simulation.pauseSimulation();

        assertFalse(simulation.isRunning());
    }

    @Test
    public void pauseWithoutStartIsSafe() {
        Simulation simulation = new Simulation(new RecordingRenderer(), new GameSettings());
        simulation.preparation();

        simulation.pauseSimulation();

        assertFalse(simulation.isRunning());
    }

    @Test
    public void runningSimulationStopsByItselfWhenOverAndReportsIt() throws InterruptedException {
        RecordingRenderer renderer = new RecordingRenderer();
        Simulation simulation = new Simulation(renderer, smallWorld(1, 1));
        simulation.preparation();

        simulation.startSimulation();
        long deadline = System.currentTimeMillis() + WAIT_LIMIT_MILLIS;
        while (simulation.isRunning() && System.currentTimeMillis() < deadline) {
            Thread.sleep(10);
        }

        assertFalse(simulation.isRunning());
        assertTrue(simulation.isOver());
        assertEquals(1, renderer.messages.size());
    }

    private void waitForTurns(RecordingRenderer renderer, int expectedRenders) throws InterruptedException {
        long deadline = System.currentTimeMillis() + WAIT_LIMIT_MILLIS;
        while (renderer.turns.size() < expectedRenders && System.currentTimeMillis() < deadline) {
            Thread.sleep(5);
        }
        assertTrue("Simulation did not produce enough turns", renderer.turns.size() >= expectedRenders);
    }

    private GameSettings fastWorld() {
        return GameSettings.builder().turnDelayMillis(20).build();
    }

    // Крошечный мир без еды и камней: только лисы и кролики
    private GameSettings smallWorld(int foxes, int rabbits) {
        return GameSettings.builder()
                .length(5).height(5)
                .foxes(foxes).wolves(0).hogs(0).rabbits(rabbits)
                .grass(0).mushrooms(0).rocks(0).trees(0)
                .attackPredators(50)
                .turnDelayMillis(5)
                .build();
    }

    private static class RecordingRenderer implements Renderer {

        private final List<Integer> turns = new CopyOnWriteArrayList<>();
        private final List<String> messages = new CopyOnWriteArrayList<>();
        private volatile GameMap lastMap;

        @Override
        public void render(GameMap map, int turn) {
            lastMap = map;
            turns.add(turn);
        }

        @Override
        public void showMessage(String message) {
            messages.add(message);
        }
    }
}
