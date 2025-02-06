package com.rubik.toll.back;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTest {
    @Test
    void testMainOutput() {
        // 重定向System.out到ByteArrayOutputStream
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            // 调用main方法
            Main.main(new String[]{});

            // 验证输出
            assertEquals("Welcome to Disney Toll System!\n", outContent.toString());
        } finally {
            // 恢复原始的System.out
            System.setOut(originalOut);
        }
    }
}