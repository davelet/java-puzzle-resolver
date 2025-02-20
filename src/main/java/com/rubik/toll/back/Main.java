package com.rubik.toll.back;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rubik.toll.back.puzzle.Board;
import com.rubik.toll.back.puzzle.NumberPuzzleSolver;
import com.rubik.toll.back.puzzle.PuzzleShuffler;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        Board initialBoard = new Board(50);
        logger.info(initialBoard);
        new PuzzleShuffler(initialBoard).shuffle(3);
        logger.info("洗牌后");
        
        new NumberPuzzleSolver(initialBoard).solve();
    }
}