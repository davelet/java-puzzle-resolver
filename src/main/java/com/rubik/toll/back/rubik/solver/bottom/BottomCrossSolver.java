package com.rubik.toll.back.rubik.solver.bottom;

import com.rubik.toll.back.rubik.Color;
import com.rubik.toll.back.rubik.Cube;
import com.rubik.toll.back.rubik.Face;
import com.rubik.toll.back.rubik.solver.LayerSolver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BottomCrossSolver extends LayerSolver {
    private static final Logger logger = LogManager.getLogger(BottomCrossSolver.class);

    public BottomCrossSolver(Cube cube) {
        super(cube);
    }

    @Override
    public String getName() {
        return "底层十字";
    }

    @Override
    protected void doSolve() {
        // 依次处理四个底层边块
        solveEdge(Face.FRONT);
        solveEdge(Face.RIGHT);
        solveEdge(Face.BACK);
        solveEdge(Face.LEFT);
    }

    private void solveEdge(Face face) {
        // 如果当前位置已经正确，则跳过
        if (isEdgeCorrect(face)) {
            return;
        }

        // 寻找对应的白色边块并还原。
        // 在处理每一面（实际上是面对应颜色的边块）时不用考虑是否破坏其他块，因为能够保证4种颜色分别处理完后一定是正确的。
        if (findEdgeInTop(face) || findEdgeInMiddle(face) || findEdgeInBottom(face)) {
            // 将边块插入到顶层
            rotateFace(face, true);
            rotateFace(face, true);
        }
        logger.info("处理底层和{}相交后：\n{}", face, cube);
    }

    private boolean isEdgeCorrect(Face face) {
        Color faceColor = face.getColor();
        int[] downCenter = getDownCenter(face);
        return cube.getColor(Face.DOWN, downCenter[0], downCenter[1]) == Color.WHITE &&
                cube.getColor(face, 2, 1) == faceColor;
    }

    private boolean findEdgeInTop(Face face) {
        // 在顶层寻找目标白色边块
        Color faceColor = face.getColor();

        int[] upCenter = getUpCenter(face);
        if (cube.getColor(Face.UP, upCenter[0], upCenter[1]) == Color.WHITE && cube.getColor(face, 0, 1) == faceColor) {
            return true;
        }
        if (cube.getColor(face, 0, 1) == Color.WHITE && cube.getColor(Face.UP, upCenter[0], upCenter[1]) == faceColor) {
            swapEdgeOnTop(face);
            return true;
        }

        Face rightSide = getRightSide(face);
        upCenter = getUpCenter(rightSide);
        if (cube.getColor(Face.UP, upCenter[0], upCenter[1]) == Color.WHITE
                && cube.getColor(rightSide, 0, 1) == faceColor) {
            rotateFace(Face.UP, true);
            return true;
        }
        if (cube.getColor(rightSide, 0, 1) == Color.WHITE
                && cube.getColor(Face.UP, upCenter[0], upCenter[1]) == faceColor) {
            rotateFace(Face.UP, true);
            swapEdgeOnTop(face);
            return true;
        }

        Face leftSide = getLeftSide(face);
        upCenter = getUpCenter(leftSide);
        if (cube.getColor(Face.UP, upCenter[0], upCenter[1]) == Color.WHITE
                && cube.getColor(leftSide, 0, 1) == faceColor) {
            rotateFace(Face.UP, false);
            return true;
        }
        if (cube.getColor(leftSide, 0, 1) == Color.WHITE
                && cube.getColor(Face.UP, upCenter[0], upCenter[1]) == faceColor) {
            rotateFace(Face.UP, false);
            swapEdgeOnTop(face);
            return true;
        }

        Face backSide = getRightSide(rightSide);
        upCenter = getUpCenter(backSide);
        if (cube.getColor(Face.UP, upCenter[0], upCenter[1]) == Color.WHITE
                && cube.getColor(backSide, 0, 1) == faceColor) {
            rotateFace(Face.UP, true);
            rotateFace(Face.UP, true);
            return true;
        }
        if (cube.getColor(backSide, 0, 1) == Color.WHITE
                && cube.getColor(Face.UP, upCenter[0], upCenter[1]) == faceColor) {
            rotateFace(Face.UP, true);
            rotateFace(Face.UP, true);
            swapEdgeOnTop(face);
            return true;
        }

        return false;
    }

    private void swapEdgeOnTop(Face face) {
        Face rightFace = getRightSide(face);
        rotateFace(face, true);
        rotateFace(rightFace, true);
        rotateFace(Face.UP, true);
        rotateFace(rightFace, false);
    }

    private boolean findEdgeInMiddle(Face face) {
        // 在中层寻找目标白色边块
        Color faceColor = face.getColor();

        Face leftSide = getLeftSide(face);
        // 检查左边：白色在相邻面且目标颜色在当前面
        if (cube.getColor(leftSide, 1, 2) == Color.WHITE && cube.getColor(face, 1, 0) == faceColor) {
            rotateFace(face, true);
            return true;
        }
        // 检查左边：白色在当前面且目标颜色在相邻面
        if (cube.getColor(face, 1, 0) == Color.WHITE && cube.getColor(leftSide, 1, 2) == faceColor) {
            rotateFace(leftSide, false);
            rotateFace(Face.UP, false);
            rotateFace(leftSide, true);
            return true;
        }

        Face rightSide = getRightSide(face);
        if (cube.getColor(rightSide, 1, 0) == Color.WHITE && cube.getColor(face, 1, 2) == faceColor) {
            rotateFace(face, false);
            return true;
        }
        if (cube.getColor(face, 1, 2) == Color.WHITE && cube.getColor(rightSide, 1, 0) == faceColor) {
            rotateFace(rightSide, true);
            rotateFace(Face.UP, true);
            rotateFace(rightSide, false);
            return true;
        }

        Face backSide = getRightSide(rightSide);
        if (cube.getColor(backSide, 1, 0) == Color.WHITE && cube.getColor(rightSide, 1, 2) == faceColor) {
            rotateFace(rightSide, false);
            rotateFace(Face.UP, true);
            rotateFace(rightSide, true);
            return true;
        }
        if (cube.getColor(rightSide, 1, 2) == Color.WHITE && cube.getColor(backSide, 1, 0) == faceColor) {
            rotateFace(backSide, true);
            rotateFace(Face.UP, false);
            rotateFace(backSide, false);
            rotateFace(Face.UP, false);
            return true;
        }

        if (cube.getColor(backSide, 1, 2) == Color.WHITE && cube.getColor(leftSide, 1, 0) == faceColor) {
            rotateFace(leftSide, true);
            rotateFace(Face.UP, false);
            rotateFace(leftSide, false);
            return true;
        }
        if (cube.getColor(leftSide, 1, 0) == Color.WHITE && cube.getColor(backSide, 1, 2) == faceColor) {
            rotateFace(backSide, false);
            rotateFace(Face.UP, false);
            rotateFace(backSide, true);
            rotateFace(Face.UP, false);
            return true;
        }

        return false;
    }

    private boolean findEdgeInBottom(Face face) {
        int[] downCenter = getDownCenter(face);
        Color color = face.getColor();
        if (cube.getColor(Face.DOWN, downCenter[0], downCenter[1]) == color
                && cube.getColor(face, 2, 1) == Color.WHITE) {
            rotateFace(face, true);
            rotateFace(face, true);
            return findEdgeInTop(face);
        }

        Face rightSide = getRightSide(face);
        if (checkBottomEdge(face, rightSide)) {
            return true;
        }

        Face leftSide = getLeftSide(face);
        if (checkBottomEdge(face, leftSide)) {
            return true;
        }

        Face backSide = getLeftSide(leftSide);
        if (checkBottomEdge(face, backSide)) {
            return true;
        }

        return false;
    }

    private boolean checkBottomEdge(Face currentFace, Face sideFace) {
        int[] downCenter = getDownCenter(sideFace);
        Color currentColor = currentFace.getColor();

        if ((cube.getColor(sideFace, 2, 1) == currentColor &&
                cube.getColor(Face.DOWN, downCenter[0], downCenter[1]) == Color.WHITE) ||
                (cube.getColor(sideFace, 2, 1) == Color.WHITE &&
                        cube.getColor(Face.DOWN, downCenter[0], downCenter[1]) == currentColor)) {

            rotateFace(sideFace, true);
            rotateFace(sideFace, true);
            return findEdgeInTop(currentFace);
        }
        return false;
    }

    private int[] getUpCenter(Face face) {
        return switch (face) {
            case FRONT -> new int[]{2, 1};
            case RIGHT -> new int[]{1, 2};
            case BACK -> new int[]{0, 1};
            case LEFT -> new int[]{1, 0};
            default -> throw new IllegalArgumentException("无效的面");
        };
    }

    private int[] getDownCenter(Face face) {
        return switch (face) {
            case FRONT -> new int[]{0, 1};
            case RIGHT -> new int[]{1, 2};
            case BACK -> new int[]{2, 1};
            case LEFT -> new int[]{1, 0};
            default -> throw new IllegalArgumentException("无效的面");
        };
    }

    protected boolean isSolved() {
        // 检查底面十字是否为白色
        if (cube.getColor(Face.DOWN, 0, 1) != Color.WHITE ||
                cube.getColor(Face.DOWN, 1, 0) != Color.WHITE ||
                cube.getColor(Face.DOWN, 1, 2) != Color.WHITE ||
                cube.getColor(Face.DOWN, 2, 1) != Color.WHITE) {
            return false;
        }

        // 检查边块颜色是否与中心块匹配
        return cube.getColor(Face.FRONT, 2, 1) == cube.getColor(Face.FRONT, 1, 1) &&
                cube.getColor(Face.RIGHT, 2, 1) == cube.getColor(Face.RIGHT, 1, 1) &&
                cube.getColor(Face.BACK, 2, 1) == cube.getColor(Face.BACK, 1, 1) &&
                cube.getColor(Face.LEFT, 2, 1) == cube.getColor(Face.LEFT, 1, 1);
    }

    @Override
    protected LayerSolver getPreviousSolver() {
        return null;
    }

    @Override
    public LayerSolver getNextSolver() {
        return new BottomCornerSolver(cube);
    }
}