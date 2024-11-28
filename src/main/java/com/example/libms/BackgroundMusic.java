package com.example.libms;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class BackgroundMusic {
    private static BackgroundMusic instance;
    private MediaPlayer mediaPlayer;

    private BackgroundMusic() {}

    public static synchronized BackgroundMusic getInstance() {
        if (instance == null) {
            instance = new BackgroundMusic();
        }
        return instance;
    }

    public void toggleBackgroundMusic(String filePath) {
        if (mediaPlayer == null || mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED) {
            playBackgroundMusic(filePath);
        } else if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            stopBackgroundMusic();
        }
    }

    public void playBackgroundMusic(String filePath) {
        if (mediaPlayer == null) {
            URL resource = getClass().getResource(filePath);
            if (resource != null) {
                Media media = new Media(resource.toExternalForm());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Phát lại vô hạn
                mediaPlayer.play();
            } else {
                System.out.println("File không tồn tại: " + filePath);
            }
        }
    }

    public void stopBackgroundMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }
}
