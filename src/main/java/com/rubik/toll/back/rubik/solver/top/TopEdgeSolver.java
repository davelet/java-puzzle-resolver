package com.rubik.toll.back.rubik.solver.top;

import com.rubik.toll.back.rubik.cube.Cube;
import com.rubik.toll.back.rubik.cube.Face;
import com.rubik.toll.back.rubik.solver.LayerSolver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TopEdgeSolver extends LayerSolver {
    private static final Logger logger = LogManager.getLogger(TopEdgeSolver.class);

    public TopEdgeSolver(Cube cube) {
        super(cube);
    }

    @Override
    public String getName() {
        return "顶层棱块求解器";
    }

    @Override
    protected void doSolve() {
        int count = 0;
        while (!isSolved()) {
            if (++count > 3) throw new IllegalStateException("");

            Face face = hasAlignedEdge();
            executeEdgePermutationAlgorithm(face);
            logger.debug("第{}次调整棱块：{}", count, cube);
        }
    }

    private Face hasAlignedEdge() {
        // 检查是否有已对齐的棱块
        for (Face face : new Face[]{Face.FRONT, Face.RIGHT, Face.BACK, Face.LEFT}) {
            if (cube.getColor(face, 0, 1) == face.getColor()) {
                return face;
            }
        }
        return Face.BACK;
    }

    private void executeEdgePermutationAlgorithm(Face face) {
        // 根据传入的face参数计算需要预先旋转的次数
        int rotations = 0;
        if (face == Face.FRONT) {
            rotations = 2;
        } else if (face == Face.RIGHT) {
            rotations = 3;
        } else if (face == Face.LEFT) {
            rotations = 1;
        }

        // 预先旋转，将已完成的面转到背面
        for (int i = 0; i < rotations; i++) {
            rotateFace(Face.UP, true);
        }

        // 执行公式（上回上勾上勾上回）：R U' R U R U R U' 最后复原公式（下回下下）：R' U' R' R' https://halo.sherlocky.com/archives/magic-cube
        rotateFace(Face.RIGHT, true);
        rotateFace(Face.UP, false);
        rotateFace(Face.RIGHT, true);
        rotateFace(Face.UP, true);
        rotateFace(Face.RIGHT, true);
        rotateFace(Face.UP, true);
        rotateFace(Face.RIGHT, true);
        rotateFace(Face.UP, false);

        rotateFace(Face.RIGHT, false);
        rotateFace(Face.UP, false);
        rotateFace(Face.RIGHT, false);
        rotateFace(Face.RIGHT, false);

        // 将魔方转回原来的方向
        for (int i = 0; i < (4 - rotations) % 4; i++) {
            rotateFace(Face.UP, true);
        }
    }

    @Override
    protected boolean isSolved() {
        return cube.isSolved();
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