package com.JfXAmp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.*;


public class LibraryService {
    List<LibraryItem> currentPlaylist = new ArrayList<LibraryItem>();
    public ObservableList<LibraryItem> observablePlaylist = FXCollections.observableArrayList(new LibraryItem());
    private LibraryItem currentPlayingMedia;
    private LibraryItem currentSelectedMedia;


    private Collection<SelectedMediaListener> selectedMediaListeners = new HashSet<>();


    public interface SelectedMediaListener extends EventListener{
        void onSelectionChange();
    }

    public void RegisterMediaSelectionListener(SelectedMediaListener listener){
        selectedMediaListeners.add(listener);

    }

    public void SelectedMediaChanges(){
        for (SelectedMediaListener listener:selectedMediaListeners ) {
            listener.onSelectionChange();
        }
    }


    //:Todo  -- Needs to handle relative locations


    public ObservableList<LibraryItem> getObservablePlaylist(){
        return observablePlaylist;
    }

    public void clearPlaylist(){observablePlaylist.clear();}

    public void handleFileLoadRequest() throws IOException{
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pick Media Files");
        List<File> fileList = fileChooser.showOpenMultipleDialog(null);
        for (File file: fileList){
            LibraryItem tmpItem = new LibraryItem(null,null,file.getAbsolutePath());
            observablePlaylist.add(tmpItem);
        }
    }

    public void handleM3ULoadRequest() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pick an M3U");

        List<File> fileList = fileChooser.showOpenMultipleDialog(null);

        for (File file: fileList) {

            for (LibraryItem item: loadM3u(file)) {
                try {
                    LibraryItem tmpItem = new LibraryItem(null, null, file.getAbsolutePath());
                    currentPlaylist.add(tmpItem);
                }
                catch (Exception e)
                { System.out.println(e.getMessage());}
            }
        }

    }

    public void RegisterPlayback (LibraryItem currentMedia){
        currentPlayingMedia = currentMedia;
    }

    public LibraryItem RequestNextMedia(){
        for (LibraryItem item: observablePlaylist ) {
            if( currentPlayingMedia == item){

               return observablePlaylist.get(observablePlaylist.indexOf(item) + 1);


            }
        }

        return new LibraryItem();
    }

    public List<LibraryItem> loadM3u(File path) throws IOException {
        List<LibraryItem> files = new ArrayList<>();

        BufferedReader bfReader = new BufferedReader(new FileReader(path));

        String currentLine;
        String [] splitString;

        if (bfReader.readLine().equals("#EXTM3U")){

            while ((currentLine = bfReader.readLine()) != null){
                splitString = currentLine.split("[:,]");
                if(splitString[0].equals("#EXTINF")){
                   files.add(new LibraryItem(Integer.parseInt(splitString[1]),splitString[2], bfReader.readLine() ));
                }
            }

        }
        return files;

    }
}

