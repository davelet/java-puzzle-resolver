package com.rubik.toll.back.rubik.solver.bottom;

import com.rubik.toll.back.rubik.Color;
import com.rubik.toll.back.rubik.Cube;
import com.rubik.toll.back.rubik.Face;
import com.rubik.toll.back.rubik.solver.LayerSolver;
import com.rubik.toll.back.rubik.solver.MiddleLayerSolver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BottomCornerSolver extends LayerSolver {
    private static final Logger logger = LogManager.getLogger(BottomCornerSolver.class);

    public BottomCornerSolver(Cube cube) {
        super(cube);
    }

    @Override
    public String getName() {
        return "底层角块求解器";
    }

    @Override
    protected void doSolve() {
        for (int i = 0; i < 4; i++) {
            // 先检查顶层的4个角块
            checkAndSolveCorner(Face.UP, 0, 0); // 左后角
            checkAndSolveCorner(Face.UP, 0, 2); // 右后角
            checkAndSolveCorner(Face.UP, 2, 0); // 左前角
            checkAndSolveCorner(Face.UP, 2, 2); // 右前角

            // 再检查底层的4个角块
            checkAndSolveCorner(Face.DOWN, 0, 0); // 左前角
            checkAndSolveCorner(Face.DOWN, 0, 2); // 右前角
            checkAndSolveCorner(Face.DOWN, 2, 0); // 左后角
            checkAndSolveCorner(Face.DOWN, 2, 2); // 右后角
        }
    }

    private void checkAndSolveCorner(Face face, int row, int col) {
        // 检查指定位置的角块是否包含白色且需要还原
        if (isWhiteCornerAndNeedsSolving(face, row, col)) {
            // 如果是底层的角块且位置错误，先将其提取到顶层
            if (face == Face.DOWN) {
                if (row == 0 && col == 0)
                    extractCornerFromBottom(Face.LEFT);
                else if (row == 0 && col == 2)
                    extractCornerFromBottom(Face.FRONT);
                else if (row == 2 && col == 0)
                    extractCornerFromBottom(Face.BACK);
                else if (row == 2 && col == 2)
                    extractCornerFromBottom(Face.RIGHT);
                else
                    throw new IllegalArgumentException("无效的底层角块位置");
                row = 2 - row;
            }
            // 旋转顶层，将角块对准目标位置
            alignCornerWithTarget(row, col);

            logger.debug("解决了底层角块位置：{} {} {}\n{}", face, row, col, cube);
        }
    }

    private boolean isWhiteCornerAndNeedsSolving(Face face, int row, int col) {
        // 获取角块的三个面的颜色
        Color[] colors = getCornerColors(face, row, col);

        // 检查是否包含白色
        boolean hasWhite = false;
        for (Color color : colors) {
            if (color == Color.WHITE) {
                hasWhite = true;
                break;
            }
        }

        // 如果不包含白色，不需要处理
        if (!hasWhite) {
            return false;
        }

        // 如果是底层角块，检查是否在正确位置
        if (face == Face.DOWN) {
            return !isBottomCornerCorrect(row, col);
        }

        // 如果是顶层角块，总是需要处理
        return true;
    }

    private Color[] getCornerColors(Face face, int row, int col) {
        if (face == Face.UP) {
            if (row == 0 && col == 0) { // 左后角
                return new Color[]{
                        cube.getColor(Face.UP, 0, 0),
                        cube.getColor(Face.BACK, 0, 2),
                        cube.getColor(Face.LEFT, 0, 0)
                };
            } else if (row == 0 && col == 2) { // 右后角
                return new Color[]{
                        cube.getColor(Face.UP, 0, 2),
                        cube.getColor(Face.BACK, 0, 0),
                        cube.getColor(Face.RIGHT, 0, 2)
                };
            } else if (row == 2 && col == 0) { // 左前角
                return new Color[]{
                        cube.getColor(Face.UP, 2, 0),
                        cube.getColor(Face.FRONT, 0, 0),
                        cube.getColor(Face.LEFT, 0, 2)
                };
            } else { // 右前角
                return new Color[]{
                        cube.getColor(Face.UP, 2, 2),
                        cube.getColor(Face.FRONT, 0, 2),
                        cube.getColor(Face.RIGHT, 0, 0)
                };
            }
        } else { // Face.DOWN
            if (row == 0 && col == 0) { // 左前角
                return new Color[]{
                        cube.getColor(Face.DOWN, 0, 0),
                        cube.getColor(Face.FRONT, 2, 0),
                        cube.getColor(Face.LEFT, 2, 2)
                };
            } else if (row == 0 && col == 2) { // 右前角
                return new Color[]{
                        cube.getColor(Face.DOWN, 0, 2),
                        cube.getColor(Face.FRONT, 2, 2),
                        cube.getColor(Face.RIGHT, 2, 0)
                };
            } else if (row == 2 && col == 0) { // 左后角
                return new Color[]{
                        cube.getColor(Face.DOWN, 2, 0),
                        cube.getColor(Face.BACK, 2, 2),
                        cube.getColor(Face.LEFT, 2, 0)
                };
            } else { // 右后角
                return new Color[]{
                        cube.getColor(Face.DOWN, 2, 2),
                        cube.getColor(Face.BACK, 2, 0),
                        cube.getColor(Face.RIGHT, 2, 2)
                };
            }
        }
    }

    private boolean isBottomCornerCorrect(int row, int col) {
        Color[] colors = getCornerColors(Face.DOWN, row, col);

        // 检查白色是否朝下
        if (colors[0] != Color.WHITE) {
            return false;
        }

        // 获取相邻面的中心块颜色
        Color frontColor = Face.FRONT.getColor();
        Color rightColor = Face.RIGHT.getColor();
        Color backColor = Face.BACK.getColor();
        Color leftColor = Face.LEFT.getColor();

        // 检查角块的其他颜色是否与中心块匹配
        if (row == 0 && col == 0) { // 左前角
            return colors[1] == frontColor && colors[2] == leftColor;
        } else if (row == 0 && col == 2) { // 右前角
            return colors[1] == frontColor && colors[2] == rightColor;
        } else if (row == 2 && col == 0) { // 左后角
            return colors[1] == backColor && colors[2] == leftColor;
        } else { // 右后角
            return colors[1] == backColor && colors[2] == rightColor;
        }
    }

    private void alignCornerWithTarget(int row0, int col0) {
        int row = row0, col = col0;

        // 然后旋转顶层，使角块对准目标位置
        while (!isCornerAligned(row, col)) {
            rotateFace(Face.UP, true);
            if (row == 0 && col == 0) {
                col = 2;
            } else if (row == 0 && col == 2) {
                row = 2;
            } else if (row == 2 && col == 2) {
                col = 0;
            } else if (row == 2 && col == 0) {
                row = 0;
            }
        }

        insertCorner(row, col);
    }

    private Face[] getCornerFaces(int row, int col) {
        if (row == 2 && col == 2) {
            return new Face[]{Face.FRONT, Face.RIGHT};
        } else if (row == 2 && col == 0) {
            return new Face[]{Face.LEFT, Face.FRONT};
        } else if (row == 0 && col == 2) {
            return new Face[]{Face.RIGHT, Face.BACK};
        } else if (row == 0 && col == 0) {
            return new Face[]{Face.BACK, Face.LEFT};
        }
        throw new IllegalArgumentException("Invalid row and col combination");
    }

    private boolean isCornerAligned(int row, int col) {
        Face[] faces = getCornerFaces(row, col);
        Face front = faces[0];
        Face right = faces[1];

        // 检查角块的颜色是否与目标位置匹配
        Color frontColor = front.getColor();
        Color rightColor = right.getColor();

        // 获取当前角块的三个面的颜色
        Color[] cornerColors = getCornerColors(Face.UP, row, col);
        Color c1 = cornerColors[0];
        Color c2 = cornerColors[1];
        Color c3 = cornerColors[2];

        return (c1 == Color.WHITE || c2 == Color.WHITE || c3 == Color.WHITE) &&
                (c1 == frontColor || c2 == frontColor || c3 == frontColor) &&
                (c1 == rightColor || c2 == rightColor || c3 == rightColor);
    }

    private void insertCorner(int row, int col) {
        Face[] faces = getCornerFaces(row, col);
        Face right = faces[1];
        boolean atBottom = false;

        // 使用角块插入公式：R U R' U'
        for (int i = 0; i < 3; i++) {
            if (atBottom) { // 换成底层的坐标
                if (isBottomCornerCorrect(2 - row, col))
                    break;
                else {
                    rotateFace(right, true);
                    rotateFace(Face.UP, true);
                    rotateFace(right, false);
                    rotateFace(Face.UP, false);
                }
            }
            rotateFace(right, true);
            rotateFace(Face.UP, true);
            rotateFace(right, false);
//            rotateFace(Face.UP, false);
            atBottom = true;
        }
    }

    private void extractCornerFromBottom(Face face) {
        // 将错误位置的角块提取到顶层
        Face right = getRightSide(face);
        rotateFace(right, true);
        rotateFace(Face.UP, true);
        rotateFace(right, false);
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
            Color centerColor = face.getColor();
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