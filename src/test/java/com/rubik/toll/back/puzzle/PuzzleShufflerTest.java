package com.rubik.toll.back.puzzle;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class PuzzleShufflerTest {
    private PuzzleShuffler shuffler;
    private static final int DEFAULT_SIZE = 3;

    @BeforeEach
    void setUp() {
        shuffler = new PuzzleShuffler(new Board(DEFAULT_SIZE));
    }

    @Test
    void testInitialBoardState() {
        int[] board = shuffler.getBoard();
        assertEquals(DEFAULT_SIZE * DEFAULT_SIZE, board.length, "棋盘大小应该是size * size");
        
        // 验证初始状态是否正确（1-8，最后一个位置是0）
        for (int i = 0; i < board.length - 1; i++) {
            assertEquals(i + 1, board[i], String.format("位置%d应该是%d", i, i + 1));
        }
        assertEquals(0, board[board.length - 1], "最后一个位置应该是0（空格）");
    }

    @Test
    void testShuffleWithZeroMoves() {
        int[] initialBoard = shuffler.getBoard();
        int[] shuffledBoard = shuffler.shuffle(0);
        
        assertArrayEquals(initialBoard, shuffledBoard, "0步移动后的棋盘应该与初始状态相同");
    }

    @Test
    void testShuffleWithNegativeMoves() {
        assertThrows(IllegalArgumentException.class, 
            () -> shuffler.shuffle(-1), 
            "负数步数应该抛出IllegalArgumentException");
    }

    @Test
    void testShuffleWithPositiveMoves() {
        int moves = 10;
        int[] shuffledBoard = shuffler.shuffle(moves);
        
        // 验证洗牌后的棋盘是否有效
        boolean[] seen = new boolean[DEFAULT_SIZE * DEFAULT_SIZE];
        int zeroCount = 0;
        
        for (int value : shuffledBoard) {
            if (value == 0) {
                zeroCount++;
            } else {
                assertTrue(value > 0 && value < DEFAULT_SIZE * DEFAULT_SIZE, 
                    "数字应该在1到" + (DEFAULT_SIZE * DEFAULT_SIZE - 1) + "之间");
            }
            if (value >= 0 && value < DEFAULT_SIZE * DEFAULT_SIZE) {
                assertFalse(seen[value], "每个数字应该只出现一次");
                seen[value] = true;
            }
        }
        
        assertEquals(1, zeroCount, "应该只有一个空格（0）");
    }

    @Test
    void testMultipleShufflesProduceDifferentResults() {
        int moves = 20;
        int[] firstShuffle = shuffler.shuffle(moves);
        int[] secondShuffle = shuffler.shuffle(moves);
        
        // 由于随机性，两次洗牌的结果很可能不同
        // 注意：理论上存在两次洗牌结果相同的可能，但概率极小
        boolean different = false;
        for (int i = 0; i < firstShuffle.length; i++) {
            if (firstShuffle[i] != secondShuffle[i]) {
                different = true;
                break;
            }
        }
        
        assertTrue(different, "多次洗牌应该产生不同的结果");
    }

    @Test
    void testLargerBoardSize() {
        int largerSize = 4;
        Board board = new Board(largerSize);
        new PuzzleShuffler(board).shuffle(10);
        System.out.println(board);
        
        assertEquals(largerSize * largerSize, board.getTiles().length, "大尺寸棋盘的大小应该是size * size");
    }
}