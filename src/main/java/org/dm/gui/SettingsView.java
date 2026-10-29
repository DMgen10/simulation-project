package org.dm.gui;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.dm.core.GameSettings;
import org.dm.core.SettingsField;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

public class SettingsView {

    private static final String LABEL_STYLE = "-fx-text-fill: white; -fx-font-size: 14px;";
    private static final String HINT_STYLE = "-fx-text-fill: #9aa0a6; -fx-font-size: 12px;";

    private final VBox root = new VBox(16);
    private final Map<SettingsField, TextField> inputs = new EnumMap<>(SettingsField.class);
    private final Label errorLabel = new Label();
    private final Consumer<GameSettings> onStart;

    public SettingsView(Map<SettingsField, Integer> initialValues, Consumer<GameSettings> onStart) {
        this.onStart = onStart;

        Label title = new Label("Simulation settings");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(10);
        int row = 0;
        for (SettingsField field : SettingsField.values()) {
            Label label = new Label(field.getLabel());
            label.setStyle(LABEL_STYLE);
            TextField input = new TextField(String.valueOf(initialValues.getOrDefault(field, field.getDefaultValue())));
            input.setPrefColumnCount(5);
            Label range = new Label(field.getMin() + " - " + field.getMax());
            range.setStyle(HINT_STYLE);

            form.add(label, 0, row);
            form.add(input, 1, row);
            form.add(range, 2, row);
            inputs.put(field, input);
            row++;
        }

        errorLabel.setStyle("-fx-text-fill: #ff6b6b; -fx-font-size: 13px;");

        Button startButton = new Button("Start");
        startButton.setDefaultButton(true);
        startButton.setOnAction(event -> submit());

        root.getChildren().addAll(title, form, errorLabel, startButton);
        root.setPadding(new Insets(24));
        root.setStyle("-fx-background-color: #2b2b2b;");
    }

    public Parent getRoot() {
        return root;
    }

    private void submit() {
        GameSettings settings;
        try {
            settings = readSettings();
        } catch (IllegalArgumentException e) {
            errorLabel.setText(e.getMessage());
            return;
        }
        errorLabel.setText("");
        onStart.accept(settings);
    }

    private GameSettings readSettings() {
        Map<SettingsField, Integer> values = new EnumMap<>(SettingsField.class);
        for (Map.Entry<SettingsField, TextField> entry : inputs.entrySet()) {
            values.put(entry.getKey(), entry.getKey().parse(entry.getValue().getText()));
        }
        return GameSettings.from(values);
    }
}
