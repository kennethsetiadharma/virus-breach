package ca.sfu.cmpt276.virusbreach.ui;

import ca.sfu.cmpt276.virusbreach.GameOptions;
import javafx.scene.media.MediaPlayer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
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

    /**
     * With no music running, pausing music does nothing.
     */
    @Test
    void pauseNoMusic() {
        assertDoesNotThrow(AudioManager::pauseMusic);
    }

    /**
     * With no music running, resuming music does nothing.
     */
    @Test
    void resumeNoMusic() {
        assertDoesNotThrow(AudioManager::resumeMusic);
    }

    /**
     * With no music running, setting music volume does nothing.
     */
    @Test
    void setMusicVolumeNoMusic() {
        assertDoesNotThrow(() -> AudioManager.setMusicVolume(0.0));
    }

    /**
     * Music can be played.
     */
    @Test
    void playMusic() {
        AudioManager.playMusic("menu_music.wav");
        assertNotNull(AudioManager.musicPlayer);
    }

    /**
     * Trying to play a music file that does not exist should not crash the game.
     */
    @Test
    void playMusicMissing() {
        assertDoesNotThrow(() -> AudioManager.playMusic("missing_asset.wav"));

        assertNull(AudioManager.musicPlayer);
    }

    /**
     * Playing a non-music file as music should not crash the game.
     */
    @Test
    void playMusicInvalid() {
        assertDoesNotThrow(() -> AudioManager.playMusic("../sprites/antivirus.png"));

        assertNull(AudioManager.musicPlayer);
    }

    /**
     * Music volume should be kept in the range [0.0, 1.0]
     */
    @Test
    void setMusicVolumeClamp() {
        AudioManager.playMusic("menu_music.wav");
        assertNotNull(AudioManager.musicPlayer);

        assertDoesNotThrow(() -> AudioManager.setMusicVolume(10.0));
        assertEquals(1.0, AudioManager.musicPlayer.getVolume());
        assertDoesNotThrow(() -> AudioManager.setMusicVolume(-10.0));
        assertEquals(0.0, AudioManager.musicPlayer.getVolume());
    }

    /**
     * If the same music is requested to start, it should continue playing.
     */
    @Test
    void keepActiveMusic() {
        AudioManager.playMusic("menu_music.wav");
        MediaPlayer player = AudioManager.musicPlayer;
        assertNotNull(player);
        AudioManager.playMusic("menu_music.wav");
        assertSame(player, AudioManager.musicPlayer);
    }

    private static void resetState() {
        AudioManager.setOptions(null);
        AudioManager.setInitialized(false);
        AudioManager.stopMusic();
    }
}
