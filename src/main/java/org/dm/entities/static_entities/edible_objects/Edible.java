package org.dm.entities.static_entities.edible_objects;

import org.dm.entities.static_entities.Inanimate;

public abstract class Edible extends Inanimate {

    private final int nutritional;

    protected Edible(int nutritional) {
        this.nutritional = nutritional;
    }

    public int getNutritional() {
        return nutritional;
    }
}