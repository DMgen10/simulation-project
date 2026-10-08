package org.dm.core;

public record Position(int x, int y) {
    public Position {
        if (x < 0 || y < 0){
            throw new IllegalArgumentException("The value cannot be less than zero");
        }
    }
}
