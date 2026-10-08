package org.dm;

import org.dm.actions.Action;
import org.dm.actions.init_actions.EntitySpawnAction;
import org.dm.actions.turn_actions.EdibilitySpawnAction;
import org.dm.core.GameMap;
import org.dm.core.GameSettings;
import org.dm.core.Position;
import org.dm.entities.Entity;
import org.dm.entities.living_entities.Creature;
import org.dm.entities.living_entities.herbivores.Herbivore;
import org.dm.entities.living_entities.predators.Predator;
import org.dm.entities.static_entities.edible_objects.Edible;
import org.dm.path_finder.PathFinder;
import org.dm.view.Renderer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Simulation {

    private GameMap map;
    private CounterMoves counterMoves;
    private Renderer renderer;
    private GameSettings settings;
    private PathFinder finder = new PathFinder();
    private List<Action> initActions = new ArrayList<>();
    private List<Action> turnActions = new ArrayList<>();
    private volatile boolean running = false;
    public Simulation() {
        settings = new GameSettings();
        map = new GameMap(settings.getLength(), settings.getHeight());
        counterMoves = new CounterMoves();
        renderer = new Renderer(map);
    }
    // подготовка
    public void preparation(){
        initActions.add(new EntitySpawnAction());
        turnActions.add(new EdibilitySpawnAction());
        for (Action action: initActions){
            action.execute(map, settings);
        }
    }
    //просимулировать и отрендерить один ход
    public void nextTurn(){
        for (Action action : turnActions){
            action.execute(map,settings);
        }

        List<Creature> creatures = new ArrayList<>();
        for (Entity entity : map.getEntities().values()){
            if (entity instanceof Creature creature){
                creatures.add(creature);
            }
        }

        for (Creature creature : creatures){
            creature.makeMove(map, finder);
        }
        showCountEntities(map);
        counterMoves.nextMove();
        renderer.showMap();
        counterMoves.showMove();
    }
    // запустить бесконечный цикл симуляции и рендеринга
    public void startSimulation(){
        running = true;

        while (running){
            nextTurn();
            if (overSimulation(map)){
                System.out.println("The simulation is completed");
                running = false;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    // приостановить бесконечный цикл симуляции и рендеринга
    public void pauseSimulation(){
        running = false;
    }

    public boolean overSimulation(GameMap map) {
        int countHerbivores = 0;
        int countPredators = 0;
        for (Map.Entry<Position, Entity> entry : map.getEntities().entrySet()) {
            if (entry.getValue() instanceof Predator) {
                countPredators++;
            }
            if ((entry.getValue() instanceof Herbivore)) {
                countHerbivores++;
            }
        }
        return countHerbivores == 0 || countPredators == 0;
    }
    public void showCountEntities(GameMap map){
        int countHerbivores = 0;
        int countPredators = 0;
        int countEdible = 0;
        for (Map.Entry<Position, Entity> entry : map.getEntities().entrySet()){
            if (entry.getValue() instanceof Predator){
                countPredators++;
            }
            if ((entry.getValue() instanceof Herbivore)){
                countHerbivores++;
            }
            if ((entry.getValue() instanceof Edible)){
                countEdible++;
            }
        }
        System.out.println("Predators: " + countPredators);
        System.out.println("Herbivores: " + countHerbivores);
        System.out.println("Edible plants: " + countEdible);
    }
    public static void main(String[] args) {
        Simulation simulation = new Simulation();
        simulation.preparation();
        simulation.startSimulation();
    }
}