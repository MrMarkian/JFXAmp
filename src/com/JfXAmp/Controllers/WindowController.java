package com.JfXAmp.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.List;

public class WindowController {

    private final ObservableList<Stage> activeWindowList = FXCollections.observableArrayList();

    public WindowController() {
        ShowWindowManager();
    }

    public void StartPlayListWindow(WindowTypes windowType){
        Playlist plWindow = new Playlist();
        plWindow.initialiseWindow(WindowTypes.NewWindow);
        plWindow.AttachWindowController(this);
        activeWindowList.add(plWindow.GetStage());;
    }

    public void StartEQWindow() {
        Equaliser equaliser = new Equaliser(MediaController.playerReference());
        equaliser.Display();
    }

    public void CloseME(Stage windowToClose){
        windowToClose.close();
        activeWindowList.remove(windowToClose);
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
        ListView<Stage> WindowList = new ListView<>();

        WindowList.setItems(activeWindowList);
        hBox.getChildren().add(WindowList);

        Scene scene = new Scene(hBox);
        WindowManagerWindow.setScene(scene);
        WindowManagerWindow.show();

    }

}
