package sample;

public class LibraryItem {
    public LibraryItem(int runtime, String title, String filepath) {
        this.runtime = runtime;
        Title = title;
        Filepath = filepath;
    }

    int runtime;
    String Title;
    String Filepath;

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
