package com.JfXAmp.Controllers;

import com.JfXAmp.Controller;
import com.JfXAmp.Controllers.Equaliser;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Slider;
import javafx.scene.media.*;
import javafx.util.Duration;

public class MediaController {

    private static Media media;
    private static MediaPlayer mediaPlayer;

    private static String Album, Artist, Title, Year, Genre;

    private static Double currentVolume =1.0;
    public static AudioSpectrumListener currentSpectrum;
    static protected ObservableList<EqualizerBand> currentEqSettings = FXCollections.observableArrayList(new EqualizerBand());;

    private static Equaliser eqWindow;

    public MediaController() {

    }

    public static void loadMedia(String file){
        media = new Media(file);


    }

    public static void stopMedia(){
        mediaPlayer.stop();
    }

    public static void continuePlayback(){
        mediaPlayer.play();
    }

    public static void setPlaybackRate(Double rate){
        mediaPlayer.setRate(rate);
    }

    public static Duration getNowTime(){
        return mediaPlayer.getCurrentTime();
    }

    public static Duration getLengthInTime(){
        return mediaPlayer.getCycleDuration();
    }

    public static AudioEqualizer getEq(){
        return mediaPlayer.getAudioEqualizer();
    }
    public static double playspeed =1;


    public static ObservableList<EqualizerBand> getcurrentEqSettings() {return currentEqSettings;}


    public static void playMedia(){
        if(mediaPlayer != null) {
            mediaPlayer.stop();
        }
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(currentVolume);

       /* mediaPlayer.currentTimeProperty().addListener((observable -> {
            cont.updatePlayTime();
        }));
*/
        media.getMetadata().addListener(new MapChangeListener<String, Object>() {
            @Override
            public void onChanged(Change<? extends String, ? extends Object> ch) {
                if (ch.wasAdded()) {
                    handleMetadata(ch.getKey(), ch.getValueAdded());
                }
            }
        });



        mediaPlayer.setAudioSpectrumNumBands(96);
        mediaPlayer.setAudioSpectrumThreshold(-120);
        mediaPlayer.setAudioSpectrumInterval(0.01);
/*        mediaPlayer.setAudioSpectrumListener((timestamp, duration, magnitudes,phases) -> {
            cont.updateVUMeters(magnitudes);
        });*/
   //     mediaPlayer.setAudioSpectrumInterval(1d/30d);

        mediaPlayer.setOnReady(new Runnable() {
            public void run() {
              //  cont.updateAllValues();
//                mediaPlayer.currentTimeProperty().addListener((observable, oldDuration, newDuration)
//                        -> cont.updateSeekSlider(oldDuration, newDuration));
                Equaliser equaliser = new Equaliser(mediaPlayer);
                equaliser.loadEQ(mediaPlayer);
                equaliser.createUI(mediaPlayer);
                mediaPlayer.setAudioSpectrumListener(currentSpectrum);
                mediaPlayer.setRate(playspeed);
                mediaPlayer.play();


            }
        });


    }

    public static boolean requestSpectrum(AudioSpectrumListener spectrumListener){

        if(mediaPlayer != null) {
            mediaPlayer.setAudioSpectrumListener(spectrumListener);
            currentSpectrum = spectrumListener;
            return true;
        } else return false;
    }

    public static void bindVolume(Slider volumeSlider){
        mediaPlayer.volumeProperty().bindBidirectional(volumeSlider.valueProperty());
    }

    public static String getMediaData(){
        String data = "";
        data = mediaPlayer.getMedia().getDuration().toString();

        return data;
    }

    public static Duration getCurrentPos(){

        return mediaPlayer.getCurrentTime();
    }

    public static String getArtist(){
        return Artist;
    }

    public static void SetVolume(Double volume){
        currentVolume = volume;
        if(mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }

    }

    public static Double getVolume(){
        return mediaPlayer.getVolume();
    }

    public static String getTitle(){
        return Title;
    }

    public static String getYear(){
        return Year;
    }

    public static String getAlbum(){
        return Album;
    }

    public static String getGenre(){
        return Genre;
    }

    private static void handleMetadata(String key, Object value) {
        if (key.equals("album")) {
            Album = value.toString();
        } else if (key.equals("artist")) {
            Artist= value.toString();
        } if (key.equals("title")) {
            Title = value.toString();
        } if (key.equals("year")) {
            Year = value.toString();
        } if (key.equals("genre")) {
            Genre = value.toString();
        }
    }

    public static MediaPlayer playerReference(){
        return mediaPlayer;
    }

    public static void Seek(double doubleValue) {
        mediaPlayer.seek(Duration.millis(doubleValue));
    }
}
