package org.dm.entities.living_entities.herbivores;
import org.dm.view.EntityType;

public class Hog extends Herbivore {

    public Hog(int speedPoint, int healthPoint) {
        super(speedPoint, healthPoint);
    }

    @Override
    public EntityType getViewType() {
        return EntityType.HOG;
    }

}
