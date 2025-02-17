package com.rubik.toll.back.rubik.solver.bottom;

import com.rubik.toll.back.rubik.Color;
import com.rubik.toll.back.rubik.Cube;
import com.rubik.toll.back.rubik.Face;
import com.rubik.toll.back.rubik.solver.LayerSolver;
import com.rubik.toll.back.rubik.solver.MiddleLayerSolver;

public class BottomCornerSolver extends LayerSolver {
    public BottomCornerSolver(Cube cube) {
        super(cube);
    }

    @Override
    public String getName() {
        return "底层角块求解器";
    }

    @Override
    protected void doSolve() {
        // 处理底层的四个角块
        for (int i = 0; i < 4; i++) {
            // 找到白色角块并将其移动到正确位置
            boolean cornerPlaced = false;
            while (!cornerPlaced) {
                // 检查顶层的角块
                if (hasWhiteCorner(Face.UP)) {
                    // 将白色角块移动到正确位置
                    alignCornerWithTarget();
                    insertCorner();
                    cornerPlaced = true;
                } else {
                    // 如果顶层没有白色角块，从底层错误位置提取一个
                    extractCornerFromBottom();
                }
            }
            // 旋转底面，处理下一个角块位置
            rotateFace(Face.DOWN, true);
        }
    }

    private boolean hasWhiteCorner(Face face) {
        // 检查指定面的四个角是否有白色
        return cube.getColor(face, 0, 0) == Color.WHITE ||
                cube.getColor(face, 0, 2) == Color.WHITE ||
                cube.getColor(face, 2, 0) == Color.WHITE ||
                cube.getColor(face, 2, 2) == Color.WHITE;
    }

    private void alignCornerWithTarget() {
        // 旋转顶层，使白色角块对准目标位置
        while (!isCornerAligned()) {
            rotateFace(Face.UP, true);
        }
    }

    private boolean isCornerAligned() {
        // 检查角块的颜色是否与目标位置匹配
        Color frontColor = cube.getColor(Face.FRONT, 1, 1);
        Color rightColor = cube.getColor(Face.RIGHT, 1, 1);

        // 获取当前角块的三个面的颜色
        Color c1 = cube.getColor(Face.UP, 2, 2);
        Color c2 = cube.getColor(Face.FRONT, 0, 2);
        Color c3 = cube.getColor(Face.RIGHT, 0, 0);

        return (c1 == Color.WHITE || c2 == Color.WHITE || c3 == Color.WHITE) &&
                (c1 == frontColor || c2 == frontColor || c3 == frontColor) &&
                (c1 == rightColor || c2 == rightColor || c3 == rightColor);
    }

    private void insertCorner() {
        // 使用角块插入公式：R U R' U'
        for (int i = 0; i < 3; i++) {
            rotateFace(Face.RIGHT, true);
            rotateFace(Face.UP, true);
            rotateFace(Face.RIGHT, false);
            rotateFace(Face.UP, false);
        }
    }

    private void extractCornerFromBottom() {
        // 将错误位置的角块提取到顶层
        rotateFace(Face.RIGHT, true);
        rotateFace(Face.UP, true);
        rotateFace(Face.RIGHT, false);
        rotateFace(Face.UP, false);
    }

    protected boolean isSolved() {
        // 首先检查底面是否都是白色
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (cube.getColor(Face.DOWN, i, j) != Color.WHITE) {
                    return false;
                }
            }
        }

        // 检查四个侧面的底层颜色是否与中心块匹配
        Face[] faces = {Face.FRONT, Face.RIGHT, Face.BACK, Face.LEFT};
        for (Face face : faces) {
            Color centerColor = cube.getColor(face, 1, 1);
            if (cube.getColor(face, 2, 0) != centerColor ||
                    cube.getColor(face, 2, 1) != centerColor ||
                    cube.getColor(face, 2, 2) != centerColor) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected LayerSolver getPreviousSolver() {
        return new BottomCrossSolver(cube);
    }

    @Override
    public LayerSolver getNextSolver() {
        return new MiddleLayerSolver(cube);
    }
}