package com.rubik.toll.back.puzzle;

public class BoardOperator {
    private final int[][] board;
    private final int[][] where;
    private final int xsize;
    private final int ysize;
    private final StringBuilder moveHistory;
    private static final char[] DIRECTION_CHARS = {'右', '左', '上', '下'};

    public BoardOperator(int[][] board, int xsize, int ysize) {
        this.board = board;
        this.xsize = xsize;
        this.ysize = ysize;
        this.where = new int[xsize * ysize][2];
        this.moveHistory = new StringBuilder();
        initializePositions();
    }

    private void initializePositions() {
        for (int i = 0; i < xsize; i++) {
            for (int j = 0; j < ysize; j++) {
                int num = board[i][j];
                where[num][0] = i;
                where[num][1] = j;
            }
        }
    }

    public void swapNeighbor(int[] src, int[] des) {
        if (moveHistory.length() > 10_0000) {
            throw new RuntimeException("已经超过100000步尚未求解成功，终止");
        }

        if (board[src[0]][src[1]] == 0) {
            int[] temp = src;
            src = des;
            des = temp;
        }

        moveHistory.append(DIRECTION_CHARS[getDirection(des[0] - src[0], des[1] - src[1])]);

        // 交换棋盘上的数字
        int temp = board[src[0]][src[1]];
        board[src[0]][src[1]] = board[des[0]][des[1]];
        board[des[0]][des[1]] = temp;

        // 更新位置信息
        where[board[src[0]][src[1]]] = new int[]{src[0], src[1]};
        where[board[des[0]][des[1]]] = new int[]{des[0], des[1]};
    }

    private int getDirection(int dx, int dy) {
        int[][] directions = PathFinder.getDirections();
        for (int i = 0; i < directions.length; i++) {
            if (directions[i][0] == dx && directions[i][1] == dy) {
                return i;
            }
        }
        return -1;
    }

    public int[] getPositionOf(int x, int y) {
        int target = (x == xsize - 1 && y == ysize - 1) ? 0 : x * ysize + y + 1;
        return where[target];
    }

    public String getMoveHistory() {
        return moveHistory.toString();
    }

    public int[][] getBoard() {
        return board;
    }

    public void doOperations(int[] operations) {
        int[] space = getPositionOf(xsize - 1, ysize - 1);
        int[][] directions = PathFinder.getDirections();
        for (int op : operations) {
            int x = space[0] - directions[op][0];
            int y = space[1] - directions[op][1];
            swapNeighbor(new int[]{x, y}, space);
            space = new int[]{x, y};
        }
    }
}