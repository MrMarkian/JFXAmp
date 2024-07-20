package com.JfXAmp.Controllers;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import static java.lang.Math.*;

public class Visualisation extends Application implements AudioSpectrumListener, BaseWindow {

    private GraphicsContext graphicsContext;

    private double t = 0.0;
    private double oldX = 400, oldY = 300;

    private float[] spectrumData;

    private final Canvas canvas = new Canvas(1280, 1200);
    private final Slider updateInterval = new Slider(0.01,1,0.1);

    GridPane gridPane = new GridPane();

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createUI()));
        stage.show();
        normalise(1);
    }

    @Override
    public void Init() {

    }

    public Group createUI() {

        VBox root = new VBox();
        MediaController.requestSpectrum(this);

        if( MediaController.currentSpectrum != null){

            updateInterval.setOnMouseReleased(e -> {
                updateDrawSpeed();
            });
            root.setPrefSize(800, 600);


            graphicsContext = canvas.getGraphicsContext2D();
            AnimationTimer timer = new AnimationTimer() {
                @Override
                public void handle(long l) {
                    t += 0.0107;
                    //  draw(canvas);
                }


            };

            timer.start();
            updateInterval.setDisable(false);
            root.getChildren().add(updateInterval);
            root.getChildren().add(canvas);
        } else {
            Label failLabel = new Label("Failed to Aquire Visualisation Data.\n Please start media, before starting a visualisation");
            root.getChildren().add(failLabel);
        }
        gridPane.add(root,0,0);
        Group g = new Group();
        g.getChildren().add(gridPane);
            return g;
    }

    @Override
    public void Close() {

    }

    private void updateDrawSpeed() {
        MediaController.playerReference().setAudioSpectrumInterval(updateInterval.getValue());

    }

    private void draw() {
        graphicsContext.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
       Point2D p = curveFunction();

        graphicsContext.setStroke(Color.BLUE);

        double newX = 400 + p.getX();
        double newY = 300 + p.getY();

        if(oldX != 400 && oldY != 300){
            graphicsContext.strokeLine(oldX,oldY,newX,newY);
        }
        oldX = newX;
        oldY = newY;

        graphicsContext.setStroke(Color.BLUE);

        double xPos = 10.0;
        if(spectrumData != null) {
            for (float f :
                    spectrumData) {

                f = normalise(f) * 120;

                if (f < 20) {
                    graphicsContext.setFill(Color.GREEN);
                }
                if (f > 20 && f < 40) {
                    graphicsContext.setFill(Color.GREENYELLOW);
                }
                if (f > 40 && f < 60) {
                    graphicsContext.setFill(Color.INDIGO);
                }
                if (f > 60) {
                    graphicsContext.setFill(Color.FIREBRICK);
                }

                graphicsContext.fillRect(xPos,20,8, f);
                //graphicsContext.strokeLine(xPos, 0, xPos, normalise(f) * 120);
                xPos+=10;
            }
        }
        xPos=10;
    }

    private Point2D curveFunction() {
        double x = sin(t) * (pow(E,cos(t)) - 2 * cos(4 + normalise(spectrumData[0])*t) - pow(sin(t/12),5));
        double y = cos(t) * (pow(E,cos(t)) - 2 * cos(4*t) - pow(sin(t/12),5));
        return new Point2D(x,y).multiply(80);
    }

    @Override
    public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
        spectrumData = magnitudes;
        draw();
    }

    public float normalise(float number){
        return ((120 + number) / 120);
    }
}
