package com.JfXAmp.Controllers;

import javafx.stage.Stage;

public interface WindowMNGR {

    void initialiseWindow(WindowTypes wndType);
    Stage GetStage();
    void CloseWindow();
    void AttachWindowController(WindowController wndController);
    WindowController GetWindowController();
    String GetWindowTitle();

}
