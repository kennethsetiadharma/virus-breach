package ca.sfu.cmpt276.group15;

import java.io.File;

public class GameOptions {
    private double volume;

    public GameOptions() {
        // default volume 0.5
        this.volume = 0.5;
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
        this.volume = Math.max(0.0, Math.min(1.0, volume));
    }
}
