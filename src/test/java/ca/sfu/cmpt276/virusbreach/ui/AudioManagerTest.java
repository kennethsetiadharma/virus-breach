package ca.sfu.cmpt276.virusbreach.ui;

import ca.sfu.cmpt276.virusbreach.GameOptions;
import javafx.scene.media.AudioClip;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
    void cannotConstruct() {
        assertThrows(UnsupportedOperationException.class, AudioManager::new);
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

        assertDoesNotThrow(() -> AudioManager.play("missing_test_sound.wav"));
    }

    @Test
    void playsDefaultVolumeForNullOptions() {
        AudioManager.setInitialized(true);
        AudioManager.setOptions(null);

        assertDoesNotThrow(() -> AudioManager.play("click.wav"));
    }

    @Test
    void playConfiguredVolume() {
        GameOptions options = new GameOptions();
        options.setVolume(0.4);
        AudioManager.setInitialized(true);
        AudioManager.setOptions(options);

        assertDoesNotThrow(() -> AudioManager.play("click.wav"));
    }

    private static void resetState() {
        AudioManager.setOptions(null);
        AudioManager.setInitialized(false);
    }
}
