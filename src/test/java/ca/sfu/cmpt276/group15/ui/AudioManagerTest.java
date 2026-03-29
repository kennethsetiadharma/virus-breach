package ca.sfu.cmpt276.group15.ui;

import ca.sfu.cmpt276.group15.GameOptions;
import javafx.scene.media.AudioClip;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AudioManagerTest {
    private String[] originalPreloadedAssets;

    @BeforeEach
    void setUp() {
        this.originalPreloadedAssets = AudioManager.getPreloadedAssets().clone();
        resetState();
    }

    @AfterEach
    void tearDown() {
        String[] currentAssets = AudioManager.getPreloadedAssets();
        System.arraycopy(this.originalPreloadedAssets, 0, currentAssets, 0, currentAssets.length);
        resetState();
    }

    @Test
    void constructorExists() {
        assertNotNull(new AudioManager());
    }

    @Test
    void initReturnsIfAlreadyInitialized() {
        GameOptions options = new GameOptions();
        AudioManager.setInitialized(true);

        AudioManager.init(options);

        assertTrue(AudioManager.isInitialized());
        assertSame(options, AudioManager.getOptions());
    }

    @Test
    void initFailsForMissingClip() {
        AudioManager.getPreloadedAssets()[0] = "missing_test_sound.wav";

        AudioManager.init(new GameOptions());

        assertFalse(AudioManager.isInitialized());
    }

    @Test
    void playWithoutInitialization() {
        assertDoesNotThrow(() -> AudioManager.play("click.wav"));
    }

    @Test
    void playReturnsForMissingClip() {
        AudioManager.setInitialized(true);
        AudioManager.setOptions(new GameOptions());
        AudioManager.getClips().put("missing.wav", null);

        assertDoesNotThrow(() -> AudioManager.play("missing.wav"));
    }

    @Test
    void playsDefaultVolumeForNullOptions() {
        AudioClip clip = AudioManager.getClip("click.wav");
        AudioManager.setInitialized(true);
        AudioManager.setOptions(null);

        assertNotNull(clip);
        assertDoesNotThrow(() -> AudioManager.play("click.wav"));
    }

    @Test
    void playConfiguredVolume() {
        AudioClip clip = AudioManager.getClip("click.wav");
        GameOptions options = new GameOptions();
        options.setVolume(0.4);
        AudioManager.setInitialized(true);
        AudioManager.setOptions(options);

        assertNotNull(clip);
        assertDoesNotThrow(() -> AudioManager.play("click.wav"));
    }

    @Test
    void cacheMissingClip() {
        assertNull(AudioManager.getClip("missing_test_sound.wav"));
        assertTrue(AudioManager.getClips().containsKey("missing_test_sound.wav"));
        assertNull(AudioManager.getClip("missing_test_sound.wav"));
    }

    private static void resetState() {
        AudioManager.getClips().clear();
        AudioManager.setOptions(null);
        AudioManager.setInitialized(false);
    }
}
