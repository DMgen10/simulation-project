package org.dm.view;

import org.dm.entities.Entity;
import org.dm.entities.living_entities.herbivores.Hog;
import org.dm.entities.living_entities.herbivores.Rabbit;
import org.dm.entities.living_entities.predators.Fox;
import org.dm.entities.living_entities.predators.Wolf;
import org.dm.entities.static_entities.edible_objects.Grass;
import org.dm.entities.static_entities.edible_objects.Mushroom;
import org.dm.entities.static_entities.inedible_objects.Rock;
import org.dm.entities.static_entities.inedible_objects.Tree;
import org.junit.Test;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

public class ViewRepositoryTest {

    private final ViewRepository repository = new ViewRepository();

    @Test
    public void everyEntityKindHasItsOwnSprite() {
        List<Entity> entities = List.of(new Fox(1, 1, 1), new Wolf(1, 1, 1), new Hog(1, 1), new Rabbit(1, 1),
                new Grass(1), new Mushroom(1), new Rock(), new Tree());
        Set<String> sprites = new HashSet<>();

        for (Entity entity : entities) {
            String sprite = repository.getImageForEntity(entity);
            assertFalse(sprite.isBlank());
            sprites.add(sprite);
        }

        assertEquals(entities.size(), sprites.size());
    }

    @Test
    public void sameKindOfEntityAlwaysGetsTheSameSprite() {
        assertEquals(repository.getImageForEntity(new Fox(1, 1, 1)),
                repository.getImageForEntity(new Fox(5, 5, 5)));
    }

    @Test
    public void emptyCellHasItsOwnSprite() {
        assertFalse(repository.getEmptySprite().isBlank());
    }

    @Test
    public void unknownEntityKindIsRejected() {
        Entity unknown = new Entity() {
        };

        assertThrows(IllegalArgumentException.class, () -> repository.getImageForEntity(unknown));
    }
}
