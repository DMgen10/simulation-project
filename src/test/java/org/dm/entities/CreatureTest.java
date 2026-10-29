package org.dm.entities;

import org.dm.entities.living_entities.Creature;
import org.dm.entities.living_entities.herbivores.Rabbit;
import org.dm.entities.living_entities.predators.Fox;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CreatureTest {

    @Test
    public void newCreatureHasFullHealth() {
        Creature rabbit = new Rabbit(2, 10);

        assertEquals(10, rabbit.getHealthPoint());
        assertEquals(10, rabbit.getMaxHealthPoint());
        assertEquals(2, rabbit.getSpeedPoint());
        assertFalse(rabbit.isDead());
    }

    @Test
    public void damageReducesHealth() {
        Creature rabbit = new Rabbit(1, 10);

        rabbit.takeDamage(3);

        assertEquals(7, rabbit.getHealthPoint());
    }

    @Test
    public void creatureIsDeadWhenHealthReachesZero() {
        Creature rabbit = new Rabbit(1, 10);

        rabbit.takeDamage(10);

        assertTrue(rabbit.isDead());
    }

    @Test
    public void creatureIsDeadWhenHealthDropsBelowZero() {
        Creature rabbit = new Rabbit(1, 10);

        rabbit.takeDamage(25);

        assertTrue(rabbit.isDead());
    }

    @Test
    public void healRestoresHealth() {
        Creature rabbit = new Rabbit(1, 10);
        rabbit.takeDamage(6);

        rabbit.heal(2);

        assertEquals(6, rabbit.getHealthPoint());
    }

    @Test
    public void healCannotExceedMaximumHealth() {
        Creature rabbit = new Rabbit(1, 10);
        rabbit.takeDamage(2);

        rabbit.heal(100);

        assertEquals(10, rabbit.getHealthPoint());
    }

    @Test
    public void predatorKeepsItsAttackPower() {
        Fox fox = new Fox(1, 12, 4);

        assertEquals(4, fox.getPowerAttack());
    }
}
