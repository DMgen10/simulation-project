package org.dm.view;

import org.dm.core.GameMap;
import org.dm.core.Position;
import org.dm.entities.living_entities.herbivores.Rabbit;
import org.dm.entities.living_entities.predators.Fox;
import org.dm.entities.static_entities.edible_objects.Grass;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConsoleRendererTest {

    private final ViewRepository repository = new ViewRepository();
    private final ConsoleRenderer renderer = new ConsoleRenderer();
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private PrintStream originalOut;

    @Before
    public void redirectOutput() {
        originalOut = System.out;
        System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
    }

    @After
    public void restoreOutput() {
        System.setOut(originalOut);
    }

    @Test
    public void rendersStatisticsAndTurnNumber() {
        GameMap map = new GameMap(3, 2);
        map.add(new Position(0, 0), new Fox(1, 10, 1));
        map.add(new Position(1, 0), new Rabbit(1, 10));
        map.add(new Position(2, 0), new Rabbit(1, 10));
        map.add(new Position(0, 1), new Grass(1));

        renderer.render(map, 7);

        String text = printed();
        assertTrue(text.contains("Predators: 1"));
        assertTrue(text.contains("Herbivores: 2"));
        assertTrue(text.contains("Edible plants: 1"));
        assertTrue(text.contains("Step: 7"));
    }

    @Test
    public void rendersOneLinePerMapRowWithSpritesOnTheirCells() {
        GameMap map = new GameMap(3, 2);
        map.add(new Position(1, 0), new Fox(1, 10, 1));
        map.add(new Position(2, 1), new Rabbit(1, 10));

        renderer.render(map, 0);

        String[] lines = printed().split(System.lineSeparator());
        String empty = repository.getEmptySprite();
        String fox = repository.getImageForEntity(new Fox(1, 1, 1));
        String rabbit = repository.getImageForEntity(new Rabbit(1, 1));
        assertEquals(empty + fox + empty, lines[3]);
        assertEquals(empty + empty + rabbit, lines[4]);
    }

    @Test
    public void emptyMapIsDrawnWithEmptySpritesOnly() {
        GameMap map = new GameMap(2, 2);

        renderer.render(map, 0);

        String[] lines = printed().split(System.lineSeparator());
        String row = repository.getEmptySprite().repeat(2);
        assertEquals(row, lines[3]);
        assertEquals(row, lines[4]);
    }

    @Test
    public void showMessagePrintsTheMessage() {
        renderer.showMessage("Hello");

        assertEquals("Hello", printed().trim());
    }

    private String printed() {
        return output.toString(StandardCharsets.UTF_8);
    }
}
