package com.rubik.toll.back.puzzle;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class NumberPuzzleSolverTest {

    @Test
    void test2x2Puzzle() {
        int[] tiles = {1, 2, 0, 3};
        Board board = new Board(tiles);
        NumberPuzzleSolver solver = new NumberPuzzleSolver(board);
        assertTrue(solver.solve(), "2x2方阵应该可以求解");
    }

    @Test
    void test3x3Puzzle() {
        int[] tiles = {5,7,3,4,8,6,1,0,2};
        Board board = new Board(tiles);
        NumberPuzzleSolver solver = new NumberPuzzleSolver(board);
        assertTrue(solver.solve(), "3x3方阵应该可以求解");
    }

    @Test
    void test4x4Puzzle() {
        int[] tiles = {1, 2, 3, 4, 5, 6, 7, 0, 9, 10, 11, 8, 13, 14, 15, 12};
        Board board = new Board(tiles);
        NumberPuzzleSolver solver = new NumberPuzzleSolver(board);
        assertTrue(solver.solve(), "4x4方阵应该可以求解");
    }

    @Test
    void testInvalidInput() {
        // 测试空输入
        assertThrows(IllegalArgumentException.class, () -> new NumberPuzzleSolver(null));

        // 测试包含重复数字的输入
        int[] duplicateTiles = {1, 2, 3, 4, 5, 6, 7, 7, 0};
        assertThrows(IllegalArgumentException.class, () -> new Board(duplicateTiles));

        // 测试包含非法数字的输入
        int[] illegalTiles = {1, 2, 3, 4, 5, 6, 7, 9, 0};
        assertThrows(IllegalArgumentException.class, () -> new Board(illegalTiles));
    }

    @Test
    void testComplexSolution() {
        int[] tiles = {1,7,2,4,5,6,0,3,14,9,11,12,8,13,15,16,17,18,10,24,21,22,23,20,19};
        Board board = new Board(tiles);
        NumberPuzzleSolver solver = new NumberPuzzleSolver(board);
        assertTrue(solver.solve(), "复杂情况应该可以求解");
    }

    @Test
    void testUnsolvableCase() {
        // 测试一个无解的情况
        // 在3x3的数字华容道中，当两个数字的相对位置颠倒时，问题无解
        int[] tiles = {2, 1, 3, 4, 5, 6, 7, 8, 0};
        Board board = new Board(tiles);
        NumberPuzzleSolver solver = new NumberPuzzleSolver(board);
        assertFalse(solver.solve(), "无解情况应该返回false");
    }

    @Test
    void test11x11Puzzle() {
        int[] tiles = {
            2, 4, 13, 14, 27, 5, 18, 19, 8, 10, 11,
            51, 3, 23, 49, 16, 20, 6, 7, 28, 21, 22,
            1, 15, 25, 26, 38, 37, 17, 41, 29, 52, 32,
            35, 24, 48, 58, 61, 39, 42, 30, 43, 31, 33,
            12, 47, 34, 46, 72, 81, 53, 54, 9, 66, 55,
            114, 45, 57, 36, 59, 60, 63, 40, 64, 77, 44,
            78, 68, 92, 69, 67, 79, 86, 97, 76, 84, 65,
            90, 101, 115, 70, 95, 94, 50, 73, 108, 88, 99,
            56, 89, 0, 96, 71, 91, 62, 74, 118, 85, 87,
            112, 100, 80, 93, 82, 116, 75, 106, 98, 119, 120,
            111, 103, 104, 102, 83, 113, 105, 107, 110, 117, 109
        };
        Board board = new Board(tiles);
        NumberPuzzleSolver solver = new NumberPuzzleSolver(board);
        assertTrue(solver.solve(), "11x11方阵应该可以求解");
    }
}