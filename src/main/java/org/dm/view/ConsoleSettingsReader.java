package org.dm.view;

import org.dm.core.GameSettings;
import org.dm.core.SettingsField;
import java.io.PrintStream;
import java.util.EnumMap;
import java.util.Map;
import java.util.Scanner;

public class ConsoleSettingsReader {

    private final Scanner scanner;
    private final PrintStream out;

    public ConsoleSettingsReader(Scanner scanner, PrintStream out) {
        this.scanner = scanner;
        this.out = out;
    }

    public GameSettings read() {
        out.println("Enter simulation settings (press Enter to keep the value in brackets):");
        Map<SettingsField, Integer> values = new EnumMap<>(SettingsField.class);
        for (SettingsField field : SettingsField.values()) {
            values.put(field, readField(field));
        }
        return GameSettings.from(values);
    }

    private int readField(SettingsField field) {
        while (true) {
            out.print(field.getLabel() + " (" + field.getMin() + "-" + field.getMax()
                    + ") [" + field.getDefaultValue() + "]: ");
            if (!scanner.hasNextLine()) {
                return field.getDefaultValue();
            }
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                return field.getDefaultValue();
            }
            try {
                return field.parse(line);
            } catch (IllegalArgumentException e) {
                out.println(e.getMessage());
            }
        }
    }
}
