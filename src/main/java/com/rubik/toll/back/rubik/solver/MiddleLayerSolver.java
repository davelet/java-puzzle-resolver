package com.rubik.toll.back.rubik.solver;

import com.rubik.toll.back.rubik.Cube;
import com.rubik.toll.back.rubik.Face;
import com.rubik.toll.back.rubik.Color;
import com.rubik.toll.back.rubik.solver.bottom.BottomCornerSolver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MiddleLayerSolver extends LayerSolver {
    private static final Logger logger = LogManager.getLogger(MiddleLayerSolver.class);

    public MiddleLayerSolver(Cube cube) {
        super(cube);
    }

    @Override
    public String getName() {
        return "中层求解器";
    }

    @Override
    protected void doSolve() {
        // 按照前、右、后、左的顺序处理中层边块
        Face[] faces = {Face.FRONT, Face.RIGHT, Face.BACK, Face.LEFT};

        // 遍历每个面，找到并还原对应的中层棱块
        for (Face face : faces) {
            // 如果当前位置已经正确，跳过
            if (isEdgeCorrect(face)) {
                continue;
            }

            // 先在中层其他位置寻找目标棱块
            if (hasTargetEdgeInMiddle(face)) {
                // 如果目标棱块在中层其他位置，先将其取出到顶层
                extractTargetEdge(face);
                logger.debug("中层挪到顶层后：{}", cube);
            }

            // 在顶层找到目标边块并还原
            findTargetEdge(face);
            if (isEdgeNeedRightInsert(face)) {
                insertEdgeRight(face);
            } else {
                rotateFace(Face.UP, false);
                insertEdgeLeft(getRightSide(face));
            }
            logger.debug("中层还原{}后：{}", face, cube);
        }
    }

    private boolean hasTargetEdgeInMiddle(Face face) {
        Color targetColor = face.getColor();
        Face rightSide = getRightSide(face);
        Color rightColor = rightSide.getColor();

        // 检查其他面的中层是否有目标棱块
        Face[] faces = {Face.FRONT, Face.RIGHT, Face.BACK, Face.LEFT};
        for (Face checkFace : faces) {
            // 只检查当前面的右边即可，因为每个棱块都会被相邻的两个面共享
            if ((cube.getColor(checkFace, 1, 2) == targetColor &&
                    cube.getColor(getRightSide(checkFace), 1, 0) == rightColor) ||
                    (cube.getColor(checkFace, 1, 2) == rightColor &&
                            cube.getColor(getRightSide(checkFace), 1, 0) == targetColor)) {
                return true;
            }
        }
        return false;
    }

    private void extractTargetEdge(Face face) {
        Color targetColor = face.getColor();
        Face rightSide = getRightSide(face);
        Color rightColor = rightSide.getColor();

        Face[] faces = {Face.FRONT, Face.RIGHT, Face.BACK, Face.LEFT};
        for (Face checkFace : faces) {
            // 检查并取出右边的棱块
            if ((cube.getColor(checkFace, 1, 2) == targetColor &&
                    cube.getColor(getRightSide(checkFace), 1, 0) == rightColor) ||
                    (cube.getColor(checkFace, 1, 2) == rightColor &&
                            cube.getColor(getRightSide(checkFace), 1, 0) == targetColor)) {
                insertEdgeRight(checkFace);
                return;
            }
        }
    }

    private boolean isEdgeCorrect(Face face) {
        Color centerColor = face.getColor();
        Face rightSide = getRightSide(face);
        Color rightCenterColor = rightSide.getColor();

        return cube.getColor(face, 1, 2) == centerColor &&
                cube.getColor(rightSide, 1, 0) == rightCenterColor;
    }

    private void findTargetEdge(Face face) {
        Color frontColor = face.getColor();
        Face rightSide = getRightSide(face);
        Color rightColor = rightSide.getColor();
        int[] upCenter = getUpCenter(face);

        int count = 0;
        // 旋转顶层直到找到目标边块
        while (!((cube.getColor(Face.UP, upCenter[0], upCenter[1]) == frontColor && cube.getColor(face, 0, 1) == rightColor) ||
                (cube.getColor(Face.UP, upCenter[0], upCenter[1]) == rightColor && cube.getColor(face, 0, 1) == frontColor))) {
            rotateFace(Face.UP, true);
            logger.debug("顶层寻找中{}, 旋转：{}", face, cube);
            if (++count > 4) throw new IllegalArgumentException("too many tries");
        }
    }

    private boolean isEdgeNeedRightInsert(Face face) {
        // 检查边块的两个颜色是否与目标位置匹配
        return cube.getColor(face, 0, 1) == face.getColor();
    }

    private void insertEdgeLeft(Face face) {
        Face leftSide = getLeftSide(face);
        // U' L' U L U F U' F'
        rotateFace(Face.UP, false);
        rotateFace(leftSide, false);
        rotateFace(Face.UP, true);
        rotateFace(leftSide, true);
        rotateFace(Face.UP, true);
        rotateFace(face, true);
        rotateFace(Face.UP, false);
        rotateFace(face, false);
    }

    private void insertEdgeRight(Face face) {
        Face rightSide = getRightSide(face);
        // U R U' R' U' F' U F
        rotateFace(Face.UP, true);
        rotateFace(rightSide, true);
        rotateFace(Face.UP, false);
        rotateFace(rightSide, false);
        rotateFace(Face.UP, false);
        rotateFace(face, false);
        rotateFace(Face.UP, true);
        rotateFace(face, true);
    }

    protected boolean isSolved() {
        // 检查四个面的中层边块是否都正确
        Face[] faces = {Face.FRONT, Face.RIGHT, Face.BACK, Face.LEFT};
        for (Face face : faces) {
            Color centerColor = face.getColor();
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
        return new TopLayerSolver(cube);
    }
}