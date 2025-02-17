package com.rubik.toll.back.rubik;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CubeShuffler {
    private static final Logger logger = LogManager.getLogger(CubeShuffler.class);
    private final Random random = new Random();
    private final Cube cube;

    public CubeShuffler(Cube cube) {
        if (cube == null) {
            throw new IllegalArgumentException("魔方实例不能为空");
        }
        this.cube = cube;
    }

    public Cube shuffle(int times) {
        if (times < 0) {
            throw new IllegalArgumentException("打乱次数不能为负数");
        }

        for (int i = 0; i < times; i++) {
            // 随机选择一个面
            Face face = Face.values()[random.nextInt(6)];
            // 随机选择旋转方向（true为顺时针，false为逆时针）
            boolean clockwise = random.nextBoolean();
            TwistDirection twistDirection = TwistDirection.of(clockwise);

            rotateFace(face, twistDirection);
        }
        return cube;
    }

    public void rotateFace(Face face, TwistDirection direction) {
        logger.info("旋转{}, 方向{}", face, direction);
        // 保存当前面的状态
        Color[][] currentFace = new Color[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                currentFace[i][j] = cube.getColor(face, i, j);
            }
        }

        // 旋转当前面
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // 矩阵转置的计算
                if (direction.isClockwise()) {
                    cube.setColor(face, i, j, currentFace[2 - j][i]);
                } else {
                    cube.setColor(face, i, j, currentFace[j][2 - i]);
                }
            }
        }

        // 旋转相邻的边
        rotateAdjacentEdges(face, direction);
    }

    private void rotateAdjacentEdges(Face face, TwistDirection direction) {
        Color[] temp = new Color[3];

        switch (face) {
            case UP:
                // 保存前面的边
                for (int i = 0; i < 3; i++) {
                    temp[i] = cube.getColor(Face.FRONT, 0, i);
                }

                if (direction.isClockwise()) {
                    // 前 -> 右 -> 后 -> 左 -> 前
                    for (int i = 0; i < 3; i++) {
                        cube.setColor(Face.FRONT, 0, i, cube.getColor(Face.RIGHT, 0, i));
                        cube.setColor(Face.RIGHT, 0, i, cube.getColor(Face.BACK, 0, i));
                        cube.setColor(Face.BACK, 0, i, cube.getColor(Face.LEFT, 0, i));
                        cube.setColor(Face.LEFT, 0, i, temp[i]);
                    }
                } else {
                    // 前 <- 右 <- 后 <- 左 <- 前
                    for (int i = 0; i < 3; i++) {
                        cube.setColor(Face.FRONT, 0, i, cube.getColor(Face.LEFT, 0, i));
                        cube.setColor(Face.LEFT, 0, i, cube.getColor(Face.BACK, 0, i));
                        cube.setColor(Face.BACK, 0, i, cube.getColor(Face.RIGHT, 0, i));
                        cube.setColor(Face.RIGHT, 0, i, temp[i]);
                    }
                }
                break;

            case DOWN:
                // 保存前面的边
                for (int i = 0; i < 3; i++) {
                    temp[i] = cube.getColor(Face.FRONT, 2, i);
                }

                if (direction.isClockwise()) {
                    // 前 <- 左 <- 后 <- 右 <- 前
                    for (int i = 0; i < 3; i++) {
                        cube.setColor(Face.FRONT, 2, i, cube.getColor(Face.LEFT, 2, i));
                        cube.setColor(Face.LEFT, 2, i, cube.getColor(Face.BACK, 2, i));
                        cube.setColor(Face.BACK, 2, i, cube.getColor(Face.RIGHT, 2, i));
                        cube.setColor(Face.RIGHT, 2, i, temp[i]);
                    }
                } else {
                    // 前 -> 左 -> 后 -> 右 -> 前
                    for (int i = 0; i < 3; i++) {
                        cube.setColor(Face.FRONT, 2, i, cube.getColor(Face.RIGHT, 2, i));
                        cube.setColor(Face.RIGHT, 2, i, cube.getColor(Face.BACK, 2, i));
                        cube.setColor(Face.BACK, 2, i, cube.getColor(Face.LEFT, 2, i));
                        cube.setColor(Face.LEFT, 2, i, temp[i]);
                    }
                }
                break;

            case FRONT:
                // 保存上面的边
                for (int i = 0; i < 3; i++) {
                    temp[i] = cube.getColor(Face.UP, 2, i);
                }

                if (direction.isClockwise()) {
                    // 上 -> 右 -> 下 -> 左 -> 上
                    for (int i = 0; i < 3; i++) {
                        cube.setColor(Face.UP, 2, i, cube.getColor(Face.LEFT, 2 - i, 2));
                        cube.setColor(Face.LEFT, 2 - i, 2, cube.getColor(Face.DOWN, 0, 2 - i));
                        cube.setColor(Face.DOWN, 0, 2 - i, cube.getColor(Face.RIGHT, i, 0));
                        cube.setColor(Face.RIGHT, i, 0, temp[i]);
                    }
                } else {
                    // 上 <- 右 <- 下 <- 左 <- 上
                    for (int i = 0; i < 3; i++) {
                        cube.setColor(Face.UP, 2, i, cube.getColor(Face.RIGHT, i, 0));
                        cube.setColor(Face.RIGHT, i, 0, cube.getColor(Face.DOWN, 0, 2 - i));
                        cube.setColor(Face.DOWN, 0, 2 - i, cube.getColor(Face.LEFT, 2 - i, 2));
                        cube.setColor(Face.LEFT, 2 - i, 2, temp[i]);
                    }
                }
                break;

            case BACK:
                // 保存上面的边
                for (int i = 0; i < 3; i++) {
                    temp[i] = cube.getColor(Face.UP, 0, i);
                }

                if (direction.isClockwise()) {
                    // 上 <- 右 <- 下 <- 左 <- 上
                    for (int i = 0; i < 3; i++) {
                        cube.setColor(Face.UP, 0, i, cube.getColor(Face.RIGHT, i, 2));
                        cube.setColor(Face.RIGHT, i, 2, cube.getColor(Face.DOWN, 2, 2 - i));
                        cube.setColor(Face.DOWN, 2, 2 - i, cube.getColor(Face.LEFT, 2 - i, 0));
                        cube.setColor(Face.LEFT, 2 - i, 0, temp[i]);
                    }
                } else {
                    // 上 -> 右 -> 下 -> 左 -> 上
                    for (int i = 0; i < 3; i++) {
                        cube.setColor(Face.UP, 0, i, cube.getColor(Face.LEFT, 2 - i, 0));
                        cube.setColor(Face.LEFT, 2 - i, 0, cube.getColor(Face.DOWN, 2, 2 - i));
                        cube.setColor(Face.DOWN, 2, 2 - i, cube.getColor(Face.RIGHT, i, 2));
                        cube.setColor(Face.RIGHT, i, 2, temp[i]);
                    }
                }
                break;

            case LEFT:
                // 保存上面的边
                for (int i = 0; i < 3; i++) {
                    temp[i] = cube.getColor(Face.UP, i, 0);
                }

                if (direction.isClockwise()) {
                    // 上 -> 前 -> 下 -> 后 -> 上
                    for (int i = 0; i < 3; i++) {
                        cube.setColor(Face.UP, i, 0, cube.getColor(Face.BACK, 2 - i, 2));
                        cube.setColor(Face.BACK, 2 - i, 2, cube.getColor(Face.DOWN, i, 0));
                        cube.setColor(Face.DOWN, i, 0, cube.getColor(Face.FRONT, i, 0));
                        cube.setColor(Face.FRONT, i, 0, temp[i]);
                    }
                } else {
                    // 上 <- 前 <- 下 <- 后 <- 上
                    for (int i = 0; i < 3; i++) {
                        cube.setColor(Face.UP, i, 0, cube.getColor(Face.FRONT, i, 0));
                        cube.setColor(Face.FRONT, i, 0, cube.getColor(Face.DOWN, i, 0));
                        cube.setColor(Face.DOWN, i, 0, cube.getColor(Face.BACK, 2 - i, 2));
                        cube.setColor(Face.BACK, 2 - i, 2, temp[i]);
                    }
                }
                break;

            case RIGHT:
                // 保存上面的边
                for (int i = 0; i < 3; i++) {
                    temp[i] = cube.getColor(Face.UP, i, 2);
                }

                if (direction.isClockwise()) {
                    // 上 <- 前 <- 下 <- 后 <- 上
                    for (int i = 0; i < 3; i++) {
                        cube.setColor(Face.UP, i, 2, cube.getColor(Face.FRONT, i, 2));
                        cube.setColor(Face.FRONT, i, 2, cube.getColor(Face.DOWN, i, 2));
                        cube.setColor(Face.DOWN, i, 2, cube.getColor(Face.BACK, 2 - i, 0));
                        cube.setColor(Face.BACK, 2 - i, 0, temp[i]);
                    }
                } else {
                    // 上 -> 前 -> 下 -> 后 -> 上
                    for (int i = 0; i < 3; i++) {
                        cube.setColor(Face.UP, i, 2, cube.getColor(Face.BACK, 2 - i, 0));
                        cube.setColor(Face.BACK, 2 - i, 0, cube.getColor(Face.DOWN, i, 2));
                        cube.setColor(Face.DOWN, i, 2, cube.getColor(Face.FRONT, i, 2));
                        cube.setColor(Face.FRONT, i, 2, temp[i]);
                    }
                }
                break;
        }
    }
}