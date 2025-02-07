package com.rubik.toll.back;

import java.util.*;
import java.time.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NumberPuzzleSolver {
    private static final Logger logger = LogManager.getLogger(NumberPuzzleSolver.class);
    private static final int SIZE = 3;
    private static final int GOAL_STATE = 123456780;

    // 状态类，用于表示数字华容道的一个状态
    private static class State implements Comparable<State> {
        // 当前数字华容道的状态数组，0表示空格
        int[] board;
        // 从初始状态到当前状态的实际移动步数
        int cost;
        // 从当前状态到目标状态的估计步数（曼哈顿距离）
        int estimate;

        public State(int[] board, int cost) {
            this.board = board.clone();
            this.cost = cost;
            this.estimate = calculateManhattanDistance();
        }

        @Override
        public int compareTo(State other) {
            return Integer.compare(this.cost + this.estimate, other.cost + other.estimate);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof State))
                return false;
            return Arrays.equals(board, ((State) obj).board);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(board);
        }

        // 计算当前状态到目标状态的曼哈顿距离
        private int calculateManhattanDistance() {
            int distance = 0;
            for (int i = 0; i < board.length; i++) {
                if (board[i] == 0)
                    continue; // 空格不计算距离
                int currentRow = i / SIZE;
                int currentCol = i % SIZE;
                int targetRow = (board[i] - 1) / SIZE;
                int targetCol = (board[i] - 1) % SIZE;
                distance += Math.abs(currentRow - targetRow) + Math.abs(currentCol - targetCol);
            }
            return distance;
        }
    }

    // 主要解决方法
    public boolean solve(int[] initialBoard) {
        if (!isValidInput(initialBoard)) {
            throw new IllegalArgumentException("输入数组必须包含0-8的数字且长度为9");
        }

        // 计算初始状态的逆序数
        if (!isSolvable(initialBoard, SIZE)) {
            logger.info("当前输入无解");
            return false;
        }

        logger.info("开始求解数字华容道（8数码问题）...");
        long startTime = System.nanoTime();

        Set<State> closedSet = new HashSet<>();
        State currentState = new State(initialBoard, 0);

        while (true) {
            logger.debug("当前状态：");
            printBoard(currentState.board);

            if (isGoalState(currentState.board)) {
                Duration duration = Duration.ofNanos(System.nanoTime() - startTime);
                logger.info("找到解决方案！总步数：{}", currentState.cost);
                logger.info("求解用时：{}分{}秒{}毫秒",
                        duration.toMinutesPart(),
                        duration.toSecondsPart(),
                        duration.toMillisPart());
                return true;
            }

            closedSet.add(currentState);

            // 生成所有可能的下一步移动
            List<State> neighbors = getNeighbors(currentState);
            State bestNeighbor = null;
            int bestScore = Integer.MAX_VALUE;

            for (State neighbor : neighbors) {
                if (closedSet.contains(neighbor))
                    continue;

                int score = neighbor.cost + neighbor.estimate;
                if (score < bestScore) {
                    bestScore = score;
                    bestNeighbor = neighbor;
                }
            }

            if (bestNeighbor == null) {
                Duration duration = Duration.ofNanos(System.nanoTime() - startTime);
                logger.info("未找到解决方案");
                logger.info("求解用时：{}分{}秒{}毫秒",
                        duration.toMinutesPart(),
                        duration.toSecondsPart(),
                        duration.toMillisPart());
                return false;
            }

            // 打印移动描述
            int movedNumber = printMove(currentState.board, bestNeighbor.board);
            logger.debug("将{}与空位交换 （当前已移动{}步，估计共{}步）", movedNumber, bestNeighbor.cost, bestNeighbor.cost + bestNeighbor.estimate);

            currentState = bestNeighbor;
        }
    }

    // 打印当前数字方阵状态
    private void printBoard(int[] board) {
        for (int i = 0; i < SIZE; i++) {
            StringBuilder line = new StringBuilder();
            for (int j = 0; j < SIZE; j++) {
                int value = board[i * SIZE + j];
                line.append(value == 0 ? "  " : value + " ");
            }
            logger.debug(line.toString());
        }
        logger.debug("");
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
        for (int i = 0; i < SIZE * SIZE; i++) {
            if (board[i] == 0)
                return i;
        }
        throw new IllegalStateException("无法找到空格位置，数组状态异常"); // 抛出异常表示数组状态异常
    }

    // 验证输入是否有效
    private boolean isValidInput(int[] board) {
        if (board == null || board.length != SIZE * SIZE)
            return false;
        boolean[] used = new boolean[SIZE * SIZE];
        for (int num : board) {
            if (num < 0 || num > SIZE * SIZE - 1 || used[num])
                return false;
            used[num] = true;
        }
        return true;
    }

    // 检查是否达到目标状态
    private boolean isGoalState(int[] board) {
        for (int i = 0; i < board.length - 1; i++) {
            if (board[i] != i + 1)
                return false;
        }
        return board[board.length - 1] == 0;
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
        return (emptyRow + inversions) % 2 == 0;
    }

    // 获取所有可能的下一步移动
    private List<State> getNeighbors(State current) {
        List<State> neighbors = new ArrayList<>();
        int emptyIndex = -1;

        // 找到空格（0）的位置
        for (int i = 0; i < current.board.length; i++) {
            if (current.board[i] == 0) {
                emptyIndex = i;
                break;
            }
        }

        // 可能的移动方向：上、下、左、右
        int[] moves = { -SIZE, SIZE, -1, 1 };

        for (int move : moves) {
            int newPos = emptyIndex + move;

            // 检查移动是否有效
            if (newPos >= 0 && newPos < SIZE * SIZE) {
                // 检查左右移动的边界条件
                if (move == -1 && emptyIndex % SIZE == 0)
                    continue;
                if (move == 1 && emptyIndex % SIZE == SIZE - 1)
                    continue;

                int[] newBoard = current.board.clone();
                newBoard[emptyIndex] = newBoard[newPos];
                newBoard[newPos] = 0;

                neighbors.add(new State(newBoard, current.cost + 1));
            }
        }

        return neighbors;
    }

}