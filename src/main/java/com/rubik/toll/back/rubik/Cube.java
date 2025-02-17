package com.rubik.toll.back.rubik;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Cube implements Cloneable {
    private final Color[][][] state;
    private static final int SIZE = 3;

    public Cube() {
        state = new Color[6][SIZE][SIZE];
        initializeCube();
    }

    private void initializeCube() {
        // 初始化每个面的颜色
        for (Face face : Face.values()) {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    state[face.ordinal()][i][j] = face.getColor();
                }
            }
        }
    }

    public Cube(Color[][][] state) {
        this.state = state;
        if (!isValid()) {
            throw new IllegalArgumentException("提供的状态不是有效的魔方状态");
        }
    }

    public Color getColor(Face face, int row, int col) {
        validatePosition(row, col);
        return state[face.ordinal()][row][col];
    }

    public void setColor(Face face, int row, int col, Color color) {
        validatePosition(row, col);
        state[face.ordinal()][row][col] = color;
    }

    private void validatePosition(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            throw new IllegalArgumentException("位置坐标超出范围：" + row + ", " + col);
        }
    }

    public Color[][][] getState() {
        return state;
    }

    public void solve() {
        CubeSolver solver = new CubeSolver(this);
        solver.solve();
    }

    public boolean isValid() {
        // 检查每个颜色出现的次数是否正确（每种颜色应该出现9次）
        Map<Color, Integer> colorCount = new HashMap<>();
        for (Face face : Face.values()) {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    Color color = state[face.ordinal()][i][j];
                    colorCount.put(color, colorCount.getOrDefault(color, 0) + 1);
                }
            }
        }
        for (Color color : Color.values()) {
            if (colorCount.getOrDefault(color, 0) != SIZE * SIZE) {
                return false;
            }
        }

        // 验证中心块颜色是否正确
        for (Face face : Face.values()) {
            if (state[face.ordinal()][1][1] != face.getColor()) {
                return false;
            }
        }

        // 验证角块和边块的合法性
        return validateCorners() && validateEdges();
    }

    private boolean validateCorners() {
        // 获取所有角块的颜色组合
        Set<String> cornerCombinations = new HashSet<>();
        // 前面的角块
        cornerCombinations.add(sortColors(Color.BLUE, Color.WHITE, Color.RED));
        cornerCombinations.add(sortColors(Color.BLUE, Color.WHITE, Color.ORANGE));
        cornerCombinations.add(sortColors(Color.BLUE, Color.YELLOW, Color.RED));
        cornerCombinations.add(sortColors(Color.BLUE, Color.YELLOW, Color.ORANGE));
        // 后面的角块
        cornerCombinations.add(sortColors(Color.GREEN, Color.WHITE, Color.RED));
        cornerCombinations.add(sortColors(Color.GREEN, Color.WHITE, Color.ORANGE));
        cornerCombinations.add(sortColors(Color.GREEN, Color.YELLOW, Color.RED));
        cornerCombinations.add(sortColors(Color.GREEN, Color.YELLOW, Color.ORANGE));

        // 检查实际的角块
        // 前面的角块
        if (isNotValidCorner(Face.FRONT, 0, 0, Face.LEFT, 0, 2, Face.UP, 2, 0, cornerCombinations)) return false;
        if (isNotValidCorner(Face.FRONT, 0, 2, Face.RIGHT, 0, 0, Face.UP, 2, 2, cornerCombinations)) return false;
        if (isNotValidCorner(Face.FRONT, 2, 0, Face.LEFT, 2, 2, Face.DOWN, 0, 0, cornerCombinations)) return false;
        if (isNotValidCorner(Face.FRONT, 2, 2, Face.RIGHT, 2, 0, Face.DOWN, 0, 2, cornerCombinations)) return false;
        // 后面的角块
        if (isNotValidCorner(Face.BACK, 0, 0, Face.RIGHT, 0, 2, Face.UP, 0, 2, cornerCombinations)) return false;
        if (isNotValidCorner(Face.BACK, 0, 2, Face.LEFT, 0, 0, Face.UP, 0, 0, cornerCombinations)) return false;
        if (isNotValidCorner(Face.BACK, 2, 0, Face.RIGHT, 2, 2, Face.DOWN, 2, 2, cornerCombinations)) return false;
        if (isNotValidCorner(Face.BACK, 2, 2, Face.LEFT, 2, 0, Face.DOWN, 2, 0, cornerCombinations)) return false;

        return true;
    }

    private boolean validateEdges() {
        // 获取所有边块的颜色组合
        Set<String> edgeCombinations = new HashSet<>();
        // 水平边块
        edgeCombinations.add(sortColors(Color.BLUE, Color.RED));
        edgeCombinations.add(sortColors(Color.BLUE, Color.ORANGE));
        edgeCombinations.add(sortColors(Color.GREEN, Color.RED));
        edgeCombinations.add(sortColors(Color.GREEN, Color.ORANGE));
        // 垂直边块
        edgeCombinations.add(sortColors(Color.BLUE, Color.YELLOW));
        edgeCombinations.add(sortColors(Color.BLUE, Color.WHITE));
        edgeCombinations.add(sortColors(Color.GREEN, Color.YELLOW));
        edgeCombinations.add(sortColors(Color.GREEN, Color.WHITE));
        // 中间层边块
        edgeCombinations.add(sortColors(Color.YELLOW, Color.RED));
        edgeCombinations.add(sortColors(Color.YELLOW, Color.ORANGE));
        edgeCombinations.add(sortColors(Color.WHITE, Color.RED));
        edgeCombinations.add(sortColors(Color.WHITE, Color.ORANGE));

        // 检查实际的边块
        // 前面的边块
        if (isNotValidEdge(Face.FRONT, 0, 1, Face.UP, 2, 1, edgeCombinations)) return false;
        if (isNotValidEdge(Face.FRONT, 1, 0, Face.LEFT, 1, 2, edgeCombinations)) return false;
        if (isNotValidEdge(Face.FRONT, 1, 2, Face.RIGHT, 1, 0, edgeCombinations)) return false;
        if (isNotValidEdge(Face.FRONT, 2, 1, Face.DOWN, 0, 1, edgeCombinations)) return false;
        // 后面的边块
        if (isNotValidEdge(Face.BACK, 0, 1, Face.UP, 0, 1, edgeCombinations)) return false;
        if (isNotValidEdge(Face.BACK, 1, 0, Face.RIGHT, 1, 2, edgeCombinations)) return false;
        if (isNotValidEdge(Face.BACK, 1, 2, Face.LEFT, 1, 0, edgeCombinations)) return false;
        if (isNotValidEdge(Face.BACK, 2, 1, Face.DOWN, 2, 1, edgeCombinations)) return false;
        // 中间的边块
        if (isNotValidEdge(Face.UP, 1, 0, Face.LEFT, 0, 1, edgeCombinations)) return false;
        if (isNotValidEdge(Face.UP, 1, 2, Face.RIGHT, 0, 1, edgeCombinations)) return false;
        if (isNotValidEdge(Face.DOWN, 1, 0, Face.LEFT, 2, 1, edgeCombinations)) return false;
        if (isNotValidEdge(Face.DOWN, 1, 2, Face.RIGHT, 2, 1, edgeCombinations)) return false;

        return true;
    }

    private boolean isNotValidCorner(Face face1, int row1, int col1,
                                     Face face2, int row2, int col2,
                                     Face face3, int row3, int col3,
                                     Set<String> validCombinations) {
        Color color1 = state[face1.ordinal()][row1][col1];
        Color color2 = state[face2.ordinal()][row2][col2];
        Color color3 = state[face3.ordinal()][row3][col3];
        return !validCombinations.contains(sortColors(color1, color2, color3));
    }

    private boolean isNotValidEdge(Face face1, int row1, int col1,
                                   Face face2, int row2, int col2,
                                   Set<String> validCombinations) {
        Color color1 = state[face1.ordinal()][row1][col1];
        Color color2 = state[face2.ordinal()][row2][col2];
        return !validCombinations.contains(sortColors(color1, color2));
    }

    private String sortColors(Color... colors) {
        Arrays.sort(colors);
        return Arrays.toString(colors);
    }

    @Override
    public Cube clone() {
        Color[][][] clonedState = new Color[6][SIZE][SIZE];
        for (int face = 0; face < 6; face++) {
            for (int i = 0; i < SIZE; i++) {
                System.arraycopy(state[face][i], 0, clonedState[face][i], 0, SIZE);
            }
        }
        return new Cube(clonedState);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("\n");

        // 第一行：上面和后面的名称
        sb.append("      ").append(Face.UP.getDescription()).append("        ")
          .append(Face.BACK.getDescription()).append("（镜像）").append("\n");

        // 第2-4行：上面和后面的颜色
        for (int i = 0; i < SIZE; i++) {
            sb.append("      ");
            // 上面
            for (int j = 0; j < SIZE; j++) {
                sb.append(state[Face.UP.ordinal()][i][j].getColorChar()).append(" ");
            }
            sb.append("    ");
            // 后面（镜像）
            for (int j = SIZE - 1; j >= 0; j--) {
                sb.append(state[Face.BACK.ordinal()][i][j].getColorChar()).append(" ");
            }
            sb.append("\n");
        }
        sb.append("\n");

        // 中间三行：左、前、右面
        for (int i = 0; i < SIZE; i++) {
            // 左面
            String space = " ";
            for (int j = 0; j < SIZE; j++) {
                sb.append(state[Face.LEFT.ordinal()][i][j].getColorChar()).append(space);
            }
            sb.append(space.repeat(2));
            // 前面
            for (int j = 0; j < SIZE; j++) {
                sb.append(state[Face.FRONT.ordinal()][i][j].getColorChar()).append(space);
            }
            sb.append(space.repeat(2));
            // 右面
            for (int j = 0; j < SIZE; j++) {
                sb.append(state[Face.RIGHT.ordinal()][i][j].getColorChar()).append(space);
            }
            sb.append("\n");
        }
        sb.append("\n");

        // 最后三行：下面
//        sb.append("      ").append(Face.DOWN.getDescription()).append("\n");
        for (int i = 0; i < SIZE; i++) {
            sb.append("      ");
            for (int j = 0; j < SIZE; j++) {
                sb.append(state[Face.DOWN.ordinal()][i][j].getColorChar()).append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    public boolean isSolved() {
        // 检查每个面的所有方块颜色是否与中心块颜色一致
        for (Face face : Face.values()) {
            Color centerColor = face.getColor();
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (state[face.ordinal()][i][j] != centerColor) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}