package com.JfXAmp;

import com.JfXAmp.Controllers.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.media.AudioSpectrumListener;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller extends Application implements Initializable {

    WindowController wndController = new WindowController(true);

    @FXML public Label fileLabel;
    @FXML private Label artistLabel;
    @FXML private Label titleLabel;

    @FXML private Label albumLabel;
    @FXML private Label yearLabel;

    @FXML private ImageView albumImage;

    @FXML private Label genreLabel;
    @FXML private Accordion AccordionArea;



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


    @Override
    public void start(Stage stage) throws Exception {

    }


    public void updateAllValues(){
        artistLabel.setText(MediaController.getArtist());
        titleLabel.setText(MediaController.getTitle());
        albumLabel.setText(MediaController.getAlbum());
        yearLabel.setText(MediaController.getYear());
        genreLabel.setText(MediaController.getGenre());

    }

    public void updatePlayTime(){
        fileLabel.setText(formatDuration(MediaController.getNowTime()) + " / " + formatDuration(MediaController.getLengthInTime()));

    }

    private String formatDuration(Duration duration) {
        double millis = duration.toMillis();
        int seconds = (int) (millis / 1000) % 60;
        int minutes = (int) (millis / (1000 * 60));
        return String.format("%02d:%02d", minutes, seconds);
    }


    public void CreatePlWindow(ActionEvent actionEvent) { wndController.StartPlayListWindow(WindowTypes.NewWindow);
    }

    public void CreateControlsWindow (ActionEvent actionEvent){ wndController.StartControllsWindow(WindowTypes.NewWindow);
    }

    public void CreateEQWindow(ActionEvent actionEvent) {
        wndController.StartEQWindow(WindowTypes.NewWindow);
    }

    public void CreateVSWindow(ActionEvent actionEvent) {
        wndController.StartVSWindow(WindowTypes.NewWindow);
    }

    public void DockPlayListWindow(ActionEvent actionEvent){
        Playlist pl = new Playlist();
        TitledPane plTitledPane = new TitledPane();
        plTitledPane.setText("PlayList");

        TilePane titlePane = new TilePane(pl.createUI());
       // titlePane.setMaxWidth(Double.MAX_VALUE);
       // titlePane.setMaxHeight(Double.MAX_VALUE);
        plTitledPane.setContent(titlePane);

        AccordionArea.getPanes().add(plTitledPane);


    }

    public void DockEqWindow(ActionEvent actionEvent){
        Equaliser eq = new Equaliser();
        TitledPane plTitledPane = new TitledPane("Equaliser", eq.createUI());
        AccordionArea.getPanes().add(plTitledPane);
    }

    public void DockVisWindow(ActionEvent actionEvent){
        Visualisation vs = new Visualisation();
        TitledPane vsTitledPane = new TitledPane("Visualisation", vs.createUI());
        AccordionArea.getPanes().add(vsTitledPane);
    }

    public void DockControlsWindow(ActionEvent actionEvent){
        Controlls controlls = new Controlls();
        TitledPane cntTitlePane = new TitledPane("Playback Controls", controlls.createUI());
        AccordionArea.getPanes().add(cntTitlePane);
    }

}
