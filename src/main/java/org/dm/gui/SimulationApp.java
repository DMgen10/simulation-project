package org.dm.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.dm.core.GameSettings;
import org.dm.core.SettingsField;
import org.dm.core.Simulation;
import java.util.Map;

public class SimulationApp extends Application {

    private Stage stage;
    private SpriteRepository sprites;
    private GameSettings settings;
    private int cellSize;
    private Canvas canvas;
    private Label statsLabel;
    private Label messageLabel;
    private Button toggleButton;
    private Button stepButton;

    private Simulation simulation;
    private GuiRenderer renderer;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        sprites = new SpriteRepository(CellSize.MAX_CELL_SIZE * 2);

        stage.setTitle("Simulation");
        stage.setResizable(false);
        showSettings(new GameSettings().toValues());
        stage.show();
    }

    @Override
    public void stop() {
        stopSimulation();
    }

    private void showSettings(Map<SettingsField, Integer> values) {
        stopSimulation();
        stage.setScene(new Scene(new SettingsView(values, this::startGame).getRoot()));
        stage.sizeToScene();
        stage.centerOnScreen();
    }

    private void startGame(GameSettings chosenSettings) {
        settings = chosenSettings;
        cellSize = CellSize.forMap(settings.getLength(), settings.getHeight());

        canvas = new Canvas((double) settings.getLength() * cellSize, (double) settings.getHeight() * cellSize);
        statsLabel = new Label();
        messageLabel = new Label();
        statsLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        messageLabel.setStyle("-fx-text-fill: #ffb347; -fx-font-size: 14px; -fx-font-weight: bold;");

        toggleButton = new Button("Start");
        stepButton = new Button("Step");
        Button restartButton = new Button("Restart");
        Button settingsButton = new Button("Settings");
        toggleButton.setOnAction(event -> toggleSimulation());
        stepButton.setOnAction(event -> makeSingleStep());
        restartButton.setOnAction(event -> restartSimulation());
        settingsButton.setOnAction(event -> showSettings(settings.toValues()));

        HBox toolbar = new HBox(10, toggleButton, stepButton, restartButton, settingsButton);
        toolbar.setPadding(new Insets(10));
        toolbar.setAlignment(Pos.CENTER_LEFT);

        HBox statusBar = new HBox(20, statsLabel, messageLabel);
        statusBar.setPadding(new Insets(10));
        statusBar.setAlignment(Pos.CENTER_LEFT);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #2b2b2b;");
        root.setTop(toolbar);
        root.setCenter(canvas);
        root.setBottom(statusBar);

        stage.setScene(new Scene(root));
        stage.sizeToScene();
        stage.centerOnScreen();

        restartSimulation();
    }

    private void stopSimulation() {
        if (simulation != null) {
            simulation.pauseSimulation();
        }
        if (renderer != null) {
            renderer.deactivate();
        }
    }

    private void restartSimulation() {
        stopSimulation();
        messageLabel.setText("");

        renderer = new GuiRenderer(canvas, statsLabel, messageLabel, sprites, cellSize, this::updateButtons);
        simulation = new Simulation(renderer, settings);
        simulation.preparation();
        updateButtons();
    }

    private void toggleSimulation() {
        if (simulation.isRunning()) {
            simulation.pauseSimulation();
        } else {
            simulation.startSimulation();
        }
        updateButtons();
    }

    private void makeSingleStep() {
        simulation.pauseSimulation();
        simulation.nextTurn();
        updateButtons();
    }

    private void updateButtons() {
        boolean over = simulation.isOver();
        toggleButton.setText(simulation.isRunning() ? "Pause" : "Start");
        toggleButton.setDisable(over);
        stepButton.setDisable(over);
    }
}
