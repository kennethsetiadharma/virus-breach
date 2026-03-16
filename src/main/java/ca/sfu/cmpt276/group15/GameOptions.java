package ca.sfu.cmpt276.group15;

/**
 * Stores game options (volume) adjusted by the player
 */
public class GameOptions {
    private double volume;

    /**
     * Creates a new GameOptions instance with default settings.
     */
    public GameOptions() {
        // default volume 0.5
        this.volume = 0.5;
    }

    /**
     * Returns the current volume setting.
     *
     * @return the volume
     */
    public double getVolume() {
        return volume;
    }

    /**
     * Sets the volume setting.
     *
     * @param volume the volume to set
     */
    public void setVolume(double volume) {
        this.volume = Math.max(0.0, Math.min(1.0, volume));
    }
}
