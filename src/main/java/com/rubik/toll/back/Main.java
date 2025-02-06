package com.rubik.toll.back;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("欢迎使用数字华容道求解器！");
        
        // 创建求解器实例
        NumberPuzzleSolver solver = new NumberPuzzleSolver();
        
        // 设置初始状态（一个需要多步移动的示例）
        int[] initialBoard = {1, 2, 3, 4, 0, 6, 7, 5, 8};
        
        System.out.println("\n初始状态：");
        printBoard(initialBoard);
        
        // 求解并获取结果
        List<int[]> solution = solver.solve(initialBoard);
        
        if (solution != null) {
            System.out.println("\n找到解决方案！总步数：" + (solution.size() - 1));
            System.out.println("\n完整解决方案：");
            
            for (int i = 0; i < solution.size(); i++) {
                System.out.println("\n第" + i + "步：");
                printBoard(solution.get(i));
            }
        } else {
            System.out.println("\n无法找到解决方案！");
        }
    }
    
    // 打印数字华容道的当前状态
    private static void printBoard(int[] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int value = board[i * 3 + j];
                System.out.print(value == 0 ? "  " : value + " ");
            }
            System.out.println();
        }
    }
}