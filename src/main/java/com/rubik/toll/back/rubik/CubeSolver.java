package com.rubik.toll.back.rubik;

import com.rubik.toll.back.rubik.solver.bottom.*;
import com.rubik.toll.back.rubik.solver.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CubeSolver {
    private static final Logger logger = LogManager.getLogger(CubeSolver.class);
    private final Cube cube;

    public CubeSolver(Cube cube) {
        if (cube == null) {
            throw new IllegalArgumentException("魔方实例不能为空");
        }
        this.cube = cube;
    }

    public void solve() {
        logger.info("开始使用层先法求解魔方...初始状态：\n{}\n", cube);

        // 第一步 还原底层十字
        LayerSolver solver = new BottomCrossSolver(cube);
        do {
            solver.solve();
            logger.info("{}已完成：\n{}\n", solver.getName(), cube);
            solver = solver.getNextSolver();
        } while (solver != null);
    }

}