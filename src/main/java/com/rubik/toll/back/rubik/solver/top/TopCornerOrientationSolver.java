package com.rubik.toll.back.rubik.solver.top;

import com.rubik.toll.back.rubik.cube.Color;
import com.rubik.toll.back.rubik.cube.Cube;
import com.rubik.toll.back.rubik.cube.Face;
import com.rubik.toll.back.rubik.solver.LayerSolver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TopCornerOrientationSolver extends LayerSolver {
    private static final Logger logger = LogManager.getLogger(TopCornerOrientationSolver.class);

    public TopCornerOrientationSolver(Cube cube) {
        super(cube);
    }

    @Override
    public String getName() {
        return "顶层角块方向求解器";
    }

    @Override
    protected void doSolve() {
        // 调整所有角块的方向
        for (int i = 0; i < 4; i++) {
            while (cube.getColor(Face.UP, 2, 2) != Color.YELLOW) {
                executeCornerTwistAlgorithm();
            }
            rotateFace(Face.UP, true);
        }
    }

    private void executeCornerTwistAlgorithm() {
        // R U R' U' (重复三次)
        for (int i = 0; i < 3; i++) {
            rotateFace(Face.RIGHT, true);
            rotateFace(Face.UP, true);
            rotateFace(Face.RIGHT, false);
            rotateFace(Face.UP, false);
        }
    }

    @Override
    protected boolean isSolved() {
        // 检查顶面是否都是黄色
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (cube.getColor(Face.UP, i, j) != Color.YELLOW) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected LayerSolver getPreviousSolver() {
        return new TopCornersSolver(cube);
    }

    @Override
    public LayerSolver getNextSolver() {
        return null;
    }
}