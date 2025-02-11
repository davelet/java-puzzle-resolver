package com.rubik.toll.back.puzzle;

import java.util.Arrays;

public class Board implements Cloneable {
    private final int[] tiles;
    private int size = 0;
    private int emptyPosition;

    public Board(int size) {
        if (size < 2) {
            throw new IllegalArgumentException("棋盘最少宽2");
        }
        this.size = size;
        this.tiles = new int[size * size];
        initializeBoard();
    }

    public Board(int[] tiles) {
        for (int i = 2; i <= 30; i++) {
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
        this.tiles = tiles;
        if (!isValidInput(tiles)) {
            throw new IllegalArgumentException("输入棋盘无效");
        }
        findEmptyPosition();
    }

    private void initializeBoard() {
        for (int i = 0; i < size * size - 1; i++) {
            tiles[i] = i + 1;
        }
        tiles[size * size - 1] = 0;
        emptyPosition = size * size - 1;
    }

    // 验证输入是否有效
    private boolean isValidInput(int[] tiles) {
        if (tiles == null || tiles.length != size * size)
            return false;
        boolean[] used = new boolean[size * size];
        for (int num : tiles) {
            if (num < 0 || num > size * size - 1 || used[num])
                return false;
            used[num] = true;
        }
        return true;
    }

    private void findEmptyPosition() {
        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i] == 0) {
                emptyPosition = i;
                return;
            }
        }
    }

    public int[] getTiles() {
        return tiles.clone();
    }

    public int getSize() {
        return size;
    }

    public int getEmptyPosition() {
        return emptyPosition;
    }

    public boolean canMove(int position) {
        int emptyRow = emptyPosition / size;
        int emptyCol = emptyPosition % size;
        int targetRow = position / size;
        int targetCol = position % size;

        return (Math.abs(emptyRow - targetRow) + Math.abs(emptyCol - targetCol)) == 1;
    }

    public void move(int position) {
        if (!canMove(position)) {
            throw new IllegalArgumentException("无效的移动");
        }

        tiles[emptyPosition] = tiles[position];
        tiles[position] = 0;
        emptyPosition = position;
    }

    public boolean isSolved() {
        for (int i = 0; i < tiles.length - 1; i++) {
            if (tiles[i] != i + 1) {
                return false;
            }
        }
        return tiles[tiles.length - 1] == 0;
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
        Board cloned = new Board(tiles);
        return cloned;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("当前状态：\n");
        // 计算最大数字的宽度
        int maxWidth = String.valueOf(size * size - 1).length();
        
        for (int i = 0; i < size; i++) {
            StringBuilder line = new StringBuilder();
            for (int j = 0; j < size; j++) {
                int value = tiles[i * size + j];
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