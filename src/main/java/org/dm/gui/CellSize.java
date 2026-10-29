package org.dm.gui;

public final class CellSize {

    public static final int MAX_CELL_SIZE = 64;
    public static final int MAX_CANVAS_SIZE = 768;

    private CellSize() {
    }

    public static int forMap(int length, int height) {
        int largestSide = Math.max(length, height);
        return Math.min(MAX_CELL_SIZE, MAX_CANVAS_SIZE / largestSide);
    }
}
