package com.JfXAmp.Controllers;

import com.JfXAmp.Controller;
import com.JfXAmp.Controllers.Equaliser;
import com.JfXAmp.LibraryItem;
import com.JfXAmp.LibraryService;
import com.JfXAmp.Model.AudioEngine;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Slider;
import javafx.scene.media.*;
import javafx.util.Duration;

import java.io.File;

public class MediaController {

    private static Media media;
    private static MediaPlayer mediaPlayer;
    private static String Album, Artist, Title, Year, Genre;

    public static AudioSpectrumListener currentSpectrum;

    static protected ObservableList<EqualizerBand> currentEqSettings = FXCollections.observableArrayList(new EqualizerBand());
    static protected Double currentVolume = 1.0d;

    private static AudioEngine currentAudioEngine = AudioEngine.JavaFX;

    private static final LibraryService libraryService = new LibraryService();

    public MediaController() {

    }

    public static LibraryService getLibraryService()
    {
        return libraryService;
    }

    public static void loadMedia(String file) {
        if (file.substring(file.length() - 4).equals(".mid")) {
            currentAudioEngine = AudioEngine.JavaMIDI;
        }
        media = new Media(file);

    }

    public static void stopMedia() {
        mediaPlayer.stop();
    }

    public static void continuePlayback() {
        mediaPlayer.play();
    }

    public static void setPlaybackRate(Double rate) {
        mediaPlayer.setRate(rate);
    }

    public static Duration getNowTime() {
        return mediaPlayer.getCurrentTime();
    }

    public static Duration getLengthInTime() {
        return mediaPlayer.getCycleDuration();
    }

    public static AudioEqualizer getEq() {
        return mediaPlayer.getAudioEqualizer();
    }

    public static double playspeed = 1;

    public static ObservableList<EqualizerBand> getcurrentEqSettings() {
        return currentEqSettings;
    }

    public static Double getcurrentVolume(){
        return currentVolume;
    }

    //todo: use various audio engines to improve playback options. including midi data

    public static void playMedia() {

        switch (currentAudioEngine) {

            case JavaFX -> {
                if (mediaPlayer != null) {
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
                                                    public void onChanged(Change<? extends String, ?> ch) {
                                                        if (ch.wasAdded()) {
                                                            handleMetadata(ch.getKey(), ch.getValueAdded());
                                                        }
                                                    }
                                                }
                );

                mediaPlayer.setAudioSpectrumNumBands(96);
                mediaPlayer.setAudioSpectrumThreshold(-120);
                mediaPlayer.setAudioSpectrumInterval(0.01);
/*        mediaPlayer.setAudioSpectrumListener((timestamp, duration, magnitudes,phases) -> {
            cont.updateVUMeters(magnitudes);
        });*/
                //     mediaPlayer.setAudioSpectrumInterval(1d/30d);

                mediaPlayer.setOnReady(new Runnable() {
                    public void run() {

                        if (Equaliser.getBands() != null) {
                            mediaPlayer.getAudioEqualizer().getBands().clear();
                            mediaPlayer.getAudioEqualizer().getBands().addAll(Equaliser.getBands());
                        }
                        mediaPlayer.setAudioSpectrumListener(currentSpectrum);
                        mediaPlayer.setRate(playspeed);
                        mediaPlayer.play();
                    }
                });

                mediaPlayer.setOnEndOfMedia(new Runnable() {
                    @Override
                    public void run() {

                        LibraryItem nextMediaToPlay = libraryService.RequestNextMedia();
                        File file = new File(nextMediaToPlay.getFilepath());
                        MediaController.getLibraryService().RegisterPlayback(nextMediaToPlay);
                        MediaController.loadMedia(file.toURI().toString());
                        MediaController.playMedia();

                    }
                });

            }
            case JavaMIDI -> {

            }
        }

    }

    //todo: Multiplex the spectrum data to allow multiple visualisations
    public static boolean requestSpectrum(AudioSpectrumListener spectrumListener) {

        if (mediaPlayer != null) {
            mediaPlayer.setAudioSpectrumListener(spectrumListener);
            currentSpectrum = spectrumListener;
            return true;
        } else return false;
    }

    public static void bindVolume(Slider volumeSlider) {
        mediaPlayer.volumeProperty().bindBidirectional(volumeSlider.valueProperty());
    }

    public static String getMediaData() {
        String data = "";
        data = mediaPlayer.getMedia().getDuration().toString();
        return data;
    }

    public static Duration getCurrentPos() {

        return mediaPlayer.getCurrentTime();
    }

    public static String getArtist() {
        return Artist;
    }

    public static void SetVolume(Double volume) {
        currentVolume = volume;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }

    }

    public static Double getVolume() {
        return currentVolume;
    }

    public static String getTitle() {
        return Title;
    }

    public static String getYear() {
        return Year;
    }

    public static String getAlbum() {
        return Album;
    }

    public static String getGenre() {
        return Genre;
    }

    private static void handleMetadata(String key, Object value) {
        if (key.equals("album")) {
            Album = value.toString();
        } else if (key.equals("artist")) {
            Artist = value.toString();
        }
        if (key.equals("title")) {
            Title = value.toString();
        }
        if (key.equals("year")) {
            Year = value.toString();
        }
        if (key.equals("genre")) {
            Genre = value.toString();
        }
    }

    public static MediaPlayer playerReference() {
        return mediaPlayer;
    }

    public static void Seek(double doubleValue) {
        mediaPlayer.seek(Duration.millis(doubleValue));
    }

}
