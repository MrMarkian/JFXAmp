package com.JfXAmp;

public class LibraryItem {

    Integer runtime;
    String Title;
    String Filepath;

    public LibraryItem(Integer runtime, String title, String filepath) {
        this.runtime = runtime;
        Title = title;
        Filepath = filepath;
    }

    public LibraryItem(){

    }

    @Override
    public String toString() {
        return Filepath;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getFilepath() {
        return Filepath;
    }

    public void setFilepath(String filepath) {
        Filepath = filepath;
    }
}
