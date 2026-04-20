package dev.scx.ffi.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import static dev.scx.ffi.test.platform.c.C.C;
import static java.lang.foreign.ValueLayout.JAVA_INT;

public class CTest {

    public static void main(String[] args) {
        test1();
        test2();
    }

    @Test
    public static void test1() {
        long a = C.strlen("abc123456");
        int a1 = "abc123456".length();
        Assert.assertEquals(a, a1);

        // 测试默认方法调用
        long b = C.abs(-123);
        long b1 = Math.abs(-123);
        Assert.assertEquals(b, b1);

        // 测试继承调用
        var c = C.sin(12);
        var c1 = Math.sin(12);
        Assert.assertEquals(c, c1);

        var d = C.sqrt(88);
        var d1 = Math.sqrt(88);
        Assert.assertEquals(d, d1);

    }

    @Test
    public static void test2() {
        var array = new int[]{2, 5, 7, 1, 4, 56, 12, 31, 99999, 90, 271, 2};

        C.qsort(array, array.length, Integer.BYTES, (aAddr, bAddr) -> {
            int a = aAddr.get(JAVA_INT, 0);
            int b = bAddr.get(JAVA_INT, 0);
            return Integer.compare(a, b);
        });

        Assert.assertEquals(array, new int[]{1, 2, 2, 4, 5, 7, 12, 31, 56, 90, 271, 99999});
    }

}
