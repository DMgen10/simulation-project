package org.dm.view;

public enum EntityType {
    GRASS("\uD83C\uDF3E"),
    ROCK("\uD83D\uDDFF"),
    TREE("\uD83C\uDF33"),
    HOG("\uD83D\uDC37"),
    RABBIT("\uD83D\uDC30"),
    FOX("\uD83E\uDD8A"),
    WOLF("\uD83D\uDC3A"),
    MUSHROOM("\uD83C\uDF44");

    private final String name;

    EntityType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}