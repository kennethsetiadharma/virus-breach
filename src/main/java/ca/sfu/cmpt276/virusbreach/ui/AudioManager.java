package ca.sfu.cmpt276.virusbreach.ui;

import ca.sfu.cmpt276.virusbreach.GameOptions;
import javafx.scene.media.AudioClip;

/**
 * Manages game audio playback. 
 * Audio clips are preloaded on init call, and played with specified volume.
 */
public class AudioManager {
    private static final String[] PRELOADED_ASSETS = {
        "bonus.wav",
        "caught.wav",
        "click.wav",
        "damage.wav",
        "reward.wav",
        "success.wav"
    };

    private static GameOptions options;
    private static boolean initialized = false;

    AudioManager() {
        throw new UnsupportedOperationException("AudioManager should not be constructed.");
    }

    /**
     * Apply the volume options and preload audio.
     * To only run once at the very start. 
     * Iterates through the list of preload assets to load them into the clips map.
     * Then, play a silent clip to reduce lag on first real clip play.
     * 
     * @param options the game options 
     * @see GameOptions
     */
    public static void init(GameOptions options) {
        AudioManager.options = options;

        if (AudioManager.initialized) return;

        for (String asset : PRELOADED_ASSETS) {
            AudioClip clip = ResourceManager.fetchAudio(asset);
            if (clip == null) return;
            clip.play(Double.MIN_NORMAL); // play to force-load audio
        }

        AudioManager.initialized = true;
    }

    /**
     * Play the specified audio clip
     * 
     * @param asset audio clip asset path
     */
    public static void play(String asset) {
        if (!initialized) return;

        AudioClip clip = ResourceManager.fetchAudio(asset);
        if (clip != null) clip.play(options == null ? 0.5 : options.getVolume());
    }

    /**
     * Used for testing only.
     * {@return array of audio files to be pre-loaded on game start}
     */
    static String[] getPreloadedAssets() {
        return PRELOADED_ASSETS;
    }

    /**
     * Used for testing only.
     * {@return the options for the currently running game instance}
     */
    static GameOptions getOptions() {
        return options;
    }

    /**
     * Sets the active game options. Used for testing only.
     *
     * @param options new options to set
     */
    static void setOptions(GameOptions options) {
        AudioManager.options = options;
    }

    /**
     * Used for testing only.
     * {@return {@code true} if audio manager is initialized, {@code false} if not}
     */
    static boolean isInitialized() {
        return initialized;
    }

    /**
     * Setter for initialized state. Used for testing only.
     *
     * @param initialized new initialized state to set
     */
    static void setInitialized(boolean initialized) {
        AudioManager.initialized = initialized;
    }
}
