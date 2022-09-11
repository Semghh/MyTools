package com.semghh.ConsoleFormat;

public enum FontColor {

    BLACK(30), RED(31), GREEN(32), YELLOW(33),
    BLUE(34), PURPLE(35), DEEP_GREEN(36), WHITE(37);


    final int colorCode;

    FontColor(int colorCode) {
        this.colorCode = colorCode;
    }

}
