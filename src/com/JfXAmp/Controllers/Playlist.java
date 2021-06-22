package com.JfXAmp.Controllers;

import com.JfXAmp.LibraryItem;
import com.JfXAmp.LibraryService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import java.io.File;
import java.io.IOException;

public class Playlist implements BaseWindow, ChangeListener<Number> {

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
        hBox.setPrefWidth(1200);
        hBox.setAlignment(Pos.TOP_LEFT);
        vBox.getChildren().add(hBox);

        vBox.getChildren().add(playListView);
        vBox.setSpacing(3);

        vBox.setAlignment(Pos.TOP_LEFT);
        vBox.setFillWidth(true);
        VBox.setVgrow(playListView,Priority.ALWAYS);
        playListView.setItems(plLibrary.getObservablePlaylist());

        plLibrary.clearPlaylist();

        Group g = new Group();
        g.getChildren().add(vBox);
        g.setAutoSizeChildren(true);

        return g;
    }

    @Override
    public void Close() {

    }


    @Override
    public void changed(ObservableValue<? extends Number> observableValue, Number oldWidth, Number newWidth) {
        double delta = newWidth.doubleValue() - oldWidth.doubleValue();


    }
}
