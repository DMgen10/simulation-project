package org.dm.core;

import org.dm.actions.Action;
import org.dm.actions.init_actions.EntitySpawnAction;
import org.dm.actions.turn_actions.EdibleSpawnAction;
import org.dm.actions.turn_actions.MoveCreaturesAction;
import org.dm.actions.turn_actions.ReproductionAction;
import org.dm.entities.living_entities.herbivores.Herbivore;
import org.dm.entities.living_entities.predators.Predator;
import org.dm.view.Renderer;
import java.util.List;

public class Simulation {

    private final GameSettings settings;
    private final GameMap map;
    private final TurnCounter turnCounter = new TurnCounter();
    private final Renderer renderer;

    private final List<Action> initActions = List.of(new EntitySpawnAction());
    private final List<Action> turnActions = List.of(new EdibleSpawnAction(), new MoveCreaturesAction(), new ReproductionAction());

    private volatile boolean running = false;
    private Thread simulationThread;

    public Simulation(Renderer renderer, GameSettings settings) {
        this.renderer = renderer;
        this.settings = settings;
        this.map = new GameMap(settings.getLength(), settings.getHeight());
    }

    public synchronized void preparation() {
        for (Action action : initActions) {
            action.execute(map, settings);
        }
        renderer.render(map, turnCounter.getValue());
    }

    public synchronized void nextTurn() {
        if (isOver()) {
            running = false;
            return;
        }
        for (Action action : turnActions) {
            action.execute(map, settings);
        }
        turnCounter.increment();
        renderer.render(map, turnCounter.getValue());

        if (isOver()) {
            running = false;
            renderer.showMessage("The simulation is over");
        }
    }

    public synchronized void startSimulation() {
        if (running) {
            return;
        }
        running = true;
        simulationThread = new Thread(this::runSimulationLoop, "simulation-thread");
        simulationThread.setDaemon(true);
        simulationThread.start();
    }

    public void pauseSimulation() {
        Thread thread;
        synchronized (this) {
            running = false;
            thread = simulationThread;
            simulationThread = null;
        }
        if (thread == null || thread == Thread.currentThread()) {
            return;
        }
        thread.interrupt();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public boolean isRunning() {
        return running;
    }

    public synchronized boolean isOver() {
        return map.count(Herbivore.class) == 0 || map.count(Predator.class) == 0;
    }

    private void runSimulationLoop() {
        while (running) {
            nextTurn();
            if (!running) {
                return;
            }
            try {
                Thread.sleep(settings.getTurnDelayMillis());
            } catch (InterruptedException e) {
                running = false;
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}