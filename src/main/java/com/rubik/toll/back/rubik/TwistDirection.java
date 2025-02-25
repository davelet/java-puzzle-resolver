package com.rubik.toll.back.rubik;

public enum TwistDirection {
    CLOCKWISE("+"),
    COUNTERCLOCKWISE("-");

    private final String mark;
    TwistDirection(String mark) {
        this.mark = mark;
    }

    public static TwistDirection of(boolean isClockwise) {
        return isClockwise ? CLOCKWISE : COUNTERCLOCKWISE;
    }

    public boolean isClockwise() {
        return this == CLOCKWISE;
    }

    @Override
    public String toString() {
        return mark;
    }
}
