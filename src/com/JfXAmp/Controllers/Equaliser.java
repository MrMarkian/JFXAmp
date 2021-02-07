package com.JfXAmp.Controllers;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioEqualizer;
import javafx.scene.media.EqualizerBand;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;


public class Equaliser {

    @FXML private  final GridPane gridPane = new GridPane();

    VBox vBox = new VBox();
    private final  Stage window;
    private float t = 0.0f;

    protected ObservableList<EqualizerBand> currentEqList = FXCollections.observableArrayList(new EqualizerBand());


    public Equaliser(MediaPlayer mediaPlayer) {

        window = new Stage(StageStyle.DECORATED);

        window.setTitle("Equaliser");
        window.setMinHeight(400);
        window.setMinWidth(400);
        window.setHeight(400);
        window.setWidth(400);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        Scene scene = new Scene(gridPane);
        createUI(mediaPlayer);
        window.setScene(scene);

    }

    public void Display() {

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (t <= 1.0f) {
                    gridPane.setOpacity(t);
                    t+= 0.01;
                } else {
                    this.stop();
                }



            }
        };
        timer.start();
        window.show();
    }

    public void updateControls()  {

    }


    public void loadEQ(MediaPlayer mediaPlayer) {
        final ObservableList<EqualizerBand> bands = mediaPlayer.getAudioEqualizer().getBands();



        if (MediaController.currentEqSettings.size() == 1 || MediaController.currentEqSettings == null ) {

            double min = EqualizerBand.MIN_GAIN;
            double max = EqualizerBand.MAX_GAIN;
            double mid = (max - min) / 2;
            double freq = 31.5;

            int bandCount = 10;

            boolean parametricEQ = true;

            if (parametricEQ){
                bands.clear();

                for (int count = 0; count < bandCount; count++){
                    bands.add(new EqualizerBand(freq, freq / 2 , 0));
                    freq *= 2;
                }
            }

    /*        bands.clear();


            for (int j = 0; j < bandCount; j++) {
                // Use j and BAND_COUNT to calculate a value between 0 and 2*pi
                double theta = (double) j / (double) (bandCount - 1) * (2 * Math.PI);

                // The cos function calculates a scale value between 0 and 0.4
                double scale = 0.4 * (1 + Math.cos(theta));

                // Set the gain to be a value between the midpoint and 0.9*max.
                double gain = min + mid + (mid * scale);

                bands.add(new EqualizerBand(freq, freq / 2 , gain));
                freq *= 2;
            }
         */
            MediaController.currentEqSettings.clear();
            MediaController.currentEqSettings.addAll(bands);


        } else
        {

            bands.clear();
            bands.addAll(MediaController.currentEqSettings);
        }
    }

     public void createUI(MediaPlayer mediaPlayer){


        gridPane.getChildren().clear();
        vBox.getChildren().clear();

        final AudioEqualizer equalizer;
        try {
           equalizer = mediaPlayer.getAudioEqualizer();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return;
        }

        Button loadButton = new Button("Load");
        loadButton.setOnAction(e-> {
            try {
                readSettingsFromText();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e->{
            try {
                writeSettingsToText();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        Button editButton = new Button("Edit");
        editButton.setOnAction(e -> {

        });

        vBox.getChildren().add(loadButton);
        vBox.getChildren().add(saveButton);
        vBox.getChildren().add(editButton);
        vBox.setSpacing(20);

        gridPane.add(vBox, 0, 0);
        final ObservableList<EqualizerBand> bands = equalizer.getBands();

        double min = -12;

        double max = 12 ;
        double mid = (max - min) / 2;
        double freq = 32;

        int bandCount = 10;

        for (int i = 0; i < bands.size(); ++i) {
            EqualizerBand eb = bands.get(i);
            Slider s = createEQSlider(eb, min, max);
            final Label l = new Label(formatFrequency(eb.getCenterFrequency()));
            final Label b = new Label(formatFrequency(eb.getBandwidth()));


            GridPane.setHalignment(l, HPos.CENTER);
            GridPane.setHalignment(b, HPos.CENTER);
            GridPane.setHalignment(s, HPos.CENTER);
            GridPane.setHgrow(s, Priority.ALWAYS);
            GridPane.setVgrow(s, Priority.ALWAYS);

            gridPane.add(l, i+1, 0);
            gridPane.add(b, i+1, 1);
            gridPane.add(s, i+1, 2);
        }


    }


    private static String formatFrequency(double centerFrequency) {
        if (centerFrequency < 1000) {
            return String.format("%.0f Hz", centerFrequency);
        } else {
            return String.format("%.1f kHz", centerFrequency / 1000);
        }
    }

    private static Slider createEQSlider(EqualizerBand eb, double min, double max) {
        final Slider s = new Slider(min, max, eb.getGain());
        s.setShowTickLabels(true);
        s.setShowTickMarks(true);
        s.setMajorTickUnit(3.00f);
        s.setMinorTickCount(1);
        s.setOrientation(Orientation.VERTICAL);
        s.valueProperty().bindBidirectional(eb.gainProperty());
        s.setPrefWidth(80);
        return s;
    }


    private void writeSettingsToText() throws IOException {

        FileChooser fileChooser = new FileChooser();

        File file = fileChooser.showSaveDialog(null);

        FileWriter fileWriter = new FileWriter(file);

        ObservableList<EqualizerBand> bands = MediaController.getcurrentEqSettings();

        for (EqualizerBand eqB : bands) {

            fileWriter.write(eqB.getCenterFrequency() + "," + eqB.getBandwidth() + "," + eqB.getGain()+"\n");
        }

        fileWriter.close();
    }

    private void readSettingsFromText() throws IOException {
        ObservableList<EqualizerBand> bands = MediaController.playerReference().getAudioEqualizer().getBands();

        FileChooser fileChooser = new FileChooser();

        File file = fileChooser.showOpenDialog(null);


        BufferedReader bfReader = new BufferedReader(new FileReader(file));

        String currentLine;

        bands.clear();

        while ((currentLine = bfReader.readLine()) != null){
            String[] stringSplit = currentLine.split(",");
            if(stringSplit.length == 3){
                bands.add(new EqualizerBand(Double.valueOf(stringSplit[0]),Double.valueOf(stringSplit[1]),Double.valueOf(stringSplit[2])));
            }
        }
        MediaController.currentEqSettings.clear();
        MediaController.currentEqSettings.addAll(bands);
        createUI(MediaController.playerReference());
    }

}
