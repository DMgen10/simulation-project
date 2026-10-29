package org.dm.core;

public record Position(int x, int y) {

    public Position {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException("Coordinates cannot be negative: " + x + ", " + y);
        }
    }

    public int distanceTo(Position other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y);
    }
}
