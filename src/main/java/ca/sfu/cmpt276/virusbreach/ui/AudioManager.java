package ca.sfu.cmpt276.virusbreach.ui;

import ca.sfu.cmpt276.virusbreach.GameOptions;
import ca.sfu.cmpt276.virusbreach.VirusBreach;
import javafx.scene.media.AudioClip;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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

    private static final Map<String, AudioClip> clips = new HashMap<>();
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
        if (initialized) {
            return;
        }

        for (String asset : PRELOADED_ASSETS) {
            getClip(asset);
        }
        
        // play silent clip (reduces lag on first real clip play)
        AudioClip clip = clips.get(PRELOADED_ASSETS[0]);
        if (clip == null) {
            return;
        }

        clip.play(0.0001);

        initialized = true;
    }

    /**
     * Play the specified audio clip
     * 
     * @param asset audio clip asset path
     */
    public static void play(String asset) {
        if (!initialized) return;

        AudioClip clip = getClip(asset);
        if (clip == null) {
            return;
        }

        clip.play(options == null ? 1.0 : options.getVolume());
    }

    /**
     * Fetches audio clip from map, or loads it to the map
     * 
     * @param asset audio clip asset path
     * @return the audio clip, or null if not found
     */
    public static AudioClip getClip(String asset) {
        if (clips.containsKey(asset)) {
            return clips.get(asset);
        }

        URL resource = VirusBreach.class.getResource("/sounds/" + asset);
        AudioClip clip = resource == null ? null : new AudioClip(resource.toExternalForm());
        clips.put(asset, clip);
        return clip;
    }

    /**
     * Getter for preloaded assets
     * 
     * @return list of preloaded asset paths
     */
    static String[] getPreloadedAssets() {
        return PRELOADED_ASSETS;
    }

    /**
     * Getter for clips map
     * 
     * @return audio clips map
     */
    static Map<String, AudioClip> getClips() {
        return clips;
    }

    /**
     * Getter for options
     * 
     * @return game options
     */
    static GameOptions getOptions() {
        return options;
    }

    /**
     * Setter for options
     * 
     * @param options new options to set
     */
    static void setOptions(GameOptions options) {
        AudioManager.options = options;
    }

    /**
     * Getter for initialized state
     * 
     * @return true if audio manager is initialized, false if not
     */
    static boolean isInitialized() {
        return initialized;
    }

    /**
     * Setter for initialized state
     * 
     * @param initialized new initialized state to set
     */
    static void setInitialized(boolean initialized) {
        AudioManager.initialized = initialized;
    }
}
