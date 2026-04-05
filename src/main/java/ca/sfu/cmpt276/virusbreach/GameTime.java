package ca.sfu.cmpt276.virusbreach;

/**
 * Helper to handle ingame time
 */
public final class GameTime {
    /**
     * Converts ticks to seconds
     *
     * @param ticks number of ticks
     * @return number of seconds
     */
    public static int ticksToSeconds(int ticks) {
        return (int) ((ticks * VirusBreach.UPDATE_INTERVAL) / 1000);
    }

    /**
     * Formats ticks as {@code MM:SS}
     *
     * @param ticks number of ticks
     * @return formatted time
     */
    public static String ticksToClock(int ticks) {
        int totalSeconds = ticksToSeconds(ticks);
        return String.format("%02d:%02d", totalSeconds / 60, totalSeconds % 60);
    }
}
