package org.dm.gui;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CellSizeTest {

    @Test
    public void defaultMapGetsTheFullCellSize() {
        assertEquals(64, CellSize.forMap(12, 12));
    }

    @Test
    public void smallMapNeverGetsCellsBiggerThanTheMaximum() {
        assertEquals(CellSize.MAX_CELL_SIZE, CellSize.forMap(5, 5));
    }

    @Test
    public void biggestMapGetsSmallerCells() {
        assertEquals(38, CellSize.forMap(20, 20));
    }

    @Test
    public void theLongerSideDecidesTheSize() {
        assertEquals(CellSize.forMap(20, 20), CellSize.forMap(20, 5));
        assertEquals(CellSize.forMap(20, 20), CellSize.forMap(5, 20));
    }

    @Test
    public void everyAllowedMapFitsIntoTheCanvasLimit() {
        for (int length = 5; length <= 20; length++) {
            for (int height = 5; height <= 20; height++) {
                int cell = CellSize.forMap(length, height);

                assertTrue(cell >= 30);
                assertTrue(length * cell <= CellSize.MAX_CANVAS_SIZE);
                assertTrue(height * cell <= CellSize.MAX_CANVAS_SIZE);
            }
        }
    }
}
