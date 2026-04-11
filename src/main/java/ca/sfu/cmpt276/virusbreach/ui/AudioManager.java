package ca.sfu.cmpt276.virusbreach.ui;

import ca.sfu.cmpt276.virusbreach.GameOptions;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

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
    static MediaPlayer musicPlayer = null;
    static String currentMusicAsset = null;

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
     * Starts looping background music. Does nothing if the same track is already playing.
     * Stops any different track that is currently playing before starting the new one.
     *
     * @param asset music file name in the sounds/ folder
     */
    public static void playMusic(String asset) {
        if (asset.equals(currentMusicAsset)) return;
        stopMusic();
        Media media = ResourceManager.fetchMusic(asset);
        if (media == null) return;
        currentMusicAsset = asset;
        musicPlayer = new MediaPlayer(media);
        musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        musicPlayer.setVolume(options == null ? 0.5 : options.getVolume());
        musicPlayer.play();
    }

    /**
     * Stops the currently playing background music track.
     */
    public static void stopMusic() {
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer.dispose();
            musicPlayer = null;
        }
        currentMusicAsset = null;
    }

    /**
     * Pauses the currently playing background music.
     */
    public static void pauseMusic() {
        if (musicPlayer != null) musicPlayer.pause();
    }

    /**
     * Resumes the background music if it was paused.
     */
    public static void resumeMusic() {
        if (musicPlayer != null) musicPlayer.play();
    }

    /**
     * Updates the music volume live (called when the options slider changes).
     *
     * @param volume volume in range [0.0, 1.0]
     */
    public static void setMusicVolume(double volume) {
        if (musicPlayer != null) musicPlayer.setVolume(Math.clamp(volume, 0.0, 1.0));
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
