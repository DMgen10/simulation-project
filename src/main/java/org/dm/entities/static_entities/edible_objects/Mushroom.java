package org.dm.entities.static_entities.edible_objects;

import org.dm.view.EntityType;

public class Mushroom extends Edible {

    public Mushroom(int nutritional) {
        super(nutritional);
    }

    @Override
    public EntityType getViewType() {
        return EntityType.MUSHROOM;
    }
}