package com.rubik.toll.back.puzzle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.time.Duration;

public class NumberPuzzleSolver {
    private static final Logger logger = LogManager.getLogger(NumberPuzzleSolver.class);
    private final Board board;
    private final int xsize, ysize;
    private final boolean[][] readyFlag;
    private final BoardOperator boardOperator;
    private final PathFinder pathFinder;
    private final SolvabilityChecker solvabilityChecker;

    // 黄金操作序列
    private static final int[] GOLD_OP = {0, 2, 1, 3, 1, 2, 0, 0, 3, 1};
    private static final int[] GOLD_OP_VERTICAL;

    static {
        // 初始化垂直方向的黄金操作序列
        GOLD_OP_VERTICAL = new int[GOLD_OP.length];
        int[] rotateMap = {3, 2, 0, 1};
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

        this.board = board;
        this.readyFlag = new boolean[size][size];
        this.boardOperator = new BoardOperator(this.board.getTiles(), size, size);
        this.pathFinder = new PathFinder(size, size, this.readyFlag);
        this.solvabilityChecker = new SolvabilityChecker(this.board.getTiles(), size, size, this.boardOperator);
    }

    public boolean solve() {
        // 记录开始时间
        long startTime = System.currentTimeMillis();

        // 打印初始状态
        printBoard("初始棋盘状态：");

        if (!solvabilityChecker.isSolvable()) {
            logger.info("unsolvable");
            return false;
        }
        
        boolean result = go();

        // 打印最终状态
        printBoard("最终棋盘状态：");

        // 计算并打印执行时间
        long endTime = System.currentTimeMillis();
        Duration duration = Duration.ofMillis(endTime - startTime);
        logger.info("求解共{}步，耗时：{}", boardOperator.getMoveHistory().length(), duration);
        logger.info(boardOperator.getMoveHistory());
        return result;
    }

    private void printBoard(String prefix) {
        logger.info("{}\n{}", prefix, board);
    }

    private void moveSpaceTo(int x, int y, Integer stillX, Integer stillY) {
        int[] space = boardOperator.getPositionOf(xsize - 1, ysize - 1);
        int[][] path = pathFinder.findPath(space[0], space[1], x, y, stillX, stillY);
        for (int i = 1; i < path.length; i++) {
            boardOperator.swapNeighbor(path[i], path[i - 1]);
        }
    }

    private void move(int srcX, int srcY, int desX, int desY) {
        int[][] path = pathFinder.findPath(srcX, srcY, desX, desY, null, null);
        for (int i = 1; i < path.length; i++) {
            moveSpaceTo(path[i][0], path[i][1], path[i - 1][0], path[i - 1][1]);
            boardOperator.swapNeighbor(path[i], path[i - 1]);
        }
    }

    private boolean go() {
        // 左上部分：xsize-2行，ysize-2列
        for (int i = 0; i < xsize - 2; i++) {
            for (int j = 0; j < ysize - 2; j++) {
                int[] pos = boardOperator.getPositionOf(i, j);
                move(pos[0], pos[1], i, j);
                readyFlag[i][j] = true;
            }
        }

        // 最后两行
        for (int i = 0; i < ysize - 2; i++) {
            int[] pos = boardOperator.getPositionOf(xsize - 1, i);
            move(pos[0], pos[1], xsize - 1, i);
            int[] space = boardOperator.getPositionOf(xsize - 1, ysize - 1);
            if (space[0] == xsize - 2 && space[1] == i) {
                boardOperator.swapNeighbor(new int[]{xsize - 2, i}, new int[]{xsize - 2, i + 1});
            }
            pos = boardOperator.getPositionOf(xsize - 2, i);
            if (pos[0] == xsize - 2 && pos[1] == i) continue;
            pos = boardOperator.getPositionOf(xsize - 2, i);
            move(pos[0], pos[1], xsize - 2, i + 2);
            moveSpaceTo(xsize - 2, i + 1, xsize - 2, i + 2);
            boardOperator.doOperations(GOLD_OP);
        }

        // 最后两列
        for (int i = 0; i < xsize - 2; i++) {
            int[] pos = boardOperator.getPositionOf(i, ysize - 2);
            move(pos[0], pos[1], i, ysize - 2);
            int[] space = boardOperator.getPositionOf(xsize - 1, ysize - 1);
            if (space[0] == i && space[1] == ysize - 1) {
                boardOperator.swapNeighbor(new int[]{i, ysize - 1}, new int[]{i + 1, ysize - 1});
            }
            pos = boardOperator.getPositionOf(i, ysize - 1);
            if (pos[0] == i && pos[1] == ysize - 1) continue;
            pos = boardOperator.getPositionOf(i, ysize - 1);
            move(pos[0], pos[1], i + 2, ysize - 1);
            moveSpaceTo(i + 1, ysize - 1, i + 2, ysize - 1);
            boardOperator.doOperations(GOLD_OP_VERTICAL);
        }

        // 最后的2x2小正方形
        int[] pos = boardOperator.getPositionOf(xsize - 2, ysize - 2);
        move(pos[0], pos[1], xsize - 2, ysize - 2);
        pos = boardOperator.getPositionOf(xsize - 2, ysize - 1);
        move(pos[0], pos[1], xsize - 2, ysize - 1);
        pos = boardOperator.getPositionOf(xsize - 1, ysize - 2);
        move(pos[0], pos[1], xsize - 1, ysize - 2);
        return true;
    }
}