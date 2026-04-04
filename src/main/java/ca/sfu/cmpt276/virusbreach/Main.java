package ca.sfu.cmpt276.virusbreach;

import javafx.application.Application;

/**
 * Entrypoint for the game.
 *
 * @see VirusBreach
 */
public class Main {
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
