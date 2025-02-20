package com.rubik.toll.back.rubik.solver.top;

import com.rubik.toll.back.rubik.cube.Cube;
import com.rubik.toll.back.rubik.cube.Face;
import com.rubik.toll.back.rubik.solver.LayerSolver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TopFaceSolver extends LayerSolver {
    private static final Logger logger = LogManager.getLogger(TopFaceSolver.class);

    public TopFaceSolver(Cube cube) {
        super(cube);
    }

    @Override
    public String getName() {
        return "顶层黄面求解器";
    }

    @Override
    protected void doSolve() {
        int a = 0;
        // 调整顶层边块到正确位置
        while (!isSolved()) {
            if (++a > 6) throw new IllegalStateException();
            int yellowCornerOnTop = countYellowCornerOnTop();
            if (yellowCornerOnTop == 1) { // 小鱼
                if (onRightSide())
                    rightHandAlgorithm();
                else
                    leftHandAlgorithm();
            } else if (yellowCornerOnTop == 2) {
                align2NotYellow();
                rightHandAlgorithm();
            } else if (yellowCornerOnTop == 0) {
                align4NotYellow();
                rightHandAlgorithm();
            }
            logger.debug("{} 处理黄色大面的结果{}", a, cube);
        }
    }

    private void align2NotYellow() {
        logger.debug("对齐\"2前4左\" 2");
        // 对齐"2前4左" 旋转顶层，直到两个黄色角块在前面
        int a = 0;
        while (cube.getColor(Face.FRONT, 0, 0) != yellow) {
            rotateFace(Face.UP, true);

            if (++a > 4) throw new IllegalStateException();
        }
    }

    private void align4NotYellow() {
        logger.debug("对齐\"2前4左\" 4");
        // 对齐"2前4左" 旋转顶层，直到四个黄色角块中有一个在左边
        while (cube.getColor(Face.LEFT, 0, 2) != yellow) {
            rotateFace(Face.UP, true);
        }
    }

    private boolean onRightSide() {
        if (countYellowCornerOnTop() != 1) {
            throw new IllegalStateException("并非小鱼状态");
        }
        if (cube.getColor(Face.UP, 0, 0) == yellow) {
            if (cube.getColor(Face.LEFT, 0, 2) == yellow) {
                rotateFace(Face.UP, false);
                return true;
            }
            if (cube.getColor(Face.BACK, 0, 0) == yellow) {
                rotateFace(Face.UP, true);
                rotateFace(Face.UP, true);
                return false;
            }
        }

        if (cube.getColor(Face.UP, 0, 2) == yellow) {
            if (cube.getColor(Face.BACK, 0, 2) == yellow) {
                rotateFace(Face.UP, false);
                rotateFace(Face.UP, false);
                return true;
            }
            if (cube.getColor(Face.RIGHT, 0, 0) == yellow) {
                rotateFace(Face.UP, true);
                return false;
            }
        }

        if (cube.getColor(Face.UP, 2, 2) == yellow) {
            if (cube.getColor(Face.RIGHT, 0, 2) == yellow) {
                rotateFace(Face.UP, true);
                return true;
            }
            if (cube.getColor(Face.FRONT, 0, 0) == yellow) {
                return false;
            }
        }

        if (cube.getColor(Face.UP, 2, 0) == yellow) {
            if (cube.getColor(Face.FRONT, 0, 2) == yellow) {
                rotateFace(Face.UP, true);
                return true;
            }
            if (cube.getColor(Face.LEFT, 0, 0) == yellow) {
                rotateFace(Face.UP, false);
                return false;
            }
        }
        throw new IllegalStateException("");
    }

    private void leftHandAlgorithm() {
        // L' U' L U' L' U'2 L
        rotateFace(Face.LEFT, false);
        rotateFace(Face.UP, false);
        rotateFace(Face.LEFT, true);
        rotateFace(Face.UP, false);
        rotateFace(Face.LEFT, false);
        rotateFace(Face.UP, false);
        rotateFace(Face.UP, false);
        rotateFace(Face.LEFT, true);
    }

    private int countYellowCornerOnTop() {
        int count = 0;
        Face face = Face.UP;
        if (cube.getColor(face, 0, 0) == yellow) {
            count++;
        }
        if (cube.getColor(face, 0, 2) == yellow) {
            count++;
        }
        if (cube.getColor(face, 2, 0) == yellow) {
            count++;
        }
        if (cube.getColor(face, 2, 2) == yellow) {
            count++;
        }
        return count;
    }

    private void rightHandAlgorithm() {
        // R U R' U R U2 R'
        rotateFace(Face.RIGHT, true);
        rotateFace(Face.UP, true);
        rotateFace(Face.RIGHT, false);
        rotateFace(Face.UP, true);
        rotateFace(Face.RIGHT, true);
        rotateFace(Face.UP, true);
        rotateFace(Face.UP, true);
        rotateFace(Face.RIGHT, false);
    }

    @Override
    protected boolean isSolved() {
        return cube.getColor(Face.UP, 0, 0) == yellow
                && cube.getColor(Face.UP, 0, 2) == yellow
                && cube.getColor(Face.UP, 2, 2) == yellow
                && cube.getColor(Face.UP, 2, 0) == yellow;
    }

    @Override
    protected LayerSolver getPreviousSolver() {
        return new TopCrossSolver(cube);
    }

    @Override
    public LayerSolver getNextSolver() {
        return new TopCornersSolver(cube);
    }
}