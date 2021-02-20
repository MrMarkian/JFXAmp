package com.JfXAmp.Controllers;

import com.JfXAmp.LibraryItem;
import com.JfXAmp.LibraryService;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;

public class Playlist implements BaseWindow{

    private final LibraryService plLibrary = new LibraryService();


    @Override
    public void Init() {

    }

    public Group createUI() {

        VBox vBox = new VBox();
        HBox hBox = new HBox();

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

        Group g = new Group();
        g.getChildren().add(vBox);

        return g;
    }

    @Override
    public void Close() {

    }


}
