package com.rubik.toll.back.rubik.solver;

import com.rubik.toll.back.rubik.Cube;
import com.rubik.toll.back.rubik.Face;
import com.rubik.toll.back.rubik.Color;
import com.rubik.toll.back.rubik.solver.bottom.BottomCornerSolver;

public class MiddleLayerSolver extends LayerSolver {
    public MiddleLayerSolver(Cube cube) {
        super(cube);
    }

    @Override
    public String getName() {
        return "中层求解器";
    }

    @Override
    protected void doSolve() {
        // 处理所有中层边块
        for (int i = 0; i < 4; i++) {
            // 如果当前位置已经正确，继续下一个
            if (isEdgeCorrect(Face.FRONT)) {
                // rotateFace(Face.CUBE, true);
                continue;
            }

            // 找到并插入正确的边块
            while (!isEdgeCorrect(Face.FRONT)) {
                // 如果中层有错误的边块，先将其取出
                if (hasIncorrectEdge(Face.FRONT)) {
                    extractEdge();
                }

                // 在顶层找到目标边块
                findTargetEdge();

                // 根据边块方向选择插入算法
                if (isEdgeNeedLeftInsert()) {
                    insertEdgeLeft();
                } else {
                    insertEdgeRight();
                }
            }

            // 旋转魔方检查下一个位置
            // rotateFace(Face.CUBE, true);
        }
    }

    private boolean isEdgeCorrect(Face face) {
        Color centerColor = cube.getColor(face, 1, 1);
        Color rightCenterColor = cube.getColor(Face.RIGHT, 1, 1);

        return cube.getColor(face, 1, 2) == centerColor &&
                cube.getColor(Face.RIGHT, 1, 0) == rightCenterColor;
    }

    private boolean hasIncorrectEdge(Face face) {
        Color centerColor = cube.getColor(face, 1, 1);
        Color rightCenterColor = cube.getColor(Face.RIGHT, 1, 1);

        return cube.getColor(face, 1, 2) != centerColor ||
                cube.getColor(Face.RIGHT, 1, 0) != rightCenterColor;
    }

    private void extractEdge() {
        // 使用右插入公式将边块取出到顶层
        insertEdgeRight();
    }

    private void findTargetEdge() {
        Color frontColor = cube.getColor(Face.FRONT, 1, 1);
        Color rightColor = cube.getColor(Face.RIGHT, 1, 1);

        // 旋转顶层直到找到目标边块
        while (!((cube.getColor(Face.UP, 2, 1) == frontColor && cube.getColor(Face.FRONT, 0, 1) == rightColor) ||
                (cube.getColor(Face.UP, 2, 1) == rightColor && cube.getColor(Face.FRONT, 0, 1) == frontColor))) {
            rotateFace(Face.UP, true);
        }
    }

    private boolean isEdgeNeedLeftInsert() {
        Color frontColor = cube.getColor(Face.FRONT, 1, 1);
        return cube.getColor(Face.FRONT, 0, 1) == frontColor;
    }

    private void insertEdgeLeft() {
        // U' L' U L U F U' F'
        rotateFace(Face.UP, false);
        rotateFace(Face.LEFT, false);
        rotateFace(Face.UP, true);
        rotateFace(Face.LEFT, true);
        rotateFace(Face.UP, true);
        rotateFace(Face.FRONT, true);
        rotateFace(Face.UP, false);
        rotateFace(Face.FRONT, false);
    }

    private void insertEdgeRight() {
        // U R U' R' U' F' U F
        rotateFace(Face.UP, true);
        rotateFace(Face.RIGHT, true);
        rotateFace(Face.UP, false);
        rotateFace(Face.RIGHT, false);
        rotateFace(Face.UP, false);
        rotateFace(Face.FRONT, false);
        rotateFace(Face.UP, true);
        rotateFace(Face.FRONT, true);
    }

    protected boolean isSolved() {
        // 检查四个面的中层边块是否都正确
        Face[] faces = {Face.FRONT, Face.RIGHT, Face.BACK, Face.LEFT};
        for (Face face : faces) {
            Color centerColor = cube.getColor(face, 1, 1);
            if (cube.getColor(face, 1, 0) != centerColor ||
                    cube.getColor(face, 1, 2) != centerColor) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected LayerSolver getPreviousSolver() {
        return new BottomCornerSolver(cube);
    }

    @Override
    public LayerSolver getNextSolver() {
        return new MiddleLayerSolver(cube);
    }
}