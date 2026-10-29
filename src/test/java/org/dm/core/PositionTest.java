package org.dm.core;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

public class PositionTest {

    @Test
    public void negativeCoordinatesAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Position(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> new Position(0, -1));
    }

    @Test
    public void distanceIsMeasuredInCellsWithoutDiagonals() {
        assertEquals(7, new Position(1, 2).distanceTo(new Position(4, 6)));
    }

    @Test
    public void distanceIsSymmetric() {
        Position a = new Position(5, 1);
        Position b = new Position(2, 4);

        assertEquals(a.distanceTo(b), b.distanceTo(a));
    }

    @Test
    public void distanceToSelfIsZero() {
        assertEquals(0, new Position(3, 3).distanceTo(new Position(3, 3)));
    }

    @Test
    public void positionsWithSameCoordinatesAreEqual() {
        assertEquals(new Position(2, 3), new Position(2, 3));
        assertEquals(new Position(2, 3).hashCode(), new Position(2, 3).hashCode());
        assertNotEquals(new Position(2, 3), new Position(3, 2));
    }
}
