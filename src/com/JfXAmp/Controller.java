package com.JfXAmp;

import com.JfXAmp.Controllers.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import javafx.scene.media.AudioSpectrumListener;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Controller extends Application implements Initializable {

    WindowController wndController = new WindowController();

    @FXML private CheckBox playListCheck;
    @FXML public Label fileLabel;
    @FXML private Label artistLabel;
    @FXML private Label titleLabel;

    @FXML private Label albumLabel;
    @FXML private Label yearLabel;

    @FXML private Slider volumeSlider;

    @FXML private Slider speedSlider;

    @FXML private Slider seekSlider;

    @FXML private ImageView albumImage;

    @FXML private Label genreLabel;

    @FXML private Button stopButton;
    @FXML private Button playButton;
    @FXML private Button setRateButton;

    @FXML private Button eqButton;

    @FXML private Button addFilesButton;


    public AudioSpectrumListener audioListner;
    private final LibraryService libraryService = new LibraryService();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void ChooseFile(ActionEvent event) throws MalformedURLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pick an MP3");
        File file = fileChooser.showOpenDialog(((MenuItem)event.getTarget()).getParentPopup().getScene().getWindow());

        MediaController.loadMedia(file.toURI().toURL().toString());
        MediaController.playMedia();

    }


    public void showEq(){

        Equaliser eqWindow = new Equaliser(MediaController.playerReference()) ;
        eqWindow.Display();

    }


    public void setVolume(){
       MediaController.SetVolume(volumeSlider.getValue() / 100);
    }

    public void speedMeUp(){
    if(MediaController.playerReference() != null) {
        MediaController.setPlaybackRate(speedSlider.getValue());
        MediaController.playspeed = speedSlider.getValue();
    }
    }

    @Override
    public void start(Stage stage) throws Exception {
        MediaController.bindVolume(volumeSlider);
    }


    public void updateTimeLabel(String text){
        fileLabel.setText(text);
    }


    public void stopButtonPressed(){
        MediaController.stopMedia();
    }
    public void playButtonPressed(){
        MediaController.continuePlayback();
    }

    public void updateAllValues(){
        artistLabel.setText(MediaController.getArtist());
        titleLabel.setText(MediaController.getTitle());
        albumLabel.setText(MediaController.getAlbum());
        yearLabel.setText(MediaController.getYear());
        genreLabel.setText(MediaController.getGenre());
        seekSlider.setMax(MediaController.playerReference().getTotalDuration().toMillis());

    }

    public void updatePlayTime(){
        fileLabel.setText(formatDuration(MediaController.getNowTime()) + " / " + formatDuration(MediaController.getLengthInTime()));

    }

    public void initTimeSeeker(){
        seekSlider.valueProperty().addListener((observable, oldvalue, newvalue) -> sliderSeek(oldvalue, newvalue));
    }

    public void sliderSeek(Number oldvalue, Number newvalue) {
        MediaController.Seek(newvalue.doubleValue());
    }

    public void sliderSeek(){
        if(!seekSlider.isValueChanging()){
        MediaController.Seek(seekSlider.getValue());}
    }

    public void showVisuals() throws Exception {
        Visualisation visWindow = new Visualisation();
        visWindow.start(new Stage());
    }

    public void updateSeekSlider(Duration oldDuration, Duration newDuration){
        if(!seekSlider.isValueChanging()) {
            seekSlider.setValue(newDuration.toMillis());
        }
    }

    private String formatDuration(Duration duration) {
        double millis = duration.toMillis();
        int seconds = (int) (millis / 1000) % 60;
        int minutes = (int) (millis / (1000 * 60));
        return String.format("%02d:%02d", minutes, seconds);
    }


    public void CreatePlWindow(ActionEvent actionEvent) {

        wndController.StartPlayListWindow(WindowTypes.NewWindow);
    }

    public void CreateEQWindow(ActionEvent actionEvent) {
        wndController.StartEQWindow();
    }

    public void CreateVSWindow(ActionEvent actionEvent) {
    }
}
