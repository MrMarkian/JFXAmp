package com.JfXAmp.Controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class WindowController {

    private final ObservableList<Scene> activeWindowList = FXCollections.observableArrayList();
    private MediaPlayer mediaPlayer;


    public WindowController(Boolean showWindowManager) {

        if(showWindowManager)
        ShowWindowManager();
    }

    public void StartPlayListWindow(WindowTypes windowType){
        Playlist pl = new Playlist();
        CreateWindow("Playlist Window V1", WindowTypes.NewWindow, pl.createUI());
    }
    public void StartEQWindow(WindowTypes windowTypes) {
        Equaliser eq = new Equaliser();
        CreateWindow("Eq Window - V1", WindowTypes.NewWindow, eq.createUI());
    }

    public void AddPlWindowToMain()
    {

    }

    public MediaPlayer getMediaPlayer(){return  mediaPlayer;}


    public void CreateWindow(String title, WindowTypes wndType, Group window ){

        switch (wndType){
            case NewWindow -> {
                Scene newScene = new Scene(window);
                Stage newWindow = new Stage(StageStyle.DECORATED);
                newWindow.setScene(newScene);
                newWindow.setTitle(title);
                newWindow.setMinHeight(400);
                newWindow.setMinWidth(400);
                newWindow.setHeight(400);
                newWindow.setWidth(400);
                newWindow.show();
                newWindow.setOnCloseRequest(e -> {
                    CloseWindow(newScene);
                });
                activeWindowList.add(newScene);
            }

            case DockedWindow -> {

            }
        }

    }

    public void CloseWindow(Scene wndToClose){

        Stage stage = (Stage) wndToClose.getWindow();
        stage.close();
        activeWindowList.remove(wndToClose);
    }



    public void ShowWindowManager()  {

        Stage WindowManagerWindow = new Stage(StageStyle.DECORATED);

        WindowManagerWindow.setTitle("Window Manager");
        WindowManagerWindow.setMinHeight(400);
        WindowManagerWindow.setMinWidth(400);
        WindowManagerWindow.setHeight(400);
        WindowManagerWindow.setWidth(400);

        HBox hBox = new HBox();
        hBox.setFillHeight(true);
        ListView<Scene> WindowList = new ListView<>();
        Button closeWindowButton = new Button("Close Window");
        closeWindowButton.setOnAction(e->{
            CloseWindow(WindowList.getSelectionModel().getSelectedItem());

        });

        WindowList.setItems(activeWindowList);
        hBox.getChildren().add(WindowList);
        hBox.getChildren().add(closeWindowButton);

        Scene scene = new Scene(hBox);
        WindowManagerWindow.setScene(scene);
        WindowManagerWindow.show();

    }

}
