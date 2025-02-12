package com.rubik.toll.back.puzzle;

import java.util.Random;

public class PuzzleShuffler {
    private int[][] board;
    private final int size;
    private final Random random;
    private int emptyRow;
    private int emptyCol;

    public PuzzleShuffler(Board board) {
        if (board == null) {
            throw new IllegalArgumentException("棋盘不能为空");
        }
        this.size = board.getSize();
        this.random = new Random();
        
        this.board = board.getTiles();
        int[] emptyPosition = board.getEmptyPosition();
        this.emptyRow = emptyPosition[0];
        this.emptyCol = emptyPosition[1];
    }

    public int[] shuffle(int moves) {
        if (moves < 0) {
            throw new IllegalArgumentException("移动步数不能为负数");
        }

        // 如果移动步数为0，直接返回当前方阵
        if (moves == 0) {
            return toOneDimensionalArray();
        }

        // 执行随机移动
        for (int i = 0; i < moves; i++) {
            moveRandomly();
        }

        return toOneDimensionalArray();
    }

    private void moveRandomly() {
        int[][] possibleMoves = getPossibleMoves();
        int moveIndex = random.nextInt(possibleMoves.length);
        int newRow = possibleMoves[moveIndex][0];
        int newCol = possibleMoves[moveIndex][1];
        
        // 交换空格与目标位置
        board[emptyRow][emptyCol] = board[newRow][newCol];
        board[newRow][newCol] = 0;
        emptyRow = newRow;
        emptyCol = newCol;
    }

    private int[][] getPossibleMoves() {
        // 可能的移动方向：上、下、左、右
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        int count = 0;
        int[][] possibleMoves = new int[4][2];

        for (int[] dir : directions) {
            int newRow = emptyRow + dir[0];
            int newCol = emptyCol + dir[1];
            
            if (newRow >= 0 && newRow < size && newCol >= 0 && newCol < size) {
                possibleMoves[count][0] = newRow;
                possibleMoves[count][1] = newCol;
                count++;
            }
        }

        int[][] result = new int[count][2];
        System.arraycopy(possibleMoves, 0, result, 0, count);
        return result;
    }

    public int[] getBoard() {
        return toOneDimensionalArray();
    }

    private int[] toOneDimensionalArray() {
        int[] result = new int[size * size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[i * size + j] = board[i][j];
            }
        }
        return result;
    }
}