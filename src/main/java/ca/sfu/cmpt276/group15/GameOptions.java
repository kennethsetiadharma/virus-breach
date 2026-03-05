package ca.sfu.cmpt276.group15;

import java.io.File;

public class GameOptions {
    private double volume;

    public GameOptions() {
    }

    public void save(File file) {

    }

    public boolean load(File file) {
        return false;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }
}
