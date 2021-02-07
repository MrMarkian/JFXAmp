package com.JfXAmp.Controllers;

import com.JfXAmp.LibraryItem;
import com.JfXAmp.LibraryService;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.net.URI;

public class Playlist implements WindowMNGR {

    private Stage window;
    private final LibraryService plLibrary = new LibraryService();

    private WindowController windowController;

    VBox vBox = new VBox();
    HBox hBox = new HBox();

    public Playlist(){

    }

    private void createUI() {
        vBox.getChildren().clear();

        Button LoadM3UButton = new Button("Load M3U");
        LoadM3UButton.setOnAction(e -> {
            try {
                plLibrary.handleM3ULoadRequest();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        Button LoadFiles = new Button("Load Files");
        LoadFiles.setOnAction(e -> {
            try {
                plLibrary.handleFileLoadRequest();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        Button ClearList = new Button("Clear Playlist");
        ClearList.setOnAction(e-> {
            plLibrary.clearPlaylist();
        });

        ListView<LibraryItem> playListView = new ListView<>();

        playListView.setOnMouseClicked(e->{
            if(playListView.getSelectionModel().getSelectedItem() !=null) {
                File file = new File(playListView.getSelectionModel().getSelectedItem().toString());
                MediaController.loadMedia(file.toURI().toString());
                MediaController.playMedia();
            }
        });

        hBox.getChildren().add(LoadM3UButton);
        hBox.getChildren().add(LoadFiles);
        hBox.getChildren().add(ClearList);
        hBox.setFillHeight(true);

        vBox.getChildren().add(hBox);

        vBox.getChildren().add(playListView);
        vBox.setSpacing(5);
        vBox.setFillWidth(true);


        playListView.setItems(plLibrary.getObservablePlaylist());

        plLibrary.clearPlaylist();
    }

    @Override
    public void initialiseWindow(WindowTypes wndType) {
        switch (wndType){
            case NewWindow -> {
                window = new Stage(StageStyle.DECORATED);
                window.setTitle("Playlist");
                window.setMinHeight(400);
                window.setMinWidth(400);
                window.setHeight(400);
                window.setWidth(400);
                Scene scene = new Scene(vBox);
                createUI();
                window.setScene(scene);
                window.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent windowEvent) {
                        CloseWindow();
                    }
                });
                window.show();
            }

            case DockedWindow -> {

            }

            case ReuseWindow -> {

            }

            default -> {

            }
        }
    }

    @Override
    public Stage GetStage() {
        return window;
    }

    @Override
    public void CloseWindow() {
    windowController.CloseME(window);
    }

    @Override
    public void AttachWindowController(WindowController wndController) {
        windowController = wndController;
    }

    @Override
    public WindowController GetWindowController() {
        return windowController;
    }

    @Override
    public String GetWindowTitle() {
        return window.getTitle();
    }
}
