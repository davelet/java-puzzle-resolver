package com.rubik.toll.back;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rubik.toll.back.puzzle.Board;
import com.rubik.toll.back.puzzle.PuzzleShuffler;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        Board initialBoard = new Board(5);
        logger.info(initialBoard);
        new PuzzleShuffler(initialBoard).shuffle(20);
        logger.info("洗牌后");
        
        // new NumberPuzzleSolver(initialBoard).solve();

        // int[] tiles = {
        //     7, 2, 3, 4, 6, 5,
        //     14, 1, 8, 9, 12, 10,
        //     13, 27, 23, 21, 18, 24,
        //     15, 0, 31, 22, 34, 35,
        //     20, 25, 19, 28, 29, 16,
        //     26, 33, 32, 17, 30, 11
        // };
        // Board initialBoard = new Board(tiles);

    }
}