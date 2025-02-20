package com.rubik.toll.back.rubik.solver;

import com.rubik.toll.back.rubik.Cube;
import com.rubik.toll.back.rubik.CubeShuffler;
import com.rubik.toll.back.rubik.Face;
import com.rubik.toll.back.rubik.TwistDirection;

public abstract class LayerSolver {
    protected final Cube cube;
    private final CubeShuffler cubeShuffler;

    protected LayerSolver(Cube cube) {
        if (cube == null) {
            throw new IllegalArgumentException("魔方实例不能为空");
        }
        this.cube = cube;
        cubeShuffler = new CubeShuffler(cube);
    }

    public abstract String getName();

    public void solve() {
        if (!isPreviousSolved())
            throw new IllegalStateException("Previous layer not solved");
        if (isSolved())
            return;
        doSolve();
    }

    protected abstract void doSolve();

    protected abstract boolean isSolved();

    private boolean isPreviousSolved() {
        LayerSolver previousSolver = getPreviousSolver();
        if (previousSolver == null) {
            return true;
        }
        return previousSolver.isSolved();
    }

    protected abstract LayerSolver getPreviousSolver();

    public abstract LayerSolver getNextSolver();

    protected void rotateFace(Face face, boolean clockwise) {
        cubeShuffler.rotateFace(face, TwistDirection.of(clockwise));
    }

    protected Face getLeftSide(Face face) {
        return switch (face) {
            case FRONT -> Face.LEFT;
            case RIGHT -> Face.FRONT;
            case BACK -> Face.RIGHT;
            case LEFT -> Face.BACK;
            default -> throw new IllegalArgumentException("无效的面");
        };
    }

    protected Face getRightSide(Face face) {
        return switch (face) {
            case FRONT -> Face.RIGHT;
            case RIGHT -> Face.BACK;
            case BACK -> Face.LEFT;
            case LEFT -> Face.FRONT;
            default -> throw new IllegalArgumentException("无效的面");
        };
    }

    protected int[] getUpCenter(Face face) {
        return switch (face) {
            case FRONT -> new int[]{2, 1};
            case RIGHT -> new int[]{1, 2};
            case BACK -> new int[]{0, 1};
            case LEFT -> new int[]{1, 0};
            default -> throw new IllegalArgumentException("无效的面");
        };
    }

    protected int[] getDownCenter(Face face) {
        return switch (face) {
            case FRONT -> new int[]{0, 1};
            case RIGHT -> new int[]{1, 2};
            case BACK -> new int[]{2, 1};
            case LEFT -> new int[]{1, 0};
            default -> throw new IllegalArgumentException("无效的面");
        };
    }
}
