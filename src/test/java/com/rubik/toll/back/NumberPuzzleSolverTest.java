package com.rubik.toll.back;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NumberPuzzleSolverTest {
    private final NumberPuzzleSolver solver = new NumberPuzzleSolver();

    @Test
    void testValidInput() {
        int[] board = {1, 2, 3, 4, 5, 6, 7, 8, 0};
        assertTrue(solver.solve(board), "已经是目标状态，应该返回true");
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
        assertTrue(solver.solve(initial), "简单情况应该可以求解");
    }

    @Test
    void testComplexSolution() {
        // 测试一个较复杂的情况
        int[] initial = {0, 1, 3, 4, 2, 5, 7, 8, 6};
        assertTrue(solver.solve(initial), "复杂情况应该可以求解");
    }

    @Test
    void testTwoStepSolution() {
        // 测试一个需要两步移动的情况
        // 初始状态：1 2 3
        //          4 5 6
        //          0 7 8
        int[] initial = {1, 2, 3, 4, 5, 6, 0, 7, 8};
        assertTrue(solver.solve(initial), "两步移动的情况应该可以求解");
    }

    @Test
    void testUnsolvableCase() {
        // 测试一个无解的情况
        // 在3x3的数字华容道中，当两个数字的相对位置颠倒时，问题无解
        int[] initial = {2, 1, 3, 4, 5, 6, 7, 8, 0};
        assertFalse(solver.solve(initial), "无解情况应该返回false");
    }
}