package com.rubik.toll.back.puzzle;

import java.util.Random;

public class PuzzleShuffler {
    private Board board;
    private final Random random;

    public PuzzleShuffler(Board board) {
        this.board = board;
        this.random = new Random();
    }

    public int[] shuffle(int moves) {
        if (moves < 0) {
            throw new IllegalArgumentException("移动步数不能为负数");
        }

        // 如果移动步数为0，直接返回当前方阵
        if (moves == 0) {
            return board.getTiles();
        }

        // 执行随机移动
        for (int i = 0; i < moves; i++) {
            moveRandomly();
        }

        return board.getTiles();
    }

    private void moveRandomly() {
        int[] possibleMoves = getPossibleMoves();
        int randomMove = possibleMoves[random.nextInt(possibleMoves.length)];
        board.move(randomMove);
    }

    private int[] getPossibleMoves() {
        int size = board.getSize();
        int emptyPosition = board.getEmptyPosition();
        int row = emptyPosition / size;
        int col = emptyPosition % size;
        int[] moves = new int[4];
        int count = 0;

        // 上
        if (row > 0) {
            moves[count++] = emptyPosition - size;
        }
        // 下
        if (row < size - 1) {
            moves[count++] = emptyPosition + size;
        }
        // 左
        if (col > 0) {
            moves[count++] = emptyPosition - 1;
        }
        // 右
        if (col < size - 1) {
            moves[count++] = emptyPosition + 1;
        }

        int[] result = new int[count];
        System.arraycopy(moves, 0, result, 0, count);
        return result;
    }

    public int[] getBoard() {
        return board.getTiles();
    }
}