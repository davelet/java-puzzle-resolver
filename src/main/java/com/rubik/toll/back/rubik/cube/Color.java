package com.rubik.toll.back.rubik.cube;

public enum Color {
    WHITE("白"),
    YELLOW("黄"),
    RED("红"),
    ORANGE("橙"),
    BLUE("蓝"),
    GREEN("绿");

    private final String colorChar;

    Color(String colorChar) {
        this.colorChar = colorChar;
    }

    public String getColorChar() {
        return colorChar;
    }

    @Override
    public String toString() {
        return colorChar;
    }
}