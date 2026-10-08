package org.dm.entities.static_entities.edible_objects;

import org.dm.entities.static_entities.Inanimate;

public class Edible extends Inanimate {

    private int nutritional;

    public Edible(int nutritional) {
        this.nutritional = nutritional;
    }

    public int getNutritional() {
        return nutritional;
    }
}