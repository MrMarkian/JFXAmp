package com.JfXAmp;

import javafx.stage.FileChooser;

import java.io.*;
import java.util.*;

public class LibraryService {

    public List<LibraryItem> currentPlaylist;

    //:Todo  -- Needs to handle relative locations

    public void handleM3ULoadRequest() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pick an M3U");

        List<File> fileList = fileChooser.showOpenMultipleDialog(null);



        for (File file: fileList) {

            for (LibraryItem item: loadM3u(file)) {
                try{
                    currentPlaylist.add(item);}
                catch (Exception e)
                { System.out.println(e.getMessage());}
            }
        }

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

