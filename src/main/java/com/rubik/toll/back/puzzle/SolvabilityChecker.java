package com.rubik.toll.back.puzzle;

import java.util.Arrays;

public class SolvabilityChecker {
    private final int[][] board;
    private final int xsize;
    private final int ysize;
    private final BoardOperator boardOperator;

    public SolvabilityChecker(int[][] board, int xsize, int ysize, BoardOperator boardOperator) {
        this.board = board;
        this.xsize = xsize;
        this.ysize = ysize;
        this.boardOperator = boardOperator;
    }

    public boolean isSolvable() {
        int[] flatBoard = new int[xsize * ysize];
        int index = 0;
        for (int i = 0; i < xsize; i++) {
            for (int j = 0; j < ysize; j++) {
                flatBoard[index++] = board[i][j];
            }
        }
        int reverse = getReverse(flatBoard);
        if (ysize % 2 == 0) {
            int[] space = boardOperator.getPositionOf(xsize - 1, ysize - 1);
            reverse ^= (xsize - 1 - space[0]) & 1;
        }
        return reverse == 0;
    }

    private int getReverse(int[] arr) {
        // 创建一个不包含0的新数组
        int[] nonZeroArr = Arrays.stream(arr)
                .filter(x -> x != 0)
                .toArray();
        return mergeSortAndCount(nonZeroArr, 0, nonZeroArr.length - 1);
    }

    private int mergeSortAndCount(int[] arr, int left, int right) {
        int count = 0;
        if (left < right) {
            int mid = (left + right) / 2;
            count ^= mergeSortAndCount(arr, left, mid);
            count ^= mergeSortAndCount(arr, mid + 1, right);
            count ^= merge(arr, left, mid, right);
        }
        return count;
    }

    private int merge(int[] arr, int left, int mid, int right) {
        int[] leftArr = Arrays.copyOfRange(arr, left, mid + 1);
        int[] rightArr = Arrays.copyOfRange(arr, mid + 1, right + 1);
        
        int i = 0, j = 0, k = left;
        int count = 0;
        
        while (i < leftArr.length && j < rightArr.length) {
            if (leftArr[i] <= rightArr[j]) {
                arr[k++] = leftArr[i++];
            } else {
                arr[k++] = rightArr[j++];
                // 当右边的数小于左边的数时，左边剩余的数都会形成逆序对
                count ^= (leftArr.length - i) & 1;
            }
        }
        
        while (i < leftArr.length) {
            arr[k++] = leftArr[i++];
        }
        while (j < rightArr.length) {
            arr[k++] = rightArr[j++];
        }
        
        return count;
    }
}