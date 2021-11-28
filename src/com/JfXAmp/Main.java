package com.JfXAmp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("View/MainWindow.fxml"));

        primaryStage.setTitle("JFXAmp - V0.1 - Markian Rutkowskyj");
        primaryStage.setScene(new Scene(root, 800, 500));

        primaryStage.setMinHeight(200);
        primaryStage.setMinWidth(400);

        primaryStage.show();



    }


    public static void main(String[] args) {
        launch(args);
    }
}
