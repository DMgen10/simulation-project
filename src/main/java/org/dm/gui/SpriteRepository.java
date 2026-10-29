package org.dm.gui;

import javafx.scene.image.Image;
import org.dm.entities.Entity;
import org.dm.entities.living_entities.herbivores.Hog;
import org.dm.entities.living_entities.herbivores.Rabbit;
import org.dm.entities.living_entities.predators.Fox;
import org.dm.entities.living_entities.predators.Wolf;
import org.dm.entities.static_entities.edible_objects.Grass;
import org.dm.entities.static_entities.edible_objects.Mushroom;
import org.dm.entities.static_entities.inedible_objects.Rock;
import org.dm.entities.static_entities.inedible_objects.Tree;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class SpriteRepository {

    private static final String IMAGES_PATH = "/images/";

    private final Map<Class<? extends Entity>, Image> sprites = new HashMap<>();
    private final Image background;

    public SpriteRepository(int spriteSize) {
        load(Fox.class, "fox.png", spriteSize);
        load(Wolf.class, "wolf.png", spriteSize);
        load(Rabbit.class, "rabbit.png", spriteSize);
        load(Hog.class, "hog.png", spriteSize);
        load(Grass.class, "grass.png", spriteSize);
        load(Mushroom.class, "mushroom.png", spriteSize);
        load(Rock.class, "rock.png", spriteSize);
        load(Tree.class, "tree.png", spriteSize);
        background = new Image(open("background.jpeg"));
    }

    public Image getSprite(Class<? extends Entity> type) {
        Image sprite = sprites.get(type);
        if (sprite == null) {
            throw new IllegalArgumentException("No sprite for " + type.getSimpleName());
        }
        return sprite;
    }

    public Image getBackground() {
        return background;
    }

    private void load(Class<? extends Entity> type, String fileName, int size) {
        sprites.put(type, new Image(open(fileName), size, size, true, true));
    }

    private InputStream open(String fileName) {
        InputStream stream = SpriteRepository.class.getResourceAsStream(IMAGES_PATH + fileName);
        if (stream == null) {
            throw new IllegalStateException("Image not found in resources: " + IMAGES_PATH + fileName);
        }
        return stream;
    }
}
