package com.rubik.toll.back.rubik;

import com.rubik.toll.back.rubik.cube.Color;
import com.rubik.toll.back.rubik.cube.Cube;
import com.rubik.toll.back.rubik.cube.Face;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CubeShufflerTest {
    private CubeShuffler shuffler;
    private Cube cube;

    @BeforeEach
    void setUp() {
        cube = new Cube();
        shuffler = new CubeShuffler(cube);
    }

    @Test
    void testConstructorWithNullCube() {
        assertThrows(IllegalArgumentException.class, 
            () -> new CubeShuffler(null), 
            "构造函数应该在接收到空的魔方实例时抛出IllegalArgumentException");
    }

    @Test
    void testShuffleWithNegativeTimes() {
        assertThrows(IllegalArgumentException.class, 
            () -> shuffler.shuffle(-1), 
            "打乱次数为负数时应该抛出IllegalArgumentException");
    }

    @Test
    void testShuffleWithZeroTimes() {
        Cube originalCube = new Cube();
        Cube shuffledCube = shuffler.shuffle(0);
        
        // 验证零次打乱后魔方状态应该保持不变
        for (Face face : Face.values()) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    assertEquals(
                        originalCube.getColor(face, i, j),
                        shuffledCube.getColor(face, i, j),
                        String.format("零次打乱后，位置(%s, %d, %d)的颜色应该保持不变", face, i, j)
                    );
                }
            }
        }
    }

    @Test
    void testShufflePreservesColorCount() {
        int times = 20;
        Cube shuffledCube = shuffler.shuffle(times);
        
        // 验证打乱后每种颜色的数量是否正确（每个颜色应该有9个）
        for (Color color : Color.values()) {
            int count = 0;
            for (Face face : Face.values()) {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (shuffledCube.getColor(face, i, j) == color) {
                            count++;
                        }
                    }
                }
            }
            assertEquals(9, count, 
                String.format("打乱后%s颜色的数量应该保持为9个", color));
        }
    }

    @Test
    void testMultipleShufflesProduceDifferentResults() {
        int times = 20;
        Cube firstShuffle = shuffler.shuffle(times);
        Cube secondShuffle = new CubeShuffler(new Cube()).shuffle(times);
        
        boolean different = false;
        // 检查至少有一个位置的颜色不同
        outerLoop:
        for (Face face : Face.values()) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (firstShuffle.getColor(face, i, j) != 
                        secondShuffle.getColor(face, i, j)) {
                        different = true;
                        break outerLoop;
                    }
                }
            }
        }
        
        assertTrue(different, "多次打乱应该产生不同的结果");
    }
}