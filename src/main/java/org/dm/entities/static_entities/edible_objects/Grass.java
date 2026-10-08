package org.dm.entities.static_entities.edible_objects;

import org.dm.view.EntityType;

public class Grass extends Edible {

    public Grass(int nutritional) {
        super(nutritional);
    }

    @Override
    public EntityType getViewType() {
        return EntityType.GRASS;
    }
}
