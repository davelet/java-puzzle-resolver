package com.rubik.toll.back.puzzle;

import java.util.LinkedList;
import java.util.Queue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NumberPuzzleSolver {
    private static final Logger logger = LogManager.getLogger(NumberPuzzleSolver.class);
    private int[][] board; // 棋盘状态
    private int xsize, ysize; // 棋盘尺寸
    private int[][] where; // 存储每个数字的位置
    private boolean[][] readyFlag; // 标识已经排好的位置
    private StringBuilder ansOp; // 记录移动操作

    // 移动方向：右、左、上、下
    private static final int[][] DIRECTIONS = { { 0, 1 }, { 0, -1 }, { -1, 0 }, { 1, 0 } };
    private static final char[] DIRECTION_CHARS = { '右', '左', '上', '下' };

    // 黄金操作序列
    private static final int[] GOLD_OP = { 0, 2, 1, 3, 1, 2, 0, 0, 3, 1 };
    private static final int[] GOLD_OP_VERTICAL;

    static {
        // 初始化垂直方向的黄金操作序列
        GOLD_OP_VERTICAL = new int[GOLD_OP.length];
        int[] rotateMap = { 3, 2, 0, 1 };
        for (int i = 0; i < GOLD_OP.length; i++) {
            GOLD_OP_VERTICAL[i] = rotateMap[GOLD_OP[i]];
        }
    }

    public NumberPuzzleSolver(Board board) {
        if (board == null) {
            throw new IllegalArgumentException("棋盘不能为空");
        }
        int size = board.getSize();
        this.xsize = size;
        this.ysize = size;

        // 将一维数组转换为二维数组
        int[] tiles = board.getTiles();
        this.board = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.board[i][j] = tiles[i * size + j];
            }
        }

        this.where = new int[size * size][2];
        this.readyFlag = new boolean[size][size];
        this.ansOp = new StringBuilder();
        initPos();
    }

    private void initPos() {
        for (int i = 0; i < xsize; i++) {
            for (int j = 0; j < ysize; j++) {
                int num = board[i][j];
                where[num][0] = i;
                where[num][1] = j;
            }
        }
    }

    private boolean isLegal(int x, int y) {
        return x >= 0 && x < xsize && y >= 0 && y < ysize;
    }

    private void swapNeighbor(int[] src, int[] des) {
        if (this.ansOp.length() > 10_0000) {
            throw new RuntimeException("已经超过100000步尚未求解成功，终止");
        }
        if (board[src[0]][src[1]] == 0) {
            int[] temp = src;
            src = des;
            des = temp;
        }
        ansOp.append(DIRECTION_CHARS[getDirection(des[0] - src[0], des[1] - src[1])]);

        // 交换棋盘上的数字
        int temp = board[src[0]][src[1]];
        board[src[0]][src[1]] = board[des[0]][des[1]];
        board[des[0]][des[1]] = temp;

        // 更新位置信息
        where[board[src[0]][src[1]]] = new int[] { src[0], src[1] };
        where[board[des[0]][des[1]]] = new int[] { des[0], des[1] };
    }

    private int getDirection(int dx, int dy) {
        for (int i = 0; i < DIRECTIONS.length; i++) {
            if (DIRECTIONS[i][0] == dx && DIRECTIONS[i][1] == dy) {
                return i;
            }
        }
        return -1;
    }

    private int[] whereIs(int x, int y) {
        int target = x * ysize + y + 1;
        if (target == xsize * ysize)
            target = 0;
        return where[target];
    }

    public boolean solve() {
        // 记录开始时间
        long startTime = System.currentTimeMillis();

        // 打印初始状态
        printBoard("初始棋盘状态：");

        if (!isSolvable()) {
            logger.info("unsolvable");
            return false;
        }
        
        boolean result = go();

        // 打印最终状态
        printBoard("最终棋盘状态：");

        // 计算并打印执行时间
        long endTime = System.currentTimeMillis();
        logger.info("求解共{}步，耗时：{}毫秒", this.ansOp.length(), endTime - startTime);
        logger.info(this.ansOp);
        return result;
    }

    private void printBoard(String prefix) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < xsize; i++) {
            for (int j = 0; j < ysize; j++) {
                if (board[i][j] == 0) {
                    sb.append("   ");
                } else {
                    sb.append(String.format("%2d ", board[i][j]));
                }
            }
            sb.append("\n");
        }
        logger.info("{}\n{}", prefix, sb.toString());
    }

    public String getAnswer() {
        return ansOp.toString();
    }

    private boolean isSolvable() {
        int[] flatBoard = new int[xsize * ysize];
        int index = 0;
        for (int i = 0; i < xsize; i++) {
            for (int j = 0; j < ysize; j++) {
                flatBoard[index++] = board[i][j];
            }
        }
        int reverse = getReverse(flatBoard);
        if (ysize % 2 == 0) {
            int[] space = whereIs(xsize - 1, ysize - 1);
            reverse ^= (xsize - 1 - space[0]) & 1;
        }
        return reverse == 0;
    }

    private int getReverse(int[] arr) {
        int ans = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != 0) {
                for (int j = i + 1; j < arr.length; j++) {
                    if (arr[j] != 0 && arr[i] > arr[j]) {
                        ans ^= 1;
                    }
                }
            }
        }
        return ans;
    }

    private int[][] getPath(int fx, int fy, int tx, int ty, Integer stillX, Integer stillY) {
        int[][] distance = new int[xsize][ysize];
        for (int i = 0; i < xsize; i++) {
            for (int j = 0; j < ysize; j++) {
                distance[i][j] = -1;
            }
        }
        Queue<int[]> q = new LinkedList<>();
        q.offer(new int[] { fx, fy });
        distance[fx][fy] = 0;
        int[][][] lastPos = new int[xsize][ysize][2];

        while (!q.isEmpty()) {
            int[] now = q.poll();
            for (int[] dir : DIRECTIONS) {
                int xx = now[0] + dir[0];
                int yy = now[1] + dir[1];
                if (isLegal(xx, yy)
                        && distance[xx][yy] == -1
                        && (stillX == null || stillY == null || xx != stillX || yy != stillY)
                        && !readyFlag[xx][yy]) {
                    q.offer(new int[] { xx, yy });
                    distance[xx][yy] = distance[now[0]][now[1]] + 1;
                    lastPos[xx][yy] = now;
                    if (xx == tx && yy == ty) {
                        LinkedList<int[]> path = new LinkedList<>();
                        int[] curr = new int[] { xx, yy };
                        while (true) {
                            path.addFirst(curr);
                            if (curr[0] == fx && curr[1] == fy)
                                break;
                            curr = lastPos[curr[0]][curr[1]];
                        }
                        return path.toArray(new int[0][]);
                    }
                }
            }
        }
        return new int[0][];
    }

    private void moveSpaceTo(int x, int y, Integer stillX, Integer stillY) {
        int[] space = whereIs(xsize - 1, ysize - 1);
        int[][] path = getPath(space[0], space[1], x, y, stillX, stillY);
        for (int i = 1; i < path.length; i++) {
            swapNeighbor(path[i], path[i - 1]);
        }
    }

    private void move(int srcX, int srcY, int desX, int desY) {
        int[][] path = getPath(srcX, srcY, desX, desY, null, null);
        for (int i = 1; i < path.length; i++) {
            moveSpaceTo(path[i][0], path[i][1], path[i - 1][0], path[i - 1][1]);
            swapNeighbor(path[i], path[i - 1]);
        }
    }

    private void doOp(int[] opList) {
        int[] space = whereIs(xsize - 1, ysize - 1);
        for (int i : opList) {
            int x = space[0] - DIRECTIONS[i][0];
            int y = space[1] - DIRECTIONS[i][1];
            swapNeighbor(new int[] { x, y }, space);
            space = new int[] { x, y };
        }
    }

    private boolean go() {
        // 左上部分：xsize-2行，ysize-2列
        for (int i = 0; i < xsize - 2; i++) {
            for (int j = 0; j < ysize - 2; j++) {
                int[] pos = whereIs(i, j);
                move(pos[0], pos[1], i, j);
                readyFlag[i][j] = true;
            }
        }

        // 最后两行
        for (int i = 0; i < ysize - 2; i++) {
            int[] pos = whereIs(xsize - 1, i);
            move(pos[0], pos[1], xsize - 1, i);
            int[] space = whereIs(xsize - 1, ysize - 1);
            if (space[0] == xsize - 2 && space[1] == i) {
                swapNeighbor(new int[] { xsize - 2, i }, new int[] { xsize - 2, i + 1 });
            }
            pos = whereIs(xsize - 2, i);
            if (pos[0] == xsize - 2 && pos[1] == i)
                continue;
            pos = whereIs(xsize - 2, i);
            move(pos[0], pos[1], xsize - 2, i + 2);
            moveSpaceTo(xsize - 2, i + 1, xsize - 2, i + 2);
            doOp(GOLD_OP);
        }

        // 最后两列
        for (int i = 0; i < xsize - 2; i++) {
            int[] pos = whereIs(i, ysize - 2);
            move(pos[0], pos[1], i, ysize - 2);
            int[] space = whereIs(xsize - 1, ysize - 1);
            if (space[0] == i && space[1] == ysize - 1) {
                swapNeighbor(new int[] { i, ysize - 1 }, new int[] { i + 1, ysize - 1 });
            }
            pos = whereIs(i, ysize - 1);
            if (pos[0] == i && pos[1] == ysize - 1)
                continue;
            pos = whereIs(i, ysize - 1);
            move(pos[0], pos[1], i + 2, ysize - 1);
            moveSpaceTo(i + 1, ysize - 1, i + 2, ysize - 1);
            doOp(GOLD_OP_VERTICAL);
        }

        // 最后的2x2小正方形
        int[] pos = whereIs(xsize - 2, ysize - 2);
        move(pos[0], pos[1], xsize - 2, ysize - 2);
        pos = whereIs(xsize - 2, ysize - 1);
        move(pos[0], pos[1], xsize - 2, ysize - 1);
        pos = whereIs(xsize - 1, ysize - 2);
        move(pos[0], pos[1], xsize - 1, ysize - 2);
        return true;
    }
}