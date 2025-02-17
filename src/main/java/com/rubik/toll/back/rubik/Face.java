package com.rubik.toll.back.rubik;

/**
 * 魔方六个面的标准颜色配置
 * <p>
 * 标准魔方的颜色配置如下：
 * - 上面（UP）：黄色（YELLOW）
 * - 下面（DOWN）：白色（WHITE）
 * - 前面（FRONT）：蓝色（BLUE）
 * - 后面（BACK）：绿色（GREEN）
 * - 左面（LEFT）：橙色（ORANGE）
 * - 右面（RIGHT）：红色（RED）
 */
public enum Face {
    UP(Color.YELLOW, "上面"),
    DOWN(Color.WHITE, "下面"),
    FRONT(Color.BLUE, "前面"),
    BACK(Color.GREEN, "后面"),
    LEFT(Color.ORANGE, "左面"),
    RIGHT(Color.RED, "右面");

    private final Color color;
    private final String description;

    Face(Color color, String description) {
        this.color = color;
        this.description = description;
    }

    public Color getColor() {
        return color;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}