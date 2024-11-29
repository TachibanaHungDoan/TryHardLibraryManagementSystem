package com.example.libms;

import javafx.scene.media.AudioClip;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SoundButtonController {
    private static SoundButtonController instance;

    private final Map<String, AudioClip> soundCache = new HashMap<>();

    private SoundButtonController() {
    }

    public static SoundButtonController getInstance() {
        if (instance == null) {
            instance = new SoundButtonController();
        }
        return instance;
    }

    public void playSound(String soundFileName) {
        AudioClip sound = soundCache.computeIfAbsent(soundFileName, _ -> new AudioClip
                (Objects.requireNonNull(SoundButtonController.class.getResource("Sound/" + soundFileName)).toExternalForm()));
        sound.play();
    }

    public void alertSoundPlay() {
        playSound("alert.mp3");
    }

    public void playButtonClickSound1() {
        playSound("buttonClickSound1.mp3");
    }

    public void playButtonClickSound2() {
        playSound("buttonClickSound2.mp3");
    }

    public void bookFlipSound() {
        playSound("bookFlipSound.mp3");
    }

    public void bookshelfSound() {
        playSound("bookshelfSound.mp3");
    }

    public void deleteConfirm() {
        playSound("DeleteConfirm.mp3");
    }

    public void logOutSound() {
        playSound("logOutSound.mp3");
    }

    public void winGameSound() {
        playSound("victory.mp3");
    }

    public void loseGameSound() {
        playSound("loss.mp3");
    }
}
