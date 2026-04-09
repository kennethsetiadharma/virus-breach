package ca.sfu.cmpt276.virusbreach;

import javafx.application.Application;

/**
 * Entrypoint for the game.
 *
 * @see VirusBreach
 */
public class Main {
    /**
     * When enabled, exceptions do not shut down the JavaFX context, to allow future tests to run.
     */
    public static boolean testMode = false;

    /**
     * Launches the game with the specified arguments.
     *
     * @param args the commandline arguments
     */
    public static void main(String[] args) {
        Application.launch(VirusBreach.class, args);
    }

    Main() {
        throw new UnsupportedOperationException("Cannot construct this class.");
    }
}
