package com.rubik.toll.back;

import com.rubik.toll.back.rubik.cube.Face;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rubik.toll.back.puzzle.Board;
import com.rubik.toll.back.puzzle.NumberPuzzleSolver;
import com.rubik.toll.back.puzzle.PuzzleShuffler;
import com.rubik.toll.back.rubik.cube.Cube;
import com.rubik.toll.back.rubik.CubeShuffler;
import com.rubik.toll.back.rubik.TwistDirection;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        Board initialBoard = new Board(5);
        logger.info(initialBoard);
        new PuzzleShuffler(initialBoard).shuffle(3);
        logger.info("洗牌后");
        
        new NumberPuzzleSolver(initialBoard).solve();

        logger.info("===================================");

        Cube cube = new Cube();
       CubeShuffler cs = new CubeShuffler(cube);
       cs.rotateFace(Face.RIGHT, TwistDirection.CLOCKWISE);
        // cube.shuffle(2);
        logger.info("扭转后的状态 - {}", cube);
        cube.solve();
    }
}