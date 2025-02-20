package com.rubik.toll.back.rubik.solver.top;

import com.rubik.toll.back.rubik.cube.Color;
import com.rubik.toll.back.rubik.cube.Cube;
import com.rubik.toll.back.rubik.cube.Face;
import com.rubik.toll.back.rubik.solver.LayerSolver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TopCornersSolver extends LayerSolver {
    private static final Logger logger = LogManager.getLogger(TopCornersSolver.class);

    public TopCornersSolver(Cube cube) {
        super(cube);
    }

    @Override
    public String getName() {
        return "顶层角块求解器";
    }

    @Override
    protected void doSolve() {
        // 调整顶层角块到正确位置
        int a = 0;
        boolean toFix = false;
        while (!isSolved()) {
            if (++a > 2) throw new IllegalStateException("");

            Face face = hasEyedCorner();
            if (face != null) {
                alignSolvedCorner(face);
                logger.debug("已解决的角块已对齐{}: {}", face, cube);
            }
            executeCornerPermutationAlgorithm();
            logger.debug("角块置换后: {}", cube);

            if (almostReady()) {
                toFix = true;
                break;
            }
        }
        if (toFix) {
            Color color = cube.getColor(Face.FRONT, 0, 0);
            if (color == Face.LEFT.getColor()) {
                rotateFace(Face.UP, true);
            } else if (color == Face.BACK.getColor()) {
                rotateFace(Face.UP, true);
                rotateFace(Face.UP, true);
            } else if (color == Face.RIGHT.getColor()) {
                rotateFace(Face.UP, false);
            }
            logger.debug("已解决顶层角块: {}", cube);
        }
    }

    private boolean almostReady() {
        for (Face face : new Face[]{Face.FRONT, Face.RIGHT, Face.BACK, Face.LEFT}) {
            if (cube.getColor(face, 0, 0) != cube.getColor(face, 0, 2)) {
                return false;
            }
        }
        return true;
    }

    private Face hasEyedCorner() {
        // 检查是否有"一对眼“ https://www.jianshu.com/p/91e0e4197b60
        for (Face face : new Face[]{Face.FRONT, Face.RIGHT, Face.BACK, Face.LEFT}) {
            if (cube.getColor(face, 0, 0) == cube.getColor(face, 0, 2)) {
                return face;
            }
        }
        return null;
    }

    private void alignSolvedCorner(Face face) {
        // 旋转顶层，将已解决的角块放到后面
        switch (face) {
            case RIGHT: {
                rotateFace(Face.UP, true);
                break;
            }
            case BACK: {
                rotateFace(Face.UP, true);
                rotateFace(Face.UP, true);
                break;
            }
            case LEFT: {
                rotateFace(Face.UP, false);
            }
            default: {
            }
        }
    }

    private void executeCornerPermutationAlgorithm() {
        // 使用单层旋转实现顶层角块置换
        // RB'RF2 R'BRF2 R2
        rotateFace(Face.RIGHT, true);
        rotateFace(Face.BACK, false);
        rotateFace(Face.RIGHT, true);
        rotateFace(Face.FRONT, true);
        rotateFace(Face.FRONT, true);

        rotateFace(Face.RIGHT, false);
        rotateFace(Face.BACK, true);
        rotateFace(Face.RIGHT, true);
        rotateFace(Face.FRONT, true);
        rotateFace(Face.FRONT, true);

        rotateFace(Face.RIGHT, true);
        rotateFace(Face.RIGHT, true);
    }

    @Override
    protected boolean isSolved() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (cube.getColor(Face.UP, i, j) != yellow) throw new IllegalStateException();
            }
        }
        // 检查所有顶层角块是否在正确位置
        for (Face face : new Face[]{Face.FRONT, Face.RIGHT, Face.BACK, Face.LEFT}) {
            if (cube.getColor(face, 0, 0) != face.getColor() ||
                    cube.getColor(face, 0, 2) != face.getColor()) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected LayerSolver getPreviousSolver() {
        return new TopFaceSolver(cube);
    }

    @Override
    public LayerSolver getNextSolver() {
        return new TopEdgeSolver(cube);
    }
}