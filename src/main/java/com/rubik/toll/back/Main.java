package com.rubik.toll.back;

public class Main {
    public static void main(String[] args) {
        System.out.println("欢迎使用数字华容道求解器！");
        
        // 创建求解器实例
        NumberPuzzleSolver solver = new NumberPuzzleSolver();
        
        // 设置初始状态（一个需要多步移动的示例）
        int[] initialBoard = {1, 2, 3, 4, 0, 6, 7, 5, 8};
        
        solver.solve(initialBoard);
    }
}