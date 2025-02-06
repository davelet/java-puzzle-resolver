package com.rubik.toll.back;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class NumberPuzzleSolverTest {
    private final NumberPuzzleSolver solver = new NumberPuzzleSolver();

    @Test
    void testValidInput() {
        int[] board = {1, 2, 3, 4, 5, 6, 7, 8, 0};
        List<int[]> solution = solver.solve(board);
        assertNotNull(solution);
        assertEquals(1, solution.size()); // 已经是目标状态，只有一个状态
        assertArrayEquals(board, solution.get(0));
    }

    @Test
    void testInvalidInput() {
        // 测试空输入
        assertThrows(IllegalArgumentException.class, () -> solver.solve(null));

        // 测试长度不正确的输入
        assertThrows(IllegalArgumentException.class, () -> solver.solve(new int[]{1, 2, 3, 4}));

        // 测试包含重复数字的输入
        assertThrows(IllegalArgumentException.class, 
            () -> solver.solve(new int[]{1, 2, 3, 4, 5, 6, 7, 7, 0}));

        // 测试包含非法数字的输入（9不再是有效数字）
        assertThrows(IllegalArgumentException.class, 
            () -> solver.solve(new int[]{1, 2, 3, 4, 5, 6, 7, 9, 0}));
    }

    @Test
    void testSimpleSolution() {
        // 测试一个简单的情况，只需要移动一步
        int[] initial = {1, 2, 3, 4, 5, 6, 7, 0, 8};
        List<int[]> solution = solver.solve(initial);
        
        assertNotNull(solution);
        assertTrue(solution.size() > 1); // 至少有初始状态和目标状态
        
        // 验证初始状态
        assertArrayEquals(initial, solution.get(0));
        
        // 验证最终状态
        int[] finalState = solution.get(solution.size() - 1);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 0}, finalState);
    }

    @Test
    void testComplexSolution() {
        // 测试一个较复杂的情况
        int[] initial = {0, 1, 3, 4, 2, 5, 7, 8, 6};
        List<int[]> solution = solver.solve(initial);
        
        assertNotNull(solution);
        assertTrue(solution.size() > 1);
        
        // 验证初始状态
        assertArrayEquals(initial, solution.get(0));
        
        // 验证最终状态
        int[] finalState = solution.get(solution.size() - 1);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 0}, finalState);
        
        // 验证每一步移动的有效性
        for (int i = 1; i < solution.size(); i++) {
            assertTrue(isValidMove(solution.get(i-1), solution.get(i)));
        }
    }

    @Test
    void testTwoStepSolution() {
        // 测试一个需要两步移动的情况
        // 初始状态：1 2 3
        //          4 5 0
        //          7 8 6
        // 第一步：将6上移
        // 第二步：将0右移
        int[] initial = {1, 2, 3, 4, 5, 6, 0, 7, 8};
        List<int[]> solution = solver.solve(initial);
        
        assertNotNull(solution);
        assertEquals(3, solution.size()); // 初始状态 + 一次移动 + 最终状态
        
        // 验证初始状态
        assertArrayEquals(initial, solution.get(0));
        
        // 验证最终状态
        int[] finalState = solution.get(solution.size() - 1);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 0}, finalState);
        
        // 验证每一步移动的有效性
        for (int i = 1; i < solution.size(); i++) {
            assertTrue(isValidMove(solution.get(i-1), solution.get(i)));
        }
    }

    // 辅助方法：验证两个状态之间的移动是否有效
    private boolean isValidMove(int[] state1, int[] state2) {
        // 找到空格在两个状态中的位置
        int empty1 = -1, empty2 = -1;
        int diff = 0;
        
        for (int i = 0; i < 9; i++) {
            if (state1[i] == 0) empty1 = i;
            if (state2[i] == 0) empty2 = i;
            if (state1[i] != state2[i]) diff++;
        }
        
        // 有效移动只会涉及两个位置的交换
        if (diff != 2) return false;
        
        // 检查空格的移动是否有效（只能上下左右移动）
        int row1 = empty1 / 3, col1 = empty1 % 3;
        int row2 = empty2 / 3, col2 = empty2 % 3;
        
        return Math.abs(row1 - row2) + Math.abs(col1 - col2) == 1;
    }
}