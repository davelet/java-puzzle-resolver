package com.rubik.toll.back.rubik.cube;

public enum Color {
    WHITE("白", "\u001B[37m"),
    YELLOW("黄", "\u001B[33m"),
    RED("红", "\u001B[31m"),
    ORANGE("橙", "\u001B[38;5;208m"),
    BLUE("蓝", "\u001B[34m"),
    GREEN("绿", "\u001B[32m");

    private final String colorChar;
    private final String ansiColor;
    private static final String RESET = "\u001B[0m";

    Color(String colorChar, String ansiColor) {
        this.colorChar = colorChar;
        this.ansiColor = ansiColor;
    }

    public String getColorChar() {
        return ansiColor + colorChar + RESET;
    }

    @Override
    public String toString() {
        return getColorChar();
    }
}