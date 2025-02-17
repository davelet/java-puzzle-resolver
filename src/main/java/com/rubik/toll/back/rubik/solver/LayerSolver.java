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
}
