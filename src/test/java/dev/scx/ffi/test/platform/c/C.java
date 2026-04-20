package dev.scx.ffi.test.platform.c;

import dev.scx.ffi.ScxFFI;
import dev.scx.ffi.annotation.SymbolName;
import dev.scx.ffi.type.FFICallback;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.JAVA_INT;

/// 提供一些 C 标准的接口
///
/// @author scx567888
/// @version 0.0.1
public interface C extends C1 {

    C C = ScxFFI.createFFI(C.class);

    long strlen(String str);

    // 测试别名
    @SymbolName("abs")
    int javaAbs(int x);

    double sqrt(double x);

    void qsort(int[] base, long nmemb, long size, Compar compar);

    // 测试默认方法
    default int abs(int x) {
        System.out.println("调用了 默认方法 abs !!!");
        return javaAbs(x);
    }

    interface Compar extends FFICallback {

        int callback(MemorySegment aAddr, MemorySegment bAddr);

        /// 指定参数内存布局
        @Override
        default MemoryLayout[] parameterTargetLayouts() {
            return new MemoryLayout[]{JAVA_INT, JAVA_INT};
        }

    }

}
