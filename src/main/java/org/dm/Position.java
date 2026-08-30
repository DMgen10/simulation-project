package org.dm;

public record Position(int x, int y) {
    public Position {
        if (x < 0 || y < 0){
            throw new IllegalArgumentException("the value cannot be less than zero");
        }
    }
}
