package com.JfXAmp.Model;

public enum Theme {
    LIGHT,
    DEFAULT,
    DARK;

    public static String getCssPath(Theme theme){

    return switch (theme) {
        case LIGHT -> "CSS/light.css";
        case DEFAULT -> "CSS/default.css";
        case DARK -> "CSS/dark.css";
    };
    }
}
