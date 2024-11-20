package com.example.libms;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Label;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeService extends Service<Void> {

    private final Label timeLabel;

    public TimeService(Label timeLabel) {
        this.timeLabel = timeLabel;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (!isCancelled()) {
                    // Cập nhật giờ mỗi giây
                    String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
                    javafx.application.Platform.runLater(() -> timeLabel.setText(currentTime));
                    Thread.sleep(1000); // Cập nhật mỗi giây
                }
                return null;
            }
        };
    }
}
