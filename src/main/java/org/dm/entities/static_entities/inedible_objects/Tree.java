package org.dm.entities.static_entities.inedible_objects;

import org.dm.entities.static_entities.Inanimate;
import org.dm.view.EntityType;

public class Tree extends Inedible {

    @Override
    public EntityType getViewType() {
        return EntityType.TREE;
    }

}