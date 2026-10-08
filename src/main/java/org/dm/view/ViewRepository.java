package org.dm.view;

import org.dm.entities.Entity;

public class ViewRepository {

    public final String EMPTY_SPRITE = " * ";
    public final String SIDE_MARGIN = " ";

    public String getImageForEntity(Entity entity){
        return SIDE_MARGIN + entity.getViewType().getName() + SIDE_MARGIN;
    }

    public String getEmptySprite() {
        return EMPTY_SPRITE;
    }
}