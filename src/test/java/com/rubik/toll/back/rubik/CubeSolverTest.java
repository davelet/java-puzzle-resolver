package com.rubik.toll.back.rubik;

import com.rubik.toll.back.rubik.solver.bottom.BottomCrossSolver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CubeSolverTest {

    private static final Logger log = LogManager.getLogger(CubeSolverTest.class);
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
        // 打乱魔方
        cubeShuffler.shuffle(20);
        // 执行测试
        cubeSolver.solve();
        BottomCrossSolver bottomCrossSolver = new BottomCrossSolver(cube);
        bottomCrossSolver.solve();
        // 验证结果
//        assertTrue(cube.isSolved());
    }
}