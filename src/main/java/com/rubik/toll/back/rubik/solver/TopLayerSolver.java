package com.rubik.toll.back.rubik.solver;

import com.rubik.toll.back.rubik.Cube;
import com.rubik.toll.back.rubik.Face;
import com.rubik.toll.back.rubik.Color;

public class TopLayerSolver extends LayerSolver {
    public TopLayerSolver(Cube cube) {
        super(cube);
    }

    @Override
    public String getName() {
        return "顶层求解器";
    }

    @Override
    protected void doSolve() {
        // 第一步：形成顶层黄色十字
        solveTopCross();

        // 第二步：调整顶层边块位置
        solveTopEdges();

        // 第三步：调整顶层角块位置
        solveTopCorners();

        // 第四步：调整角块方向
        orientTopCorners();

        if (!isSolved()) {
            throw new IllegalStateException("求解失败");
        }
    }

    private void solveTopCross() {
        // 根据当前顶面黄色块的形状选择算法
        while (!isTopCrossFormed()) {
            if (isTopDot()) {
                // 只有中心是黄色的情况
                executeTopCrossAlgorithm();
            } else if (isTopL()) {
                // L形状的情况
                alignTopL();
                executeTopCrossAlgorithm();
            } else if (isTopLine()) {
                // 一条线的情况
                alignTopLine();
                executeTopCrossAlgorithm();
            }
        }
    }

    private void solveTopEdges() {
        // 调整顶层边块到正确位置
        while (!areTopEdgesAligned()) {
            if (countAlignedEdges() == 0) {
                // 没有对齐的边块，执行基本算法
                executeEdgePermutationAlgorithm();
            } else {
                // 有一个对齐的边块，将其放到正确位置
                alignSolvedEdge();
                executeEdgePermutationAlgorithm();
            }
        }
    }

    private void solveTopCorners() {
        // 调整顶层角块到正确位置
        while (!areTopCornersAligned()) {
            if (hasAlignedCorner()) {
                alignSolvedCorner();
            }
            executeCornerPermutationAlgorithm();
        }
    }

    private void orientTopCorners() {
        // 调整所有角块的方向
        for (int i = 0; i < 4; i++) {
            while (cube.getColor(Face.UP, 2, 2) != Color.YELLOW) {
                executeCornerTwistAlgorithm();
            }
            rotateFace(Face.UP, true);
        }
    }

    private boolean isTopCrossFormed() {
        return cube.getColor(Face.UP, 0, 1) == Color.YELLOW &&
                cube.getColor(Face.UP, 1, 0) == Color.YELLOW &&
                cube.getColor(Face.UP, 1, 2) == Color.YELLOW &&
                cube.getColor(Face.UP, 2, 1) == Color.YELLOW;
    }

    private boolean isTopDot() {
        return cube.getColor(Face.UP, 1, 1) == Color.YELLOW &&
                cube.getColor(Face.UP, 0, 1) != Color.YELLOW &&
                cube.getColor(Face.UP, 1, 0) != Color.YELLOW &&
                cube.getColor(Face.UP, 1, 2) != Color.YELLOW &&
                cube.getColor(Face.UP, 2, 1) != Color.YELLOW;
    }

    private boolean isTopL() {
        // 检查是否形成L形状
        return (cube.getColor(Face.UP, 1, 1) == Color.YELLOW &&
                cube.getColor(Face.UP, 1, 0) == Color.YELLOW &&
                cube.getColor(Face.UP, 2, 1) == Color.YELLOW);
    }

    private boolean isTopLine() {
        // 检查是否形成一条线
        return (cube.getColor(Face.UP, 1, 0) == Color.YELLOW &&
                cube.getColor(Face.UP, 1, 1) == Color.YELLOW &&
                cube.getColor(Face.UP, 1, 2) == Color.YELLOW);
    }

    private void executeTopCrossAlgorithm() {
        // F R U R' U' F'
        rotateFace(Face.FRONT, true);
        rotateFace(Face.RIGHT, true);
        rotateFace(Face.UP, true);
        rotateFace(Face.RIGHT, false);
        rotateFace(Face.UP, false);
        rotateFace(Face.FRONT, false);
    }

