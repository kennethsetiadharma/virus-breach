package ca.sfu.cmpt276.group15.ui;

import ca.sfu.cmpt276.group15.HackingGame;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.net.URL;

public class AudioManager {
    public static void play(String asset) {
        Thread.startVirtualThread(() -> playClip(asset));
    }

    private static void playClip(String asset) {
        URL resource = HackingGame.class.getResource("/sounds/" + asset);
        if (resource == null) {
            return;
        }

        try (AudioInputStream stream = AudioSystem.getAudioInputStream(resource)) {
            Clip clip = AudioSystem.getClip();
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
            clip.open(stream);
            clip.start();
        } catch (Exception ignored) {
        }
    }
}
