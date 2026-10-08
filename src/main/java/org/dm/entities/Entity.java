package org.dm.entities;

import org.dm.view.EntityType;

public abstract class Entity {

    private EntityType entityType;
    public EntityType getViewType() {
        return entityType;
    }
}