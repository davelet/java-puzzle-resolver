package com.rubik.toll.back.puzzle;

import java.util.ArrayList;
import java.util.List;

public class State implements Comparable<State> {
    /** 当前状态的棋盘布局 */
    private final Board board;
    /** 从初始状态到当前状态的移动步数 */
    private final int moves;
    /** 当前状态到目标状态的曼哈顿距离估计值 */
    private final int manhattan;

    public State(Board board, int moves) {
        this.board = board;
        this.moves = moves;
        this.manhattan = calculateManhattan();
    }

    private int calculateManhattan() {
        int size = board.getSize();
        int distance = 0;
        int[] tiles = board.getTiles();

        // 检查每一行是否已完成排序
        boolean[] completedRows = new boolean[size];
        for (int row = 0; row < size; row++) {
            boolean isRowCompleted = true;
            for (int col = 0; col < size; col++) {
                int index = row * size + col;
                int expectedValue = row * size + col + 1;
                if (row == size - 1 && col == size - 1) {
                    expectedValue = 0;
                }
                if (tiles[index] != expectedValue) {
                    isRowCompleted = false;
                    break;
                }
            }
            completedRows[row] = isRowCompleted;
            if (!isRowCompleted) {
                // 如果当前行未完成，后续行也不需要检查
                break;
            }
        }

        // 计算曼哈顿距离，已完成的行不参与计算
        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i] != 0) {
                int currentRow = i / size;
                if (!completedRows[currentRow]) {
                    int currentCol = i % size;
                    int targetRow = (tiles[i] - 1) / size;
                    int targetCol = (tiles[i] - 1) % size;
                    distance += Math.abs(currentRow - targetRow) + Math.abs(currentCol - targetCol);
                }
            }
        }
        return distance;
    }

    public List<State> getNextStates() {
        List<State> nextStates = new ArrayList<>();
        int emptyPos = board.getEmptyPosition();
        int size = board.getSize();
        int[] tiles = board.getTiles();

        // 检查每一行是否已完成排序
        boolean[] completedRows = new boolean[size];
        for (int row = 0; row < size; row++) {
            boolean isRowCompleted = true;
            for (int col = 0; col < size; col++) {
                int index = row * size + col;
                int expectedValue = row * size + col + 1;
                if (row == size - 1 && col == size - 1) {
                    expectedValue = 0;
                }
                if (tiles[index] != expectedValue) {
                    isRowCompleted = false;
                    break;
                }
            }
            completedRows[row] = isRowCompleted;
            if (!isRowCompleted) {
                // 如果当前行未完成，后续行也不需要检查
                break;
            }
        }

        // 检查所有可能的移动方向
        int[] directions = {-size, size, -1, 1}; // 上、下、左、右
        for (int direction : directions) {
            int newPos = emptyPos + direction;
            
            // 检查新位置是否在有效范围内且移动是否有效
            if (newPos >= 0 && newPos < size * size && board.canMove(newPos)) {
                // 检查移动是否涉及已完成的行
                int newRow = newPos / size;
                if (direction == -size && completedRows[newRow]) {
                    continue; // 跳过涉及已完成行的移动
                }
                Board newBoard = new Board(board.getTiles());
                newBoard.move(newPos);
                nextStates.add(new State(newBoard, moves + 1));
            }
        }
        return nextStates;
    }

    public boolean isSolved() {
        return board.isSolved();
    }

    public Board getBoard() {
        return board;
    }

    public int getMoves() {
        return moves;
    }

    public int getManhattan() {
        return manhattan;
    }

    @Override
    public int compareTo(State other) {
        if (this.moves == other.moves) {
            return Integer.compare(this.manhattan + this.moves, other.manhattan + other.moves);
        }
        return Integer.compare(other.moves, this.moves); // 已经走步数多的优先
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        State other = (State) obj;
        return board.equals(other.board);
    }

    @Override
    public int hashCode() {
        return board.hashCode();
    }
}