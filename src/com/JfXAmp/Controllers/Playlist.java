package com.JfXAmp.Controllers;

import com.JfXAmp.LibraryItem;
import com.JfXAmp.LibraryService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import java.io.File;
import java.io.IOException;

public class Playlist implements BaseWindow, ChangeListener<Number>, LibraryService.SelectedMediaListener {

    ListView<LibraryItem> playListView = new ListView<>();

    @Override
    public void Init() {

    }

    public Group createUI() {

        HBox hBox = new HBox();
        VBox vBox = new VBox();
        vBox.setPrefSize(800, 600);
        Button LoadM3UButton = new Button("Load M3U");

        LoadM3UButton.setOnAction(e -> {
            try {
                MediaController.getLibraryService().handleM3ULoadRequest();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        Button LoadFiles = new Button("Load Files");
        LoadFiles.setOnAction(e -> {
            try {
                MediaController.getLibraryService().handleFileLoadRequest();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        Button ClearList = new Button("Clear Playlist");
        ClearList.setOnAction(e-> {
            MediaController.getLibraryService().clearPlaylist();
        });



        playListView.setOnMouseClicked(e->{

            if (e.getClickCount() == 2){

                if(playListView.getSelectionModel().getSelectedItem() !=null) {
                    File file = new File(playListView.getSelectionModel().getSelectedItem().getFilepath());
                    MediaController.getLibraryService().RegisterPlayback(playListView.getSelectionModel().getSelectedItem());
                    MediaController.loadMedia(file.toURI().toString());
                    MediaController.playMedia();
                }
            }
        });

        playListView.setPrefWidth(vBox.getWidth());

        hBox.setSpacing(5);
        hBox.setPadding(new Insets(5,5,5,5));

        hBox.getChildren().add(LoadM3UButton);
        hBox.getChildren().add(LoadFiles);
        hBox.getChildren().add(ClearList);

        vBox.getChildren().add(hBox);
        vBox.getChildren().add(playListView);

        VBox.setVgrow(vBox,Priority.ALWAYS);

        if (MediaController.getLibraryService().getObservablePlaylist() != null) {
            playListView.setItems(MediaController.getLibraryService().getObservablePlaylist());
            MediaController.getLibraryService().clearPlaylist();
        }
/*        BorderPane mainBorderPane = new BorderPane();
        mainBorderPane.setTop(hBox);
        mainBorderPane.setCenter(playListView);
        mainBorderPane.setLayoutX(20);
        mainBorderPane.setLayoutY(30);*/

        Group g = new Group();
        g.setAutoSizeChildren(true);
        g.getChildren().add(vBox);
        return g;
    }

    @Override
    public void Close() {

    }



    @Override
    public void changed(ObservableValue<? extends Number> observableValue, Number oldWidth, Number newWidth) {
        double delta = newWidth.doubleValue() - oldWidth.doubleValue();


    }

    @Override
    public void onSelectionChange() {
       // playListView.getSelectionModel().select(MediaController.getLibraryService().get);
    }
}
