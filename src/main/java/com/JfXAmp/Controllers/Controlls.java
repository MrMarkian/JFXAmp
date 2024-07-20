package com.JfXAmp.Controllers;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class Controlls extends Application implements BaseWindow {

    Slider progressSlider = new Slider();
    Slider volumeSlider = new Slider();


    @Override
    public void Init() {

    }

    @Override
    public Group createUI() {
        Group root = new Group();

        HBox hbox = new HBox( );
        Button playButton = new Button("Play");
        Button stopButton = new Button("Stop");
        Slider progressSlider = new Slider();
        Slider volumeSlider = new Slider();


        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MediaController.playMedia();
            }
        });

        stopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                MediaController.stopMedia();
            }
        });


        volumeSlider.setOrientation(Orientation.VERTICAL);
        volumeSlider.setMax(1);
        volumeSlider.setBlockIncrement(0.1);
        volumeSlider.setShowTickMarks(true);
        volumeSlider.setShowTickLabels(true);

        volumeSlider.setOnScroll(e-> {
            System.out.println(volumeSlider.getValue());
            MediaController.SetVolume(volumeSlider.getValue());
        });

        volumeSlider.setValue(MediaController.getVolume());

      //  MediaController.bindVolume(volumeSlider);

        hbox.getChildren().addAll(playButton,stopButton,progressSlider,volumeSlider);

        root.getChildren().add(hbox);
        return root;
    }

    @Override
    public void Close() {

    }

    public Slider getVolumeSlider(){
        return volumeSlider;
    }

    @Override
    public void start(Stage stage) throws Exception {
        MediaController.bindVolume(volumeSlider);
    }
}
