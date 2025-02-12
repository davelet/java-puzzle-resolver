package com.rubik.toll.back.puzzle;

import java.util.LinkedList;
import java.util.Queue;

public class PathFinder {
    private final int xsize;
    private final int ysize;
    private final boolean[][] readyFlag;
    private static final int[][] DIRECTIONS = {{0, 1}, {0, -1}, {-1, 0}, {1, 0}};

    public PathFinder(int xsize, int ysize, boolean[][] readyFlag) {
        this.xsize = xsize;
        this.ysize = ysize;
        this.readyFlag = readyFlag;
    }

    private boolean isLegal(int x, int y) {
        return x >= 0 && x < xsize && y >= 0 && y < ysize;
    }

    public int[][] findPath(int fx, int fy, int tx, int ty, Integer stillX, Integer stillY) {
        int[][] distance = new int[xsize][ysize];
        for (int i = 0; i < xsize; i++) {
            for (int j = 0; j < ysize; j++) {
                distance[i][j] = -1;
            }
        }
        Queue<int[]> q = new LinkedList<>();
        q.offer(new int[]{fx, fy});
        distance[fx][fy] = 0;
        int[][][] lastPos = new int[xsize][ysize][2];

        while (!q.isEmpty()) {
            int[] now = q.poll();
            for (int[] dir : DIRECTIONS) {
                int xx = now[0] + dir[0];
                int yy = now[1] + dir[1];
                if (isLegal(xx, yy)
                        && distance[xx][yy] == -1
                        && (stillX == null || stillY == null || xx != stillX || yy != stillY)
                        && !readyFlag[xx][yy]) {
                    q.offer(new int[]{xx, yy});
                    distance[xx][yy] = distance[now[0]][now[1]] + 1;
                    lastPos[xx][yy] = now;
                    if (xx == tx && yy == ty) {
                        LinkedList<int[]> path = new LinkedList<>();
                        int[] curr = new int[]{xx, yy};
                        while (true) {
                            path.addFirst(curr);
                            if (curr[0] == fx && curr[1] == fy) break;
                            curr = lastPos[curr[0]][curr[1]];
                        }
                        return path.toArray(new int[0][]);
                    }
                }
            }
        }
        return new int[0][];
    }

    public static int[][] getDirections() {
        return DIRECTIONS;
    }
}