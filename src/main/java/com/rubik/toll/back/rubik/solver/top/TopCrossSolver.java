package com.rubik.toll.back.rubik.solver.top;

import com.rubik.toll.back.rubik.cube.Cube;
import com.rubik.toll.back.rubik.cube.Face;
import com.rubik.toll.back.rubik.solver.LayerSolver;
import com.rubik.toll.back.rubik.solver.middle.MiddleLayerSolver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TopCrossSolver extends LayerSolver {
    private static final Logger logger = LogManager.getLogger(TopCrossSolver.class);

    public TopCrossSolver(Cube cube) {
        super(cube);
    }

    @Override
    public String getName() {
        return "顶层十字求解器";
    }

    @Override
    protected void doSolve() {
        // 根据当前顶面黄色块的形状选择算法
        int a = 0;
        while (!isSolved()) {
            if (++a > 4) throw new IllegalArgumentException();

            if (isTopDot()) {
                // 只有中心是黄色的情况
                executeTopCross();
            } else if (isTopL()) {
                // L形状的情况
                executeTopCross();
            } else {
                // 一条线的情况
                alignTopLine();
                executeTopCross();
            }
            logger.debug("{}处理黄色十字的结果{}", a, cube);
        }
    }

    private boolean isTopDot() {
        return cube.getColor(Face.UP, 0, 1) != yellow &&
                cube.getColor(Face.UP, 1, 0) != yellow &&
                cube.getColor(Face.UP, 1, 2) != yellow &&
                cube.getColor(Face.UP, 2, 1) != yellow;
    }

    private boolean isTopL() {
        // 检查是否形成L形状（四种可能的方向）
        boolean down = cube.getColor(Face.UP, 2, 1) == yellow;
        boolean left = cube.getColor(Face.UP, 1, 0) == yellow;
        boolean right = cube.getColor(Face.UP, 1, 2) == yellow;
        boolean up = cube.getColor(Face.UP, 0, 1) == yellow;

        boolean leftDown = left && down;
        if (leftDown) {
            rotateFace(Face.UP, true);
            return true;
        }
        boolean rightDown = right && down;
        if (rightDown) {
            rotateFace(Face.UP, false);
            rotateFace(Face.UP, false);
            return true;
        }
        boolean rightUp = right && up;
        if (rightUp) {
            rotateFace(Face.UP, false);
            return true;
        }
        return left && up;
    }

    private boolean isTopLine() {
        return cube.getColor(Face.UP, 1, 0) == yellow &&
                cube.getColor(Face.UP, 1, 2) == yellow;
    }

    private void executeTopCross() {
        // F R U R' U' F'
        rotateFace(Face.FRONT, true);
        rotateFace(Face.RIGHT, true);
        rotateFace(Face.UP, true);
        rotateFace(Face.RIGHT, false);
        rotateFace(Face.UP, false);
        rotateFace(Face.FRONT, false);

        logger.debug("executeTopCrossAlgorithm: {}", cube);
    }

    private void alignTopLine() {
        // 旋转顶层，使直线处于水平位置
        if (!isTopLine()) {
            rotateFace(Face.UP, true);
            logger.debug("alignTopLine: {}", cube);
        }
    }

    @Override
    protected boolean isSolved() {
        return cube.getColor(Face.UP, 0, 1) == yellow &&
                cube.getColor(Face.UP, 1, 0) == yellow &&
                cube.getColor(Face.UP, 1, 2) == yellow &&
                cube.getColor(Face.UP, 2, 1) == yellow;
    }

    @Override
    protected LayerSolver getPreviousSolver() {
        return new MiddleLayerSolver(cube);
    }

    @Override
    public LayerSolver getNextSolver() {
        return new TopFaceSolver(cube);
    }
}