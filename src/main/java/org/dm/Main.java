package org.dm;

import org.dm.core.GameSettings;
import org.dm.core.Simulation;
import org.dm.view.ConsoleRenderer;
import org.dm.view.ConsoleSettingsReader;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            GameSettings settings = new ConsoleSettingsReader(scanner, System.out).read();
            Simulation simulation = new Simulation(new ConsoleRenderer(), settings);
            simulation.preparation();
            printHelp();
            runCommandLoop(scanner, simulation);
            simulation.pauseSimulation();
        }
    }

    private static void runCommandLoop(Scanner scanner, Simulation simulation) {
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine().trim().toLowerCase();
            if (command.equals("q")) {
                return;
            }
            switch (command) {
                case "s" -> simulation.startSimulation();
                case "p" -> simulation.pauseSimulation();
                case "n" -> {
                    simulation.pauseSimulation();
                    if (simulation.isOver()) {
                        System.out.println("The simulation is over");
                    } else {
                        simulation.nextTurn();
                    }
                }
                default -> printHelp();
            }
        }
    }

    private static void printHelp() {
        System.out.println("Commands: s - start/continue, p - pause, n - one turn, q - quit");
    }
}
