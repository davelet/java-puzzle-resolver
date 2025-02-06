package com.rubik.toll.back;

import java.util.*;

public class NumberPuzzleSolver {
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
        // 指向父状态的引用，用于重建解决路径
        State parent;
        
        public State(int[] board, int cost, State parent) {
            this.board = board.clone();
            this.cost = cost;
            this.estimate = calculateManhattanDistance();
            this.parent = parent;
        }
        
        @Override
        public int compareTo(State other) {
            return Integer.compare(this.cost + this.estimate, other.cost + other.estimate);
        }
        
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof State)) return false;
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
                if (board[i] == 0) continue; // 空格不计算距离
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
    public List<int[]> solve(int[] initialBoard) {
        if (!isValidInput(initialBoard)) {
            throw new IllegalArgumentException("输入数组必须包含0-8的数字且长度为9");
        }
        
        System.out.println("开始求解数字华容道（8数码问题）...");
        // System.out.println("初始状态：");
        // printBoard(initialBoard);
        // System.out.println();
        
        PriorityQueue<State> openSet = new PriorityQueue<>();
        Set<State> closedSet = new HashSet<>();
        State initialState = new State(initialBoard, 0, null);
        openSet.offer(initialState);
        
        while (!openSet.isEmpty()) {
            State current = openSet.poll();
            
            System.out.println("当前状态：");
            printBoard(current.board);
            
            if (isGoalState(current.board)) {
                System.out.println("找到解决方案！总步数：" + current.cost);
                return reconstructPath(current);
            }
            
            closedSet.add(current);
            
            // 生成所有可能的下一步移动
            List<State> neighbors = getNeighbors(current);
            for (int i = neighbors.size(); i > 0; i--) {
                State neighbor = neighbors.get(i - 1);
                if (closedSet.contains(neighbor)) continue;
                
                if (!openSet.contains(neighbor)) {
                    // 打印移动描述
                    printMove(current.board, neighbor.board);
                    System.out.println("当前已移动步数：" + neighbor.cost + "步，估计距离目标还需：" + neighbor.estimate + "步\n");
                    
                    openSet.offer(neighbor);
                }
            }
        }
        
        System.out.println("未找到解决方案");
        return null; // 无解
    }
    
    // 打印当前数字方阵状态
    private void printBoard(int[] board) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                int value = board[i * SIZE + j];
                System.out.print(value == 0 ? "  " : value + " ");
            }
            System.out.println();
        }
    }
    
    // 打印移动描述
    private void printMove(int[] oldBoard, int[] newBoard) {
        int oldEmpty = -1, newEmpty = -1;
        int movedNumber = -1;
        
        for (int i = 0; i < SIZE * SIZE; i++) {
            if (oldBoard[i] == 0) oldEmpty = i;
            if (newBoard[i] == 0) newEmpty = i;
        }
        
        movedNumber = oldBoard[newEmpty];
        System.out.println("将" + movedNumber + "与空位交换");
    }
    
    // 验证输入是否有效
    private boolean isValidInput(int[] board) {
        if (board == null || board.length != SIZE * SIZE) return false;
        boolean[] used = new boolean[SIZE * SIZE];
        for (int num : board) {
            if (num < 0 || num > SIZE * SIZE - 1 || used[num]) return false;
            used[num] = true;
        }
        return true;
    }
    
    // 检查是否达到目标状态
    private boolean isGoalState(int[] board) {
        for (int i = 0; i < board.length - 1; i++) {
            if (board[i] != i + 1) return false;
        }
        return board[board.length - 1] == 0;
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
        int[] moves = {-SIZE, SIZE, -1, 1};
        
        for (int move : moves) {
            int newPos = emptyIndex + move;
            
            // 检查移动是否有效
            if (newPos >= 0 && newPos < SIZE * SIZE) {
                // 检查左右移动的边界条件
                if (move == -1 && emptyIndex % SIZE == 0) continue;
                if (move == 1 && emptyIndex % SIZE == SIZE - 1) continue;
                
                int[] newBoard = current.board.clone();
                newBoard[emptyIndex] = newBoard[newPos];
                newBoard[newPos] = 0;
                
                neighbors.add(new State(newBoard, current.cost + 1, current));
            }
        }
        
        return neighbors;
    }
    
    // 重建解决路径
    private List<int[]> reconstructPath(State state) {
        LinkedList<int[]> path = new LinkedList<>();
        while (state != null) {
            path.addFirst(state.board);
            state = state.parent;
        }
        return path;
    }
}