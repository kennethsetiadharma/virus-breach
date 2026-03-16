package ca.sfu.cmpt276.group15.ui;

import ca.sfu.cmpt276.group15.GameOptions;
import ca.sfu.cmpt276.group15.HackingGame;
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
    private static AudioClip getClip(String asset) {
        if (clips.containsKey(asset)) {
            return clips.get(asset);
        }

        URL resource = HackingGame.class.getResource("/sounds/" + asset);
        AudioClip clip = resource == null ? null : new AudioClip(resource.toExternalForm());
        clips.put(asset, clip);
        return clip;
    }
}
