package org.dm.entities;

import org.dm.core.GameMap;
import org.dm.core.Position;
import org.dm.entities.living_entities.Creature;
import org.dm.entities.living_entities.herbivores.Rabbit;
import org.dm.entities.living_entities.predators.Fox;
import org.dm.entities.living_entities.predators.Wolf;
import org.dm.entities.static_entities.edible_objects.Grass;
import org.dm.path_finder.PathFinder;
import org.dm.testutil.ReadyCreatures;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ReproductionTest {

    private final PathFinder finder = new PathFinder();

    @Test
    public void newCreatureCannotReproduceRightAway() {
        assertFalse(new Rabbit(1, 10).canReproduce());
        assertFalse(new Fox(1, 12, 4).canReproduce());
    }

    @Test
    public void wellFedHerbivoreBecomesReadyAfterAWhile() {
        int turns = ReadyCreatures.turnsUntilReady(new Rabbit(1, 12), () -> new Grass(0));

        assertTrue("Reproduction should need some turns", turns > 1);
    }

    @Test
    public void wellFedPredatorBecomesReadyAfterAWhile() {
        int turns = ReadyCreatures.turnsUntilReady(new Fox(1, 12, 50), () -> new Rabbit(1, 1));

        assertTrue("Reproduction should need some turns", turns > 1);
    }

    @Test
    public void predatorsBreedSlowerThanHerbivores() {
        int herbivoreTurns = ReadyCreatures.turnsUntilReady(new Rabbit(1, 12), () -> new Grass(0));
        int predatorTurns = ReadyCreatures.turnsUntilReady(new Wolf(1, 12, 50), () -> new Rabbit(1, 1));

        assertTrue(predatorTurns > herbivoreTurns);
    }

    @Test
    public void hungryCreatureCannotReproduceEvenWhenCooldownIsOver() {
        GameMap map = new GameMap(3, 3);
        Rabbit rabbit = new Rabbit(1, 30);
        map.add(new Position(1, 1), rabbit);

        for (int turn = 0; turn < 12; turn++) {
            rabbit.makeMove(map, finder);
        }

        assertFalse(rabbit.canReproduce());
    }

    @Test
    public void injuredCreatureCannotReproduce() {
        Rabbit rabbit = new Rabbit(1, 10);
        rabbit.takeDamage(5);
        GameMap map = new GameMap(2, 1);
        map.add(new Position(0, 0), rabbit);

        for (int turn = 0; turn < 40; turn++) {
            map.add(new Position(1, 0), new Grass(0));
            rabbit.makeMove(map, finder);
        }

        assertFalse(rabbit.canReproduce());
    }

    @Test
    public void creatureWithTinyHealthNeverReproducesSoItCannotKillItself() {
        Rabbit rabbit = new Rabbit(1, 1);
        GameMap map = new GameMap(2, 1);
        map.add(new Position(0, 0), rabbit);

        for (int turn = 0; turn < 60; turn++) {
            map.add(new Position(1, 0), new Grass(0));
            rabbit.makeMove(map, finder);
        }

        assertFalse(rabbit.canReproduce());
    }

    @Test
    public void reproductionCostsAThirdOfMaximumHealth() {
        Rabbit rabbit = ReadyCreatures.readyRabbit(30);

        rabbit.reproduce();

        assertEquals(20, rabbit.getHealthPoint());
        assertFalse(rabbit.isDead());
    }

    @Test
    public void parentCannotReproduceAgainImmediately() {
        Rabbit rabbit = ReadyCreatures.readyRabbit(30);

        rabbit.reproduce();

        assertFalse(rabbit.canReproduce());
    }

    @Test
    public void offspringIsTheSameSpeciesWithTheSameStats() {
        Rabbit parent = ReadyCreatures.readyRabbit(30);

        Creature child = parent.reproduce();

        assertNotSame(parent, child);
        assertTrue(child instanceof Rabbit);
        assertEquals(parent.getSpeedPoint(), child.getSpeedPoint());
        assertEquals(30, child.getMaxHealthPoint());
        assertEquals(30, child.getHealthPoint());
    }

    @Test
    public void predatorOffspringInheritsAttackPower() {
        Fox parent = ReadyCreatures.readyFox(12);

        Creature child = parent.reproduce();

        assertTrue(child instanceof Fox);
        assertEquals(parent.getPowerAttack(), ((Fox) child).getPowerAttack());
    }

    @Test
    public void newbornCannotReproduceRightAway() {
        Creature child = ReadyCreatures.readyRabbit(30).reproduce();

        assertFalse(child.canReproduce());
    }

    @Test
    public void reproducingWhenNotAllowedThrows() {
        assertThrows(IllegalStateException.class, () -> new Rabbit(1, 10).reproduce());
    }
}
