package com.rubik.toll.back.puzzle;

import java.time.Duration;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NumberPuzzleSolver {
    private static final Logger logger = LogManager.getLogger(NumberPuzzleSolver.class);
    private final int size;
    private final Board board;

    public NumberPuzzleSolver(Board initialBoard) {
        if (initialBoard == null) {
            throw new IllegalArgumentException("输入棋盘不能为空");
        }
        
        int boardSize = initialBoard.getSize();
        if (boardSize > PuzzleConstants.MAX_SIZE) {
            throw new IllegalArgumentException("棋盘大小不能超过" + PuzzleConstants.MAX_SIZE);
        } else if (boardSize < PuzzleConstants.MIN_SIZE) {
            throw new IllegalArgumentException("棋盘大小不能小于" + PuzzleConstants.MIN_SIZE);
        }
        
        this.size = boardSize;
        this.board = initialBoard;
    }

    // 主要解决方法
    public boolean solve() {
        // 计算初始状态的逆序数
        if (!isSolvable(board.getTiles(), size)) {
            logger.info(board);
            logger.info("当前输入无解");
            return false;
        }

        logger.info("开始求解数字华容道...");
        long startTime = System.nanoTime();

        PriorityQueue<State> openSet = new PriorityQueue<>();
        Set<State> closedSet = new HashSet<>();
        openSet.offer(new State(board, 0));

        while (!openSet.isEmpty()) {
            State currentState = openSet.poll();
            logger.debug(currentState.getBoard());

            if (currentState.isSolved()) {
                Duration duration = Duration.ofNanos(System.nanoTime() - startTime);
                logger.info("找到解决方案！总步数：{}", currentState.getMoves());
                logger.info("求解用时：{}分{}秒{}毫秒\n",
                        duration.toMinutesPart(),
                        duration.toSecondsPart(),
                        duration.toMillisPart());
                return true;
            }

            // 检查当前状态的总步数是否超过限制
            if (currentState.getMoves() + currentState.getManhattan() > 1000) {
                Duration duration = Duration.ofNanos(System.nanoTime() - startTime);
                logger.info("预计需要超过1000步才能解决，判定为无解");
                logger.info("求解用时：{}分{}秒{}毫秒\n",
                        duration.toMinutesPart(),
                        duration.toSecondsPart(),
                        duration.toMillisPart());
                return false;
            }

            closedSet.add(currentState);

            for (State nextState : currentState.getNextStates()) {
                if (!closedSet.contains(nextState) && !openSet.contains(nextState)) {
                    openSet.offer(nextState);
                    // 打印移动描述
                    int movedNumber = printMove(currentState.getBoard().getTiles(), nextState.getBoard().getTiles());
                    logger.debug("将{}与空位交换 （当前已移动{}步，估计共{}步）", movedNumber, nextState.getMoves(),
                            nextState.getMoves() + nextState.getManhattan());
                }
            }
        }

        Duration duration = Duration.ofNanos(System.nanoTime() - startTime);
        logger.info("未找到解决方案");
        logger.info("求解用时：{}分{}秒{}毫秒\n",
                duration.toMinutesPart(),
                duration.toSecondsPart(),
                duration.toMillisPart());
        return false;
    }

    // 打印移动描述
    private int printMove(int[] oldBoard, int[] newBoard) {
        // 找到新旧状态中空格的位置
        int newEmpty = findEmptyPosition(newBoard);
        // 获取移动的数字
        int movedNumber = oldBoard[newEmpty];
        return movedNumber;
    }

    // 查找空格位置的辅助方法
    private int findEmptyPosition(int[] board) {
        for (int i = 0; i < size * size; i++) {
            if (board[i] == 0)
                return i;
        }
        throw new IllegalStateException("无法找到空格位置，数组状态异常"); // 抛出异常表示数组状态异常
    }

    // 计算逆序数并判断是否有解
    private boolean isSolvable(int[] board, int board_width) {
        // 计算不包含空格的数字的逆序数
        int inversions = 0;
        for (int i = 0; i < board.length - 1; i++) {
            if (board[i] == 0)
                continue;
            for (int j = i + 1; j < board.length; j++) {
                if (board[j] != 0 && board[i] > board[j]) {
                    inversions++;
                }
            }
        }

        // 获取空格所在行数（从顶部数起，0-based）
        int emptyRow = 0;
        for (int i = 0; i < board.length; i++) {
            if (board[i] == 0) {
                emptyRow = i / board_width;
                break;
            }
        }

        // 对于3x3的8数码问题（奇数大小）：
        // - 只需要考虑逆序数的奇偶性
        // - 逆序数为偶数时有解
        if (board_width % 2 == 1) {
            return inversions % 2 == 0;
        }

        // 对于偶数大小的情况：
        // - 从顶部数起，空格在偶数行时，逆序数为奇数有解
        // - 从顶部数起，空格在奇数行时，逆序数为偶数有解
        return (emptyRow + inversions) % 2 == 1;
    }
}