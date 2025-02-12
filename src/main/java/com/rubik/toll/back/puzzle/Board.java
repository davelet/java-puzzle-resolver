package com.rubik.toll.back.puzzle;

import java.util.Arrays;

public class Board implements Cloneable {
    private final int[][] tiles;
    private int size = 0;
    private int[] emptyPosition;

    public Board(int[][] tiles) {
        if (tiles == null || tiles.length == 0 || tiles[0].length != tiles.length) {
            throw new IllegalArgumentException("输入棋盘无效");
        }
        this.size = tiles.length;
        if (size < 2) {
            throw new IllegalArgumentException("棋盘最少宽2");
        }
        this.tiles = tiles;
        if (!isValidInput()) {
            throw new IllegalArgumentException("输入棋盘无效");
        }
        findEmptyPosition();
    }

    public Board(int size) {
        if (size < 2) {
            throw new IllegalArgumentException("棋盘最少宽2");
        }
        this.size = size;
        this.tiles = new int[size][size];
        initializeBoard();
    }

    public Board(int[] tiles) {
        for (int i = 2; i <= 100; i++) {
            if (tiles.length == i * i) {
                this.size = i;
                break;
            } else if (tiles.length < i * i) {
                throw new IllegalArgumentException("输入棋盘无效");
            }
        }
        if (this.size == 0) {
            throw new IllegalArgumentException("输入棋盘无效");
        }
        this.tiles = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.tiles[i][j] = tiles[i * size + j];
            }
        }
        if (!isValidInput()) {
            throw new IllegalArgumentException("输入棋盘无效");
        }
        findEmptyPosition();
    }

    private void initializeBoard() {
        int value = 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == size - 1 && j == size - 1) {
                    tiles[i][j] = 0;
                } else {
                    tiles[i][j] = value++;
                }
            }
        }
        emptyPosition = new int[]{size - 1, size - 1};
    }

    // 验证输入是否有效
    private boolean isValidInput() {
        boolean[] used = new boolean[size * size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int num = tiles[i][j];
                if (num < 0 || num > size * size - 1 || used[num])
                    return false;
                used[num] = true;
            }
        }
        return true;
    }

    private void findEmptyPosition() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tiles[i][j] == 0) {
                    emptyPosition = new int[]{i, j};
                    return;
                }
            }
        }
    }

    public int[][] getTiles() {
        return tiles;
    }

    public int getSize() {
        return size;
    }

    public int[] getEmptyPosition() {
        return emptyPosition;
    }

    public boolean canMove(int position) {
        int targetRow = position / size;
        int targetCol = position % size;

        return (Math.abs(emptyPosition[0] - targetRow) + Math.abs(emptyPosition[1] - targetCol)) == 1;
    }

    public void move(int position) {
        if (!canMove(position)) {
            throw new IllegalArgumentException("无效的移动");
        }

        int targetRow = position / size;
        int targetCol = position % size;

        tiles[emptyPosition[0]][emptyPosition[1]] = tiles[targetRow][targetCol];
        tiles[targetRow][targetCol] = 0;
        emptyPosition = new int[]{targetRow, targetCol};
    }

    public boolean isSolved() {
        int value = 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == size - 1 && j == size - 1) {
                    if (tiles[i][j] != 0) return false;
                } else if (tiles[i][j] != value++) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Board other = (Board) obj;
        return Arrays.equals(tiles, other.tiles);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(tiles);
    }

    @Override
    public Board clone() {
        Board cloned = new Board(getTiles());
        return cloned;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("\n");
        // 计算最大数字的宽度
        int maxWidth = String.valueOf(size * size - 1).length();
        
        for (int i = 0; i < size; i++) {
            StringBuilder line = new StringBuilder();
            for (int j = 0; j < size; j++) {
                int value = tiles[i][j];
                if (value == 0) {
                    // 空格位置用相同宽度的空格填充
                    line.append(" ".repeat(maxWidth)).append(" ");
                } else {
                    // 数字左对齐，右边用空格补齐
                    String numStr = String.valueOf(value);
                    line.append(numStr)
                        .append(" ".repeat(maxWidth - numStr.length()))
                        .append(" ");
                }
            }
            sb.append(line).append("\n");
        }
        return sb.toString();
    }
}