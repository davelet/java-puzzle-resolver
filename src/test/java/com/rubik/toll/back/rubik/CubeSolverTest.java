package com.rubik.toll.back.rubik;

import com.rubik.toll.back.rubik.cube.Cube;
import com.rubik.toll.back.rubik.cube.Face;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CubeSolverTest {
    private Cube cube;

    private CubeSolver cubeSolver;
    private CubeShuffler cubeShuffler;

    @BeforeEach
    void setUp() {
        cube = new Cube();
        cubeSolver = new CubeSolver(cube);
        cubeShuffler = new CubeShuffler(cube);
    }


    @Test
    void solve_NoNeedExecuteBottomCrossSolver() {

        // 执行测试
        cubeSolver.solve();

        // 验证结果
        assertTrue(cube.isSolved());
    }

    @Test
    void solve_ExecuteBottomCrossSolver() {
        // 打乱魔方
//        cubeShuffler.shuffle(1);
        cubeShuffler.rotateFace(Face.BACK, TwistDirection.CLOCKWISE);
        // 执行测试
        cubeSolver.solve();
        // 验证结果
        assertTrue(cube.isSolved());
    }

    @Test
    void solve_ExecuteBottomCornerSolver() {
        for (int a = 0; a < 20; a++) {
            System.out.println("第轮测试 " + a + "<UNK>");
            // 打乱魔方
            cubeShuffler.shuffle(20);
            // 执行测试
            cubeSolver.solve();
            // 验证结果
            assertTrue(cube.isSolved());
        }
    }
}