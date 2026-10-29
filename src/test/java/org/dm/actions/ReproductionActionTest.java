package org.dm.actions;

import org.dm.actions.turn_actions.ReproductionAction;
import org.dm.core.GameMap;
import org.dm.core.GameSettings;
import org.dm.core.Position;
import org.dm.entities.living_entities.herbivores.Rabbit;
import org.dm.entities.living_entities.predators.Fox;
import org.dm.entities.static_entities.inedible_objects.Rock;
import org.dm.testutil.ReadyCreatures;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReproductionActionTest {

    private final ReproductionAction action = new ReproductionAction();
    private final GameSettings settings = GameSettings.builder()
            .length(5).height(5)
            .foxes(0).wolves(0).hogs(0).rabbits(0)
            .build();

    @Test
    public void readyParentGivesBirthOnAFreeNeighbouringCell() {
        GameMap map = new GameMap(5, 5);
        Rabbit parent = ReadyCreatures.readyRabbit(30);
        Position parentPosition = new Position(2, 2);
        map.add(parentPosition, parent);

        action.execute(map, settings);

        assertEquals(2, map.count(Rabbit.class));
        Rabbit child = map.getEntitiesOfType(Rabbit.class).stream()
                .filter(rabbit -> rabbit != parent)
                .findFirst().orElseThrow();
        assertEquals(1, parentPosition.distanceTo(map.getPosition(child)));
    }

    @Test
    public void parentPaysForTheBirth() {
        GameMap map = new GameMap(5, 5);
        Rabbit parent = ReadyCreatures.readyRabbit(30);
        map.add(new Position(2, 2), parent);

        action.execute(map, settings);

        assertEquals(20, parent.getHealthPoint());
    }

    @Test
    public void creatureThatIsNotReadyDoesNotReproduce() {
        GameMap map = new GameMap(5, 5);
        map.add(new Position(2, 2), new Rabbit(1, 30));

        action.execute(map, settings);

        assertEquals(1, map.count(Rabbit.class));
    }

    @Test
    public void noBirthWhenThereIsNoFreeCellNextToTheParent() {
        GameMap map = new GameMap(2, 2);
        Rabbit parent = ReadyCreatures.readyRabbit(30);
        map.add(new Position(0, 0), parent);
        map.add(new Position(1, 0), new Rock());
        map.add(new Position(0, 1), new Rock());

        action.execute(map, settings);

        assertEquals(1, map.count(Rabbit.class));
        assertEquals(30, parent.getHealthPoint());
    }

    @Test
    public void newbornsAndParentsNeedTimeBeforeTheNextBirth() {
        GameMap map = new GameMap(5, 5);
        map.add(new Position(2, 2), ReadyCreatures.readyRabbit(30));

        action.execute(map, settings);
        action.execute(map, settings);
        action.execute(map, settings);

        assertEquals(2, map.count(Rabbit.class));
    }

    @Test
    public void herbivoreReproducesWhileBelowTheLimit() {
        GameMap map = new GameMap(5, 5);
        fillWithRabbits(map, settings.getMaxHerbivores() - 2);
        map.add(new Position(2, 2), ReadyCreatures.readyRabbit(30));

        action.execute(map, settings);

        assertEquals(settings.getMaxHerbivores(), map.count(Rabbit.class));
    }

    @Test
    public void herbivoreDoesNotReproduceAtTheLimit() {
        GameMap map = new GameMap(5, 5);
        Rabbit parent = ReadyCreatures.readyRabbit(30);
        fillWithRabbits(map, settings.getMaxHerbivores() - 1);
        map.add(new Position(2, 2), parent);

        action.execute(map, settings);

        assertEquals(settings.getMaxHerbivores(), map.count(Rabbit.class));
        assertEquals(30, parent.getHealthPoint());
    }

    @Test
    public void predatorReproducesWhileBelowTheLimit() {
        GameMap map = new GameMap(5, 5);
        for (int i = 0; i < settings.getMaxPredators() - 2; i++) {
            map.add(new Position(i, 4), new Fox(1, 12, 4));
        }
        map.add(new Position(2, 2), ReadyCreatures.readyFox(12));

        action.execute(map, settings);

        assertEquals(settings.getMaxPredators(), map.count(Fox.class));
    }

    @Test
    public void predatorDoesNotReproduceAtTheLimit() {
        GameMap map = new GameMap(5, 5);
        Fox parent = ReadyCreatures.readyFox(12);
        for (int i = 0; i < settings.getMaxPredators() - 1; i++) {
            map.add(new Position(i, 4), new Fox(1, 12, 4));
        }
        map.add(new Position(2, 2), parent);

        action.execute(map, settings);

        assertEquals(settings.getMaxPredators(), map.count(Fox.class));
        assertEquals(12, parent.getHealthPoint());
    }

    @Test
    public void fullPredatorPopulationDoesNotBlockHerbivores() {
        GameMap map = new GameMap(5, 5);
        for (int i = 0; i < settings.getMaxPredators(); i++) {
            map.add(new Position(i, 4), new Fox(1, 12, 4));
        }
        map.add(new Position(2, 2), ReadyCreatures.readyRabbit(30));

        action.execute(map, settings);

        assertEquals(2, map.count(Rabbit.class));
    }

    @Test
    public void limitsAreAtLeastAsBigAsTheStartingPopulation() {
        GameSettings defaults = new GameSettings();

        assertTrue(defaults.getMaxHerbivores() >= defaults.getTotalHogs() + defaults.getTotalRabbit());
        assertTrue(defaults.getMaxPredators() >= defaults.getTotalFox() + defaults.getTotalWolf());
    }

    private void fillWithRabbits(GameMap map, int amount) {
        int placed = 0;
        for (int y = 4; y >= 0 && placed < amount; y--) {
            for (int x = 0; x < 5 && placed < amount; x++) {
                map.add(new Position(x, y), new Rabbit(1, 10));
                placed++;
            }
        }
    }
}
