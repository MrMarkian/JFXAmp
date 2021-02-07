package sample;

import java.io.*;
import java.util.*;

public class LibraryService {

    //:Todo  -- Needs to handle relative locations

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

