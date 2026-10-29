package org.dm.gui;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.dm.core.GameMap;
import org.dm.core.Position;
import org.dm.entities.Entity;
import org.dm.entities.living_entities.Creature;
import org.dm.entities.living_entities.herbivores.Herbivore;
import org.dm.entities.living_entities.predators.Predator;
import org.dm.entities.static_entities.edible_objects.Edible;
import org.dm.view.Renderer;

import java.util.ArrayList;
import java.util.List;

public class GuiRenderer implements Renderer {

    private final Canvas canvas;
    private final Label statsLabel;
    private final Label messageLabel;
    private final SpriteRepository sprites;
    private final int cellSize;
    private final int spritePadding;
    private final double healthBarHeight;
    private final Runnable onFinished;

    private volatile boolean active = true;

    public GuiRenderer(Canvas canvas, Label statsLabel, Label messageLabel,
                       SpriteRepository sprites, int cellSize, Runnable onFinished) {
        this.canvas = canvas;
        this.statsLabel = statsLabel;
        this.messageLabel = messageLabel;
        this.sprites = sprites;
        this.cellSize = cellSize;
        this.spritePadding = Math.max(2, cellSize / 16);
        this.healthBarHeight = Math.max(3, cellSize / 12.0);
        this.onFinished = onFinished;
    }

    public void deactivate() {
        active = false;
    }

    @Override
    public void render(GameMap map, int turn) {
        List<Cell> cells = takeSnapshot(map);
        String stats = "Turn: " + turn
                + "    Predators: " + map.count(Predator.class)
                + "    Herbivores: " + map.count(Herbivore.class)
                + "    Edible: " + map.count(Edible.class);
        Platform.runLater(() -> draw(cells, stats));
    }

    @Override
    public void showMessage(String message) {
        Platform.runLater(() -> {
            if (!active) {
                return;
            }
            messageLabel.setText(message);
            onFinished.run();
        });
    }

    private record Cell(Class<? extends Entity> type, int x, int y, double health) {
    }

    private List<Cell> takeSnapshot(GameMap map) {
        List<Cell> cells = new ArrayList<>();
        for (int y = 0; y < map.getHeightY(); y++) {
            for (int x = 0; x < map.getLengthX(); x++) {
                Entity entity = map.getEntity(new Position(x, y));
                if (entity != null) {
                    cells.add(new Cell(entity.getClass(), x, y, healthOf(entity)));
                }
            }
        }
        return cells;
    }

    private double healthOf(Entity entity) {
        if (entity instanceof Creature creature) {
            return Math.max(0, (double) creature.getHealthPoint() / creature.getMaxHealthPoint());
        }
        return -1;
    }

    private void draw(List<Cell> cells, String stats) {
        if (!active) {
            return;
        }
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        graphics.drawImage(sprites.getBackground(), 0, 0, canvas.getWidth(), canvas.getHeight());
        drawGrid(graphics);

        for (Cell cell : cells) {
            double left = (double) cell.x() * cellSize;
            double top = (double) cell.y() * cellSize;
            graphics.drawImage(sprites.getSprite(cell.type()),
                    left + spritePadding, top + spritePadding,
                    cellSize - 2.0 * spritePadding, cellSize - 2.0 * spritePadding);
            if (cell.health() >= 0) {
                drawHealthBar(graphics, left, top, cell.health());
            }
        }
        statsLabel.setText(stats);
    }

    private void drawGrid(GraphicsContext graphics) {
        graphics.setStroke(Color.rgb(0, 0, 0, 0.12));
        graphics.setLineWidth(1);
        for (double x = 0; x <= canvas.getWidth(); x += cellSize) {
            graphics.strokeLine(x, 0, x, canvas.getHeight());
        }
        for (double y = 0; y <= canvas.getHeight(); y += cellSize) {
            graphics.strokeLine(0, y, canvas.getWidth(), y);
        }
    }

    private void drawHealthBar(GraphicsContext graphics, double left, double top, double health) {
        double barWidth = cellSize - 2.0 * spritePadding;
        double barLeft = left + spritePadding;
        double barTop = top + 2;
        graphics.setFill(Color.rgb(0, 0, 0, 0.6));
        graphics.fillRect(barLeft, barTop, barWidth, healthBarHeight);
        graphics.setFill(Color.hsb(120 * health, 0.85, 0.9));
        graphics.fillRect(barLeft, barTop, barWidth * health, healthBarHeight);
    }
}