    private void alignTopL() {
        // 旋转顶层，使L形状对准正确位置
        while (!isTopL()) {
            rotateFace(Face.UP, true);
        }
    }

    private void alignTopLine() {
        // 旋转顶层，使直线处于水平位置
        while (!isTopLine()) {
            rotateFace(Face.UP, true);
        }
    }

    private boolean areTopEdgesAligned() {
        // 检查所有顶层边块是否对齐
        for (Face face : new Face[]{Face.FRONT, Face.RIGHT, Face.BACK, Face.LEFT}) {
            if (cube.getColor(face, 0, 1) != cube.getColor(face, 1, 1)) {
                return false;
            }
        }
        return true;
    }

    private int countAlignedEdges() {
        // 计算已对齐的边块数量
        int count = 0;
        for (Face face : new Face[]{Face.FRONT, Face.RIGHT, Face.BACK, Face.LEFT}) {
            if (cube.getColor(face, 0, 1) == cube.getColor(face, 1, 1)) {
                count++;
            }
        }
        return count;
    }

    private void executeEdgePermutationAlgorithm() {
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

    private void alignSolvedEdge() {
        // 旋转顶层，将已解决的边块放到后面
        while (cube.getColor(Face.FRONT, 0, 1) == cube.getColor(Face.FRONT, 1, 1)) {
            rotateFace(Face.UP, true);
        }
    }

    private boolean areTopCornersAligned() {
        // 检查所有顶层角块是否在正确位置
        for (Face face : new Face[]{Face.FRONT, Face.RIGHT, Face.BACK, Face.LEFT}) {
            if (cube.getColor(face, 0, 0) != cube.getColor(face, 1, 1) ||
                    cube.getColor(face, 0, 2) != cube.getColor(face, 1, 1)) {
                return false;
            }
        }
        return true;
    }

    private boolean hasAlignedCorner() {
        // 检查是否有已对齐的角块
        for (Face face : new Face[]{Face.FRONT, Face.RIGHT, Face.BACK, Face.LEFT}) {
            if (cube.getColor(face, 0, 0) == cube.getColor(face, 1, 1) &&
                    cube.getColor(face, 0, 2) == cube.getColor(face, 1, 1)) {
                return true;
            }
        }
        return false;
    }

    private void alignSolvedCorner() {
        // 旋转顶层，将已解决的角块放到后面
        while (cube.getColor(Face.FRONT, 0, 0) == cube.getColor(Face.FRONT, 1, 1) &&
                cube.getColor(Face.FRONT, 0, 2) == cube.getColor(Face.FRONT, 1, 1)) {
            rotateFace(Face.UP, true);
        }
    }

    private void executeCornerPermutationAlgorithm() {
        // R U R' U' R' F R2 U' R' U' R U R' F'
        rotateFace(Face.RIGHT, true);
        rotateFace(Face.UP, true);
        rotateFace(Face.RIGHT, false);
        rotateFace(Face.UP, false);
        rotateFace(Face.RIGHT, false);
        rotateFace(Face.FRONT, true);
        rotateFace(Face.RIGHT, true);
        rotateFace(Face.RIGHT, true);
        rotateFace(Face.UP, false);
        rotateFace(Face.RIGHT, false);
        rotateFace(Face.UP, false);
        rotateFace(Face.RIGHT, true);
        rotateFace(Face.UP, true);
        rotateFace(Face.RIGHT, false);
        rotateFace(Face.FRONT, false);
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

    protected boolean isSolved() {
        // 检查顶面是否都是黄色
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (cube.getColor(Face.UP, i, j) != Color.YELLOW) {
                    return false;
                }
            }
        }

        // 检查四个侧面的顶层颜色是否与中心块匹配
        Face[] faces = {Face.FRONT, Face.RIGHT, Face.BACK, Face.LEFT};
        for (Face face : faces) {
            Color centerColor = cube.getColor(face, 1, 1);
            if (cube.getColor(face, 0, 0) != centerColor ||
                    cube.getColor(face, 0, 1) != centerColor ||
                    cube.getColor(face, 0, 2) != centerColor) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected LayerSolver getPreviousSolver() {
        return new MiddleLayerSolver(cube);
    }

    @Override
    public LayerSolver getNextSolver() {
        return null;
    }
}