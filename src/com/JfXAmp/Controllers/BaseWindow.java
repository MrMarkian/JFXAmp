package com.JfXAmp.Controllers;

import javafx.scene.Group;

public interface BaseWindow {

    WindowTypes wndType = null;

    void Init();
    Group createUI();
    void Close();

}
