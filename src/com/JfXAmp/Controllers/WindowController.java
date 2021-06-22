package com.JfXAmp.Controllers;


import com.JfXAmp.Model.Theme;
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
import javafx.stage.Window;

import java.util.List;


public class WindowController {

    private final ObservableList<Window> activeWindowList = FXCollections.observableArrayList();
    private final ObservableList<Scene> activeSceneList = FXCollections.observableArrayList();
    private final ObservableList<Group> activeGroupList = FXCollections.observableArrayList();


    private final Equaliser eq = new Equaliser();

    private MediaPlayer mediaPlayer;
    private Theme theme = Theme.DARK;

    public WindowController(Boolean showWindowManager) {

        if(showWindowManager)
        ShowWindowManager();
    }

    public void StartPlayListWindow(WindowTypes windowType){
        Playlist pl = new Playlist();
        CreateWindow("Playlist Window V1", WindowTypes.NewWindow, pl.createUI());
    }
    public void StartEQWindow(WindowTypes windowTypes) {

        CreateWindow("Eq Window - V1", WindowTypes.NewWindow, eq.createUI());
    }

    public void StartVSWindow(WindowTypes windowTypes){
        Visualisation vs = new Visualisation();
        CreateWindow("Visualisation - V1", WindowTypes.NewWindow,vs.createUI());
    }

    public void DockPlaylistWindow(){
        Playlist pl = new Playlist();
        InjectPane(pl.createUI(),activeSceneList.get(0));
    }

    public void AddPlWindowToMain()
    {

    }

    public MediaPlayer getMediaPlayer(){return  mediaPlayer;}


    private void CreateWindow(String title, WindowTypes wndType, Group window ){

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
                activeSceneList.add(newScene);
                activeGroupList.add(window);
                activeWindowList.add(newWindow);

            }

            case DockedWindow -> {


            }
        }




    }

    private void InjectPane(Group window, Scene DockingWindow){



    }


    public void CloseWindow(Scene wndToClose){
        Stage stage = (Stage) wndToClose.getWindow();
        stage.close();
        activeSceneList.remove(wndToClose);
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
        Button dockWindowButton = new Button("Dock Window");
        closeWindowButton.setOnAction(e->{


            CloseWindow(WindowList.getSelectionModel().getSelectedItem());

        });

        WindowList.setItems(activeSceneList);
        hBox.getChildren().add(WindowList);
        hBox.getChildren().add(closeWindowButton);

        Scene scene = new Scene(hBox);
        WindowManagerWindow.setScene(scene);
        WindowManagerWindow.show();

    }

    //EQ Settings passback to MediaController

    public Equaliser getEq(){
        return eq;
    }

    //THEME SUPPORT - Expand

    public Theme getTheme() {return theme;}
    public void setTheme(Theme theme){
        this.theme = theme;
    }

    public void updateTheme(){

    }

}
