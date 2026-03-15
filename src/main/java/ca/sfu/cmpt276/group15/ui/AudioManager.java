package ca.sfu.cmpt276.group15.ui;

import ca.sfu.cmpt276.group15.GameOptions;
import ca.sfu.cmpt276.group15.HackingGame;
import javafx.scene.media.AudioClip;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AudioManager {
    private static final String[] PRELOADED_ASSETS = {
        "bonus.wav",
        "caught.wav",
        "damage.wav",
        "reward.wav",
        "success.wav"
    };

    private static final Map<String, AudioClip> clips = new HashMap<>();
    private static GameOptions options;

    public static void setOptions(GameOptions options) {
        AudioManager.options = options;
        preload();
    }

    public static void play(String asset) {
        AudioClip clip = getClip(asset);
        if (clip == null) {
            return;
        }

        clip.play(options == null ? 1.0 : options.getVolume());
    }

    private static void preload() {
        for (String asset : PRELOADED_ASSETS) {
            getClip(asset);
        }
    }

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
