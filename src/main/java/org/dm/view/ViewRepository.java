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
import java.util.Map;

public class ViewRepository {

    private static final String EMPTY_SPRITE = " * ";
    private static final String SIDE_MARGIN = " ";

    private final Map<Class<? extends Entity>, String> sprites = Map.of(
            Grass.class, "\uD83C\uDF3E",
            Rock.class, "\uD83D\uDDFF",
            Tree.class, "\uD83C\uDF33",
            Hog.class, "\uD83D\uDC37",
            Rabbit.class, "\uD83D\uDC30",
            Fox.class, "\uD83E\uDD8A",
            Wolf.class, "\uD83D\uDC3A",
            Mushroom.class, "\uD83C\uDF44"
    );

    public String getImageForEntity(Entity entity) {
        String sprite = sprites.get(entity.getClass());
        if (sprite == null) {
            throw new IllegalArgumentException("No sprite for " + entity.getClass().getSimpleName());
        }
        return SIDE_MARGIN + sprite + SIDE_MARGIN;
    }

    public String getEmptySprite() {
        return EMPTY_SPRITE;
    }
}
